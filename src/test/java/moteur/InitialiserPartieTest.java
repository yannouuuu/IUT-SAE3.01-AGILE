package moteur;

import modele.Carte;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InitialiserPartieTest {

    @Test
    void distribueLesDixHuitCartesRestantesEtTroisCartesCachees() {
        InitialiserPartie.Distribution distribution = new InitialiserPartie().systemeDistribution();

        List<Carte> distribuees = new ArrayList<>();
        for (List<Carte> inventaire : distribution.inventaires()) {
            distribuees.addAll(inventaire);
        }

        assertEquals(4, distribution.inventaires().size());
        assertEquals(18, distribuees.size());
        assertEquals(3, distribution.cartesCachees().length);

        for (Carte cachee : distribution.cartesCachees()) {
            assertFalse(distribuees.contains(cachee), "une carte cachee ne doit pas etre distribuee");
        }
    }
}
