package plateau_de_jeu;

public class Mur extends Case{
    
    private char car;
    
    public Mur(){
        super('#');
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
        Mur other = (Mur) o;
        if(this.car != other.car) return false ;
        return true ;  
    }
}
