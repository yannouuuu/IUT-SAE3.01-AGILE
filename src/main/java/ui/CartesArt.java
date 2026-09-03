package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Charge les dessins ASCII des 21 cartes du Cluedo depuis resources/Cartes.txt.
 *
 * Ce fichier est un "strip" : chaque ligne contient les 21 cartes cote a cote,
 * separees par des &. On decoupe donc chaque ligne sur ce separateur pour
 * reconstituer, colonne par colonne, le dessin complet (9 lignes) de chaque carte.
 */
final class CartesArt {

    private static final String FICHIER = "/Cartes.txt";
    private static final int LARGEUR_CARTE = 19;

    /** Ordre des colonnes dans Cartes.txt : la cle est "TYPE:NOM_ENUM". */
    private static final String[] COLONNES = {
            "SUSPECT:LEBLANC", "SUSPECT:OLIVE", "SUSPECT:PERVENCHE", "SUSPECT:VIOLET",
            "SUSPECT:ROSE", "SUSPECT:MOUTARDE",
            "LIEU:HALL", "LIEU:SALON", "LIEU:SALLE_A_MANGER", "LIEU:CUISINE",
            "LIEU:SALLE_DE_BAL", "LIEU:VERANDA", "LIEU:BILLARD", "LIEU:BIBLIOTHEQUE", "LIEU:BUREAU",
            "ARME:COUTEAU", "ARME:CHANDELIER", "ARME:PISTOLET", "ARME:CORDE", "ARME:MATRAQUE",
            "ARME:CLEF_ANGLAISE"
    };

    private static final Map<String, List<String>> CARTES = charger();

    private CartesArt() {
    }

    /** Le dessin (plusieurs lignes) de la carte, ou un cadre vide si la cle est inconnue. */
    static List<String> dessin(String cle) {
        List<String> art = CARTES.get(cle.toUpperCase());
        if (art == null || art.isEmpty()) {
            return carteVide();
        }
        return art;
    }

    static int largeur() {
        return LARGEUR_CARTE;
    }

    private static Map<String, List<String>> charger() {
        Map<String, List<String>> cartes = new LinkedHashMap<>();
        try (InputStream flux = CartesArt.class.getResourceAsStream(FICHIER)) {
            if (flux == null) {
                return cartes;
            }

            List<String[]> colonnesParLigne = new ArrayList<>();
            try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(flux, StandardCharsets.UTF_8))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    colonnesParLigne.add(ligne.split("&", -1));
                }
            }

            for (int col = 0; col < COLONNES.length; col++) {
                List<String> dessin = new ArrayList<>();
                for (String[] champs : colonnesParLigne) {
                    if (col < champs.length) {
                        dessin.add(champs[col].trim());
                    }
                }
                cartes.put(COLONNES[col], dessin);
            }
        } catch (IOException e) {
            // Fichier illisible : les cartes s'afficheront vides plutot que de faire planter le jeu.
        }
        return cartes;
    }

    private static List<String> carteVide() {
        List<String> art = new ArrayList<>();
        art.add("-".repeat(LARGEUR_CARTE));
        for (int i = 0; i < 7; i++) {
            art.add("|" + " ".repeat(LARGEUR_CARTE - 2) + "|");
        }
        art.add("-".repeat(LARGEUR_CARTE));
        return art;
    }
}
