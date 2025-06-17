package src.model;

import java.sql.*;

public class DB{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_tmd";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection connection;

    // lakukan koneksi ke database
    public DB() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        crateTableIfNotExists(); // panggil method untuk membuat tabel jika belum ada    
    }

    // method untuk membuat tabel database apabila tabel "thasil" belum ada
    private void crateTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS thasil (" +
                     "username VARCHAR(100)," +
                     "skor INT," +
                     "count INT," +
                     ")";
        try (Statement stmt = connection.createStatement()) { // gunakan statement untuk eksekusi sql
            stmt.executeUpdate(sql);
        }
    }

    // method untuk insert data game ke dalam tabel "thasil"
    public void saveScore(String username, int score, int count) throws SQLException {
        String sql = "INSERT INTO thasil (username, skor, count) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { // gunakan PreparedStatement untuk menghindari SQL Injection
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.setInt(3, count);
            pstmt.executeUpdate();
        }
    }

    // method untuk menutup koneksi ke database
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}