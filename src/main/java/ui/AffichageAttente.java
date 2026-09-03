package ui;

import java.util.List;

/**
 * Ecran de passation affiche entre deux joueurs humains, en mode multijoueur local.
 * Il efface les infos privees du joueur precedent avant de reveler celles du suivant.
 */
public final class AffichageAttente {

    private static final String CYAN = "\u001B[96m";

    public static List<String> rendu(String personnage, String pastille, int largeurTotale) {
        List<String> contenu = List.of(
                "Passe l'appareil a :",
                pastille + " " + personnage,
                "",
                "Appuie sur Entree quand tu es pret."
        );
        List<String> boite = Cadre.dessiner("CHANGEMENT DE JOUEUR", contenu, CYAN);
        return Cadre.centrerBloc(boite, largeurTotale);
    }
}
