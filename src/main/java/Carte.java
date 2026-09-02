import java.util.Random;

public class Carte {
    private final Armes A;
    private final Lieux L;
    private final Suspects S;

    public Carte(){
        Random rand = new Random();
        this.A = Armes.values()[rand.nextInt(Armes.values().length)];
        this.L = Lieux.values()[rand.nextInt(Lieux.values().length)];
        this.S = Suspects.values()[rand.nextInt(Suspects.values().length)];
    }

    public Armes getArmes() {return this.A;}

    public Lieux getLieux() {return this.L;}

    public Suspects getSuspects() {return this.S;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Carte other = (Carte) obj;
        return this.A == other.A &&
            this.L == other.L &&
            this.S == other.S;
    }

}