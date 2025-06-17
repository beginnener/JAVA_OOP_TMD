package src.model;


/*
 *  Kelas ini merepresentasikan entitas pengguna dalam permainan
 *  Kelas ini menyimpan informasi tentang nama pengguna, skor, dan banyak bola yang terkumpul
 */
public class UserScore {
    private String username;
    private int score;
    private int count;

    // Constructor
    public UserScore(String username, int score, int count) {
        this.username = username;
        this.score = score;
        this.count = count;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    @Override
    public String toString() { // untuk menampilkan log user
        return "UserScore{" +
                "username='" + username + '\'' +
                ", score=" + score +
                ", count=" + count +
                '}';
    }
}
