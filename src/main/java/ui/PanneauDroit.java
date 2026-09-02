package ui;

import deplacement.De;

import java.util.List;

/** Panneau de droite : le carnet, ou la face du de pendant le deplacement. */
public class PanneauDroit {

    private final AffichageCarnet carnet = new AffichageCarnet();
    private final De de = new De();
    private boolean vueCarnet = false;
    private int valeurDe;

    public void ouvrirCarnet() {
        vueCarnet = true;
        valeurDe = 0;
    }

    public void revenirMap() {
        vueCarnet = false;
        valeurDe = 0;
    }

    public void lancerDe() {
        valeurDe = de.lancer();
    }

    public boolean surCarnet() {
        return vueCarnet;
    }

    public void traiter(String saisie) {
        if (vueCarnet) {
            carnet.traiter(saisie);
        }
    }

    public List<String> lignes() {
        if (valeurDe > 0) {
            return AffichageDe.rendu(valeurDe);
        }
        return carnet.rendu(vueCarnet);
    }

    public String aide() {
        if (vueCarnet) {
            return "+ Saisie (Espace = cocher, z/s = naviguer, /map = revenir, q = quitter) > ";
        }
        return "+ Saisie (/carnet = ouvrir le carnet, /de = lancer le de, q = quitter) > ";
    }
}
