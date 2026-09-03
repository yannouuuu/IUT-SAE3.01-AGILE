package ui;

import carnet.Carnet;
import deplacement.De;

import java.util.List;

/** Panneau de droite : le carnet, ou la face du de pendant le deplacement. */
public class PanneauDroit {

    private final Carnet carnet = new Carnet();
    private final De     de     = new De();
    private int valeurDe = 0;

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
        if (valeurDe > 0) {
            return AffichageDe.rendu(valeurDe);
        }
        return AffichageCarnet.rendu(carnet);
    }

    public String aide() {
        if (carnet.estOuvert()) {
            return "+ Saisie (Espace = cocher, z/s = naviguer, /map = revenir) > ";
        }
        return "+ Saisie (/de, /carnet, /fin, /aide, q = quitter) > ";
    }
}
