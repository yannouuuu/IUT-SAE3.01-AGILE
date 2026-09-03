package interaction;

import java.util.Scanner;

/** Boite de saisie : lit ce que tape l'utilisateur et controle les nombres. */
public class Saisie {

    private final Scanner clavier;
    private boolean terminee = false;

    public Saisie(Scanner clavier) {
        this.clavier = clavier;
    }

    /** Affiche l'invite puis retourne la ligne tapee, sans espaces autour. */
    public String lireLigne(String invite) {
        System.out.print(invite);
        if (!clavier.hasNextLine()) {
            // plus rien a lire : on fait comme si l'utilisateur quittait
            terminee = true;
            return "q";
        }
        String ligne = clavier.nextLine();
        return ligne.trim();
    }

    /** Vrai quand il n'y a plus rien a lire (entree fermee). */
    public boolean estTerminee() {
        return terminee;
    }

    public String LireLaDirection(String terminal) {
        return normaliserDirection(lireLigne(terminal));
    }

    public static String normaliserDirection(String ligne) {
        if (ligne.endsWith("[A"))
            return "z";
        if (ligne.endsWith("[B"))
            return "s";
        if (ligne.endsWith("[D"))
            return "q";
        if (ligne.endsWith("[C"))
            return "d";
        return ligne;
    }

    /** Redemande tant que l'utilisateur ne tape pas un nombre entre min et max. */
    public int lireNumero(String invite, int min, int max) {
        while (!terminee) {
            String reponse = lireLigne(invite);
            try {
                int numero = Integer.parseInt(reponse);
                if (numero >= min && numero <= max) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // ce n'est pas un nombre : on redemande juste en dessous
            }
            System.out.println("Merci d'entrer un nombre entre " + min + " et " + max + ".");
        }
        return min;
    }
}
