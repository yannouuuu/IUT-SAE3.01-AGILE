package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Rendu des cartes du joueur, dans le meme style que la map.
 * Remplace la map dans la zone principale quand on tape /cartes.
 *
 * Les dessins de chaque carte viennent de resources/Cartes.txt (voir CartesArt) :
 * on les affiche cote a cote, plusieurs par ligne.
 */
public class AffichageCartes {

    private static final String JAUNE = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private static final String BLANC = "\u001B[97m";

    private static final int CARTES_PAR_LIGNE = 6;
    private static final int ESPACE_ENTRE_CARTES = 2;

    /** Produit les lignes pretes a afficher a la place de la map : les cartes du joueur, cote a cote. */
    public static List<String> rendu(String personnage, List<String> clesCartes) {
        List<String> lignes = new ArrayList<>();
        lignes.add(JAUNE + "Tes cartes, " + personnage + " :" + RESET);
        lignes.add("");

        if (clesCartes.isEmpty()) {
            lignes.add(BLANC + "(aucune carte)" + RESET);
        } else {
            for (int debut = 0; debut < clesCartes.size(); debut += CARTES_PAR_LIGNE) {
                int fin = Math.min(debut + CARTES_PAR_LIGNE, clesCartes.size());
                lignes.addAll(ligneDeCartes(clesCartes.subList(debut, fin)));
                lignes.add("");
            }
        }

        lignes.add("Tape /map pour revenir sur le plateau.");
        return lignes;
    }

    /** Assemble cote a cote les dessins de plusieurs cartes, ligne de dessin par ligne de dessin. */
    private static List<String> ligneDeCartes(List<String> cles) {
        List<List<String>> dessins = new ArrayList<>();
        for (String cle : cles) {
            dessins.add(CartesArt.dessin(cle));
        }

        int hauteur = 0;
        for (List<String> dessin : dessins) {
            hauteur = Math.max(hauteur, dessin.size());
        }

        String espace = " ".repeat(ESPACE_ENTRE_CARTES);
        List<String> lignes = new ArrayList<>();
        for (int y = 0; y < hauteur; y++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dessins.size(); i++) {
                List<String> dessin = dessins.get(i);
                String morceau = y < dessin.size() ? dessin.get(y) : "";
                sb.append(BLANC).append(pad(morceau, CartesArt.largeur())).append(RESET);
                if (i < dessins.size() - 1) {
                    sb.append(espace);
                }
            }
            lignes.add(sb.toString());
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
