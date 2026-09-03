package modele;

import java.util.Arrays;
import modele.*;
import carnet.Carnet.Entree;
import java.util.ArrayList;
public class Joueur {

    private final Suspects personnage;
    private int x;
    private int y;
    private Carte[] cardInventory;
    private ArrayList<Suspects> suspectsCoche;
    private ArrayList<Armes> armeCoche;
    private ArrayList<Lieux> lieuxCoche;

    public Joueur(Suspects personnage, int x, int y, Carte[] cI) {
        this.personnage = personnage;
        this.x = x;
        this.y = y;
        this.cardInventory = cI;
        this.suspectsCoche = new ArrayList<>();
        this.armeCoche = new ArrayList<>();
        this.lieuxCoche = new ArrayList<>();
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

    public String getPosition(){
        return "("+this.x+","+this.y+")";
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public Carte[] getCardInventory() {
        return cardInventory;
    }

    public boolean peutAccuser(){
        return (armeCoche.size() == 5 && suspectsCoche.size() == 5 && lieuxCoche.size() == 8);
    }

    public Object[] quiAccuser(){
        Object[] retour = new Object[3];
        for (Armes nomArmes : Armes.values()){
            if (!armeCoche.contains(nomArmes)){
                retour[1] = nomArmes;
            }
        }
        for (Suspects nomSuspect : Suspects.values()){
            if (!suspectsCoche.contains(nomSuspect)){
                retour[0] = nomSuspect;
            }
        }
        for (Lieux nomLieux : Lieux.values()){
            if (!lieuxCoche.contains(nomLieux)){
                retour[2] = nomLieux;
            }
        }
        return retour;
    }

    public void ajouterCoche(Object obj){
        if (obj.getClass().equals(Armes.class)){
            armeCoche.add((Armes) obj);
        } else if (obj.getClass().equals(Suspects.class)){
            suspectsCoche.add((Suspects) obj);
        } else if (obj.getClass().equals(Lieux.class)){
            lieuxCoche.add((Lieux) obj);
        }
    }

    /**
     * Le personnage incarne par ce joueur : son nom et la couleur de sa pastille.
     */
    public Suspects getPersonnage() {
        return personnage;
    }

    @Override
    public String toString() {
        String tab = "" ;
        for( int i = 0 ; i < cardInventory.length ; i++){
            tab = cardInventory[i].toString();
        }
        return personnage + " Ligne " + x + " colonne " + y +"("+tab+")";
    }
}