package ui;

/** Detecte la largeur actuelle du terminal, pour centrer l'affichage dynamiquement. */
public final class Terminal {

    private static final int LARGEUR_PAR_DEFAUT = 100;

    private Terminal() {
    }

    /**
     * Largeur du terminal en colonnes.
     * Lit simplement la variable d'environnement COLUMNS quand elle existe (le shell la
     * met a jour a chaque redimensionnement dans beaucoup de terminaux) ; sinon, on garde
     * une largeur par defaut raisonnable.
     */
    public static int largeur() {
        String colonnes = System.getenv("COLUMNS");
        if (colonnes == null) {
            return LARGEUR_PAR_DEFAUT;
        }

        String texte = rogner(colonnes);
        if (texte.isEmpty()) {
            return LARGEUR_PAR_DEFAUT;
        }

        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            return LARGEUR_PAR_DEFAUT;
        }
    }

    private static String rogner(String texte) {
        int debut = 0;
        while (debut < texte.length() && texte.charAt(debut) <= ' ') {
            debut++;
        }
        int fin = texte.length();
        while (fin > debut && texte.charAt(fin - 1) <= ' ') {
            fin--;
        }
        return texte.substring(debut, fin);
    }
}
