import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Boîte de saisie affichée sous le plateau.
 * L'utilisateur peut noter ce qu'il veut quand il le veut ("q" pour quitter).
 */
public class SaisieUtilisateur {

    private static final int TAILLE_MAX = 120;

    private final List<String> notes = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void boucle() {
        while (true) {
            afficherPlateau();
            String saisie = lireSaisie();
            if (saisie.equalsIgnoreCase("q")) {
                break;
            }
            notes.add(saisie);
        }
        System.out.println("Au revoir !");
    }

    private void afficherPlateau() {
        System.out.println();
        System.out.println(CluedoMap.render());
        if (!notes.isEmpty()) {
            System.out.println("Vos notes :");
            for (int i = 0; i < notes.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + notes.get(i));
            }
        }
    }

    /** Contrôle de saisie : rejette une ligne vide ou trop longue. */
    private String lireSaisie() {
        while (true) {
            System.out.print("+ Saisie (q = quitter) > ");
            if (!scanner.hasNextLine()) {
                return "q";
            }
            String ligne = scanner.nextLine().trim();
            if (ligne.isEmpty()) {
                System.out.println("! Saisie vide, recommencez (ou q pour quitter).");
            } else if (ligne.length() > TAILLE_MAX) {
                System.out.println("! Trop long : " + ligne.length() + " caracteres (max " + TAILLE_MAX + ").");
            } else {
                return ligne;
            }
        }
    }
}
