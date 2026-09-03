import moteur.BoucleDeJeu;

import java.util.Scanner;

/** Point d'entree : lance la boucle de jeu (Menu -> Partie -> Fin). */
public class Main {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        BoucleDeJeu jeu = new BoucleDeJeu(clavier);
        jeu.lancer();
    }
}