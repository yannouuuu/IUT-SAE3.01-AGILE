import java;
import java.math.RoundingMode;
import java.util.Random;

public class Carte {
    private final Arm A;
    private final Lieux L;
    private final Suspect S;

    public Carte(){
        Random rand = new Random();
        this.A = Arm.values()[rand.nextInt(Arm.values().length)];
        this.L = Lieux.values()[rand.nextInt(Lieux.values().length)];
        this.S = Suspect.values()[rand.nextInt(Suspect.values().length)];
    }

}