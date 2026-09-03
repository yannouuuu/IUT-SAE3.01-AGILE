package plateau_de_jeu;

public class Passage2 extends Case{
    private char car;
    
    public Passage2(){
        super('%');
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
        Passage2 other = (Passage2) o;
        if(this.car != other.car) return false ;
        return true ;  
    }
}
