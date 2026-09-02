package deplacement;

import java.util.Scanner;

public class Movement {
    private Personage person;
    private int caseRestant;

    public Movement(Personage pers){
        this.person = pers;
        this.caseRestant =0;
    }

    public void commencement(int nombreCase){
        this.caseRestant = nombreCase ;
    }

    public boolean estTerminer() {
        return caseRestant == 0;
    }

    public boolean deplacer(int x, int y){
        if(caseRestant <= 0){
            return false;
        }

        int newX = this.person.getX + x;
        int newY = this.person.getY + y;

        if(newX < 0 || newX> 126 || newY < 0 || newY > 36 ){
            return false ;
        }
        
        this.person.setPosition(newX, newY);
        this.caseRestant--;

        return true;
    }

    public boolean moveUp(){
        return deplacer(0,1);
    }

    public boolean moveDown(){
         return deplacer(0,-1);
    }

    public boolean moveLeft(){
        return deplacer(-1, 0);
     
    }

    public boolean moveRight(){
        return deplacer(1, 0);
    }

    public Personage getPersonage(){
        return this.person;
    }

    }

