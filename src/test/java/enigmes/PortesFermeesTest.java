package enigmes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortesFermeesTest {

    private PortesFermees portes;

    @BeforeEach
    void setUp() {
        portes = new PortesFermees();
        portes.ajouter("Bureau", "Cuisine");
    }

    @Test
    void salleVerrouilleeBloqueTantQueLaCleNestPasTrouvee() {
        assertFalse(portes.peutEntrer("Bureau"));
    }

    @Test
    void ramasserLaCleDansLaBonneSalleDeverrouilleLaPorte() {
        List<String> messages = portes.ramasserCles("Cuisine");

        assertEquals(1, messages.size());
        assertTrue(portes.peutEntrer("Bureau"));
    }

    @Test
    void ramasserDansUneAutreSalleNeDeverrouilleRien() {
        List<String> messages = portes.ramasserCles("Salon");

        assertTrue(messages.isEmpty());
        assertFalse(portes.peutEntrer("Bureau"));
    }

    @Test
    void uneSalleNonVerrouilleeEstToujoursAccessible() {
        assertTrue(portes.peutEntrer("Salon"));
    }

    @Test
    void marquerCleTrouveeDeverrouilleSansPasserParLaSalle() {
        portes.marquerCleTrouvee("Bureau");

        assertTrue(portes.peutEntrer("Bureau"));
    }
}
