package carnet;

import java.util.ArrayList;
import java.util.List;

/**
 * Etat metier du carnet : liste des suspects/armes/lieux a cocher,
 * curseur de selection, et ouverture/fermeture via /carnet et /map.
 */
public class Carnet {

    public static final String[] SUSPECTS = {"Moutarde", "Rose", "Pervenche", "Violet", "Olive", "Leblanc"};
    public static final String[] ARMES    = {"Chandelier", "Corde", "Pistolet", "Couteau", "Clef anglaise", "Matraque"};
    public static final String[] LIEUX    = {"Cuisine", "Salle a bal", "Salon", "Salle a manger",
                                              "Bureau", "Bibliotheque", "Hall", "Veranda"};

    private final List<Entree> entrees = new ArrayList<>();
    private int selection;
    private boolean ouvert = false;

    public Carnet() {
        ajouter("Suspects", SUSPECTS);
        ajouter("Armes",    ARMES);
        ajouter("Lieux",    LIEUX);
        selection = 1;
    }

    private void ajouter(String titre, String[] noms) {
        entrees.add(new Entree(titre, true));
        for (String nom : noms) {
            entrees.add(new Entree(nom, false));
        }
    }

    // --- commandes /carnet et /map ---

    public void ouvrir()        { ouvert = true; }
    public void fermer()        { ouvert = false; }
    public boolean estOuvert()  { return ouvert; }

    // --- saisie interne (espace = cocher, z = monter, s = descendre) ---

    /** Retourne true si la saisie a ete consommee par le carnet. */
    public boolean traiter(String saisie) {
        if (!ouvert) return false;
        if (saisie.isBlank()) {
            cocher();
        } else if (saisie.equals("z")) {
            monter();
        } else if (saisie.equals("s")) {
            descendre();
        } else {
            return false;
        }
        return true;
    }

    private void cocher() {
        Entree e = entrees.get(selection);
        if (!e.estTitre()) {
            e.toggleCoche();
        }
    }

    private void monter() {
        do {
            if (selection > 0) selection--;
        } while (entrees.get(selection).estTitre() && selection > 0);
    }

    private void descendre() {
        do {
            if (selection < entrees.size() - 1) selection++;
        } while (entrees.get(selection).estTitre() && selection < entrees.size() - 1);
    }

    // --- lecture de l'etat pour le rendu ---

    public List<Entree> getEntrees() {
        return entrees;
    }

    public int getSelection() {
        return selection;
    }

    // --- modele d'une entree ---

    public static class Entree {
        private final String nom;
        private final boolean titre;
        private boolean coche;

        public Entree(String nom, boolean titre) {
            this.nom   = nom;
            this.titre = titre;
            this.coche = false;
        }

        public String getNom()      { return nom; }
        public boolean estTitre()   { return titre; }
        public boolean estCoche()   { return coche; }
        public void toggleCoche()   { coche = !coche; }
    }
}
