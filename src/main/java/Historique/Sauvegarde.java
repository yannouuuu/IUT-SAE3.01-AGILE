package Historique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Petit utilitaire d'ecriture/lecture de fichier, utilise pour la
 * sauvegarde/chargement de partie (voir moteur.BoucleDeJeu).
 */
public class Sauvegarde extends Exception {

    private final String resultat;

    public Sauvegarde(String resultat) {
        this.resultat = resultat;
    }

    /** Ecrit le contenu dans le fichier indique (le cree ou le remplace). */
    public void sauvegrade(String chemin) {
        try (FileWriter fichier = new FileWriter(new File(chemin))) {
            fichier.write(this.resultat);
            fichier.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Relit tout le contenu d'un fichier, ligne par ligne. */
    public static String importer(String chemin) {
        StringBuilder resultat = new StringBuilder();

        try (BufferedReader fichier = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            while ((ligne = fichier.readLine()) != null) {
                resultat.append(ligne).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultat.toString();
    }
}
