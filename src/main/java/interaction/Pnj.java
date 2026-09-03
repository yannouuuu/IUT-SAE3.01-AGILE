package interaction;

import enigmes.Enigme;

/**
 * Un personnage non joueur pose sur la map.
 *
 * Quand le joueur est dans la meme salle que lui et tape /parler,
 * le PNJ donne l'enigme qui indique ou se trouve la cle d'une porte fermee.
 */
public class Pnj {

    private final String nom;
    private final String salle;
    private final String salleVerrouillee;
    private final Enigme enigme;

    public Pnj(String nom, String salle, String salleVerrouillee, Enigme enigme) {
        this.nom = nom;
        this.salle = salle;
        this.salleVerrouillee = salleVerrouillee;
        this.enigme = enigme;
    }

    public String getNom() {
        return nom;
    }

    public String getSalle() {
        return salle;
    }

    public String dialogue() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" : \"La porte de la salle ").append(salleVerrouillee);
        sb.append(" est fermee a cle...\"\n");
        sb.append(nom).append(" : \"Voici mon enigme : ").append(enigme.getQuestion()).append("\"\n");
        sb.append("(La reponse a l'enigme est la salle ou se trouve la cle.)");
        return sb.toString();
    }
}
