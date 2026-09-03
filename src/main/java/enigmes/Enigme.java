package enigmes;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Une enigme donnee par un PNJ : une question dont la reponse est le nom
 * de la salle ou se trouve la cle d'une porte fermee.
 *
 * Les textes des enigmes sont ranges dans resources/Enigme.csv
 * (une ligne = "Salle,Enigme").
 */
public class Enigme {

    private final String question;
    private final String salleDeLaCle;

    public Enigme(String question, String salleDeLaCle) {
        this.question = question;
        this.salleDeLaCle = salleDeLaCle;
    }

    public String getQuestion() {
        return question;
    }

    public String getSalleDeLaCle() {
        return salleDeLaCle;
    }

    /** Construit l'enigme de la salle demandee a partir du fichier Enigme.csv. */
    public static Enigme pourSalle(String salleDeLaCle) {
        InputStream fichier = Enigme.class.getResourceAsStream("/Enigme.csv");
        if (fichier == null) {
            throw new IllegalStateException("Enigme.csv introuvable dans les resources");
        }

        Scanner lecteur = new Scanner(fichier, "UTF-8");
        lecteur.nextLine(); // on saute l'en-tete "Salle,Enigme"

        while (lecteur.hasNextLine()) {
            String ligne = lecteur.nextLine();
            int virgule = ligne.indexOf(',');
            if (virgule < 0) {
                continue;
            }
            String salle = ligne.substring(0, virgule).trim();
            String texte = ligne.substring(virgule + 1).trim();
            if (salle.equals(salleDeLaCle)) {
                lecteur.close();
                return new Enigme(texte, salle);
            }
        }

        lecteur.close();
        throw new IllegalArgumentException("Pas d'enigme pour la salle : " + salleDeLaCle);
    }
}
