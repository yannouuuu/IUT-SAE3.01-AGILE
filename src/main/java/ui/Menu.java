package ui;

import java.util.Scanner;

/** Ecran d'accueil : logo + choix du mode. */
public class Menu {

    public static String afficher(Scanner clavier) {
        System.out.println(Logo.rendu(70));
        System.out.println();
        System.out.println("  1 - Solo (contre des bots)");
        System.out.println("  2 - Multijoueur (bientot)");
        System.out.println("  q - Quitter");
        System.out.println();
        System.out.print("> ");
        String choix = clavier.nextLine();
        if (choix == null) {
            return "q";
        }
        return choix.toLowerCase();
    }
}
