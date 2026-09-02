package deplacement;

import java.util.Random;

/** Le de : un lancer donne un nombre entre 1 et 6. */
public class De {

    private final Random rand = new Random();

    public int lancer() {
        return rand.nextInt(6) + 1;
    }
}
