package modele;

public abstract class Carte {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return true;
    }

    public boolean demande(Carte cDemander, Joueur p) {
        Carte[] inventory = p.getCardInventory();
        for (Carte c : inventory) {
            if (c.equals(cDemander)) {
                System.out.println(p.getNom() + " a cette carte.");
                return true;
            }
        }
        System.out.println(p.getNom() + " n'a pas cette carte.");
        return false;
    }

    public abstract String toString();
}
