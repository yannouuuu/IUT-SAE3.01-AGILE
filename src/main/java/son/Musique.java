package son;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/** Musique de fond : demarre au lancement, /musique pour couper ou remettre. */
public class Musique {

    private Clip clip;
    private boolean active = false;

    /** Charge le fichier musique.wav et le joue en boucle. */
    public void demarrer() {
        try {
            InputStream fichier = new BufferedInputStream(
                    Musique.class.getResourceAsStream("/musique.wav"));
            AudioInputStream audio = AudioSystem.getAudioInputStream(fichier);
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            active = true;
        } catch (Exception e) {
            System.out.println("Musique indisponible : " + e.getMessage());
        }
    }

    public void basculer() {
        if (clip == null) {
            return;
        }
        if (active) {
            clip.stop();
            active = false;
        } else {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            active = true;
        }
    }

    // arrete et ferme le clip, sinon la musique tourne encore et le programme ne quitte pas
    public void arreter() {
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.close();
    }
}