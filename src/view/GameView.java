package src.view;

import javax.swing.*;
import java.awt.*;
import src.presenter.GamePresenter;

/**
 * GameView adalah panel utama yang menampilkan seluruh tampilan permainan.
 * Ini termasuk GamePanel (area gameplay) dan HUDPanel (informasi seperti skor).
 * GameView terhubung dengan GamePresenter untuk mengelola logika permainan.
 */
public class GameView extends JPanel {
    private final GamePresenter presenter;
    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;           // HUD Panel untuk info game (skor, waktu, dll)
    private final JFrame parentFrame;
    private JPanel gameOverPanel;
    private final JLayeredPane layeredPane;


    public GameView(JFrame frame, String username) {
        this.parentFrame = frame;

        // Inisialisasi presenter
        this.presenter = new GamePresenter(this, username);

        // Inisialisasi panel-panel
        this.gamePanel = new GamePanel();
        this.gamePanel.setPreferredSize(new Dimension(800, 600));

        this.hudPanel = new HUDPanel(presenter); // HUDPanel terhubung ke presenter

        // Hubungkan presenter ke gamePanel
        gamePanel.setPresenter(presenter);

        // Gunakan layeredPane
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        layeredPane.setLayout(null);

        // set bound gamepanel dan hudPanel
        hudPanel.setBounds(0, 0, 800, 50); // Atur ukuran HUDPanel
        gamePanel.setBounds(0, 0, 800, 600);

        // Gunakan panel biasa untuk layout hud panel dan game panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.setBounds(0, 0, 800, 600);
        containerPanel.add(hudPanel, BorderLayout.NORTH);
        containerPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Tambahkan panel ke layeredPane
        layeredPane.add(containerPanel, JLayeredPane.DEFAULT_LAYER);
        
        // Tambahkan layeredPane ke GameView
        setLayout(new BorderLayout());
        add(layeredPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 600));

        // Fokus input ke gamePanel
        gamePanel.setFocusable(true);
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());

        setVisible(true);

        // Mulai permainan
        presenter.startGame();

        parentFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                presenter.saveScoreOnClose();
                parentFrame.dispose();
            }
        });
    }

    // Getter
    public GamePanel getGamePanel() { return gamePanel; }

    public HUDPanel getHUDPanel() { return hudPanel; }

    public JFrame getParentFrame() { return parentFrame; }

    public GamePresenter getPresenter() { return presenter; }

    
    public void showGameOverOverlay(int score, String username) {
        gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setOpaque(true);
        gameOverPanel.setBackground(new Color(0, 0, 0, 180));
        gameOverPanel.setBounds(200, 150, 400, 300);

        JLabel gameOverLabel = new JLabel("Waktu Habis");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retryButton = new JButton("Main Lagi");
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> {
            parentFrame.setContentPane(new GameView(parentFrame, username));
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JButton exitButton = new JButton("Keluar");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuView(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        gameOverPanel.add(Box.createVerticalStrut(100));
        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(scoreLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(retryButton);
        gameOverPanel.add(Box.createVerticalStrut(10));
        gameOverPanel.add(exitButton);

        layeredPane.add(gameOverPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    // tampilan overlay ketika permainan dijeda
    public void pausedOverlay() {
        JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 120));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(null);
        overlayPanel.setBounds(0, 0, 800, 600);

        JLabel pausedLabel = new JLabel("GAME PAUSED");
        pausedLabel.setFont(new Font("Arial", Font.BOLD, 48));
        pausedLabel.setForeground(Color.WHITE);
        pausedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pausedLabel.setBounds(0, 250, 800, 100);
        
        overlayPanel.add(pausedLabel);
        layeredPane.add(overlayPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    // Menghapus overlay paused jika ada
    public void removePausedOverlay() {
        for (Component comp : layeredPane.getComponentsInLayer(JLayeredPane.MODAL_LAYER)) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
                layeredPane.remove(comp);
            }
        }
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    // Menutup game view dan kembali ke menu utama
    public void close() {
        parentFrame.getContentPane().removeAll();
        parentFrame.setContentPane(new MainMenuView(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    
    // Menampilkan pesan sistem
    public void showError(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}