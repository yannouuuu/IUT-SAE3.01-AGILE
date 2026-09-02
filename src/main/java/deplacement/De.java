package deplacement;

import java.util.Random;

/** Le de : un lancer donne un nombre entre 1 et 6. */
public class De {

    private int numb ;
    private final Random rand; 
    

    public De(){
        this.rand = new Random();
        roll();    
    }
     

    public int lancer() {
        return rand.nextInt(6) + 1;
    }

    
}
