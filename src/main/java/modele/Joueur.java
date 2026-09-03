package modele;

import carnet.Carnet;

public class Joueur {

    private final Suspects personnage;
    private int x;
    private int y;
    private Carte[] cardInventory;

    /** Carnet de deduction personnel : chaque joueur a le sien, pour ne pas voir celui des autres. */
    private final Carnet carnet = new Carnet();

    public Joueur(Suspects personnage, int x, int y, Carte[] cI) {
        this.personnage = personnage;
        this.x = x;
        this.y = y;
        this.cardInventory = cI;
    }

    public Joueur(Suspects personnage, Carte[] cI) {
        this(personnage, 1, 12, cI);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public Carte[] getCardInventory() {
        return cardInventory;
    }

    public Carnet getCarnet() {
        return carnet;
    }

    /**
     * Le personnage incarne par ce joueur : son nom et la couleur de sa pastille.
     */
    public Suspects getPersonnage() {
        return personnage;
    }

    @Override
    public String toString() {
        return personnage + " Ligne " + x + " colonne " + y;
    }
}