package plateau_de_jeu;

public class Couloir extends Case{
    
    private char car;
    
    public Couloir(){
        super('.');
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
        Couloir other = (Couloir) o;
        if(this.car != other.car) return false ;
        return true ;  
    }
}
