package plateau_de_jeu;

public class Plateau {
    private final int HAUTEUR = 36;
    private final int LARGEUR = 126;
    private final Case[][] grille;

    public Plateau(){
        this.grille = new Case[HAUTEUR][LARGEUR];
        
    }



    public Case[][] getGrille() {
        return grille;
    }
    public int getHAUTEUR() {
        return HAUTEUR;
    }
    public int getLARGEUR() {
        return LARGEUR;
    }

}
