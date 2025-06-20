_Saya Natasha Adinda Cantika dengan NIM 2312120 mengerjakan TMD dalam mata kuliah DPBO untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin_

# 16-Bit Video Game Using JAVA Swing and MVP Framework

Sebuah game 2D berbasis Java Swing di mana pemain melempar lasso untuk menangkap bola-bola dan mengantarkannya ke keranjang untuk mendapatkan skor.

---

## Struktur dan penjelasan direktori
```
/TMD/
│
├── src/
│ ├── model/
│ │ ├── DBConnection.java ← Menyediakan koneksi ke database dan membuat tabel jika belum ada
│ │ ├── UserScore.java ← Kelas data (POJO) yang merepresentasikan entitas user dan skornya
│ │ ├── UserModel.java ← Akses data (DAO) untuk operasi CRUD terhadap tabel user; berinteraksi dengan database
│ │ ├── Player.java ← Representasi objek pemain dalam game (posisi, gambar, animasi)
│ │ ├── SkillBall.java ← Representasi bola skill (posisi, skor, animasi, status)
│ │ ├── Lasso.java ← Representasi lasso untuk menangkap bola
│ │ ├── MusicPlayer.java ← Utilitas untuk memutar dan mengontrol musik latar belakang
│ │ └── Keranjang.java ← Representasi checkpoint (keranjang) tempat mengantar bola
│
│ ├── view/
│ │ ├── MainMenuView.java ← Tampilan awal game: input nama pemain & daftar skor
│ │ ├── GameView.java ← Kontainer utama yang menggabungkan GamePanel & HUDPanel
│ │ ├── HUDPanel.java ← Panel informasi HUD: skor, bola tertangkap, waktu
│ │ └── GamePanel.java ← Panel utama gameplay (render entitas & animasi, input)
│
│ ├── presenter/
│ │ ├── KontrakGamePresenter.java ← Interface penghubung antara view dan presenter
│ │ ├── MainMenuPresenter.java ← Logika menu: validasi nama, ambil data user dari DB
│ │ └── GamePresenter.java ← Logika utama permainan: gerakan, skor, tabrakan, timer
│
│ └── Main.java ← Entry point program, inisialisasi frame utama
│
├── lib/ ← Driver MySQL JDBC
│
├── assets/
│ ├── Bg.png ← Gambar background
│ ├── bola.png ← Gambar bola
│ ├── keranjang.png ← Gambar keranjang
│ ├── player-idle.png ← Gambar idle karakter
│ ├── player-walking/
│ │ ├── player-walking 1.png
│ │ └── player-walking 2.png
│ └── audio/
│ └── bgmusic.wav ← Musik latar belakang permainan
│
└── README.md ← Dokumentasi ini
```

## Alur Program

### 1. Start Game

- Dimulai dari `MainMenuView`, pemain memasukkan nama.
- Saat klik "Mulai", program inisialisasi `GameView` & `GamePresenter`.

### 2. Render Aset

- `GamePanel.paintComponent()` menggambar:
  - Background (`Bg.png`)
  - Player (`player-idle.png` atau animasi `player-walking`)
  - Bola (`bola.png`)
  - Lasso (garis atau gambar)
  - Keranjang (`keranjang.png`)

### 3. Game Loop

Timer 30ms (≈ 33 FPS) melakukan:
- Gerakan bola (`moveBalls`)
- Update animasi player
- Update posisi lasso
- Deteksi tabrakan:
  - **Lasso–Bola**: bola ditarik
  - **Bola–Player**: bola ditandai “delivered”
  - **Bola–Keranjang**: skor bertambah

### 4. Perhitungan Skor

- Bola harus:
  - Ditangkap dengan lasso
  - Diantar ke player
  - Diserahkan ke keranjang

→ Setelah sampai keranjang, skor ditambah sesuai `scoreValue`.

### 5. Database (CRUD)

- Setelah game berakhir (`quitGame()`), skor pemain disimpan via `UserModel.insertOrUpdateUser()`
- Disimpan di SQLite (via JDBC) oleh `DBConnection`

### 6. Musik

- `MusicPlayer` memutar file `.wav` dari `assets/audio/bgmusic.wav`
- Musik dapat di-loop dan dikontrol volumenya

### 7. Input Handler

- Arrow keys: menggerakkan player
- Spasi: pause / resume
- Mouse click: melempar lasso ke arah klik
- ESC: keluar dari game

---

## Cara Menjalankan

1. **Build & Compile:**
   ```bash
   javac -cp "lib/mysql-connector-j-9.3.0.jar" -d bin src/Main.java src/model/*.java src/presenter/*.java src/view/*.java
   java -cp "bin;lib/mysql-connector-j-9.3.0.jar" src.Main
