package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout general : assemble le logo, la map et le panneau de droite
 * (carnet ou de). L'affichage est pilote par moteur.BoucleDeJeu.
 */
public class Rendu {

    private final AffichageMap map = new AffichageMap();
    private final PanneauDroit panneau = new PanneauDroit();

    private boolean surCartes = false;
    private String personnageAffiche = "";
    private List<String> clesCartesAffichees = new ArrayList<>();

    public PanneauDroit getPanneau() {
        return panneau;
    }

    public AffichageMap getMap() {
        return map;
    }

    /** Remplace la map par les cartes du joueur, dans la zone principale. */
    public void ouvrirCartes(String personnage, List<String> clesCartes) {
        surCartes = true;
        personnageAffiche = personnage;
        clesCartesAffichees = clesCartes;
    }

    /** Revient sur la map, que les cartes soient affichees ou non. */
    public void revenirMap() {
        surCartes = false;
    }

    public boolean surCartes() {
        return surCartes;
    }

    /** Affiche l'ecran complet : logo, map ou cartes avec les personnages, panneau de droite. */
    public void afficher(List<Marqueur> marqueurs) {
        List<String> lignesGauche;
        if (surCartes) {
            lignesGauche = AffichageCartes.rendu(personnageAffiche, clesCartesAffichees);
        } else {
            lignesGauche = map.lignes(marqueurs);
        }
        List<String> lignesDroite = panneau.lignes();
        int largeurGauche = largeurPanneau(lignesGauche) + 3;

        System.out.println();
        System.out.println(Logo.rendu(largeurGauche + largeurPanneau(lignesDroite)));

        int nbLignes = Math.max(lignesGauche.size(), lignesDroite.size());
        for (int i = 0; i < nbLignes; i++) {
            System.out.println(pad(ligneDroite(lignesGauche, i), largeurGauche) + ligneDroite(lignesDroite, i));
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
