package src.model;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

public class SkillBall {
    private int x, y;
    private int width, height;
    private String label;
    private int speed;
    private int scoreValue;
    private Image image;
    private boolean movingLeft;  // arah gerak bola
    public boolean delivered = false; // status bola sedang ke keranjang atau tidak 

    // konstruktor
    public SkillBall(String label, Image image, int scoreValue) {
        // y akan diacak antara 20~40 atau 450~480
        Random rand = new Random();
        if (rand.nextBoolean()) { this.y = rand.nextInt(5) * 5 + 20; } 
        else { this.y = rand.nextInt(7) * 5 + 450; }
        this.width = 40 + (scoreValue / 2);
        this.height = 40 + (scoreValue / 2);
        this.label = label;
        this.image = image;
        this.scoreValue = scoreValue;
        
        if(y >= 20 && y <= 40) this.movingLeft = false; // jika bola muncul di atas, arah gerak bola ke kanan
        else this.movingLeft = true; // jika bola muncul di bawah, arah gerak bola ke kiri
        this.speed = new Random().nextInt(3) + 2;  // kecepatan acak

        if (movingLeft) { this.x = 800; } // muncul dari kanan layar 
        else { this.x = -width; } // muncul dari kiri layar

        this.delivered = false; // status bola belum diambil
    }

    // metode untuk menggerakkan bola
    public void move() {
        x += movingLeft ? -speed : speed; // cek apakah bola bergerak kekiri atau kanan, jika kiri kurangi speed, jika kanan tambahkan speed 
    }

    // getter setter
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getScoreValue() { return scoreValue; }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public Image getImage() { return image; }
    
    public int getWidth() { return width; }
    
    public int getHeight() { return height; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public boolean isMovingLeft() { return movingLeft; }
    public void setMovingLeft(boolean movingLeft) { // jika bola sudah diambil, set arah gerak bola
        this.movingLeft = movingLeft;
        this.x = movingLeft ? 800 : -width; // reset posisi sesuai arah
    }

    public boolean isOutOfScreen() {
        return (x < -width || x > 800); // misal width frame = 800
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
}