package enigmes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

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
    private boolean porteFerme = false;
    private List<String> phrases = new ArrayList<>();
    private String phrase = "./res/Phrase.csv";
    private Random random = new Random();

    public Enigme(String question, String salleDeLaCle) {
        this.question = question;
        this.salleDeLaCle = salleDeLaCle;
        try (Scanner sc = new Scanner (new File (phrase))){
            while (sc.hasNextLine()) {
                phrases.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichier Non Trouver.");
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getSalleDeLaCle() {
        return salleDeLaCle;
    }

    public void setPorteFerme (Boolean b) {
        this.porteFerme = b;
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

    public String getPhrase() {
        return phrases.get(random.nextInt(phrases.size()-1)+1);
    }

    /*à revoir*/
    /*public String enigmePourPnj (String nomSalle) throws FileNotFoundException, IOException {
        if (nomSalle.equals(this.salle) && porteFerme) {
            return this.getEnigme();
        }
        return this.getPhrase();
    }*/

}
