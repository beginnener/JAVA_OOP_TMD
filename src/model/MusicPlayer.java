package src.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * MusicPlayer.java
 * Kelas yang mengelola pemutaran musid dalam game, menggunakan Java Sound API.
 * Kelas ini dapat memutar musik dalam mode loop atau satu kali, serta menyediakan fungsi untuk menghentikan, menjeda, dan melanjutkan pemutaran musik.
 */
public class MusicPlayer {
    private Clip clip;

    public void play(String filePath, boolean loop) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loop) {
                System.out.println("Playing music in loop: " + filePath);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }
}
