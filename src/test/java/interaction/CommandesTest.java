package interaction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandesTest {

    @Test
    void reconnaitLesCommandesConnues() {
        assertEquals(Commandes.Type.DE, Commandes.interpreter("/de"));
        assertEquals(Commandes.Type.CARNET, Commandes.interpreter("/carnet"));
        assertEquals(Commandes.Type.FIN_TOUR, Commandes.interpreter("/fin"));
        assertEquals(Commandes.Type.QUITTER, Commandes.interpreter("q"));
        assertEquals(Commandes.Type.SAVE, Commandes.interpreter("/save"));
    }

    @Test
    void estInsensibleALaCasse() {
        assertEquals(Commandes.Type.AIDE, Commandes.interpreter("/AIDE"));
        assertEquals(Commandes.Type.CARTES, Commandes.interpreter("/Cartes"));
    }

    @Test
    void renvoieAutrePourUneCommandeInconnue() {
        assertEquals(Commandes.Type.AUTRE, Commandes.interpreter("/blabla"));
        assertEquals(Commandes.Type.AUTRE, Commandes.interpreter(""));
    }
}
