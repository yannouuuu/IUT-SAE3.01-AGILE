package ui;

import carnet.Carnet;
import carnet.Carnet.Entree;

import java.util.ArrayList;
import java.util.List;

/**
 * Rendu du carnet, dans le meme style que la map.
 *
 * Le cadre est gris quand on est sur la map, jaune quand le carnet est actif.
 * Les titres des categories sont en cyan et les coches en vert.
 */
public class AffichageCarnet {

    private static final String JAUNE = "\u001B[33m";
    private static final String CYAN  = "\u001B[96m";
    private static final String VERT  = "\u001B[92m";
    private static final String GRIS  = "\u001B[90m";
    private static final String RESET = "\u001B[0m";

    private static final int MARGE = 2;

    /** Produit les lignes encadrees du carnet. Lit l'etat depuis {@link Carnet}. */
    public static List<String> rendu(Carnet carnet) {
        List<String> contenu = construireContenu(carnet);

        int largeurContenu = 0;
        for (String ligne : contenu) {
            int longueur = longueurVisible(ligne);
            if (longueur > largeurContenu) {
                largeurContenu = longueur;
            }
        }
        int largeurInterne = largeurContenu + MARGE * 2;

        String couleurBord;
        if (carnet.estOuvert()) {
            couleurBord = JAUNE;
        } else {
            couleurBord = GRIS;
        }

        String traits = repeter('─', largeurInterne);
        String marge = repeter(' ', MARGE);

        List<String> lignes = new ArrayList<>();
        lignes.add(couleurBord + "┌" + traits + "┐" + RESET);
        lignes.add(couleurBord + "│" + RESET + centrer("CARNET", largeurInterne)
                + couleurBord + "│" + RESET);
        lignes.add(couleurBord + "├" + traits + "┤" + RESET);
        for (String ligne : contenu) {
            lignes.add(couleurBord + "│" + RESET + marge + pad(ligne, largeurContenu) + marge
                    + couleurBord + "│" + RESET);
        }
        lignes.add(couleurBord + "└" + traits + "┘" + RESET);
        return lignes;
    }

    /** Une ligne par entree : les titres en cyan, la selection en jaune, les coches en vert. */
    private static List<String> construireContenu(Carnet carnet) {
        List<Entree> entrees = carnet.getEntrees();
        int selection = carnet.getSelection();

        List<String> contenu = new ArrayList<>();
        for (int i = 0; i < entrees.size(); i++) {
            Entree e = entrees.get(i);

            String prefixe = "  ";
            if (i == selection) {
                prefixe = JAUNE + "> " + RESET;
            }

            if (e.estTitre()) {
                contenu.add(prefixe + CYAN + e.getNom() + " :" + RESET);
            } else {
                String caseACocher = "[ ]";
                if (e.estPossedee()) {
                    caseACocher = "[" + JAUNE + "●" + RESET + "]";
                } else if (e.estCoche()) {
                    caseACocher = "[" + VERT + "✓" + RESET + "]";
                }
                contenu.add(prefixe + caseACocher + " " + e.getNom());
            }
        }

        contenu.add("");
        contenu.add(JAUNE + "●" + RESET + " mes cartes   " + VERT + "✓" + RESET + " deduction");
        return contenu;
    }

    // --- petits outils de mise en page ---

    private static String centrer(String texte, int largeur) {
        int gauche = (largeur - texte.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gauche; i++) {
            sb.append(' ');
        }
        sb.append(texte);
        while (sb.length() < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String repeter(char caractere, int nb) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nb; i++) {
            sb.append(caractere);
        }
        return sb.toString();
    }

    private static String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (longueurVisible(sb.toString()) < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /** Longueur affichee a l'ecran : on ne compte pas les codes couleur. */
    private static int longueurVisible(String ligne) {
        String sansCouleur = ligne.replaceAll("\u001B\\[[0-9;]*m", "");
        return sansCouleur.length();
    }
}
