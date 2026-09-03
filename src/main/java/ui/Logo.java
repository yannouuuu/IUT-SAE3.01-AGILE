package ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Logo affiche au-dessus du plateau, charge depuis resources/logo.txt. */
public class Logo {

    public static String rendu(int largeur) {
        StringBuilder sb = new StringBuilder();
        for (String ligne : lignes()) {
            int marge = (largeur - ligne.length()) / 2;
            if (marge < 0) {
                marge = 0;
            }
            for (int i = 0; i < marge; i++) {
                sb.append(' ');
            }
            sb.append(ligne).append('\n');
        }
        return sb.toString();
    }

    private static List<String> lignes() {
        InputStream fichier = Logo.class.getResourceAsStream("/logo.txt");
        if (fichier == null) {
            throw new IllegalStateException("logo.txt introuvable dans les resources");
        }

        List<String> lignes = new ArrayList<>();
        Scanner lecteur = new Scanner(fichier, "UTF-8");
        while (lecteur.hasNextLine()) {
            lignes.add(lecteur.nextLine());
        }
        lecteur.close();
        return lignes;
    }
}
