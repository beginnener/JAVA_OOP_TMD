package src.model;

import java.awt.Rectangle;

/*
 * Keranjang.java
 * Kelas ini merepresentasikan keranjang dalam permainan.
 * Kelas ini menyimpan informasi tentang posisi, ukuran keranjang, dan menyediakan metode untuk mendapatkan
 */
public class Keranjang {
    private int x, y;                                                       // posisi keranjang
    private int width, height;                                              // ukuran keranjang

    // konstruktor
    public Keranjang(int x, int y, int width, int height) {                
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // === getter setter ===
    // mendapatkan dan mengatur posisi x
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    // mendapatkan dan mengatur posisi y
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    // mendapatkan dan mengatur ukuran keranjang
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    // mendapatkan dan mengatur tinggi keranjang
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    // mengatur posisi keranjang
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // mendapatkan rect keranjang
    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
