package ui;

import carnet.Carnet;
import deplacement.De;
import modele.Joueur;

import java.util.ArrayList;
import java.util.List;

/** Panneau de droite : le carnet du joueur courant, ou la face du de pendant le deplacement. */
public class PanneauDroit {

    private Carnet carnet = new Carnet();
    private final De     de     = new De();
    private int valeurDe = 0;

    /** Bascule l'affichage et la saisie vers le carnet personnel du joueur dont c'est le tour. */
    public void afficherCarnetDe(Joueur joueur) {
        this.carnet = joueur.getCarnet();
        valeurDe = 0;
    }

    public void ouvrirCarnet() {
        carnet.ouvrir();
        valeurDe = 0;
    }

    public void revenirMap() {
        carnet.fermer();
        valeurDe = 0;
    }

    /** Lance le de : sa face remplace le carnet, et la valeur est retournee. */
    public int lancerDe() {
        valeurDe = de.lancer();
        return valeurDe;
    }

    public boolean surCarnet() {
        return carnet.estOuvert();
    }

    public Carnet getCarnet() {
        return carnet;
    }

    /** Retourne true si la saisie a ete consommee par le carnet. */
    public boolean traiter(String saisie) {
        return carnet.traiter(saisie);
    }

    public List<String> lignes() {
        List<String> lignes = new ArrayList<>();
        if (valeurDe > 0) {
            lignes.addAll(AffichageDe.rendu(valeurDe));
        } else {
            lignes.addAll(AffichageCarnet.rendu(carnet));
        }
        lignes.add("");
        lignes.addAll(AffichageAide.lignes());
        return lignes;
    }

    public String aide() {
        if (carnet.estOuvert()) {
            return "+ Saisie (Espace = cocher, z/s = naviguer, /map = revenir) > ";
        }
        return "+ Saisie (/de, /carnet, /fin, q = quitter) > ";
    }
}
