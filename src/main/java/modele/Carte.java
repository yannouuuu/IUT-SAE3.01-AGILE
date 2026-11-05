package modele;

public abstract class Carte {

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public abstract int hashCode();

    public abstract String toString();
}