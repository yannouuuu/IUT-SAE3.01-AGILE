/* 

public class Portes extends Case{
    private char car;
    private Lieux endroit;
    private boolean estFerme;
    private Personage peutOuvrir;

    public Portes(Lieux endroit, boolean estFerme){
        super('=');
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

    @Override
    public char getCar() {
        // TODO Auto-generated method stub
        return super.getCar();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null) return false ;
        if(this.getClass() != o.getClass()) return false ;
        Portes other = (Portes) o;
        if(this.car != other.car) return false ;
        return true ;  
    }
}
*/