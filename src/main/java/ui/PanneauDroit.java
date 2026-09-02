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

    public void lancerDe() {
        valeurDe = de.lancer();
    }

    public boolean surCarnet() {
        return carnet.estOuvert();
    }

    public void traiter(String saisie) {
        carnet.traiter(saisie);
    }

    public List<String> lignes() {
        if (valeurDe > 0) {
            return AffichageDe.rendu(valeurDe);
        }
        return AffichageCarnet.rendu(carnet);
    }

    public String aide() {
        if (carnet.estOuvert()) {
            return "+ Saisie (Espace = cocher, z/s = naviguer, /map = revenir, q = quitter) > ";
        }
        return "+ Saisie (/carnet = ouvrir le carnet, /de = lancer le de, q = quitter) > ";
    }
}
