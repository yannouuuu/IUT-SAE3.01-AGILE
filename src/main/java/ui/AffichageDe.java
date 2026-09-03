package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Face du de, affichee a la place du carnet pendant le deplacement.
 * Meme style que la map : bordure grise, points en rouge.
 */
public class AffichageDe {

    private static final String ROUGE = "\u001B[91m";
    private static final String GRIS  = "\u001B[90m";
    private static final String RESET = "\u001B[0m";

    private static final String[][] FACES = {
            {"┌───────┐", "│       │", "│   ●   │", "│       │", "└───────┘"},
            {"┌───────┐", "│ ●     │", "│       │", "│     ● │", "└───────┘"},
            {"┌───────┐", "│ ●     │", "│   ●   │", "│     ● │", "└───────┘"},
            {"┌───────┐", "│ ●   ● │", "│       │", "│ ●   ● │", "└───────┘"},
            {"┌───────┐", "│ ●   ● │", "│   ●   │", "│ ●   ● │", "└───────┘"},
            {"┌───────┐", "│ ●   ● │", "│ ●   ● │", "│ ●   ● │", "└───────┘"},
    };

    public static List<String> rendu(int valeur) {
        List<String> lignes = new ArrayList<>();
        lignes.add("Ton lancer :");
        for (String ligne : FACES[valeur - 1]) {
            lignes.add(colorer(ligne));
        }
        return lignes;
    }

    /** Bordure du de en gris, points en rouge. */
    private static String colorer(String ligne) {
        String pointsEnRouge = ligne.replace("●", ROUGE + "●" + GRIS);
        return GRIS + pointsEnRouge + RESET;
    }
}
