public class Portes {
    private Lieux endroit;
    private boolean estFerme;
    private Personage peutOuvrir;

    public Portes(Lieux endroit, boolean estFerme){
        this.endroit = endroit;
        this.estFerme = estFerme;
        this.peutOuvrir = null;
    }

    public boolean ouvrirLaPorte(Personage veutOuvrir){
        if (!this.estFerme && peutOuvrir.equals(veutOuvrir)){
            this.estFerme = false;
            return true;
        }
        return false;
    }

    public boolean relierJoueur(Personage j){
        if (this.peutOuvrir == null){
            this.peutOuvrir = j;
            return true;
        }
        return false;
    }
}
