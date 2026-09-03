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

    public void joueurSuivant() {
        indexCourant = indexCourant + 1;
        if (indexCourant >= joueurs.length) {
            indexCourant = 0;
        }
    }

    /** Pour la sauvegarde : a qui c'etait le tour. */
    public int getIndexCourant() {
        return indexCourant;
    }

    /** Pour le chargement : on reprend au meme joueur qu'a la sauvegarde. */
    public void definirIndexCourant(int index) {
        this.indexCourant = index;
    }
}
