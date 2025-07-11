package src.model;

import java.sql.*;

/*
 * DBConnection.java
 * Kelas ini bertugas untuk mengelola koneksi ke database MySQL
 * Kelas ini menyediakan metode untuk eksekusi query update dan mengambil prepared statement
 * Kelas ini juga membuat tabel 'thasil' jika belum ada
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_tmd";                  // inisiasi url database
    private static final String USER = "root";                                                  // inisiasi username database
    private static final String PASS = "";                                                      // inisiasi password database, kosongkan jika tidak ada password                                    

    private static Connection connection;                                                       // buat variabel connection untuk menyimpan koneksi dari database       

    // Static block untuk inisialisasi satu kali
    static {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method untuk membuat tabel thasil jika belum ada
    private static void createTableIfNotExists() throws SQLException { 
        String sql = """
            CREATE TABLE IF NOT EXISTS thasil (
                username VARCHAR(100) PRIMARY KEY,
                skor INT,
                count INT
            )
        """;
        executeUpdate(sql);
    }

    // Method global untuk eksekusi update query
    public static void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    // Method global untuk mengambil prepared statement
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    // Method untuk menutup koneksi ketika game selesai
    // Pastikan untuk memanggil metode ini di akhir permainan atau saat aplikasi ditutup
    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}