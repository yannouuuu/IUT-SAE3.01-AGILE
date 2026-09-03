package enigmes;

import java.util.ArrayList;
import java.util.List;

/**
 * Les portes verrouillees du plateau.
 *
 * Chaque porte fermee connait la salle qu'elle bloque et la salle ou se
 * cache sa cle. Quand le joueur entre dans la salle de la cle, il la
 * ramasse et la porte devient ouvrable.
 */
public class PortesFermees {

    /** Une porte fermee : la salle bloquee, la salle de la cle, et l'etat de la cle. */
    public static class Porte {
        private final String salleVerrouillee;
        private final String salleDeLaCle;
        private boolean cleTrouvee = false;

        public Porte(String salleVerrouillee, String salleDeLaCle) {
            this.salleVerrouillee = salleVerrouillee;
            this.salleDeLaCle = salleDeLaCle;
        }

        public String getSalleVerrouillee() {
            return salleVerrouillee;
        }

        public String getSalleDeLaCle() {
            return salleDeLaCle;
        }

        public boolean estCleTrouvee() {
            return cleTrouvee;
        }
    }

    private final List<Porte> portes = new ArrayList<>();

    public void ajouter(String salleVerrouillee, String salleDeLaCle) {
        portes.add(new Porte(salleVerrouillee, salleDeLaCle));
    }

    public List<Porte> getPortes() {
        return portes;
    }

    /** Vrai si la salle n'est pas verrouillee, ou si sa cle a ete trouvee. */
    public boolean peutEntrer(String salle) {
        for (Porte porte : portes) {
            boolean verrouillee = porte.getSalleVerrouillee().equals(salle);
            if (verrouillee && !porte.estCleTrouvee()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ramasse toutes les cles cachees dans cette salle.
     * Retourne un message par cle trouvee (liste vide s'il n'y a rien).
     */
    public List<String> ramasserCles(String salle) {
        List<String> messages = new ArrayList<>();
        for (Porte porte : portes) {
            boolean cleIci = porte.getSalleDeLaCle().equals(salle);
            if (cleIci && !porte.estCleTrouvee()) {
                porte.cleTrouvee = true;
                messages.add("Tu as trouve la cle de la salle : " + porte.getSalleVerrouillee() + " !");
            }
        }
        return messages;
    }
}
