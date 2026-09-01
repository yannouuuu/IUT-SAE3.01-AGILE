import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Plateau de Cluedo en ASCII.
 *
 * Legende  : # = mur | . = couloir | === = porte | <-> = passage secret
 */
public class CluedoMap {

    private static final int CELL_W = 24;
    private static final int CELL_H = 6;
    private static final int DOOR_W = 5;
    private static final int ROWS = 5;
    private static final int COLS = 5;

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

    public static String render() {
        int height = ROWS * (CELL_H + 1) + 1;
        int width = COLS * (CELL_W + 1) + 1;
        char[][] grid = new char[height][width];
        for (char[] row : grid) Arrays.fill(row, ' ');

        // Bordure extérieure
        for (int y = 0; y < height; y++) {
            grid[y][0] = '#';
            grid[y][width - 1] = '#';
        }
        for (int x = 0; x < width; x++) {
            grid[0][x] = '#';
            grid[height - 1][x] = '#';
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
                        grid[top][x] = '#';
                        grid[top + CELL_H + 1][x] = '#';
                    }
                    for (int y = top; y <= top + CELL_H + 1; y++) {
                        grid[y][left] = '#';
                        grid[y][left + CELL_W + 1] = '#';
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

        // Portes (trous avec marqueurs =)
        for (String[] door : DOORS) {
            int[] pos = find(door[0]);
            int r = pos[0], c = pos[1];
            int top = r * (CELL_H + 1);
            int left = c * (CELL_W + 1);
            switch (door[1]) {
                case "N" -> {
                    int start = left + 1 + (CELL_W - DOOR_W) / 2;
                    for (int x = start; x < start + DOOR_W; x++) grid[top][x] = '=';
                }
                case "S" -> {
                    int start = left + 1 + (CELL_W - DOOR_W) / 2;
                    for (int x = start; x < start + DOOR_W; x++) grid[top + CELL_H + 1][x] = '=';
                }
                case "W" -> {
                    int start = top + 1 + (CELL_H - DOOR_W) / 2;
                    for (int y = start; y < start + DOOR_W; y++) grid[y][left] = '=';
                }
                case "E" -> {
                    int start = top + 1 + (CELL_H - DOOR_W) / 2;
                    for (int y = start; y < start + DOOR_W; y++) grid[y][left + CELL_W + 1] = '=';
                }
            }
        }

        String[] rows = new String[height + 2];
        for (int y = 0; y < height; y++) {
            rows[y] = new String(grid[y]);
        }
        rows[height] = "";
        rows[height + 1] = "Legende : # mur | . couloir | === porte | <-> passage secret";
        return String.join("\n", rows);
    }

    private static List<String> labelLines(String room) {
        List<String> lines = new ArrayList<>();
        lines.add(room);
        for (String[] secret : SECRET) {
            if (secret[0].equals(room))
                lines.add("<-> " + secret[1]);
        }
        return lines;
    }

    private static int[] find(String room) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (room.equals(LAYOUT[r][c]))
                    return new int[]{r, c};
            }
        }
        throw new IllegalArgumentException("Pièce inconnue : " + room);
    }
}
