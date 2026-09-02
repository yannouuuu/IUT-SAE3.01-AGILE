package ui;

import carnet.Carnet;
import carnet.Carnet.Entree;

import java.util.ArrayList;
import java.util.List;

/** Rendu ASCII du carnet. Lit l'etat depuis {@link Carnet}, n'en modifie rien. */
public class AffichageCarnet {

    private static final String JAUNE = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    /** Produit les lignes encadrees du carnet. Bord jaune quand actif. */
    public static List<String> rendu(Carnet carnet) {
        List<Entree> entrees   = carnet.getEntrees();
        int          selection = carnet.getSelection();
        boolean      actif     = carnet.estOuvert();

        List<String> contenu = new ArrayList<>();
        for (int i = 0; i < entrees.size(); i++) {
            Entree e = entrees.get(i);

            String prefixe;
            if (i == selection) {
                prefixe = "> ";
            } else {
                prefixe = "  ";
            }

            if (e.estTitre()) {
                contenu.add(prefixe + e.getNom() + " :");
            } else {
                String coche;
                if (e.estCoche()) {
                    coche = "x";
                } else {
                    coche = " ";
                }
                contenu.add(prefixe + "[" + coche + "] " + e.getNom());
            }
        }

        int largeurContenu = 0;
        for (String ligne : contenu) {
            if (ligne.length() > largeurContenu) largeurContenu = ligne.length();
        }

        int marge = 2;
        int largeurInterne = largeurContenu + marge * 2;

        StringBuilder sbBord = new StringBuilder();
        for (int i = 0; i < largeurInterne; i++) {
            sbBord.append('-');
        }
        String bord = sbBord.toString();

        StringBuilder sbPad = new StringBuilder();
        for (int i = 0; i < marge; i++) {
            sbPad.append(' ');
        }
        String padding = sbPad.toString();

        List<String> lignes = new ArrayList<>();
        lignes.add("+" + bord + "+");
        for (String ligne : contenu) {
            lignes.add("|" + padding + pad(ligne, largeurContenu) + padding + "|");
        }
        lignes.add("+" + bord + "+");

        if (actif) {
            for (int i = 0; i < lignes.size(); i++) {
                lignes.set(i, JAUNE + lignes.get(i) + RESET);
            }
        }
        return lignes;
    }

    private static String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (sb.length() < largeur) sb.append(' ');
        return sb.toString();
    }
}
