package modele;

public class Joueur {

    private final String nom;
    private int x;
    private int y;
    private Carte[] cardInventory;

    public Joueur(String nom, int x, int y, Carte[] cI) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.cardInventory = cI;
    }

    public Joueur(String nom, Carte[] cI) {
        this(nom, 1, 12, cI);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public Carte[] getCardInventory() { return cardInventory; }

    public String getNom() { return nom; }

    @Override
    public String toString() {
        return nom + " Ligne " + x + " colonne " + y;
    }
}
