package src.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import java.awt.Font;
import src.presenter.GamePresenter;

/*
 * GamePanel.java 
 * Panel utama yang menampilkan asset asset game.
 * Kelas ini menghubungkan antara GamePresenter dan tampilan GUI/GameView.
 * Kelas ini juga menangani interaksi pengguna seperti klik mouse untuk melempar lasso
 * Kelas tidak boleh mengakses model secara langsung, hanya melalui GamePresenter.
*/

public class GamePanel extends JPanel {
    private GamePresenter presenter;                                                // Presenter yang mengelola logika permainan
    private Image[] playerWalkImages = new Image[2];                                // Array untuk menyimpan gambar animasi berjalan player
    private Image playerIdleImage;                                                  // Gambar idle player (tidak bergerak)       
    private Image backgroundImage;                                                  // Gambar background permainan    
    private Image keranjangImg;                                                     // Gambar keranjang untuk menampung bola
    private Image bolaImg;                                                          // Gambar bola yang dilempar

    // Konstruktor GamePanel
    public GamePanel() {
        addKeyListener(new KeyAdapter() {                                           // Tambahkan key listener ke GamePanel
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        presenter.quitGame();                                       // Kembali ke menu utama saat tombol SPACE ditekan
                        break;
                    case KeyEvent.VK_ESCAPE:
                        presenter.quitGame();                                       // Kembali ke menu utama saat tombol ESC ditekan
                        break;
                    case KeyEvent.VK_P:
                        presenter.pauseGame();                                      // Pause game saat tombol P ditekan
                        break;
                    default:
                        presenter.handleKeyPress(e.getKeyCode());
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP ||
                    e.getKeyCode() == KeyEvent.VK_DOWN ||
                    e.getKeyCode() == KeyEvent.VK_LEFT ||
                    e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    presenter.setPlayerWalking(false);                      // Set walking ke false saat tombol arah dilepas
                }
            }
        });
        addMouseListener(new MouseAdapter() {                                       // tambahkan mouse listener ke GamePanel
            @Override
            public void mouseClicked(MouseEvent e) {
                presenter.throwLasso(e.getX(), e.getY());                           // Tangani klik mouse: lempar lasso ke titik klik
            }
        });

        // Load gambar sekali saja untuk rendering
        playerWalkImages[0] = new ImageIcon("assets/player-walking/player-walking 1.png").getImage();
        playerWalkImages[1] = new ImageIcon("assets/player-walking/player-walking 2.png").getImage();
        playerIdleImage = new ImageIcon("assets/player-idle.png").getImage();
        backgroundImage = new ImageIcon("assets/Bg.png").getImage();
        keranjangImg = new ImageIcon("assets/keranjang.png").getImage();
        bolaImg = new ImageIcon("assets/soulball.png").getImage();
    }

    
    @Override

    // metode utama untuk merender seluruh tampilan game
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (presenter != null) {
            drawBackground(g);
            drawGameObjects(g);
        }
    }
    
    // metode untuk menggambar background
    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    // satukan seluruh Metode untuk menggambar objek game; hanya menggambar objek karakter, bola, lasso, dan keranjang
    private void drawGameObjects(Graphics g) {
        renderPlayer(g);
        renderBalls(g);
        renderLassso(g);
        renderKeranjang(g);
    }
    
    // Metode untuk menggambar player
    private void renderPlayer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        Rectangle playerRect = presenter.getPlayerRectangle();
        Image playerImage;

        // Ambil gambar tergantung status
        if (presenter.isPlayerWalking()) {
            int frame = presenter.getPlayerWalkFrame();
            playerImage = playerWalkImages[frame % playerWalkImages.length];
        } else {
            playerImage = playerIdleImage;
        }
        
        // Cek arah menghadap (butuh flag di presenter/player)
        boolean facingLeft = presenter.isPlayerFacingLeft(); 

        if (facingLeft) { // Flip gambar ke kiri
            g2d.drawImage(playerImage,
                playerRect.x + playerRect.width, playerRect.y,
                -playerRect.width, playerRect.height,
                this);
            } else { // Gambar normal}
            g2d.drawImage(playerImage,
            playerRect.x, playerRect.y,
            playerRect.width, playerRect.height,
            this);
        }
    }
    
    // Metode untuk menggambar bola
    public void renderBalls(Graphics g) {
        Graphics2D balls = (Graphics2D) g;
        for (GamePresenter.BallInfo info : presenter.getBallInfos()) {
            if (bolaImg != null) {
                balls.drawImage(bolaImg, info.x, info.y, info.width, info.height, null);
            } else {
                balls.setColor(Color.GREEN);
                balls.fillOval(info.x, info.y, info.width, info.height);
            }
            
            // Tampilkan scoreValue di tengah bola
            String scoreStr = String.valueOf(info.scoreValue);
            int strWidth = g.getFontMetrics().stringWidth(scoreStr);
            int strHeight = g.getFontMetrics().getAscent();
            int centerX = info.x + (info.width - strWidth) / 2;
            int centerY = info.y + (info.height + strHeight) / 2 - 3;
            g.setColor(Color.BLACK);
            g.setFont(getFont().deriveFont(18f)); // Set font size
            g.drawString(scoreStr, centerX, centerY);
        }
    }
    
    // Metode untuk menggambar lasso
    public void renderLassso(Graphics g){
        if (presenter.isLassoActive()) {
            Point playerCenter = presenter.getPlayerCenter();
            Point lassoPoint = presenter.getLassoPoint();
            g.setColor(Color.YELLOW);
            g.drawLine(playerCenter.x, playerCenter.y, lassoPoint.x, lassoPoint.y);
        }
    }

    // Metode untuk menggambar keranjang
    public void renderKeranjang(Graphics g){
        Rectangle keranjang = presenter.getKeranjangBound();
        if (keranjangImg != null) {
            g.drawImage(keranjangImg, keranjang.x, keranjang.y, keranjang.width, keranjang.height, this);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(keranjang.x, keranjang.y, keranjang.width, keranjang.height);
        }
    }

    // === Getter dan Setter untuk presenter ===
    public void setPresenter(GamePresenter presenter) { this.presenter = presenter; }
    public GamePresenter getPresenter() { return presenter; }
}
