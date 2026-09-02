package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        try (InputStream in = Logo.class.getResourceAsStream("/logo.txt")) {
            if (in == null) {
                throw new IllegalStateException("logo.txt introuvable dans les resources");
            }
            List<String> lignes = new ArrayList<>();
            BufferedReader lecteur = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                lignes.add(ligne);
            }
            return lignes;
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire logo.txt", e);
        }
    }
}
