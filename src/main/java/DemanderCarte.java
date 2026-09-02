public class DemanderCarte {
    
    public boolean demande(Carte cDemander, Personage p) {
        Carte[] inventory = p.getCardInventory();
        for (Carte c : inventory) {
            if (c.equals(cDemander)) {
                System.out.println(p.getNom() + " a cette carte." );
                return true;
            }
        }
        System.out.println(p.getNom() + " n'a pas cette carte.");
        return false;
    }
}
