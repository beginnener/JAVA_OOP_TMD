package src.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* 
 *  Model untuk mengelola data pengguna
 *  Kelas ini menyediakan metode untuk mendapatkan semua pengguna, mendapatkan pengguna berdasarkan nama,
 *  dan menyimpan atau memperbarui data pengguna.
 */
public class UserModel {

    // method untuk mendapatkan isi dari seluruh thasil
    public List<UserScore> getAllUsers() {
        List<UserScore> list = new ArrayList<>();
        String query = "SELECT * FROM thasil ORDER BY skor DESC";                                           // ambil semua data dari tabel thasil dan mengurutkannya berdasarkan skor secara menurun

        try (PreparedStatement ps = DBConnection.prepareStatement(query);                                   // panggil method static preparedStatement dari DBConnection
             ResultSet rs = ps.executeQuery()) {                                                            // eksekusi query dan simpan hasilnya ke ResultSet

            while (rs.next()) {                                                                             // iterasi setiap baris hasil dan buat objek UserScore
                list.add(new UserScore(
                    rs.getString("username"),                                                   // ambil username dari kolom "username"
                    rs.getInt("skor"),                                                          // ambil skor dari kolom "skor"
                    rs.getInt("count")                                                          // ambil count dari kolom "count"                      
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list; // return daftar UserScore yang berisi semua pengguna
    }

    // method untuk mendapatkan user berdasarkan username
    public UserScore getUser(String username) { // akses method static getUser dari kelas UserScore
        String query = "SELECT * FROM thasil WHERE username = ?";                                           // query untuk mengambil data user berdasarkan username
        try (PreparedStatement ps = DBConnection.prepareStatement(query)) {                                 // gunakan prepared statement untuk menghindari SQL injection
            ps.setString(1, username);                                                       // set parameter pertama dari prepared statement dengan username yang diberikan
            ResultSet rs = ps.executeQuery();                                                               // eksekusi query dan simpan hasilnya ke ResultSet
            if (rs.next()) {                                                                                // jika data ditemukan, maka buat dan kembalikan objek UserScore  
                return new UserScore(
                    rs.getString("username"),
                    rs.getInt("skor"),
                    rs.getInt("count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // method untuk menyimpan user baru ke dalam database
    public void insertUser(UserScore user) {
        String insert = "INSERT INTO thasil (username, skor, count) VALUES (?, ?, ?)";                       // query untuk memasukkan data user baru
        try (PreparedStatement ps = DBConnection.prepareStatement(insert)) {                                  // gunakan prepared statement untuk menghindari SQL injection
            ps.setString(1, user.getUsername());                                                  // set parameter pertama dengan username
            ps.setInt(2, user.getScore());                                                        // set parameter kedua dengan skor
            ps.setInt(3, user.getCount());                                                        // set parameter ketiga dengan count
            ps.executeUpdate();                                                                      // eksekusi query insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    // method untuk menyimpan atau memperbarui user; jika user belum ada, data akan disimpan, jika sudah ada data akan diupdate
    public void updateUser(UserScore user) {
        UserScore existing = getUser(user.getUsername());                                                   // cek apakah user sudah ada di database dengan memanggil method getUser()

        try {
            if (user.getScore() > existing.getScore()) {                                              // jika user sudah ada dan skor baru lebih tinggi dari skor yang ada, maka update data
                String update = "UPDATE thasil SET skor = ?, count = ? WHERE username = ?";                  // jalankann query update
                try (PreparedStatement ps = DBConnection.prepareStatement(update)) {                         // gunakan prepared statement untuk menghindari SQL injection
                    ps.setInt(1, user.getScore());
                    ps.setInt(2, user.getCount());
                    ps.setString(3, user.getUsername());
                    ps.executeUpdate();                                                                      // eksekusi query update                                       
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}