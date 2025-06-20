package src;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import src.view.MainMenuView;

public class Main {
    public static void main(String[] args) {
        
        // buat frame
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Road to the future");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new MainMenuView(frame)); // memanggil MainMenuView diawal
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}