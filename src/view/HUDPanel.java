package src.view;

import javax.swing.*;
import java.awt.*;
import src.presenter.GamePresenter;

/**
 * Panel khusus untuk menampilkan informasi HUD (skor, waktu, dll).
 */
public class HUDPanel extends JPanel {
    private final GamePresenter presenter;

    public HUDPanel(GamePresenter presenter) {
        this.presenter = presenter;
        setPreferredSize(new Dimension(800, 40));
        setBackground(Color.LIGHT_GRAY); // Gaya HUD
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + presenter.getScore(), 10, 25);
        g.drawString("Count: " + presenter.getCount(), 150, 25);
        // Tambahkan elemen lain seperti timer jika perlu
    }
}
