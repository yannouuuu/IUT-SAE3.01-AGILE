package ui;

import java.util.Arrays;
import java.util.List;

/** Face du de, affichee a la place du carnet pendant le deplacement. */
public class AffichageDe {

    private static final String[][] FACES = {
            {"+-------+", "|       |", "|   o   |", "|       |", "+-------+"},
            {"+-------+", "| o     |", "|       |", "|     o |", "+-------+"},
            {"+-------+", "| o     |", "|   o   |", "|     o |", "+-------+"},
            {"+-------+", "| o   o |", "|       |", "| o   o |", "+-------+"},
            {"+-------+", "| o   o |", "|   o   |", "| o   o |", "+-------+"},
            {"+-------+", "| o   o |", "| o   o |", "| o   o |", "+-------+"},
    };

    public static List<String> rendu(int valeur) {
        return Arrays.asList(FACES[valeur - 1]);
    }
}
