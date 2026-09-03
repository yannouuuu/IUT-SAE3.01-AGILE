package moteur;

import modele.Bot;
import modele.Carte;
import modele.Joueur;
import modele.Suspects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TourManagerTest {

    @Test
    void tourneEntreLesJoueursEtRevientAuDebut() {
        Joueur humain = new Joueur(Suspects.MOUTARDE, new Carte[0]);
        Bot bot = new Bot(Suspects.ROSE, new Carte[0]);
        TourManager tours = new TourManager(new Joueur[]{humain, bot});

        assertEquals(humain, tours.joueurCourant());
        assertFalse(tours.estUnBot());

        tours.joueurSuivant();
        assertEquals(bot, tours.joueurCourant());
        assertTrue(tours.estUnBot());

        tours.joueurSuivant();
        assertEquals(humain, tours.joueurCourant());
    }

    @Test
    void definirIndexCourantPermetDeReprendreAuBonJoueur() {
        Joueur j1 = new Joueur(Suspects.VIOLET, new Carte[0]);
        Joueur j2 = new Joueur(Suspects.OLIVE, new Carte[0]);
        TourManager tours = new TourManager(new Joueur[]{j1, j2});

        tours.definirIndexCourant(1);

        assertEquals(1, tours.getIndexCourant());
        assertEquals(j2, tours.joueurCourant());
    }
}
