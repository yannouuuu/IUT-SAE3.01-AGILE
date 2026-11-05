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
    public static final String[] LIEUX    = {"Cuisine", "Salle de bal", "Salon", "Salle a manger",
                                              "Billard", "Bureau", "Bibliotheque", "Hall", "Veranda"};

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

    /** Efface toutes les coches et possessions d'une partie precedente, pour repartir a zero. */
    public void reinitialiser() {
        for (Entree entree : entrees) {
            entree.reinitialiser();
        }
        selection = 1;
        ouvert = false;
    }

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
        if (!e.estTitre() && !e.estPossedee()) {
            e.toggleCoche();
        }
    }

    /** Coche une carte en deduction, par exemple quand un joueur nous la montre. */
    public void cocherCarte(String nom) {
        for (Entree entree : entrees) {
            boolean memeNom = !entree.estTitre() && entree.getNom().equalsIgnoreCase(nom);
            if (memeNom) {
                entree.cocher();
            }
        }
    }

    /** Marque une carte comme possedee par le joueur (ses cartes de depart). */
    public void marquerPossedee(String nom) {
        for (Entree entree : entrees) {
            boolean memeNom = !entree.estTitre() && entree.getNom().equalsIgnoreCase(nom);
            if (memeNom) {
                entree.marquerPossedee();
            }
        }
    }

    private void monter() {
        int cible = selection - 1;
        while (cible >= 0 && entrees.get(cible).estTitre()) {
            cible--;
        }
        if (cible >= 0) {
            selection = cible;
        }
    }

    private void descendre() {
        int cible = selection + 1;
        while (cible < entrees.size() && entrees.get(cible).estTitre()) {
            cible++;
        }
        if (cible < entrees.size()) {
            selection = cible;
        }
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
        private boolean possedee;

        public Entree(String nom, boolean titre) {
            this.nom      = nom;
            this.titre    = titre;
            this.coche    = false;
            this.possedee = false;
        }

        public String getNom()        { return nom; }
        public boolean estTitre()     { return titre; }
        public boolean estCoche()     { return coche; }
        public boolean estPossedee()  { return possedee; }
        public void toggleCoche()     { coche = !coche; }
        public void cocher()          { coche = true; }
        public void marquerPossedee() { possedee = true; }
        public void reinitialiser()   { coche = false; possedee = false; }
    }
}
