import ui.Menu;
import ui.Rendu;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        String choix = Menu.afficher(clavier);
        
        ItnitialiserPartie init = new ItnitialiserPartie();

        String nomJoueur = init.demandePerso();

        Object[] resultat = init.systemeDistribution();

        List<List<Carte>> inventaires = (List<List<Carte>>) resultat[0];
        Carte[] cartesCachees = (Carte[]) resultat[1];

        Personage[] joueurs = init.creerJoueurs(nomJoueur, inventaires);

    }
}
