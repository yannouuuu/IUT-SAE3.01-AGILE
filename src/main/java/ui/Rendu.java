package ui;

import deplacement.De;

import java.util.List;
import java.util.Scanner;

/** Assemble le plateau, le carnet et la saisie, et traite les commandes /carnet, /map et /de. */
public class Rendu {

    private final Scanner clavier;
    private final AffichageCarnet carnet = new AffichageCarnet();
    private final De de = new De();
    private boolean vueCarnet = false;
    private int valeurDe;

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
                vueCarnet = true;
                valeurDe = 0;
            } else if (saisie.equalsIgnoreCase("/map")) {
                vueCarnet = false;
                valeurDe = 0;
            } else if (saisie.equalsIgnoreCase("/de")) {
                valeurDe = de.lancer();
            } else if (vueCarnet) {
                carnet.traiter(saisie);
            }
        }
    }

    private void afficher() {
        List<String> lignesMap = AffichageMap.lignes();
        List<String> lignesDroite = valeurDe > 0 ? AffichageDe.rendu(valeurDe) : carnet.rendu(vueCarnet);
        int largeurMap = lignesMap.get(0).length() + 3;

        System.out.println();
        System.out.println(Logo.rendu(largeurMap + largeurCarnet(lignesDroite)));

        for (int i = 0; i < lignesMap.size(); i++) {
            String ligneDroite = i < lignesDroite.size() ? lignesDroite.get(i) : "";
            System.out.println(pad(lignesMap.get(i), largeurMap) + ligneDroite);
        }

        System.out.println();
        if (vueCarnet) {
            System.out.print("+ Saisie (Espace = cocher, z/s = naviguer, /map = revenir, q = quitter) > ");
        } else {
            System.out.print("+ Saisie (/carnet = ouvrir le carnet, /de = lancer le de, q = quitter) > ");
        }
    }

    private int largeurCarnet(List<String> lignes) {
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
