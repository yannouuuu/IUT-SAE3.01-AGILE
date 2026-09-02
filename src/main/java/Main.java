import ui.Menu;
import ui.Rendu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        String choix = Menu.afficher(clavier);
        if (!choix.equals("q")) {
            new Rendu(clavier).boucle();
        }
    }
}
