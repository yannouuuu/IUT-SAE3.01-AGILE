package modele;

import java.util.Objects;

public class CarteArme extends Carte {
    private final Armes arme;

    public CarteArme(Armes arme) {
        this.arme = arme;
    }

    public Armes getArme() {
        return arme;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        CarteArme other = (CarteArme) obj;
        return this.arme == other.arme;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arme);
    }

    @Override
    public String toString() {
        return "Arme : " + arme;
    }
}