package moteur;

import deplacement.Mouvement;
import enigmes.Enigme;
import enigmes.PortesFermees;
import interaction.Pnj;
import modele.Carte;
import modele.Joueur;
import modele.Lieux;
import ui.AffichageMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Etat partage de la partie : les joueurs et leur position, les cartes
 * cachees (la solution), les portes fermees et les PNJ.
 *
 * Les positions sont exprimees en points de deplacement sur la map
 * (les pointilles du couloir) : x = ligne du point, y = colonne du point.
 */
public class EtatPartie {

    private final Joueur[] joueurs;
    private final Carte[] cartesCachees;
    private final PortesFermees portes = new PortesFermees();
    private final List<Pnj> pnjs = new ArrayList<>();

    /** Facon dont la partie s'est terminee, pour adapter l'ecran de fin. */
    public enum Resultat { VICTOIRE, DEFAITE, ABANDON }

    private boolean terminee = false;
    private String messageFin = "";
    private Resultat resultat = Resultat.ABANDON;

    public EtatPartie(Joueur[] joueurs, Carte[] cartesCachees) {
        this.joueurs = joueurs;
        this.cartesCachees = cartesCachees;
        preparerPortesEtPnjs();
    }

    /** Deux portes fermees, chacune avec son PNJ qui donne l'enigme de la cle. */
    private void preparerPortesEtPnjs() {
        portes.ajouter("Bureau", "Cuisine");
        portes.ajouter("Salle de bal", "Salon");

        pnjs.add(new Pnj("Le majordome", "Hall", "Bureau", Enigme.pourSalle("Cuisine")));
        pnjs.add(new Pnj("La gouvernante", "Salle à manger", "Salle de bal", Enigme.pourSalle("Salon")));
    }

    // --- joueurs ---

    public Joueur[] getJoueurs() {
        return joueurs;
    }


    /**
     * Deplace un joueur d'un point. Retourne false si le mouvement est
     * impossible (bord du plateau, mur, ou porte fermee a cle).
     */
    public boolean deplacer(Joueur joueur, int dLigne, int dColonne) {
        return Mouvement.deplacer(joueur, dLigne, dColonne, portes);
    }


    /** Nom de la salle ou se trouve le joueur, ou null s'il est dans le couloir. */
    public String salleDuJoueur(Joueur joueur) {
        return AffichageMap.salleAuPoint(joueur.getX(), joueur.getY());
    }

    // --- portes et PNJ ---

    public PortesFermees getPortes() {
        return portes;
    }

    public List<Pnj> getPnjs() {
        return pnjs;
    }

    public Pnj pnjDansSalle(String salle) {
        if (salle == null) {
            return null;
        }
        for (Pnj pnj : pnjs) {
            if (pnj.getSalle().equals(salle)) {
                return pnj;
            }
        }
        return null;
    }

    // --- solution ---

    public Carte[] getCartesCachees() {
        return cartesCachees;
    }

    // --- correspondance entre les salles de la map et l'enum Lieux ---

    public Lieux lieuPourSalle(String salle) {
        if (salle.equals("Cuisine")) {
            return Lieux.CUISINE;
        }
        if (salle.equals("Salle de bal")) {
            return Lieux.SALLE_DE_BAL;
        }
        if (salle.equals("Véranda")) {
            return Lieux.VERANDA;
        }
        if (salle.equals("Salle à manger")) {
            return Lieux.SALLE_A_MANGER;
        }
        if (salle.equals("Billard")) {
            return Lieux.BILLARD;
        }
        if (salle.equals("Bibliothèque")) {
            return Lieux.BIBLIOTHEQUE;
        }
        if (salle.equals("Salon")) {
            return Lieux.SALON;
        }
        if (salle.equals("Hall")) {
            return Lieux.HALL;
        }
        if (salle.equals("Bureau")) {
            return Lieux.BUREAU;
        }
        throw new IllegalArgumentException("Salle inconnue : " + salle);
    }

    // --- fin de partie ---

    public boolean estTerminee() {
        return terminee;
    }

    public String getMessageFin() {
        return messageFin;
    }

    public Resultat getResultat() {
        return resultat;
    }

    public void terminer(String message, Resultat resultat) {
        terminee = true;
        messageFin = message;
        this.resultat = resultat;
    }
}
