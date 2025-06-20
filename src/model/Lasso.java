package src.model;

import java.awt.Rectangle;

/*
 * Lasso.java
 * Kelas ini merepresentasikan lasso yang digunakan untuk menangkap bola dalam permainan.
 * Kelas ini menyimpan informasi tentang posisi, status, kecepatan, jangkauan lasso,
 * dan menyediakan metode untuk melempar lasso, mengambil bola, dan mengembalikan lasso ke pemain.
 * Kelas ini juga menangani animasi lemparan lasso dan pengembalian lasso ke pemain.
 */
public class Lasso {
    // atribut dasar
    private int x, y;                                                                           // posisi lasso
    private boolean active;                                                                     // status lasso (aktif/tidak)
    private int speed;                                                                          // kecepatan lasso
    private int range;                                                                          // jangkauan lasso

    // Tambahan untuk animasi
    private int targetX, targetY;                                                               // titik tujuan lempar
    private int originX, originY;                                                               // posisi player saat dilempar
    private boolean throwing = false;                                                           // status melempar lasso

    // Bola yang sedang ditarik
    private SkillBall grabbedBall = null;                                                       // instansiasi bola yang sedang di-grab

    // konstruktor
    public Lasso(int x, int y) {        
        this.x = x;
        this.y = y;
        this.active = false;
        this.speed = 15;                                                                        // kecepatan default
        this.range = 300;                                                                       // jangkauan default dalam pixel
    }

    // Lempar lasso ke target
    public void throwTo(int targetX, int targetY, int originX, int originY) {
        if (!throwing) {                                                                // jika lasso tidak sedang dilempar atau mengembalikan
            this.targetX = targetX;                                                         // set koordinat target x
            this.targetY = targetY;                                                         // set koordinat target y
            this.originX = originX;                                                         // set posisi awal player saat melempar 
            this.originY = originY;                                                         // set posisi awal player saat melempar      
            this.active = true;                                                             // set status lasso aktif 
            this.throwing = true;                                                           // set status melempar lasso true
        }
    }

    // method untuk mengambil bola  
    public void grabBall(SkillBall ball) {
        this.grabbedBall = ball;
    }

    // method untuk mendapatkan bola yang sedang di-grab
    public SkillBall getGrabbedBall() {
        return grabbedBall;
    }

    // method untuk menghapus bola yang sedang di-grab
    public void clearGrabbedBall() {
        this.grabbedBall = null;
    }

    // method untuk mengembalikan lasso ke player (returning)
    public void update() {
        if (throwing) {                                                                     // jika status lasso sedang dilempar
            // Bergerak menuju target                   
            double dx = targetX - x;                                                            // perhitungan jarak x ke target
            double dy = targetY - y;                                                            // perhitungan jarak y ke target
            double dist = Math.sqrt(dx*dx + dy*dy);                                             // hitung jarak ke target
            if (dist <= speed) {                                                                // jika jarak ke target kurang dari atau sama dengan kecepatan
                x = targetX;                                                                        // set posisi x ke target
                y = targetY;                                                                        // set posisi y ke target
                throwing = false;                                                                   // set status melempar lasso menjadi false
            } else {                                                                            // jika jarak ke target lebih dari kecepatan
                x += (int)(speed * dx / dist);                                                      // gerakkan lasso ke arah target
                y += (int)(speed * dy / dist);                                                      // gerakkan lasso ke arah target
            }
        } else{                                                             // jika lasso sedang mengembalikan
            // Bergerak kembali ke player
            double dx = originX - x;                                                            // perhitungan jarak x ke origin
            double dy = originY - y;                                                            // perhitungan jarak y ke origin   
            double dist = Math.sqrt(dx*dx + dy*dy);                                             // hitung jarak ke origin
            if (dist <= speed) {                                                                // jika jarak ke origin kurang dari atau sama dengan kecepatan
                x = originX;                                                                        // set posisi x ke origin
                y = originY;                                                                        // set posisi y ke origin
                active = false;                                                                     // set status lasso tidak aktif 
            } else {                                                                            // jika jarak ke origin lebih dari kecepatan
                x += (int)(speed * dx / dist);                                                      // gerakkan lasso ke arah origin
                y += (int)(speed * dy / dist);                                                      // gerakkan lasso ke arah origin
            }
            // Jika sedang menarik bola
            if (grabbedBall != null) {                                                          // jika ada bola yang sedang di-grab
                // Gerakkan bola menuju origin (player)                  
                double dxBall = originX - grabbedBall.getX();                                       // perhitungan jarak x bola ke origin
                double dyBall = originY - grabbedBall.getY();                                       // perhitungan jarak y bola ke origin
                double distBall = Math.sqrt(dxBall*dxBall + dyBall*dyBall);                         // hitung jarak bola ke origin
                if (distBall <= speed) {                                                            // jika jarak bola ke origin kurang dari atau sama dengan kecepatan
                    grabbedBall.setPosition(originX, originY);                                          // set posisi bola ke origin (player)
                } else {                                                                            // jika jarak bola ke origin lebih dari kecepatan
                    grabbedBall.setPosition(                                                            // gerakkan bola ke arah origin
                        grabbedBall.getX() + (int)(speed * dxBall / distBall),                           // set posisi x bola
                        grabbedBall.getY() + (int)(speed * dyBall / distBall)                            // set posisi y bola
                    );
                }
            }
        }
    }

    // === Getter dan Setter ===
    public boolean isActive() { return active; }
    public boolean isThrowing() { return throwing; }
    public void setThrowing(boolean throwing) {
        this.throwing = throwing; 
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public int getRange() { return range; }
    public Rectangle getBounds() {
        return new Rectangle(x - 4, y - 4, 8, 8); // Mengembalikan rectangle untuk bounding box lasso
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setRange(int range) { this.range = range; }
}
