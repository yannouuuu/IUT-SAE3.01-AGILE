package ui;

/**
 * Un symbole a dessiner sur une case de la map : la pastille d'un joueur
 * ou la lettre d'un PNJ. La couleur est un code ANSI (chaine vide = sans couleur).
 */
public class Marqueur {

    private final char symbole;
    private final int ligne;
    private final int colonne;
    private final String couleur;

    public Marqueur(char symbole, int ligne, int colonne) {
        this(symbole, ligne, colonne, "");
    }

    public Marqueur(char symbole, int ligne, int colonne, String couleur) {
        this.symbole = symbole;
        this.ligne = ligne;
        this.colonne = colonne;
        this.couleur = couleur;
    }

    public char getSymbole() {
        return symbole;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public String getCouleur() {
        return couleur;
    }
}
