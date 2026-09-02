package modele;

public class CarteSuspect extends Carte {
    private final Suspects suspect;

    public CarteSuspect(Suspects suspect) {
        this.suspect = suspect;
    }

    public Suspects getSuspect() {
        return suspect;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        CarteSuspect other = (CarteSuspect) obj;
        return this.suspect == other.suspect;
    }

    @Override
    public String toString() {
        return "Suspect : " + suspect;
    }
}
