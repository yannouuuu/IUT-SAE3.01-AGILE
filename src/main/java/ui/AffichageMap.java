package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Plateau de Cluedo en ASCII.
 *
 * Legende  : █ = mur | . = couloir | === = porte | <-> = passage secret
 */
public class AffichageMap {

    private static final int CELL_W = 24;
    private static final int CELL_H = 6;
    private static final int ROWS = 5;
    private static final int COLS = 5;

    // Caracteres et couleurs du decor
    private static final char MUR = '█';
    private static final String GRIS = "\u001B[90m";
    private static final String JAUNE = "\u001B[33m";
    private static final String CYAN = "\u001B[96m";
    private static final String RESET = "\u001B[0m";

    /**
     * Decoupage d'une case en points de deplacement : les pointilles du couloir.
     * Un pas du de = un point. Une case fait 3 points de haut et 8 de large.
     */
    public static final int POINTS_PAR_CASE_LIGNE = 3;
    public static final int POINTS_PAR_CASE_COLONNE = 8;
    public static final int NB_POINTS_LIGNES = ROWS * POINTS_PAR_CASE_LIGNE;
    public static final int NB_POINTS_COLONNES = COLS * POINTS_PAR_CASE_COLONNE;

    /** null = couloir */
    private static final String[][] LAYOUT = {
            {"Cuisine", null, "Salle de bal", null, "Véranda"},
            {null, null, null, null, null},
            {"Salle à manger", null, "Billard", null, "Bibliothèque"},
            {null, null, null, null, null},
            {"Salon", null, "Hall", null, "Bureau"},
    };

    /** Portes : {pièce, côté} avec côté = N/S/E/W */
    private static final String[][] DOORS = {
            {"Cuisine", "E"},
            {"Salle de bal", "S"},
            {"Salle de bal", "E"},
            {"Véranda", "W"},
            {"Salle à manger", "N"},
            {"Salle à manger", "E"},
            {"Billard", "N"},
            {"Billard", "S"},
            {"Bibliothèque", "N"},
            {"Bibliothèque", "W"},
            {"Salon", "N"},
            {"Salon", "E"},
            {"Hall", "N"},
            {"Hall", "E"},
            {"Hall", "W"},
            {"Bureau", "N"},
            {"Bureau", "W"},
    };

    /** Passages secrets : {pièce de départ, pièce d'arrivée} (symétriques) */
    private static final String[][] SECRET = {
            {"Cuisine", "Bureau"},
            {"Véranda", "Salon"},
    };

    /** Nom de la salle sur laquelle se trouve ce point, ou null si c'est le couloir. */
    public static String salleAuPoint(int ligne, int colonne) {
        return LAYOUT[ligne / POINTS_PAR_CASE_LIGNE][colonne / POINTS_PAR_CASE_COLONNE];
    }

    /** Le point au milieu d'une salle : la position d'un joueur qui est dans cette salle. */
    public static int[] pointCentreSalle(String piece) {
        int[] cellule = positionPiece(piece);
        int ligne = cellule[0] * POINTS_PAR_CASE_LIGNE + 1;
        int colonne = cellule[1] * POINTS_PAR_CASE_COLONNE + 3;
        return new int[]{ligne, colonne};
    }

    /**
     * Le point du couloir juste devant une porte de la salle.
     * cote : "N", "S", "E" ou "W". Retourne null si la salle n'a pas de porte de ce cote.
     */
    public static int[] pointDevantPorte(String piece, String cote) {
        boolean existe = false;
        for (String[] porte : DOORS) {
            if (porte[0].equals(piece) && porte[1].equals(cote)) {
                existe = true;
            }
        }
        if (!existe) {
            return null;
        }

        int[] cellule = positionPiece(piece);
        int haut = cellule[0] * POINTS_PAR_CASE_LIGNE;
        int gauche = cellule[1] * POINTS_PAR_CASE_COLONNE;

        if (cote.equals("N")) {
            return new int[]{haut - 1, gauche + 4};
        }
        if (cote.equals("S")) {
            return new int[]{haut + POINTS_PAR_CASE_LIGNE, gauche + 4};
        }
        if (cote.equals("W")) {
            return new int[]{haut + 1, gauche - 1};
        }
        return new int[]{haut + 1, gauche + POINTS_PAR_CASE_COLONNE}; // cote E
    }

    /** Lignes du plateau pretes a afficher, sans personnages. */
    public List<String> lignes() {
        return lignes(new ArrayList<>());
    }

    /** Lignes du plateau pretes a afficher, avec les personnages poses dessus. */
    public List<String> lignes(List<Marqueur> marqueurs) {
        int height = ROWS * (CELL_H + 1) + 1;
        int width = COLS * (CELL_W + 1) + 1;
        char[][] grid = new char[height][width];
        for (char[] row : grid) Arrays.fill(row, ' ');

        // Bordure extérieure
        for (int y = 0; y < height; y++) {
            grid[y][0] = MUR;
            grid[y][width - 1] = MUR;
        }
        for (int x = 0; x < width; x++) {
            grid[0][x] = MUR;
            grid[height - 1][x] = MUR;
        }

        // Couloirs (pointillés)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (LAYOUT[r][c] == null) {
                    int top = r * (CELL_H + 1) + 1;
                    int left = c * (CELL_W + 1) + 1;
                    for (int dy = 1; dy < CELL_H; dy += 2) {
                        for (int dx = 1; dx < CELL_W; dx += 3) {
                            grid[top + dy][left + dx] = '.';
                        }
                    }
                }
            }
        }

        // Murs des pièces
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (LAYOUT[r][c] != null) {
                    int top = r * (CELL_H + 1);
                    int left = c * (CELL_W + 1);
                    for (int x = left; x <= left + CELL_W + 1; x++) {
                        grid[top][x] = MUR;
                        grid[top + CELL_H + 1][x] = MUR;
                    }
                    for (int y = top; y <= top + CELL_H + 1; y++) {
                        grid[y][left] = MUR;
                        grid[y][left + CELL_W + 1] = MUR;
                    }
                }
            }
        }

        // Étiquettes des pièces
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                String room = LAYOUT[r][c];
                if (room != null) {
                    List<String> lines = labelLines(room);

                    int top = r * (CELL_H + 1) + 1;
                    int left = c * (CELL_W + 1) + 1;
                    int startY = top + (CELL_H - lines.size()) / 2;

                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        int startX = left + (CELL_W - line.length()) / 2;

                        for (int k = 0; k < line.length(); k++) {
                            grid[startY + i][startX + k] = line.charAt(k);
                        }
                    }
                }
            }
        }

        // Portes : une seule case '=', alignee sur l'unique point d'entree reel (pointDevantPorte)
        for (String[] door : DOORS) {
            int[] pos = positionPiece(door[0]);
            int r = pos[0], c = pos[1];
            int top = r * (CELL_H + 1);
            int left = c * (CELL_W + 1);
            int[] point = pointDevantPorte(door[0], door[1]);
            switch (door[1]) {
                case "N" -> {
                    int colOffset = point[1] % POINTS_PAR_CASE_COLONNE;
                    grid[top][left + 2 + colOffset * 3] = '=';
                }
                case "S" -> {
                    int colOffset = point[1] % POINTS_PAR_CASE_COLONNE;
                    grid[top + CELL_H + 1][left + 2 + colOffset * 3] = '=';
                }
                case "W" -> {
                    int rowOffset = point[0] % POINTS_PAR_CASE_LIGNE;
                    grid[top + 2 + rowOffset * 2][left] = '=';
                }
                case "E" -> {
                    int rowOffset = point[0] % POINTS_PAR_CASE_LIGNE;
                    grid[top + 2 + rowOffset * 2][left + CELL_W + 1] = '=';
                }
            }
        }

        // Personnages (joueurs, PNJ) : leurs coordonnees sont des points de deplacement.
        // Dans une salle : en haut a gauche de la case. Dans le couloir : sur le pointille.
        // Quand plusieurs partagent le meme point, on les decale vers la droite.
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < marqueurs.size(); i++) {
            Marqueur m = marqueurs.get(i);
            int dejaSurLePoint = 0;
            for (int j = 0; j < i; j++) {
                Marqueur autre = marqueurs.get(j);
                boolean memePoint = autre.getLigne() == m.getLigne() && autre.getColonne() == m.getColonne();
                if (memePoint) {
                    dejaSurLePoint++;
                }
            }

            int celluleLigne = m.getLigne() / POINTS_PAR_CASE_LIGNE;
            int celluleColonne = m.getColonne() / POINTS_PAR_CASE_COLONNE;

            int y;
            int x;
            if (LAYOUT[celluleLigne][celluleColonne] != null) {
                // dans une salle
                y = celluleLigne * (CELL_H + 1) + 1;
                x = celluleColonne * (CELL_W + 1) + 2 + dejaSurLePoint * 2;
            } else {
                // dans le couloir, pile sur un pointille
                y = celluleLigne * (CELL_H + 1) + 2 + (m.getLigne() % POINTS_PAR_CASE_LIGNE) * 2;
                x = celluleColonne * (CELL_W + 1) + 2 + (m.getColonne() % POINTS_PAR_CASE_COLONNE) * 3
                        + dejaSurLePoint;
            }
            grid[y][x] = m.getSymbole();
            positions.add(new Position(y, x, m.getCouleur()));
        }

        List<String> rows = new ArrayList<>(height + 1);
        for (int y = 0; y < height; y++) {
            rows.add(colorerLigne(grid[y], y, positions));
        }
        rows.add("Legende : " + GRIS + MUR + RESET + " mur | " + GRIS + "." + RESET + " couloir | "
                + JAUNE + "===" + RESET + " porte | " + CYAN + "<->" + RESET + " passage secret | "
                + CYAN + "P" + RESET + " pnj");
        return rows;
    }

    /** Ou se trouve un marqueur dans la grille de caracteres, et sa couleur. */
    private static class Position {
        private final int y;
        private final int x;
        private final String couleur;

        Position(int y, int x, String couleur) {
            this.y = y;
            this.x = x;
            this.couleur = couleur;
        }
    }

    /**
     * Construit la ligne en colorant chaque caractere : les marqueurs
     * (joueurs, PNJ) ont leur propre couleur, le decor a la sienne.
     * On n'ecrit un code couleur que quand la couleur change.
     */
    private String colorerLigne(char[] caracteres, int y, List<Position> positions) {
        StringBuilder sb = new StringBuilder();
        String couleurCourante = "";

        for (int x = 0; x < caracteres.length; x++) {
            String couleur = couleurDuMarqueur(y, x, positions);
            if (couleur.isEmpty()) {
                couleur = couleurDuDecor(caracteres[x]);
            }
            if (!couleur.equals(couleurCourante)) {
                sb.append(RESET);
                sb.append(couleur);
                couleurCourante = couleur;
            }
            sb.append(caracteres[x]);
        }

        if (!couleurCourante.isEmpty()) {
            sb.append(RESET);
        }
        return sb.toString();
    }

    /** La couleur du marqueur pose a cet endroit, ou une chaine vide s'il n'y en a pas. */
    private String couleurDuMarqueur(int y, int x, List<Position> positions) {
        for (Position position : positions) {
            if (position.y == y && position.x == x) {
                return position.couleur;
            }
        }
        return "";
    }

    /** La couleur du decor : murs et couloir en gris, portes en jaune, passages en cyan. */
    private String couleurDuDecor(char caractere) {
        if (caractere == MUR || caractere == '.') {
            return GRIS;
        }
        if (caractere == '=') {
            return JAUNE;
        }
        if (caractere == '<' || caractere == '>' || caractere == '-') {
            return CYAN;
        }
        return "";
    }

    /** Taille de la grille logique en cases (1 case = une salle ou un couloir). */
    public int getNbLignes() {
        return ROWS;
    }

    public int getNbColonnes() {
        return COLS;
    }

    /** Coordonnees {ligne, colonne} d'une salle dans la grille 5x5 des cases. */
    public static int[] positionPiece(String piece) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (piece.equals(LAYOUT[r][c])) {
                    return new int[]{r, c};
                }
            }
        }
        throw new IllegalArgumentException("Pièce inconnue : " + piece);
    }

    private List<String> labelLines(String room) {
        List<String> lines = new ArrayList<>();
        lines.add(room);
        for (String[] secret : SECRET) {
            if (secret[0].equals(room))
                lines.add("<-> " + secret[1]);
        }
        return lines;
    }
}
