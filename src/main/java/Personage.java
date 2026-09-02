public class Personage {

    private String nom;
    private int x;
    private int y;
    private Carte[] cardInventory ;

    public Personage(String nom , int x, int y, Carte [] cI){
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.cardInventory = cI;
    }

    public Personage(String nom, Carte [] cI){
        this(nom,1,12, cI);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Carte[] getCardInventory() {return cardInventory;}
    
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