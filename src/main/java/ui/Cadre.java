package ui;

import java.util.ArrayList;
import java.util.List;

public final class Cadre {

    private static final String RESET = "\u001B[0m";
    private static final int MARGE = 2;

    private Cadre() {
    }

    /** Boite avec un titre centre (optionnel), une ligne de separation, puis le contenu centre. */
    public static List<String> dessiner(String titre, List<String> contenu, String couleurBord) {
        int largeurContenu = 0;
        for (String ligne : contenu) {
            largeurContenu = Math.max(largeurContenu, longueurVisible(ligne));
        }
        if (titre != null) {
            largeurContenu = Math.max(largeurContenu, longueurVisible(titre));
        }
        int largeurInterne = largeurContenu + MARGE * 2;

        String traits = repeter('─', largeurInterne);

        List<String> lignes = new ArrayList<>();
        lignes.add(couleurBord + "┌" + traits + "┐" + RESET);
        if (titre != null) {
            lignes.add(couleurBord + "│" + RESET + centrer(titre, largeurInterne) + couleurBord + "│" + RESET);
            lignes.add(couleurBord + "├" + traits + "┤" + RESET);
        }
        for (String ligne : contenu) {
            lignes.add(couleurBord + "│" + RESET + centrer(ligne, largeurInterne) + couleurBord + "│" + RESET);
        }
        lignes.add(couleurBord + "└" + traits + "┘" + RESET);
        return lignes;
    }

    /** Centre chaque ligne d'un bloc dans une largeur totale, pour l'afficher au milieu de l'ecran. */
    public static List<String> centrerBloc(List<String> lignes, int largeurTotale) {
        int largeurBloc = 0;
        for (String ligne : lignes) {
            largeurBloc = Math.max(largeurBloc, longueurVisible(ligne));
        }
        String prefixe = repeter(' ', Math.max(0, (largeurTotale - largeurBloc) / 2));

        List<String> centrees = new ArrayList<>();
        for (String ligne : lignes) {
            centrees.add(prefixe + ligne);
        }
        return centrees;
    }

    private static String centrer(String texte, int largeur) {
        int gauche = (largeur - longueurVisible(texte)) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gauche; i++) {
            sb.append(' ');
        }
        sb.append(texte);
        while (longueurVisible(sb.toString()) < largeur) {
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

    /** Longueur affichee a l'ecran : on ne compte pas les codes couleur ANSI. */
    private static int longueurVisible(String ligne) {
        String sansCouleur = ligne.replaceAll("\u001B\\[[0-9;]*m", "");
        return sansCouleur.length();
    }
}
