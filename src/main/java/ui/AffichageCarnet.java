package ui;

import java.util.ArrayList;
import java.util.List;

/** Carnet des suspects : on coche les elements comme sur le carnet papier. */
public class AffichageCarnet {

    private static final String JAUNE = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    private static final String[] SUSPECTS = {"Moutarde", "Rose", "Pervenche", "Violet", "Olive", "Leblanc"};
    private static final String[] ARMES = {"Chandelier", "Corde", "Pistolet", "Couteau", "Clef anglaise", "Matraque"};
    private static final String[] LIEUX = {"Cuisine", "Salle a bal", "salon", "Salle a manger",
            "Bureau", "Bibliotheque", "Hall", "Veranda"};

    private final List<Entree> entrees = new ArrayList<>();
    private int selection;

    public AffichageCarnet() {
        ajouter("Suspects", SUSPECTS);
        ajouter("Armes", ARMES);
        ajouter("Lieux", LIEUX);
        selection = 1;
    }

    private void ajouter(String titre, String[] noms) {
        entrees.add(new Entree(titre, true));
        for (String nom : noms) {
            entrees.add(new Entree(nom, false));
        }
    }

    /** " " (ligne vide) = cocher/decocher, z = monter, s = descendre. */
    public void traiter(String saisie) {
        if (saisie.isBlank()) {
            cocher();
        } else if (saisie.equals("z")) {
            monter();
        } else if (saisie.equals("s")) {
            descendre();
        }
    }

    private void cocher() {
        Entree e = entrees.get(selection);
        if (!e.titre) {
            e.coche = !e.coche;
        }
    }

    private void monter() {
        do {
            if (selection > 0) {
                selection--;
            }
        } while (entrees.get(selection).titre && selection > 0);
    }

    private void descendre() {
        do {
            if (selection < entrees.size() - 1) {
                selection++;
            }
        } while (entrees.get(selection).titre && selection < entrees.size() - 1);
    }

    /** Rend le carnet encadre de jaune quand il est actif. */
    public List<String> rendu(boolean actif) {
        List<String> contenu = new ArrayList<>();
        for (int i = 0; i < entrees.size(); i++) {
            Entree e = entrees.get(i);
            String prefixe = i == selection ? "> " : "  ";
            if (e.titre) {
                contenu.add(prefixe + e.nom + " :");
            } else {
                contenu.add(prefixe + "[" + (e.coche ? "x" : " ") + "] " + e.nom);
            }
        }

        int largeur = 0;
        for (String ligne : contenu) {
            if (ligne.length() > largeur) {
                largeur = ligne.length();
            }
        }

        List<String> lignes = new ArrayList<>();
        String bord = "-".repeat(largeur);
        lignes.add("+" + bord + "+");
        for (String ligne : contenu) {
            lignes.add("| " + pad(ligne, largeur - 2) + " |");
        }
        lignes.add("+" + bord + "+");

        if (actif) {
            for (int i = 0; i < lignes.size(); i++) {
                lignes.set(i, JAUNE + lignes.get(i) + RESET);
            }
        }
        return lignes;
    }

    private String pad(String ligne, int largeur) {
        StringBuilder sb = new StringBuilder(ligne);
        while (sb.length() < largeur) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static class Entree {
        final String nom;
        final boolean titre;
        boolean coche;

        Entree(String nom, boolean titre) {
            this.nom = nom;
            this.titre = titre;
        }
    }
}
