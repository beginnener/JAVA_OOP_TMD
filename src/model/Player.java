package src.model;

import java.awt.Image;

public class Player {
    private int x, y;                           // posisi player
    private int width, height;        // ukuran player
    private Image image;                        // gambar player
    private boolean walking = false;
    private int walkFrame = 0;
    private int walkFrameCounter = 0;

    public Player(int startX, int startY, int width, int height, Image image) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    // Getter dan setter posisi
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Image getImage() { return image; }

    public boolean isWalking() { return walking; }
    public void setWalking(boolean walking) { this.walking = walking; }
    public int getWalkFrame() { return walkFrame; }
    public void updateWalkFrame() {
        walkFrameCounter++;
        if (walkFrameCounter >= 5) { // ganti frame setiap 5 timer tick
            walkFrame = (walkFrame + 1) % 2;
            walkFrameCounter = 0;
        }
    }
    public void resetWalkFrame() {
        walkFrame = 0;
        walkFrameCounter = 0;
    }

    // metode untuk menggerakan (update) posisi player
    public void move(int dx, int dy) {
        // untuk menetapkan batas gerakan agar tidak keluar dari layar
        if (dx < 0) { dx = Math.max(dx, -x); } // tidak boleh keluar dari kiri layar 
        else if (dx > 0) { dx = Math.min(dx, 784 - x - width); } // tidak boleh keluar dari kanan layar (misal lebar layar 800)
        this.x += dx;
        
        // tetapkan batas gerakan vertikal agar tidak keluar dari layar
        if (dy < 0) { dy = Math.max(dy, -y); } // tidak boleh keluar dari atas layar
        else if (dy > 0) { dy = Math.min(dy, 521 - y - height); } // tidak boleh keluar dari bawah layar (misal tinggi layar 600)
        this.y += dy;
    }
}
