package enigmes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnigmeTest {

    @Test
    void chargeLEnigmeDepuisLesResources() {
        Enigme enigme = Enigme.pourSalle("Cuisine");

        assertEquals("Cuisine", enigme.getSalleDeLaCle());
        assertFalse(enigme.getQuestion().isBlank());
    }

    @Test
    void refuseUneSalleSansEnigme() {
        assertThrows(IllegalArgumentException.class, () -> Enigme.pourSalle("Placard"));
    }
}
