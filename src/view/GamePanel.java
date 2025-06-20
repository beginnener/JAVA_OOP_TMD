package src.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Graphics;
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
    private GamePresenter presenter;
    private boolean paused = false; // Tambahkan variabel untuk status pause

    private Image[] playerWalkImages = new Image[2];
    private Image playerIdleImage;
    private Image backgroundImage;
    private Image keranjangImg;

    public GamePanel() {
        addKeyListener(new KeyAdapter() { // Tambahkan key listener ke GamePanel
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        presenter.quitGame(); // Kembali ke menu utama saat tombol SPACE ditekan
                        break;
                    case KeyEvent.VK_ESCAPE:
                        presenter.quitGame(); // Kembali ke menu utama saat tombol ESC ditekan
                        break;
                    default:
                        presenter.handleKeyPress(e.getKeyCode());
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Jika tombol arah dilepas, set walking ke false
                if (e.getKeyCode() == KeyEvent.VK_UP ||
                    e.getKeyCode() == KeyEvent.VK_DOWN ||
                    e.getKeyCode() == KeyEvent.VK_LEFT ||
                    e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    presenter.setPlayerWalking(false); // Set walking ke false saat tombol arah dilepas
                }
            }
        });
        addMouseListener(new MouseAdapter() { // tambahkan mouse listener ke GamePanel
            @Override
            public void mouseClicked(MouseEvent e) {
                // Tangani klik mouse: lempar lasso ke titik klik
                presenter.throwLasso(e.getX(), e.getY());
                // System.out.println("Mouse clicked at: " + e.getX() + ", " + e.getY());
            }
        });

        // Load gambar sekali saja
        playerWalkImages[0] = new ImageIcon("assets/player-walking/player-walking 1.png").getImage();
        playerWalkImages[1] = new ImageIcon("assets/player-walking/player-walking 2.png").getImage();
        playerIdleImage = new ImageIcon("assets/player-idle.png").getImage();
        backgroundImage = new ImageIcon("assets/Bg.png").getImage();
        keranjangImg = new ImageIcon("assets/keranjang.png").getImage();
    }

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }
    public GamePresenter getPresenter() {
        return presenter;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
        repaint(); // Memperbarui tampilan saat status pause berubah
    }
    public boolean isPaused() {
        return paused;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (presenter != null) {
            // Gambar background, player, bola, lasso, skor
            drawBackground(g);
            drawGameObjects(g);
        }
    }

    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Metode untuk menggambar objek game; hanya menggambar objek karakter, bola, dan lasso
    private void drawGameObjects(Graphics g) {
        // Gambar player
        Rectangle playerRect = presenter.getPlayerRectangle();
        Image playerImage;
        if (presenter.isPlayerWalking()) {
            int frame = presenter.getPlayerWalkFrame();
            playerImage = playerWalkImages[frame % 2];
        } else {
            playerImage = playerIdleImage;
        }
        g.drawImage(playerImage, playerRect.x, playerRect.y, playerRect.width, playerRect.height, this);

        // Gambar bola-bola skill
        for (GamePresenter.BallInfo info : presenter.getBallInfos()) {
            g.setColor(Color.GREEN);
            g.fillOval(info.x, info.y, info.width, info.height);

            // Tampilkan scoreValue di tengah bola
            String scoreStr = String.valueOf(info.scoreValue);
            int strWidth = g.getFontMetrics().stringWidth(scoreStr);
            int strHeight = g.getFontMetrics().getAscent();
            int centerX = info.x + (info.width - strWidth) / 2;
            int centerY = info.y + (info.height + strHeight) / 2 - 3;
            g.setColor(Color.BLACK);
            g.drawString(scoreStr, centerX, centerY);
        }

        // gambar lasso
        if (presenter.isLassoActive()) {
            Point playerCenter = presenter.getPlayerCenter();
            Point lassoPoint = presenter.getLassoPoint();
            g.drawLine(playerCenter.x, playerCenter.y, lassoPoint.x, lassoPoint.y);
        }

        // gambar keranjang
        Rectangle keranjang = presenter.getKeranjangBound();
        if (keranjangImg != null) {
            g.drawImage(keranjangImg, keranjang.x, keranjang.y, keranjang.width, keranjang.height, this);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(keranjang.x, keranjang.y, keranjang.width, keranjang.height);
        }
    }
}
