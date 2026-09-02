package deplacement;

import java.util.Random;

/** Le de : un lancer donne un nombre entre 1 et 6. */
public class De {

<<<<<<< HEAD
    private int numb ;
    private Random rand; 
    

    public De(){
        this.rand = new Random();
        roll();    
    }
     
    public void roll() {
        this.numb = rand.nextInt(6) + 1;
=======
    private final Random rand = new Random();

    public int lancer() {
        return rand.nextInt(6) + 1;
>>>>>>> ffc8b5015f537ac03113a99717f3741782843250
    }
}
