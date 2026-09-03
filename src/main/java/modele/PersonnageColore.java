package modele;

/**
 * une couleur ANSI et une pastille prete a afficher.
 */
public interface PersonnageColore {

    /** Code ANSI de la couleur du personnage. */
    String getCouleur();

    /** La pastille coloree du personnage, prete a afficher dans un texte. */
    String pastille();
}
