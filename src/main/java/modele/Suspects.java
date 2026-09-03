package modele;

/**
 * Les six personnages du Cluedo, chacun avec la couleur officielle de son pion.
 */
public enum Suspects implements PersonnageColore {

    MOUTARDE("\u001B[33m"),   // Colonel Moutarde : jaune
    ROSE("\u001B[91m"),       // Mademoiselle Rose : rouge ecarlate
    PERVENCHE("\u001B[94m"),  // Madame Pervenche : bleu
    VIOLET("\u001B[35m"),     // Professeur Violet : violet
    OLIVE("\u001B[32m"),      // Docteur Olive : vert
    LEBLANC("\u001B[97m");    // Madame Leblanc : blanc

    /** Remet la couleur du terminal a la normale apres une pastille. */
    public static final String RESET = "\u001B[0m";

    /** Le rond colore qui represente le personnage sur la map. */
    public static final char SYMBOLE_PASTILLE = '\u25CF'; // ●

    private final String couleur;

    Suspects(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public String getCouleur() {
        return couleur;
    }

    /** La pastille coloree du personnage, prete a afficher dans un texte. */
    @Override
    public String pastille() {
        return couleur + SYMBOLE_PASTILLE + RESET;
    }
}
