package src.model;

import java.awt.Rectangle;

public class Lasso {
    private int x, y;                                                                           // posisi lasso
    private boolean active;                                                                     // status lasso (aktif/tidak)
    private int speed;                                                                          // kecepatan lasso
    private int range;                                                                          // jangkauan lasso

    // Tambahan untuk animasi
    private int targetX, targetY;                                                               // titik tujuan lempar
    private int originX, originY;                                                               // posisi player saat dilempar
    private boolean throwing = false;                                                           // status melempar lasso
    private boolean returning = false;                                                  // status mengembalikan lasso                      

    // Bola yang sedang ditarik
    private SkillBall grabbedBall = null;                                                       // instansiasi bola yang sedang di-grab

    // konstruktor
    public Lasso(int x, int y) {        
        this.x = x;
        this.y = y;
        this.active = false;
        this.speed = 10; // kecepatan default
        this.range = 300; // jangkauan default dalam pixel
    }

    // Lempar lasso ke target
    public void throwTo(int targetX, int targetY, int originX, int originY) {
        if (!throwing && !returning) {                                                  // jika lasso tidak sedang dilempar atau mengembalikan
            this.targetX = targetX;                                                         // set target koordinat x
            this.targetY = targetY;                                                         // set target koordinat y
            this.originX = originX;                                                         // set posisi awal player saat melempar 
            this.originY = originY;                                                         // set posisi awal player saat melempar      
            this.active = true;                                                             // set status lasso aktif 
            this.throwing = true;                                                           // set status melempar lasso     
            this.returning = false;                                                         // set status mengembalikan lasso
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

    // method untuk mengembalikan lasso ke player
    public void update() {
        if (throwing) {                                                                // jika lasso sedang dilempar
            // Bergerak menuju target                   
            double dx = targetX - x;                                                        // perhitungan jarak x ke target
            double dy = targetY - y;                                                        // perhitungan jarak y ke target
            double dist = Math.sqrt(dx*dx + dy*dy);                                        // hitung jarak ke target
            if (dist <= speed) {                                                            // jika jarak ke target kurang dari atau sama dengan kecepatan
                x = targetX;                                                                    // set posisi x ke target
                y = targetY;                                                                    // set posisi y ke target
                throwing = false;                                                               // set status melempar lasso menjadi false
                returning = true;                                                               // set status mengembalikan lasso menjadi true
            } else {                                                                        // jika jarak ke target lebih dari kecepatan
                x += (int)(speed * dx / dist);                                                  // gerakkan lasso ke arah target
                y += (int)(speed * dy / dist);                                                   // gerakkan lasso ke arah target
            }
        } else if (returning) {                                                         // jika lasso sedang mengembalikan
            // Bergerak kembali ke player
            double dx = originX - x;                                                      // perhitungan jarak x ke origin
            double dy = originY - y;                                                      // perhitungan jarak y ke origin   
            double dist = Math.sqrt(dx*dx + dy*dy);                                      // hitung jarak ke origin
            if (dist <= speed) {                                                         // jika jarak ke origin kurang dari atau sama dengan kecepatan
                x = originX;                                                                    // set posisi x ke origin
                y = originY;                                                                    // set posisi y ke origin
                returning = false;                                                       // set status mengembalikan lasso menjadi false
                active = false;                                                          // set status lasso tidak aktif 
            } else {                                                                  // jika jarak ke origin lebih dari kecepatan
                x += (int)(speed * dx / dist);                                           // gerakkan lasso ke arah origin
                y += (int)(speed * dy / dist);                                           // gerakkan lasso ke arah origin
            }
            // Jika sedang menarik bola
            if (grabbedBall != null) {                                              // jika ada bola yang sedang di-grab
                // Gerakkan bola menuju origin (player)                  
                double dxBall = originX - grabbedBall.getX();                           // perhitungan jarak x bola ke origin
                double dyBall = originY - grabbedBall.getY();                           // perhitungan jarak y bola ke origin
                double distBall = Math.sqrt(dxBall*dxBall + dyBall*dyBall);             // hitung jarak bola ke origin
                if (distBall <= speed) {                                                // jika jarak bola ke origin kurang dari atau sama dengan kecepatan
                    grabbedBall.setPosition(originX, originY);                          // set posisi bola ke origin (player)
                } else {                                                                // jika jarak bola ke origin lebih dari kecepatan
                    grabbedBall.setPosition(                                                 // gerakkan bola ke arah origin
                        grabbedBall.getX() + (int)(speed * dxBall / distBall),               // set posisi x bola
                        grabbedBall.getY() + (int)(speed * dyBall / distBall)                // set posisi y bola
                    );
                }
            }
        } else {
            // Setelah kembali, reset/hapus bola yang di-grab
            if (grabbedBall != null) {
                grabbedBall = null;
            }
        }
    }

    public boolean isActive() { return active; }

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
