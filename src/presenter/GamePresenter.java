package src.presenter;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import src.model.*;
import src.view.GameView;

/*
 * GamePresenter.java
 * Kelas presenter yang mengelola logika permainan.
 * Kelas ini menghubungkan antara model (Player, Lasso, SkillBall, Keranjang) dan view (GameView).
 * Kelas ini juga menangani input dari pengguna seperti gerakan player dan lemparan lasso.
 */

public class GamePresenter implements KontrakGamePresenter {
    private GameView view; // insisiasi GameView sebagai tampilan utama permainan
    private Player player; // inisiasi Player sebagai objek player
    private Lasso lasso; // inisiasi Lasso sebagai objek lasso yang digunakan untuk menangkap bola
    private ArrayList<SkillBall> balls; // inisiasi ArrayList untuk menyimpan bola-bola yang akan ditangkap
    private Keranjang keranjang;    // inisiasi Keranjang sebagai objek
    private String username; // inisiasi String untuk menyimpan username pemain
    private int score = 0;  // inisiasi int untuk menyimpan skor pemain
    private int count = 0;  // inisiasi int untuk menyimpan jumlah bola yang berhasil ditangkap
    private int timeLeft = 120; // detik
    private int frameCounter = 0; // untuk menghitung 1 detik

    private Timer gameTimer;
    private boolean gameEnded = false; // untuk menandai apakah permainan sudah berakhir

    private List<SkillBall> deliveringBalls = null;

    private MusicPlayer musicPlayer = new MusicPlayer(); // inisialisasi MusicPlayer untuk memutar musik
    private final String backgroundMusicPath = "assets/audio/bgmusic.wav"; // path musik latar belakang

    // Konstruktor
    public GamePresenter(GameView view, String username) {
        this.view = view;
        this.balls = new ArrayList<>();
        balls.add(new SkillBall(null, null, 10)); // tambahkan bola pertama di awal permainan
        balls.add(new SkillBall(null, null, 20)); // tambahkan bola pertama di awal permainan
        balls.add(new SkillBall(null, null, 45)); // tambahkan bola pertama di awal permainan
        balls.add(new SkillBall(null, null, 50)); // tambahkan bola pertama di awal permainan
        this.username = username;
        this.player = new Player(375, 275, 70, 70, null); // posisi tengah (default gambar = null)
        this.lasso = new Lasso(player.getX(), player.getY());
        this.keranjang = new Keranjang(684, 217 ,100, 80);
        this.deliveringBalls = new ArrayList<>(); // inisialisasi list untuk bola yang sedang diantar ke keranjang
    }

    // == Metode untuk keperluan game ==
    @Override
    public void startGame() { // looping dengan timer
        musicPlayer.play(backgroundMusicPath, true); // mulai memutar musik latar belakang        
        gameTimer = new Timer(30, new ActionListener() {                                // inisiasi timer dengan interval 30ms
            public void actionPerformed(ActionEvent e) {                                     // setiap 30ms, update game
                moveBalls();                                                                    // mulai gerakkan bola
                checkLassoBallCollisions();                                                     // cek tabrakan antara lasso dan bola
                maybeSpawnBall();                                                               // spawn bola-bola baru

                // Update animasi player
                if (player.isWalking()) {
                    player.updateWalkFrame();
                } else {
                    player.resetWalkFrame();
                }

                // Update posisi lasso jika belum dilempar
                if (!lasso.isActive()) {
                    int px = player.getX() + player.getWidth() / 2;
                    int py = player.getY() + player.getHeight() / 2;
                    lasso.setPosition(px, py);
                }
                lasso.update(); // update status lasso
                view.repaint();

                // Hitung waktu mundur
                frameCounter++;
                if (frameCounter >= 1000 / 30) { // 1000 ms / 30 ms = ~33 frame = 1 detik
                    timeLeft--;
                    frameCounter = 0;
                    if (timeLeft <= 0) {
                        System.out.println("waktu habis bro, keluar dari game..");
                        if (gameTimer != null) {
                            gameTimer.stop();
                            gameTimer = null;
                        }
                        quitGame();
                    }
                }
            }
        });
        gameTimer.start();
    }

    // method untuk menggerakkan bola
    private void moveBalls() {
        Iterator<SkillBall> it = balls.iterator();
        while (it.hasNext()) {
            SkillBall ball = it.next();
            // Lewati bola yang sedang diantar ke keranjang
            if (ball.isDelivered()) continue;
            ball.move();
            if (ball.isOutOfScreen()) {
                it.remove();
            }
        }
    }

    // method untuk mungkin menambahkan bola baru jika bola di iterasi sudah habis
    private void maybeSpawnBall() {
        if (new Random().nextInt(100) < 1) { // 1% chance setiap frame
            balls.add(new SkillBall(null, null, 10)); // tambahkan bola pertama di awal permainan
            balls.add(new SkillBall(null, null, 20)); // tambahkan bola pertama di awal permainan
            balls.add(new SkillBall(null, null, 45)); // tambahkan bola pertama di awal permainan
            balls.add(new SkillBall(null, null, 50)); // tambahkan bola pertama di awal permainan
        }
    }
    
    // method untuk mengecek tabrakan antara lasso dan bola
    private void checkLassoBallCollisions() {
        Iterator<SkillBall> it = balls.iterator();
        while (it.hasNext()) {
            SkillBall ball = it.next();
            if (lasso.getBounds().intersects(ball.getBounds()) && lasso.getGrabbedBall() == null && lasso.isActive()) {
                lasso.grabBall(ball);
                System.out.println("ball with score " + ball.getScoreValue() + " is grabbed");
            }
        }
        
        // Setelah iterasi, cek jika bola sudah sampai ke player
        SkillBall grabbed = lasso.getGrabbedBall();
        if (grabbed != null) {
            // State 1: Bola masih ditarik ke player
            if (!grabbed.isDelivered()) {
                Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth() + 100, player.getHeight() + 100);
                Rectangle ballRect = new Rectangle(grabbed.getX(), grabbed.getY(), grabbed.getWidth(), grabbed.getHeight());
                if (playerRect.intersects(ballRect)) { // cek tabrakan bola dengan player
                    System.out.println("Bola sudah sampai ke player: " + grabbed.getScoreValue());
                    grabbed.setDelivered(true);
                    deliveringBalls.add(grabbed);
                    lasso.clearGrabbedBall(); // lasso sudah tidak pegang bola
                }
            }
        } 
        if (deliveringBalls != null && !deliveringBalls.isEmpty()) {
            System.out.println("Bola sedang diantar ke keranjang");
            // State 2: Bola sedang diantar ke keranjang
            Iterator<SkillBall> deliverIt = deliveringBalls.iterator();
            
            while (deliverIt.hasNext()) {
                SkillBall deliveringBall = deliverIt.next();
                if (deliveringBall.isDelivered()) {
                    moveToKeranjang(deliveringBall);
                    if (checkBallKeranjangCollions(deliveringBall)) deliverIt.remove(); // jika bola tidak sedang diantar, hapus dari list
                } else {
                    deliverIt.remove(); // jika bola sudah diantar, hapus dari list
                }
            }
        }
    }

    public boolean checkBallKeranjangCollions(SkillBall ball) {
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
        Rectangle keranjangRect = new Rectangle(keranjang.getX(), keranjang.getY(), keranjang.getWidth(), keranjang.getHeight());
        if (keranjangRect.intersects(ballRect)) {
            System.out.println("score added : " + ball.getScoreValue());
            score += ball.getScoreValue();
            count++;
            balls.remove(ball);
            ball = null;
            return true; // bola berhasil masuk keranjang
        }
        return false; // bola belum masuk keranjang
    }

    public void moveToKeranjang(SkillBall ball){
        // Hitung pusat bola dan keranjang
            int ballCenterX = ball.getX() + ball.getWidth() / 2;
            int ballCenterY = ball.getY() + ball.getHeight() / 2;
            int keranjangCenterX = keranjang.getX() + keranjang.getWidth() / 2;
            int keranjangCenterY = keranjang.getY() + keranjang.getHeight() / 2;

            // Hitung jarak
            double dist = Math.hypot(ballCenterX - keranjangCenterX, ballCenterY - keranjangCenterY);

            if (dist < 5) { // threshold, bisa diubah
                // Set posisi bola tepat di tengah keranjang
                ball.setX(keranjangCenterX - ball.getWidth() / 2);
                ball.setY(keranjangCenterY - ball.getHeight() / 2);
                // Proses bola masuk keranjang (tambah skor, hapus bola, dsb)
            } else {
                // Gerakkan bola ke target dengan kecepatan tetap
                double angle = Math.atan2(keranjangCenterY - ballCenterY, keranjangCenterX - ballCenterX);
                int speed = 5;
                ball.setX((int)(ball.getX() + speed * Math.cos(angle)));
                ball.setY((int)(ball.getY() + speed * Math.sin(angle)));
            }

            System.out.println("Menggerakkan bola ke keranjang: " + ball.getX() + ", " + ball.getY());
    }
    
    public boolean isLassoActive() {
        if (lasso.isActive()) {
            // System.out.println("Lasso is active");
            return true;
        } else {
            // System.out.println("Lasso is not active");
            return false;
        }
    }
    
    public void quitGame() {
        if (gameEnded) return;
        gameEnded = true;
        if (gameTimer != null) {
            musicPlayer.stop(); // stop music
            gameTimer.stop();
            gameTimer = null;
        }
        System.out.println("USername: " + username + "is quitted");
        UserScore userScore = new UserScore(username, score, count);
        UserModel userModel = new UserModel();
        userModel.insertOrUpdateUser(userScore); // Simpan skor ke database
        System.out.println("Game ended");
        view.close(); // Kembali ke menu utama
    }

    public void saveScoreOnClose() {
        System.out.println("Frame closed by user (X), saving score...");
        UserScore userScore = new UserScore(username, score, count);
        UserModel userModel = new UserModel();
        userModel.insertOrUpdateUser(userScore);
    }

    // === input handler ===
    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                player.move(0, -10); 
                player.setWalking(true); // set player walking
                break;
            case KeyEvent.VK_DOWN:
                player.move(0, 10); 
                player.setWalking(true); // set player walking
                break;
            case KeyEvent.VK_LEFT:
                player.move(-10, 0); 
                player.setWalking(true); // set player walking
                break;
            case KeyEvent.VK_RIGHT:
                player.move(10, 0); 
                player.setWalking(true); // set player walking
                break;
        }
    }

    public void throwLasso(int mouseX, int mouseY) {
        // Titik asal: tengah player
        int originX = player.getX() + player.getWidth() / 2;
        int originY = player.getY() + player.getHeight() / 2;
        lasso.throwTo(mouseX, mouseY, originX, originY);
    }

    // === getter untuk view ===
    public void setScore(int score) {
        this.score = score;
    }

    // Jika ingin menambah skor secara bertahap:
    public void addScore(int delta) {
        this.score += delta;
    }

    public Rectangle getPlayerRectangle() { // Mengembalikan rectangle posisi player
        return new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }

    public Rectangle getLassoRectangle() { // Mengembalikan rectangle posisi lasso
        return new Rectangle(lasso.getX(), lasso.getY());
    }

    public Point getPlayerCenter() {  // Mengembalikan titik tengah player
        int centerX = player.getX() + player.getWidth() / 2;
        int centerY = player.getY() + player.getHeight() / 2;
        return new Point(centerX, centerY);
    }

    public static class BallInfo {  // Mengambil informasi bola untuk ditampilkan di view
        public final int x, y, width, height, scoreValue;
        public BallInfo(int x, int y, int width, int height, int scoreValue) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.scoreValue = scoreValue;
        }
    }

    public List<BallInfo> getBallInfos() { // Mengembalikan list informasi bola
        List<BallInfo> infos = new ArrayList<>();
        for (SkillBall ball : balls) {
            infos.add(new BallInfo(
                ball.getX(),
                ball.getY(),
                ball.getWidth(),
                ball.getHeight(),
                ball.getScoreValue()
            ));
            // System.out.println("Ball Info: " + ball.getX() + ", " + ball.getY() + ", " + ball.getWidth() + ", " + ball.getHeight() + ", " + ball.getScoreValue());
        }
        return infos;
    }

    // Mengembalikan posisi ujung lasso
    public Point getLassoPoint() { return new Point(lasso.getX(), lasso.getY()); }

    // Mengembalikan score
    public int getScore() { return score; }

    // Mengembalikan jumlah bola yang berhasil ditangkap
    public int getCount() { return count; }

    // Mengembalikan keranjang
    public Rectangle getKeranjangBound(){
        return new Rectangle(keranjang.getX(), keranjang.getY(), keranjang.getWidth(), keranjang.getHeight());
    }

    public int getTimeLeft() {
        return timeLeft;
    } 

    // set player walking
    public void setPlayerWalking(boolean walking) {
        player.setWalking(walking);
    }

    // kembalikan status player walking
    public boolean isPlayerWalking() {
        return player.isWalking();
    }

    // return player walk frame
    public int getPlayerWalkFrame() {
        return player.getWalkFrame();
    }
}