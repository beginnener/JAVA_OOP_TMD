package src.model;

import java.awt.Image;

/*
 * Player.java
 * Kelas ini merepresentasikan objek pemain dalam permainan.
 * kelas ini menyimpan informasi tentang posisi, ukuran, gambar pemain, dan status gerakan pemain
 * Kelas ini juga menyediakan metode untuk menggerakkan pemain, memperbarui frame animasi, dan mengatur status gerakan.
 */
public class Player {
    private int x, y;                                                           // posisi player
    private int width, height;                                                  // ukuran player
    private Image image;                                                        // gambar player
    private boolean walking = false;                                            // status gerakan player (apakah sedang berjalan atau tidak)          
    private boolean walkingLeft = false;                                        // arah gerak player
    private int walkFrame = 0;                                                  // frame animasi gerakan berjalan (0 atau 1)                
    private int walkFrameCounter = 0;                                           // penghitung frame untuk animasi gerakan berjalan                     

    // konstruktor
    public Player(int startX, int startY, int width, int height, Image image) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    // === Getter dan setter posisi ===
    // mendapatkan dan mengatur posisi x 
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    // mendapatkan dan mengatur posisi y
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    // mendapatkan dan mengatur ukuran player
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // mendapatkan gambar player
    public Image getImage() { return image; }

    // getter setter status gerakan
    public boolean isWalking() { return walking; }
    public void setWalking(boolean walking) { this.walking = walking; }
    
    // mendapatkan dan mengatur frame animasi gerakan berjalan
    public int getWalkFrame() { return walkFrame; }
    public void updateWalkFrame() {                                             // walkFrame player akan dipanggil dan setiap 5 frame akan berubah
        walkFrameCounter++;                                                         // tambah 1 walkFrameCounter setiap kali updateWalkFrame dipanggil
        if (walkFrameCounter >= 5) {                                                // jika walkFrameCounter sudah mencapai 5, artinya sudah waktunya untuk mengganti frame animasi           
            walkFrame = (walkFrame + 1) % 2;                                        // ganti frame animasi, jika sudah mencapai 2 (0 dan 1) kembali ke 0  
            walkFrameCounter = 0;                                                   // reset walkFrameCounter ke 0     
        }
    }
    public void resetWalkFrame() {                                                  // reset walkFrame dan walkFrameCounter ke 0
        walkFrame = 0;
        walkFrameCounter = 0;
    }

    // metode untuk menggerakan (update) posisi player
    public void move(int dx, int dy) {
        if (dx < 0) { dx = Math.max(dx, -x); }                                  // jika player sedang bergerak kekiri, terapkan perhitungan supaya player tidak keluar dari layar
        else if (dx > 0) { dx = Math.min(dx, 790 - x - width); }                // tidak boleh keluar dari kanan layar (misal lebar layar 784px)
        this.x += dx;
        
        if (dy < 0) { dy = Math.max(dy, -y); }                                  // tidak boleh keluar dari atas layar
        else if (dy > 0) { dy = Math.min(dy, 530 - y - height); }               // tidak boleh keluar dari bawah layar (misal tinggi layar 521px)
        this.y += dy;
    }

    // metode untuk mengatur arah gerak player
    public void setWalkingLeft(boolean walkingLeft) { this.walkingLeft = walkingLeft; }
    public boolean isWalkingLeft() { return walkingLeft; }
}
