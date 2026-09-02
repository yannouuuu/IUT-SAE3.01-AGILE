package modele;

public class Portes {
    private Lieux endroit;
    private boolean estFerme;
    private Joueur peutOuvrir;

    public Portes(Lieux endroit, boolean estFerme) {
        this.endroit = endroit;
        this.estFerme = estFerme;
        this.peutOuvrir = null;
    }

    public boolean ouvrirLaPorte(Joueur veutOuvrir) {
        if (this.estFerme && peutOuvrir != null && peutOuvrir.equals(veutOuvrir)) {
            this.estFerme = false;
            return true;
        }
        return false;
    }

    public boolean relierJoueur(Joueur j) {
        if (this.peutOuvrir == null) {
            this.peutOuvrir = j;
            return true;
        }
        return false;
    }
}
