package carnet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarnetTest {

    @Test
    void cocherCarteMarqueLentreeCorrespondante() {
        Carnet carnet = new Carnet();

        carnet.cocherCarte("Moutarde");

        assertTrue(entree(carnet, "Moutarde").estCoche());
    }

    @Test
    void marquerPossedeeNeCocheParEllememe() {
        Carnet carnet = new Carnet();

        carnet.marquerPossedee("Rose");

        assertTrue(entree(carnet, "Rose").estPossedee());
        assertFalse(entree(carnet, "Rose").estCoche());
    }

    @Test
    void reinitialiserEffaceLesCochesEtLesPossessions() {
        Carnet carnet = new Carnet();
        carnet.marquerPossedee("Corde");
        carnet.cocherCarte("Cuisine");

        carnet.reinitialiser();

        assertFalse(entree(carnet, "Corde").estPossedee());
        assertFalse(entree(carnet, "Cuisine").estCoche());
    }

    @Test
    void laNavigationNeSelectionneJamaisUnTitre() {
        Carnet carnet = new Carnet();
        carnet.ouvrir();

        carnet.traiter("z");

        assertEquals(1, carnet.getSelection());
    }

    @Test
    void carnetFermeParDefautEtOuvreSurCommande() {
        Carnet carnet = new Carnet();

        assertFalse(carnet.estOuvert());
        carnet.ouvrir();
        assertTrue(carnet.estOuvert());
        carnet.fermer();
        assertFalse(carnet.estOuvert());
    }

    private Carnet.Entree entree(Carnet carnet, String nom) {
        for (Carnet.Entree entree : carnet.getEntrees()) {
            if (!entree.estTitre() && entree.getNom().equalsIgnoreCase(nom)) {
                return entree;
            }
        }
        throw new IllegalArgumentException("Entree introuvable : " + nom);
    }
}
