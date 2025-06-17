package src.model;

/*
 * Kelas ini merepresentasikan entitas pemain dalam permainan
 * Kelas ini digunakan untuk menyimpan informasi tentang state, posisi pemain, skor, dan jumlah bola yang terkumpul
 * Kelas ini akan digunakan oleh GamePresenter untuk mengelola logika permainan
 */
public class Player {
    private int x; // posisi horizontal pemain
    private int y; // posisi vertikal pemain
    private int score; // skor pemain
    private int count;
    private int speed = 10; // kecepatan gerakan pemain
    private Direction direction; // arah gerakan pemain
    private State state; // state pemain
    private boolean isPowerupActive; // status power-up

    public enum Direction { UP, DOWN, LEFT, RIGHT, NONE }
    public enum State { IDLE, MOVING, SHOOTING}

    // Konstruktor
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.score = 0;
        this.count = 0;
        this.direction = Direction.NONE;
        this.state = State.IDLE;
        this.isPowerupActive = false;
    }

    // Getters and Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public boolean isPowerupActive() { return isPowerupActive; }
    public void setPowerupActive(boolean powerupActive) { isPowerupActive = powerupActive; }

    @Override
    // Tampilkan log pemain
    public String toString() {
        return "Player{" +
                "x=" + x +
                ", y=" + y +
                ", score=" + score +
                ", count=" + count +
                ", direction=" + direction +
                ", state=" + state +
                ", isPowerupActive=" + isPowerupActive +
                '}';
    }

    // Method untuk menggerakkan pemain
    public void move(Direction direction) {
        this.direction = direction; // Set arah gerakan pemain
        this.state = State.MOVING; // Set state ke MOVING saat pemain bergerak
        
        switch (direction) {
            case UP:
                y -= speed; // Gerakkan pemain ke atas
                break;
            case DOWN:
                y += speed; // Gerakkan pemain ke bawah
                break;  
            case LEFT:
                x -= speed; // Gerakkan pemain ke kiri
                break;
            case RIGHT:
                x += speed; // Gerakkan pemain ke kanan
                break;
            case NONE:
                this.state = State.IDLE; // Set state ke IDLE jika tidak ada gerakan
                break;
            default:
                break;
        }
    }
    
    // Method untuk atur state menembak
    public void shoot() {
        if (state != State.SHOOTING) {
            this.state = State.SHOOTING; // Set state ke SHOOTING saat pemain menembak
        }
    }

    // Method untuk 
}
