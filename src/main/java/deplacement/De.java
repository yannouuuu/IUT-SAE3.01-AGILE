package java.deplacement;

import java.util.Random;

public class De {

    private int numb ;
    private Random rand; 
    

    public De(){
        this.rand = new Random();
        roll();    
    }
     
    public void roll() {
        this.numb = rand.nextInt(6) + 1;
    }

    public int getNum(){
        return this.numb ;
    }
    
}
