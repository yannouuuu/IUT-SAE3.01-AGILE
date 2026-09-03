package ui;

import java.util.List;
import java.util.Scanner;

/** Ecran d'accueil : logo + choix du mode */
public class Menu {

    private static final String JAUNE = "\u001B[33m";
    private static final String CYAN  = "\u001B[96m";
    private static final String RESET = "\u001B[0m";

    public static String afficher(Scanner clavier) {
        int largeur = Terminal.largeur();
        System.out.println(Logo.rendu(largeur));
        System.out.println();

        List<String> options = List.of(
                CYAN + "1" + RESET + " - Solo (contre des bots)",
                CYAN + "2" + RESET + " - Multijoueur (2 a 4 joueurs)",
                CYAN + "3" + RESET + " - Charger une partie",
                JAUNE + "q" + RESET + " - Quitter"
        );
        List<String> boite = Cadre.dessiner("M E N U", options, JAUNE);
        for (String ligne : Cadre.centrerBloc(boite, largeur)) {
            System.out.println(ligne);
        }

        System.out.println();
        System.out.print(centrer("> ", largeur));
        String choix = clavier.nextLine();
        if (choix == null) {
            return "q";
        }
        return choix.toLowerCase();
    }

    /** Centre un court prompt sur une ligne de largeur donnee. */
    private static String centrer(String texte, int largeur) {
        int marge = Math.max(0, (largeur - texte.length()) / 2);
        return " ".repeat(marge) + texte;
    }
}
