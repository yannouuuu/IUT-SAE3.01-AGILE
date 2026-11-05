package modele;

import java.util.Objects;

public class CarteLieu extends Carte {
    private final Lieux lieu;

    public CarteLieu(Lieux lieu) {
        this.lieu = lieu;
    }

    public Lieux getLieu() {
        return lieu;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        CarteLieu other = (CarteLieu) obj;
        return this.lieu == other.lieu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lieu);
    }

    @Override
    public String toString() {
        return "Lieu : " + lieu;
    }
}