package moteur;

import modele.Bot;
import modele.Carte;
import modele.Joueur;
import modele.Lieux;
import modele.Suspects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EtatPartieTest {

    private EtatPartie nouvellePartie() {
        Joueur[] joueurs = {
                new Joueur(Suspects.MOUTARDE, new Carte[0]),
                new Bot(Suspects.ROSE, new Carte[0])
        };
        return new EtatPartie(joueurs, new Carte[0]);
    }

    @Test
    void lieuPourSalleFaitLaCorrespondanceAvecLenum() {
        EtatPartie partie = nouvellePartie();

        assertEquals(Lieux.CUISINE, partie.lieuPourSalle("Cuisine"));
        assertEquals(Lieux.SALLE_DE_BAL, partie.lieuPourSalle("Salle de bal"));
        assertEquals(Lieux.BUREAU, partie.lieuPourSalle("Bureau"));
    }

    @Test
    void lieuPourSalleRefuseUneSalleInconnue() {
        EtatPartie partie = nouvellePartie();

        assertThrows(IllegalArgumentException.class, () -> partie.lieuPourSalle("Placard"));
    }

    @Test
    void terminerEnregistreLeResultatEtLeMessage() {
        EtatPartie partie = nouvellePartie();
        assertFalse(partie.estTerminee());

        partie.terminer("Bravo !", EtatPartie.Resultat.VICTOIRE);

        assertTrue(partie.estTerminee());
        assertEquals(EtatPartie.Resultat.VICTOIRE, partie.getResultat());
        assertEquals("Bravo !", partie.getMessageFin());
    }
}
