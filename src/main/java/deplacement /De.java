package java.deplacement;

import java.util.Random;

public class De {

    private int num ;

    public De(){
        Random rand = new Random();
        this.num = rand.nextInt(6)+1;
    }

    public int getNum(){
        return this.num ;
    }
    
}
