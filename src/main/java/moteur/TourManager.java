package moteur;

import modele.Bot;
import modele.Joueur;

/** Gere l'ordre des tours : le joueur, puis les bots, et on recommence. */
public class TourManager {

    private final Joueur[] joueurs;
    private int indexCourant = 0;

    public TourManager(Joueur[] joueurs) {
        this.joueurs = joueurs;
    }

    public Joueur joueurCourant() {
        return joueurs[indexCourant];
    }

    public boolean estUnBot() {
        return joueurCourant() instanceof Bot;
    }

    public Joueur[] getJoueurs() {
        return joueurs;
    }

    public void joueurSuivant() {
        indexCourant = indexCourant + 1;
        if (indexCourant >= joueurs.length) {
            indexCourant = 0;
        }
    }
}
