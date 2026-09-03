package interaction;

/** Transforme le texte tape par le joueur en commande connue du jeu. */
public class Commandes {

    public enum Type {
        DE,
        CARNET,
        MAP,
        CARTES,
        PARLER,
        HYPOTHESE,
        ACCUSER,
        PASSAGE,
        FIN_TOUR,
        AIDE,
        QUITTER,
        AUTRE,
        CHEAT_MOUVEMENT,
        SAVE,
        MUSIQUE
    }

    public static Type interpreter(String saisie) {
        String texte = saisie.toLowerCase();
        if (texte.equals("/de")) {
            return Type.DE;
        }
        if (texte.equals("/carnet")) {
            return Type.CARNET;
        }
        if (texte.equals("/map")) {
            return Type.MAP;
        }
        if (texte.equals("/cartes")) {
            return Type.CARTES;
        }
        if (texte.equals("/parler")) {
            return Type.PARLER;
        }
        if (texte.equals("/hypothese")) {
            return Type.HYPOTHESE;
        }
        if (texte.equals("/accuser")) {
            return Type.ACCUSER;
        }
        if (texte.equals("/passage")) {
            return Type.PASSAGE;
        }
        if (texte.equals("/fin")) {
            return Type.FIN_TOUR;
        }
        if (texte.equals("/aide")) {
            return Type.AIDE;
        }
        if (texte.equals("q")) {
            return Type.QUITTER;
        }
        if (texte.equals("/cheat_mouvement")){
            return Type.CHEAT_MOUVEMENT;
        }
        if (texte.equals("/save")){
            return Type.SAVE;
        }
        if (texte.equals("/musique")){
            return Type.MUSIQUE;
        }
        return Type.AUTRE;
    }
}