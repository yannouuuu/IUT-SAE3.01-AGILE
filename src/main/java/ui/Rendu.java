package ui;

import java.util.List;
import java.util.Scanner;

/** Assemble le plateau, le carnet et la saisie, et traite les commandes /carnet, /map et /de. */
public class Rendu {

    private final Scanner clavier;
    private final AffichageMap map = new AffichageMap();
    private final PanneauDroit panneau = new PanneauDroit();

    public Rendu(Scanner clavier) {
        this.clavier = clavier;
    }

    public void boucle() {
        while (true) {
            afficher();
            String saisie = clavier.nextLine();
            if (saisie == null || saisie.equalsIgnoreCase("q")) {
                System.out.println("Au revoir !");
                return;
            }
            if (saisie.equalsIgnoreCase("/carnet")) {
                panneau.ouvrirCarnet();
            } else if (saisie.equalsIgnoreCase("/map")) {
                panneau.revenirMap();
            } else if (saisie.equalsIgnoreCase("/de")) {
                panneau.lancerDe();
            } else {
                panneau.traiter(saisie);
            }
        }
    }

    private void afficher() {
        List<String> lignesMap = map.lignes();
        List<String> lignesDroite = panneau.lignes();
        int largeurMap = lignesMap.get(0).length() + 3;

        System.out.println();
        System.out.println(Logo.rendu(largeurMap + largeurPanneau(lignesDroite)));

        for (int i = 0; i < lignesMap.size(); i++) {
            System.out.println(pad(lignesMap.get(i), largeurMap) + ligneDroite(lignesDroite, i));
        }

        System.out.println();
        System.out.print(panneau.aide());
    }

    private String ligneDroite(List<String> lignes, int index) {
        if (index < lignes.size()) {
            return lignes.get(index);
        }
        return "";
    }

    private int largeurPanneau(List<String> lignes) {
        int max = 0;
        for (String ligne : lignes) {
            if (ligne.length() > max) {
                max = ligne.length();
            }
        }
        return max;
    }

    private String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (sb.length() < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
