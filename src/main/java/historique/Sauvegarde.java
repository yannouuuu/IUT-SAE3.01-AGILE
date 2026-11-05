package historique;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

/** Petit utilitaire de lecture/ecriture de fichier, pour la sauvegarde de partie. */
public final class Sauvegarde {

    private Sauvegarde() {
    }

    /** Ecrit le contenu dans le fichier indique (le cree ou le remplace). */
    public static void ecrire(String chemin, String contenu) {
        try (FileWriter fichier = new FileWriter(chemin)) {
            fichier.write(contenu);
            fichier.write("\n");
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible d'ecrire " + chemin, e);
        }
    }

    /** Relit tout le contenu d'un fichier, ligne par ligne. */
    public static String lire(String chemin) {
        StringBuilder resultat = new StringBuilder();

        try (BufferedReader fichier = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            while ((ligne = fichier.readLine()) != null) {
                resultat.append(ligne).append("\n");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de lire " + chemin, e);
        }
        return resultat.toString();
    }
}
