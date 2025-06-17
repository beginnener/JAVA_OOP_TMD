package src.view;

import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.*;
import src.presenter.MainMenuPresenter;

/*
 *  MainMenuView.java
 *  Kelas ini berfungsi untuk mengatur tampilan menu utama permainan
 *  Kelasi ini menguhubngkan antara MainMenuPresenter dan tampilan GUI
 *  Kelas ini juga menangani interaksi pengguna seperti memasukkan username dan menampilkan skor
 *  Apabila pengguna menekan tombol "Start Game", presenter akan memeriksa apakah username valid
 *  Jika valid, presenter akan mengalihkan tampilan ke game; jika tidak valid, presenter akan menampilkan pesan kesalahan
 */

public class MainMenuView extends JPanel {
    private JFrame parentFrame; // inisiasi Jframe sebagai frame utama
    private JTextField usernameField; // inisiasi JTextField untuk input username
    private JTable scoreTable; // inisiasi JTable untuk menampilkan tabel skor
    private JButton startButton, quitButton; // inisiasi JButton untuk tombol "Start Game" dan "Quit"
    private MainMenuPresenter presenter; // inisiasi presenter untuk menghubungkan tampilan dengan logika bisnis

    // Konstruktor
    public MainMenuView(JFrame frame) {
        this.parentFrame = frame;                                                           // inisiasi parentFrame dengan JFrame yang diberikan
        this.presenter = new MainMenuPresenter(this);                                       // instansiasi presenter dengan MainMenuPresenter dan menghubungkannya dengan MainMenuView

        setLayout(new BorderLayout());

        // Panel atas: input username
        JPanel topPanel = new JPanel();                                                     // inisiasi Jpanel untuk "div" atas
        topPanel.add(new JLabel("Username:"));                                          // inisiasi JLabel untuk label "Username"
        usernameField = new JTextField(100);                                         // inisiasi JTextField untuk input username dengan panjang 15 karakter
        topPanel.add(usernameField);                                                         // menambahkan JTextField ke panel atas
        add(topPanel, BorderLayout.NORTH);                                                      // menambahkan panel atas ke bagian utara dari layout BorderLayout

        // Panel tengah: tabel skor
        scoreTable = new JTable();                                                               // inisiasi JTable untuk menampilkan skor
        add(new JScrollPane(scoreTable), BorderLayout.CENTER);                                  // tambahkan JScrollPane yang membungkus JTable ke bagian tengah dari layout BorderLayout

        // Panel bawah: tombol
        JPanel bottomPanel = new JPanel();                                                      // inisiasi JPanel untuk "div" bawah
        startButton = new JButton("Start Game");                                            // inisiasi JButton untuk tombol "Start Game"
        quitButton = new JButton("Quit");                                                   // inisiasi JButton untuk tombol "Quit"                                

        bottomPanel.add(startButton);                                                           // menambahkan tombol "Start Game" ke panel bawah
        bottomPanel.add(quitButton);                                                            // menambahkan tombol "Quit" ke panel bawah        
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listener
        startButton.addActionListener(e -> presenter.onStartClicked(getUsername()));            // ketika tombol "Start Game" diklik, panggil metode onStartClicked pada presenter dengan username yang dimasukkan
        quitButton.addActionListener(e -> System.exit(0));                              // ketika tombol "Quit" diklik, keluar dari aplikasi

        // Load data dari database
        presenter.loadTable();                                                                  // memanggil metode loadTable pada presenter untuk memuat data skor dari database
    }

    // Metode untuk mendapatkan username dari input field
    public String getUsername() {
        return usernameField.getText().trim();
    }

    // Metode untuk mengatur model tabel skor
    public void setTableModel(TableModel model) {
        scoreTable.setModel(model);                                                             // gunakan kelas dari Jtabel untuk mengisi table skor dengan model yang diberikan
    }

    // Ganti tampilan ke game
    public void switchToGame() {
        parentFrame.setContentPane(new GameView(parentFrame, getUsername()));                   // ganti konten dari parentFrame dengan GameView, mengirimkan parentFrame dan username yang dimasukkan
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
