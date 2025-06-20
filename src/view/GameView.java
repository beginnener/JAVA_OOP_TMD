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

    public GameView(JFrame frame, String username) {
        this.parentFrame = frame;

        // Inisialisasi presenter
        this.presenter = new GamePresenter(this, username);

        // Inisialisasi panel-panel
        this.gamePanel = new GamePanel();
        this.gamePanel.setPreferredSize(new Dimension(800, 600));
        // this.gamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.hudPanel = new HUDPanel(presenter); // HUDPanel terhubung ke presenter

        // Hubungkan presenter ke gamePanel
        gamePanel.setPresenter(presenter);

        // Set layout utama
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        // Panel utama permainan (diletakkan di tengah)
        add(gamePanel, BorderLayout.CENTER);

        // Panel HUD (diletakkan di atas sebagai informasi)
        add(hudPanel, BorderLayout.NORTH);

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

    // Memperbarui tampilan visual
    public void updateView() {
        gamePanel.repaint();
        hudPanel.repaint();
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

    // Menutup game view dan kembali ke menu utama
    public void close() {
        parentFrame.getContentPane().removeAll();
        parentFrame.setContentPane(new MainMenuView(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}