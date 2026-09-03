package ui;

import java.util.List;

/**
 * Layout general : assemble le logo, la map et le panneau de droite
 * (carnet ou de). L'affichage est pilote par moteur.BoucleDeJeu.
 */
public class Rendu {

    private final AffichageMap map = new AffichageMap();
    private final PanneauDroit panneau = new PanneauDroit();

    public PanneauDroit getPanneau() {
        return panneau;
    }

    public AffichageMap getMap() {
        return map;
    }

    /** Affiche l'ecran complet : logo, map avec les personnages, panneau de droite. */
    public void afficher(List<Marqueur> marqueurs) {
        List<String> lignesMap = map.lignes(marqueurs);
        List<String> lignesDroite = panneau.lignes();
        int largeurMap = longueurVisible(lignesMap.get(0)) + 3;

        System.out.println();
        System.out.println(Logo.rendu(largeurMap + largeurPanneau(lignesDroite)));

        for (int i = 0; i < lignesMap.size(); i++) {
            System.out.println(pad(lignesMap.get(i), largeurMap) + ligneDroite(lignesDroite, i));
        }

        System.out.println();
    }

    private String ligneDroite(List<String> lignes, int index) {
        if (index < lignes.size()) {
            return lignes.get(index);
        }
        return "";
    }

    private int largeurPanneau(List<String> lignes) {
        int max = 0;
        for (String ligne : lignes) {
            int longueur = longueurVisible(ligne);
            if (longueur > max) {
                max = longueur;
            }
        }
        return max;
    }

    private String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (longueurVisible(sb.toString()) < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /** Longueur affichee a l'ecran : on ne compte pas les codes couleur ANSI. */
    private int longueurVisible(String ligne) {
        String sansCouleur = ligne.replaceAll("\u001B\\[[0-9;]*m", "");
        return sansCouleur.length();
    }
}
