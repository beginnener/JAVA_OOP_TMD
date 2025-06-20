package src.model;

import java.awt.Rectangle;

public class Keranjang {
    private int x, y;                                                       // posisi keranjang
    private int width, height;                                              // ukuran keranjang

    public Keranjang(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
