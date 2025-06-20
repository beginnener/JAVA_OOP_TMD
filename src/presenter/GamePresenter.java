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
 * Presenter utama untuk logika game, menghubungkan model dan view.
 */

public class GamePresenter implements KontrakGamePresenter {
    // View utama game
    private GameView view;
    // Objek player
    private Player player;
    // Objek lasso
    private Lasso lasso;
    // Daftar bola di arena
    private ArrayList<SkillBall> balls;
    // Keranjang target
    private Keranjang keranjang;
    // Nama pemain
    private String username;
    // Skor pemain
    private int score = 0;
    // Jumlah bola masuk keranjang
    private int count = 0;
    // Sisa waktu (detik)
    private int timeLeft = 60;
    // Counter frame untuk timer
    private int frameCounter = 0;
    // Timer utama game
    private Timer gameTimer;
    // Status game selesai
    private boolean gameEnded = false;
    // Status pause
    private boolean paused = false;
    // Bola yang sedang diantar ke keranjang
    private List<SkillBall> deliveringBalls = null;
    // Pemutar musik latar
    private MusicPlayer musicPlayer = new MusicPlayer();
    // Path musik latar
    private final String backgroundMusicPath = "assets/audio/bgmusic.wav";

    // Konstruktor presenter, inisialisasi model dan view
    public GamePresenter(GameView view, String username) {
        this.view = view;
        this.balls = new ArrayList<>();
        balls.add(new SkillBall(null, null, 10));
        balls.add(new SkillBall(null, null, 20));
        balls.add(new SkillBall(null, null, 45));
        balls.add(new SkillBall(null, null, 50));
        this.username = username;
        this.player = new Player(375, 275, 70, 70, null);
        this.lasso = new Lasso(player.getX(), player.getY());
        this.keranjang = new Keranjang(684, 217, 100, 80);
        this.deliveringBalls = new ArrayList<>();
    }

    // == Public API == 
    // Mulai game dan timer utama
    @Override
    public void startGame() {
        musicPlayer.play(backgroundMusicPath, true);
        gameTimer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveBalls();
                checkLassoBallCollisions();
                maybeSpawnBall();

                System.out.println("posisi player: " + player.getX() + player.getY());

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
                lasso.update();
                view.repaint();

                // Hitung waktu mundur
                frameCounter(gameTimer);
            }
        });
        gameTimer.start();
    }

    // Hitung waktu mundur per frame
    public void frameCounter(Timer timer) {
        frameCounter++;
        if (frameCounter >= 1000 / 30) {
            timeLeft--;
            frameCounter = 0;
            if (timeLeft <= 0) {
                gameOver();
                System.out.println("waktu habis, main lagi?");
                if (gameTimer != null) {
                    gameTimer.stop();
                    gameTimer = null;
                }
                musicPlayer.stop();
            }
        }
    }

    // Pause atau resume game
    public void pauseGame() {
        if (gameEnded) return;
        if (gameTimer != null && gameTimer.isRunning()) {
            view.pausedOverlay();
            gameTimer.stop();
            musicPlayer.pause();
            setPaused(true);
            System.out.println("Game paused");
        } else {
            view.removePausedOverlay();
            gameTimer.start();
            musicPlayer.resume();
            setPaused(false);
            System.out.println("Game resumed");
        }
    }

    // Keluar dari game, simpan skor
    public void quitGame() {
        if (gameEnded) return;
        gameEnded = true;
        if (gameTimer != null) {
            musicPlayer.stop();
            gameTimer.stop();
            gameTimer = null;
        }
        System.out.println("USername: " + username + "is quitted");
        UserScore userScore = new UserScore(username, score, count);
        UserModel userModel = new UserModel();
        userModel.insertOrUpdateUser(userScore);
        System.out.println("Game ended");
        view.close();
    }

    // Game over, tampilkan overlay dan simpan skor
    public void gameOver() {
        if (gameEnded) return;
        gameEnded = true;
        view.showGameOverOverlay(score, username);
        if (gameTimer != null) {
            musicPlayer.stop();
            gameTimer.stop();
            gameTimer = null;
        }
        System.out.println("USername: " + username + "is quitted");
        UserScore userScore = new UserScore(username, score, count);
        UserModel userModel = new UserModel();
        userModel.insertOrUpdateUser(userScore);
        System.out.println("Game ended");
    }

    // Simpan skor saat frame di-close
    public void saveScoreOnClose() {
        System.out.println("Frame closed by user (X), saving score...");
        UserScore userScore = new UserScore(username, score, count);
        UserModel userModel = new UserModel();
        userModel.insertOrUpdateUser(userScore);
    }

    // Handler input keyboard untuk player
    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                player.move(0, -10);
                player.setWalking(true);
                break;
            case KeyEvent.VK_DOWN:
                player.move(0, 10);
                player.setWalking(true);
                break;
            case KeyEvent.VK_LEFT:
                player.move(-10, 0);
                player.setWalking(true);
                player.setWalkingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.move(10, 0);
                player.setWalking(true);
                player.setWalkingLeft(false);
                break;
        }
    }

    // Lempar lasso ke arah mouse
    public void throwLasso(int mouseX, int mouseY) {
        int originX = player.getX() + player.getWidth() / 2;
        int originY = player.getY() + player.getHeight() / 2;
        lasso.throwTo(mouseX, mouseY, originX, originY);
        lasso.setThrowing(true);
    }

    // == Getter/setter ==
    public void setScore(int score) { this.score = score; }
    public void addScore(int delta) { this.score += delta; }
    public Rectangle getPlayerRectangle() {
        return new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
    public Rectangle getLassoRectangle() {
        return new Rectangle(lasso.getX(), lasso.getY());
    }
    public Point getPlayerCenter() {
        int centerX = player.getX() + player.getWidth() / 2;
        int centerY = player.getY() + player.getHeight() / 2;
        return new Point(centerX, centerY);
    }
    public List<BallInfo> getBallInfos() {
        List<BallInfo> infos = new ArrayList<>();
        for (SkillBall ball : balls) {
            infos.add(new BallInfo(
                ball.getX(),
                ball.getY(),
                ball.getWidth(),
                ball.getHeight(),
                ball.getScoreValue()
            ));
        }
        return infos;
    }
    public Point getLassoPoint() { return new Point(lasso.getX(), lasso.getY()); }
    public int getScore() { return score; }
    public int getCount() { return count; }
    public Rectangle getKeranjangBound() {
        return new Rectangle(keranjang.getX(), keranjang.getY(), keranjang.getWidth(), keranjang.getHeight());
    }
    public int getTimeLeft() { return timeLeft; }
    public void setPlayerWalking(boolean walking) { player.setWalking(walking); }
    public boolean isPlayerWalking() { return player.isWalking(); }
    public int getPlayerWalkFrame() { return player.getWalkFrame(); }
    public boolean isPlayerFacingLeft() { return player.isWalkingLeft(); }
    public void setPaused(boolean paused) { this.paused = paused; }
    public boolean isPaused() { return paused; }
    public boolean isLassoActive() { return lasso.isActive(); }

    // Update posisi semua bola
    private void moveBalls() {
        Iterator<SkillBall> it = balls.iterator();
        while (it.hasNext()) {
            SkillBall ball = it.next();
            if (ball.isDelivered()) continue;
            ball.move();
            if (ball.isOutOfScreen()) {
                it.remove();
            }
        }
    }

    // Spawn bola baru secara acak
    private void maybeSpawnBall() {
        if (new Random().nextInt(100) < 1) {
            balls.add(new SkillBall(null, null, 10));
            balls.add(new SkillBall(null, null, 20));
            balls.add(new SkillBall(null, null, 45));
            balls.add(new SkillBall(null, null, 50));
        }
    }

    // Cek tabrakan lasso dengan bola, antar bola ke player dan keranjang
    private void checkLassoBallCollisions() {
        Iterator<SkillBall> it = balls.iterator();
        while (it.hasNext()) {
            SkillBall ball = it.next();
            if (lasso.getBounds().intersects(ball.getBounds()) && lasso.getGrabbedBall() == null && lasso.isActive()) {
                if (!ball.isDelivered() && lasso.isThrowing()) {
                    lasso.grabBall(ball);
                    lasso.setThrowing(false);
                }
                System.out.println("ball with score " + ball.getScoreValue() + " is grabbed");
                break;
            }
        }

        SkillBall grabbed = lasso.getGrabbedBall();
        if (grabbed != null) {
            if (!grabbed.isDelivered()) {
                Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth() + 100, player.getHeight() + 100);
                Rectangle ballRect = new Rectangle(grabbed.getX(), grabbed.getY(), grabbed.getWidth(), grabbed.getHeight());
                if (playerRect.intersects(ballRect)) {
                    System.out.println("Bola sudah sampai ke player: " + grabbed.getScoreValue());
                    grabbed.setDelivered(true);
                    deliveringBalls.add(grabbed);
                    lasso.clearGrabbedBall();
                }
            }
        }
        if (deliveringBalls != null && !deliveringBalls.isEmpty()) {
            System.out.println("Bola sedang diantar ke keranjang");
            Iterator<SkillBall> deliverIt = deliveringBalls.iterator();
            while (deliverIt.hasNext()) {
                SkillBall deliveringBall = deliverIt.next();
                if (deliveringBall.isDelivered()) {
                    moveToKeranjang(deliveringBall);
                    if (checkBallKeranjangCollions(deliveringBall)) deliverIt.remove();
                } else {
                    deliverIt.remove();
                }
            }
        }
    }

    // Cek bola masuk keranjang, tambah skor dan hapus bola
    public boolean checkBallKeranjangCollions(SkillBall ball) {
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
        Rectangle keranjangRect = new Rectangle(keranjang.getX(), keranjang.getY(), keranjang.getWidth(), keranjang.getHeight());
        if (keranjangRect.intersects(ballRect)) {
            System.out.println("score added : " + ball.getScoreValue());
            score += ball.getScoreValue();
            count++;
            balls.remove(ball);
            ball = null;
            return true;
        }
        return false;
    }

    // Gerakkan bola ke keranjang dengan animasi
    public void moveToKeranjang(SkillBall ball) {
        int ballCenterX = ball.getX() + ball.getWidth() / 2;
        int ballCenterY = ball.getY() + ball.getHeight() / 2;
        int keranjangCenterX = keranjang.getX() + keranjang.getWidth() / 2;
        int keranjangCenterY = keranjang.getY() + keranjang.getHeight() / 2;

        double dist = Math.hypot(ballCenterX - keranjangCenterX, ballCenterY - keranjangCenterY);

        if (dist < 5) {
            ball.setX(keranjangCenterX - ball.getWidth() / 2);
            ball.setY(keranjangCenterY - ball.getHeight() / 2);
        } else {
            double angle = Math.atan2(keranjangCenterY - ballCenterY, keranjangCenterX - ballCenterX);
            int speed = 5;
            ball.setX((int) (ball.getX() + speed * Math.cos(angle)));
            ball.setY((int) (ball.getY() + speed * Math.sin(angle)));
        }
        System.out.println("Menggerakkan bola ke keranjang: " + ball.getX() + ", " + ball.getY());
    }

    // Info bola untuk view
    public static class BallInfo {
        public final int x, y, width, height, scoreValue;
        public BallInfo(int x, int y, int width, int height, int scoreValue) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.scoreValue = scoreValue;
        }
    }
}