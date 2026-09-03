package interaction;

import java.util.Scanner;

// lit ce que tape l'utilisateur
public class Saisie {

    private final Scanner clavier;
    private boolean terminee = false;

    public Saisie(Scanner clavier) {
        this.clavier = clavier;
    }

    // affiche l'invite et renvoie la ligne tapee
    public String lireLigne(String invite) {
        System.out.print(invite);
        if (!clavier.hasNextLine()) {
            // plus rien a lire, on quitte
            terminee = true;
            return "q";
        }
        String ligne = clavier.nextLine();
        return rogner(ligne);
    }

    private static String rogner(String texte) {
        int debut = 0;
        while (debut < texte.length() && texte.charAt(debut) <= ' ') {
            debut++;
        }
        int fin = texte.length();
        while (fin > debut && texte.charAt(fin - 1) <= ' ') {
            fin--;
        }
        return texte.substring(debut, fin);
    }

    // vrai si y'a plus rien a lire
    public boolean estTerminee() {
        return terminee;
    }

    // lit la direction et change les fleches en z/s/q/d
    public String LireLaDirection(String terminal) {
        return normaliserDirection(lireLigne(terminal));
    }

    public static String normaliserDirection(String ligne) {
        if (ligne.endsWith("[A")) {
            return "z";
        }
        if (ligne.endsWith("[B")) {
            return "s";
        }
        if (ligne.endsWith("[D")) {
            return "q";
        }
        if (ligne.endsWith("[C")) {
            return "d";
        }
        return ligne;
    }

    // redemande jusqu'a avoir un nombre entre min et max
    public int lireNumero(String invite, int min, int max) {
        while (!terminee) {
            String reponse = lireLigne(invite);
            try {
                int numero = Integer.parseInt(reponse);
                if (numero >= min && numero <= max) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // pas un nombre, on redemande
            }
            System.out.println("Merci d'entrer un nombre entre " + min + " et " + max + ".");
        }
        return min;
    }
}
