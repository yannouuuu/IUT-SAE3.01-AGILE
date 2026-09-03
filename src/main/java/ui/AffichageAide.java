package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappel permanent des commandes disponibles.
 *
 * Affiche sous le carnet/de dans le panneau de droite, pour eviter d'avoir
 * a taper /aide pour les revoir : elles restent visibles en permanence.
 * Texte blanc sur fond sombre, pour bien se detacher du reste de l'ecran.
 */
public final class AffichageAide {

    private static final String BLANC = "\u001B[97m";
    private static final String FOND  = "\u001B[100m";
    private static final String RESET = "\u001B[0m";

    private AffichageAide() {
    }

    public static List<String> lignes() {
        List<String> contenu = List.of(
                "Commandes :",
                "/de        lancer le de",
                "/carnet    ouvrir le carnet",
                "/cartes    voir tes cartes",
                "/map       revenir sur la map",
                "/parler    parler au pnj",
                "/hypothese faire une hypothese",
                "/passage   passage secret",
                "/accuser   accusation finale",
                "/fin       terminer ton tour",
                "/save      sauvegarder la partie",
                "/musique   couper / remettre la musique",
                "q          quitter"
            
        );

        int largeur = 0;
        for (String ligne : contenu) {
            largeur = Math.max(largeur, ligne.length());
        }

        List<String> lignes = new ArrayList<>();
        for (String ligne : contenu) {
            lignes.add(FOND + BLANC + pad(ligne, largeur) + RESET);
        }
        return lignes;
    }

    private static String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (sb.length() < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
