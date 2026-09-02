package plateau_de_jeu;

public abstract class Case {
    private char car;
    
    public Case(char car){
        this.car = car ;
    }

    public char getCar(){
        return this.car;
    }

    public abstract boolean equals(Object o);

}
