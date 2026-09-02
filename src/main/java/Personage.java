public class Personage {

    private String nom;
    private int x;
    private int y;

    public Personage(String nom , int x, int y){
        this.nom = nom;
        this.x = x;
        this.y = y;
    }

    public Personage(String nom){
        this(nom,1,12);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }

    public void setPosition(int x , int y){
        setX(x);
        setY(y);
    }

    public String getNom(){
        return this.nom ; 
    }
    
    public String toString(){
        return this.nom +" Ligne " + this.x + " colone" + this.y ;
    }



}