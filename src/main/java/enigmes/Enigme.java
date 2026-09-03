package enigmes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;

public class Enigme {

    private String salle;
    private boolean porteFerme = false;
    private List<String> phrases = new ArrayList<>();
    private String enigmes = "./src/resources/Enigme.csv";
    private String phrase = "./src/resources/Phrase.csv";
    private Random random = new Random();

    public Enigme (String salle) {
        this.salle = salle;
        try (Scanner sc = new Scanner (new File (phrase))){
            while (sc.hasNextLine()) {
                phrases.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichier Non Trouver.");
        }
    }

    public String getSalle() {
        return this.salle;
    }

    public void setPorteFerme (Boolean b) {
        this.porteFerme = b;
    }

    public String getEnigme() throws FileNotFoundException, IOException {
        String enigme = "";
        String piece = "";
        try (Scanner sc = new Scanner (new File (enigmes))) {
            while (sc.hasNextLine() && !piece.equals(this.salle)) {
                String ligne = sc.nextLine();
                Scanner scan = new Scanner (ligne);
                scan.useDelimiter(",");
                piece = scan.next();
                enigme = scan.next();
            }
        } catch (FileNotFoundException flfe) {
            flfe.printStackTrace();
        }
        return enigme;
    }

    public String getPhrase() {
        return phrases.get(random.nextInt(phrases.size()-1)+1);
    }

    public String choisirSalle (String nomSalle) throws FileNotFoundException, IOException {
        if (nomSalle.equals(this.salle) && porteFerme) {
            return this.getEnigme();
        }
        return this.getPhrase();
    }

}