package java.deplacement;

import java.util.Random;

public class De {

    private int numb ;

    public De(){
        Random rand = new Random();
        this.numb = rand.nextInt(6)+1;
    }

    public int getNum(){
        return this.numb ;
    }
    
}
