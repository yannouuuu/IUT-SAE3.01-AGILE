package deplacement;

import enigmes.PortesFermees;
import modele.Joueur;
import ui.AffichageMap;

/**
 * Deplacement d'un joueur point par point sur la map.
 *
 * Un pas du de = un point du couloir (les pointilles de la map).
 * Pour entrer dans une salle, il faut etre pile devant une de ses portes.
 * Pour sortir d'une salle, on prend la porte du cote demande.
 */
public class Mouvement {

    /**
     * Essaie de deplacer le joueur d'un point dans la direction donnee
     * (dLigne / dColonne valent -1, 0 ou 1). Retourne true si le joueur a bouge.
     */
    public static boolean deplacer(Joueur joueur, int dLigne, int dColonne, PortesFermees portes) {
        String salleActuelle = AffichageMap.salleAuPoint(joueur.getX(), joueur.getY());
        if (salleActuelle != null) {
            return sortirDeLaSalle(joueur, salleActuelle, dLigne, dColonne);
        }
        return avancerDansLeCouloir(joueur, dLigne, dColonne, portes);
    }

    /** Depuis une salle, on sort par la porte du cote demande, s'il y en a une. */
    private static boolean sortirDeLaSalle(Joueur joueur, String salle, int dLigne, int dColonne) {
        String cote = coteDansLaDirection(dLigne, dColonne);
        int[] sortie = AffichageMap.pointDevantPorte(salle, cote);
        if (sortie == null) {
            return false; // pas de porte de ce cote
        }
        joueur.setPosition(sortie[0], sortie[1]);
        return true;
    }

    /** Dans le couloir, on avance d'un point, ou on entre dans une salle par sa porte. */
    private static boolean avancerDansLeCouloir(Joueur joueur, int dLigne, int dColonne, PortesFermees portes) {
        int ligne = joueur.getX() + dLigne;
        int colonne = joueur.getY() + dColonne;

        boolean horsPlateau = ligne < 0 || ligne >= AffichageMap.NB_POINTS_LIGNES
                || colonne < 0 || colonne >= AffichageMap.NB_POINTS_COLONNES;
        if (horsPlateau) {
            return false;
        }

        String salleCible = AffichageMap.salleAuPoint(ligne, colonne);
        if (salleCible == null) {
            joueur.setPosition(ligne, colonne);
            return true;
        }

        // On veut entrer dans une salle : il faut etre pile devant une de ses portes.
        // Si on entre par le haut, la porte est sur le cote sud de la salle, etc.
        String cotePorte = coteOppose(coteDansLaDirection(dLigne, dColonne));
        int[] devantPorte = AffichageMap.pointDevantPorte(salleCible, cotePorte);

        boolean surLaPorte = devantPorte != null
                && devantPorte[0] == joueur.getX()
                && devantPorte[1] == joueur.getY();
        if (!surLaPorte) {
            return false; // c'est un mur
        }

        if (!portes.peutEntrer(salleCible)) {
            return false; // porte fermee a cle
        }

        int[] centre = AffichageMap.pointCentreSalle(salleCible);
        joueur.setPosition(centre[0], centre[1]);
        return true;
    }

    /** Le cote vise quand on bouge : haut = "N", bas = "S", gauche = "W", droite = "E". */
    private static String coteDansLaDirection(int dLigne, int dColonne) {
        if (dLigne == -1) {
            return "N";
        }
        if (dLigne == 1) {
            return "S";
        }
        if (dColonne == -1) {
            return "W";
        }
        return "E";
    }

    /** Le cote d'en face : N et S sont opposes, W et E aussi. */
    private static String coteOppose(String cote) {
        if (cote.equals("N")) {
            return "S";
        }
        if (cote.equals("S")) {
            return "N";
        }
        if (cote.equals("W")) {
            return "E";
        }
        return "W";
    }
}
