package deplacement;

public class Mouvement {
    private int x;
    private int y;
    private int caseRestant;

    // Coordonnees stockees localement pour eviter la dependance au package par defaut
    public Mouvement(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.caseRestant = 0;
    }

    public void commencement(int nombreCase) {
        this.caseRestant = nombreCase;
    }

    public boolean estTermine() {
        return caseRestant == 0;
    }

    public boolean deplacer(int dx, int dy) {
        if (caseRestant <= 0) {
            return false;
        }

        int newX = this.x + dx;
        int newY = this.y + dy;

        if (newX < 0 || newX > 126 || newY < 0 || newY > 36) {
            return false;
        }

        this.x = newX;
        this.y = newY;
        this.caseRestant--;

        return true;
    }

    public boolean moveUp()    { return deplacer(0,  1); }
    public boolean moveDown()  { return deplacer(0, -1); }
    public boolean moveLeft()  { return deplacer(-1, 0); }
    public boolean moveRight() { return deplacer(1,  0); }

    public int getX() { return x; }
    public int getY() { return y; }
}
