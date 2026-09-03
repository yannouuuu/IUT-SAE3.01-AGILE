package moteur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import interaction.Commandes;
import interaction.Pnj;
import interaction.Saisie;
import modele.Armes;
import modele.Bot;
import modele.Carte;
import modele.CarteArme;
import modele.CarteLieu;
import modele.CarteSuspect;
import modele.Joueur;
import modele.Lieux;
import modele.Suspects;
import ui.AffichageMap;
import ui.Marqueur;
import ui.Menu;
import ui.Rendu;

/** Machine a etats du jeu : Menu -> Partie -> Fin. */
public class BoucleDeJeu {

    private enum Etat { MENU, PARTIE, FIN, ARRET }

    /** Les salles du plateau, dans l'ordre propose au joueur pour l'accusation. */
    private static final String[] SALLES = {
            "Cuisine", "Salle de bal", "Véranda",
            "Salle à manger", "Billard", "Bibliothèque",
            "Salon", "Hall", "Bureau"
    };

    /**
     * Points de depart des pions, colles aux bordures comme sur le vrai plateau :
     * a gauche entre Cuisine et Salle a manger, a gauche entre Salle a manger et Salon,
     * a droite entre Veranda et Bibliotheque, a droite entre Bibliotheque et Bureau.
     */
    private static final int[][] DEPARTS = {
            {4, 0},   // bordure gauche, entre Cuisine et Salle a manger
            {10, 0},  // bordure gauche, entre Salle a manger et Salon
            {4, 39},  // bordure droite, entre Veranda et Bibliotheque
            {10, 39}, // bordure droite, entre Bibliotheque et Bureau
    };

    private final Scanner clavier;
    private final Saisie saisie;
    private final Rendu rendu = new Rendu();
    private final Random hasard = new Random();

    /** Les messages a afficher sous la map au prochain rafraichissement. */
    private final List<String> messages = new ArrayList<>();

    private Etat etat = Etat.MENU;
    private EtatPartie partie;
    private TourManager tours;

    public BoucleDeJeu(Scanner clavier) {
        this.clavier = clavier;
        this.saisie = new Saisie(clavier);
    }

    public void lancer() {
        while (etat != Etat.ARRET) {
            if (etat == Etat.MENU) {
                faireMenu();
            } else if (etat == Etat.PARTIE) {
                faireUnTour();
            } else if (etat == Etat.FIN) {
                faireFin();
            }
        }
    }

    /** Ajoute un message qui sera affiche sous la map au prochain rafraichissement. */
    private void dire(String message) {
        messages.add(message);
    }

    // Etat MENU

    private void faireMenu() {
        effacerEcran();
        String choix = Menu.afficher(clavier);
        if (choix.equals("1")) {
            preparerPartie();
            etat = Etat.PARTIE;
        } else if (choix.equals("2")) {
            System.out.println("Le multijoueur arrive bientot !");
        } else if (choix.equals("q")) {
            System.out.println("Au revoir !");
            etat = Etat.ARRET;
        } else {
            System.out.println("Choix inconnu, tape 1, 2 ou q.");
        }
    }

    private void preparerPartie() {
        ItnitialiserPartie init = new ItnitialiserPartie();

        Object[] resultat = init.systemeDistribution();
        List<List<Carte>> inventaires = (List<List<Carte>>) resultat[0];
        Carte[] cartesCachees = (Carte[]) resultat[1];

        Joueur[] joueurs = init.creerJoueurs(inventaires);
        partie = new EtatPartie(joueurs, cartesCachees);
        tours = new TourManager(joueurs);

        // Chaque pion demarre sur un point de depart du bord de la map
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i].setPosition(DEPARTS[i][0], DEPARTS[i][1]);
        }

        // Les cartes du joueur sont hors de cause : on les coche d'office dans le carnet
        cocherCartesDansLeCarnet(joueurs[0]);

        Suspects personnage = joueurs[0].getPersonnage();
        dire("Bienvenue ! Tu joues " + personnage + " " + personnage.pastille() + ".");
        dire("Tes " + joueurs[0].getCardInventory().length
                + " cartes de depart sont deja cochees dans ton carnet (/carnet pour les voir).");

        // Avant meme d'afficher la map pour la premiere fois, on montre ses cartes de depart au joueur
        afficherCartesJoueursTemporairement(joueurs[0]);
    }

    private void cocherCartesDansLeCarnet(Joueur joueur) {
        for (Carte carte : joueur.getCardInventory()) {
            rendu.getPanneau().getCarnet().marquerPossedee(nomPourCarnet(carte));
        }
    }

    /** Transforme une carte en nom du carnet : SALLE_DE_BAL -> "salle de bal". */
    private String nomPourCarnet(Carte carte) {
        String nomEnum = "";
        if (carte instanceof CarteSuspect carteSuspect) {
            nomEnum = carteSuspect.getSuspect().name();
        } else if (carte instanceof CarteArme carteArme) {
            nomEnum = carteArme.getArme().name();
        } else if (carte instanceof CarteLieu carteLieu) {
            nomEnum = carteLieu.getLieu().name();
        }
        return nomEnum.replace('_', ' ');
    }

    /** Les cles des cartes du joueur ("SUSPECT:MOUTARDE", ...), pour retrouver leur dessin dans AffichageCartes. */
    private List<String> clesCartes(Joueur joueur) {
        List<String> cles = new ArrayList<>();
        for (Carte carte : joueur.getCardInventory()) {
            cles.add(cleCarte(carte));
        }
        return cles;
    }

    private String cleCarte(Carte carte) {
        if (carte instanceof CarteSuspect carteSuspect) {
            return "SUSPECT:" + carteSuspect.getSuspect().name();
        } else if (carte instanceof CarteArme carteArme) {
            return "ARME:" + carteArme.getArme().name();
        } else if (carte instanceof CarteLieu carteLieu) {
            return "LIEU:" + carteLieu.getLieu().name();
        }
        return "";
    }

    // Etat PARTIE : un appel = le tour d'un joueur
    
    private void faireUnTour() {
        Joueur courant = tours.joueurCourant();

        if (tours.estUnBot()) {
            jouerTourBot((Bot) courant);
        } else {
            jouerTourJoueur(courant);
        }

        if (partie.estTerminee()) {
            etat = Etat.FIN;
        } else {
            tours.joueurSuivant();
        }
    }

    // Tour du joueur humain

    private void jouerTourJoueur(Joueur joueur) {
        boolean tourFini = false;
        boolean deDejaLance = false;

        while (!tourFini && !partie.estTerminee()) {
            afficherEcran();
            System.out.println("--- A toi de jouer, " + joueur.getPersonnage() + " "
                    + joueur.getPersonnage().pastille() + " ! (/aide pour les commandes) ---");
            String texte = saisie.lireLigne(invite());
            Commandes.Type commande = Commandes.interpreter(texte);

            if (commande == Commandes.Type.DE) {
                if (deDejaLance) {
                    dire("Tu as deja lance le de ce tour-ci.");
                } else {
                    deDejaLance = true;
                    lancerDeEtDeplacer(joueur);
                }
            } else if (commande == Commandes.Type.CARNET) {
                rendu.getPanneau().ouvrirCarnet();
            } else if (commande == Commandes.Type.MAP) {
                rendu.getPanneau().revenirMap();
                rendu.revenirMap();
            } else if (commande == Commandes.Type.CARTES) {
                afficherCartesJoueursTemporairement(joueur);
            } else if (commande == Commandes.Type.PARLER) {
                parlerAuPnj(joueur);
            } else if (commande == Commandes.Type.HYPOTHESE) {
                faireHypothese(joueur);
            } else if (commande == Commandes.Type.ACCUSER) {
                faireAccusation(joueur);
            } else if (commande == Commandes.Type.PASSAGE) {
                prendrePassageSecret(joueur);
            } else if (commande == Commandes.Type.FIN_TOUR) {
                tourFini = true;
            } else if (commande == Commandes.Type.AIDE) {
                direAide();
            } else if (commande == Commandes.Type.QUITTER) {
                partie.terminer("Partie abandonnee. La solution etait :\n" + solutionEnTexte());
            } else if (commande == Commandes.Type.CHEAT_MOUVEMENT){
                cheatMouvement(joueur);
            } else if (commande == Commandes.Type.CHEAT_SOLUTION){
                dire("La solution est :");
                dire(solutionEnTexte());
            }else {
                boolean consommee = rendu.getPanneau().traiter(texte);
                if (!consommee) {
                    dire("Commande inconnue. Tape /aide pour voir la liste.");
                }
            }
        }
    }

    private String invite() {
        return rendu.getPanneau().aide();
    }

    // Affiche les cartes du joueur humain pendant 5 secondes, puis revient a la map
    private void afficherCartesJoueursTemporairement(Joueur joueur) {
        rendu.ouvrirCartes(joueur.getPersonnage().toString(), clesCartes(joueur));
        afficherEcran();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // rien
        }
        rendu.revenirMap();
    }

    private void direAide() {
        dire("Commandes disponibles :");
        dire("  /de        lancer le de puis se deplacer (z haut, s bas, q gauche, d droite)");
        dire("  /carnet    ouvrir le carnet (Espace = cocher, z/s = naviguer)");
        dire("  /cartes    voir tes cartes");
        dire("  /map       revenir sur la map");
        dire("  /parler    discuter avec le PNJ present dans ta salle");
        dire("  /hypothese faire une hypothese dans la salle ou tu es");
        dire("  /passage   prendre le passage secret de la salle ou tu es");
        dire("  /accuser   accusation finale : bonne = victoire, mauvaise = defaite !");
        dire("  /fin       terminer ton tour");
        dire("  q          quitter la partie");
    }

    // De et deplacement

    private void lancerDeEtDeplacer(Joueur joueur) {
        int valeur = rendu.getPanneau().lancerDe();
        dire("Tu as fait " + valeur + " !");

        int pasRestants = valeur;
        while (pasRestants > 0 && !partie.estTerminee()) {
            afficherEcran();
            String touche = saisie.lireLigne("Direction (" + pasRestants + " pas restants, z/q/s/d, x pour s'arreter) > ");

            if (touche.equals("x") || saisie.estTerminee()) {
                pasRestants = 0;
                continue;
            }

            int dLigne = 0;
            int dColonne = 0;
            if (touche.equals("z")) {
                dLigne = -1;
            } else if (touche.equals("s")) {
                dLigne = 1;
            } else if (touche.equals("q")) {
                dColonne = -1;
            } else if (touche.equals("d")) {
                dColonne = 1;
            } else {
                dire("Tape z, q, s, d pour bouger ou x pour t'arreter.");
                continue;
            }

            boolean deplace = partie.deplacer(joueur, dLigne, dColonne);
            if (!deplace) {
                dire(partie.raisonBlocage(joueur, dLigne, dColonne));
                continue;
            }

            pasRestants = pasRestants - 1;

            String salle = partie.salleDuJoueur(joueur);
            if (salle != null) {
                // Entrer dans une salle termine le deplacement, comme au Cluedo
                pasRestants = 0;
                arriveeDansSalle(joueur, salle);
            }
        }

        // Le deplacement est fini : le carnet remplace a nouveau le de
        rendu.getPanneau().revenirMap();
    }


    private void cheatMouvement(Joueur joueur) {
        int pasRestants = 999;
        while (pasRestants > 0 && !partie.estTerminee()) {
            afficherEcran();
            String touche = saisie.lireLigne("Direction (" + pasRestants + " pas restants, z/q/s/d, x pour s'arreter) > ");

            if (touche.equals("x") || saisie.estTerminee()) {
                pasRestants = 0;
                continue;
            }

            int dLigne = 0;
            int dColonne = 0;
            if (touche.equals("z")) {
                dLigne = -1;
            } else if (touche.equals("s")) {
                dLigne = 1;
            } else if (touche.equals("q")) {
                dColonne = -1;
            } else if (touche.equals("d")) {
                dColonne = 1;
            } else {
                dire("Tape z, q, s, d pour bouger ou x pour t'arreter.");
                continue;
            }

            boolean deplace = partie.deplacer(joueur, dLigne, dColonne);
            if (!deplace) {
                dire(partie.raisonBlocage(joueur, dLigne, dColonne));
                continue;
            }

            pasRestants = pasRestants - 1;

            String salle = partie.salleDuJoueur(joueur);
            if (salle != null) {
                // Entrer dans une salle termine le deplacement, comme au Cluedo
                pasRestants = 0;
                arriveeDansSalle(joueur, salle);
            }
        }

        // Le deplacement est fini : le carnet remplace a nouveau le de
        rendu.getPanneau().revenirMap();
    }

    private void arriveeDansSalle(Joueur joueur, String salle) {
        dire("Tu entres dans : " + salle + ".");

        for (String message : partie.getPortes().ramasserCles(salle)) {
            dire(message);
        }

        Pnj pnj = partie.pnjDansSalle(salle);
        if (pnj != null) {
            dire(pnj.getNom() + " est ici. Tape /parler pour discuter.");
        }

        dire("Tu peux faire une /hypothese dans cette salle, ou /fin pour finir ton tour.");
    }

    private void prendrePassageSecret(Joueur joueur) {
        String salle = partie.salleDuJoueur(joueur);
        if (salle == null) {
            dire("Tu dois etre dans une salle avec un passage secret (<->).");
            return;
        }

        String destination = null;
        if (salle.equals("Cuisine")) {
            destination = "Bureau";
        } else if (salle.equals("Bureau")) {
            destination = "Cuisine";
        } else if (salle.equals("Véranda")) {
            destination = "Salon";
        } else if (salle.equals("Salon")) {
            destination = "Véranda";
        }

        if (destination == null) {
            dire("Il n'y a pas de passage secret dans cette salle.");
            return;
        }

        if (!partie.getPortes().peutEntrer(destination)) {
            dire("Le passage debouche sur une porte fermee a cle... il te faut la cle de : " + destination + ".");
            return;
        }

        int[] position = AffichageMap.pointCentreSalle(destination);
        joueur.setPosition(position[0], position[1]);
        dire("Tu empruntes le passage secret...");
        arriveeDansSalle(joueur, destination);
    }

    // PNJ et enigmes

    private void parlerAuPnj(Joueur joueur) {
        String salle = partie.salleDuJoueur(joueur);
        Pnj pnj = partie.pnjDansSalle(salle);
        if (pnj == null) {
            dire("Il n'y a personne a qui parler ici.");
            return;
        }
        dire(pnj.dialogue());
    }

    // Hypothese : les autres joueurs montrent une carte s'ils en ont une

    private void faireHypothese(Joueur joueur) {
        String salle = partie.salleDuJoueur(joueur);
        if (salle == null) {
            dire("Tu dois etre dans une salle pour faire une hypothese.");
            return;
        }

        System.out.println("Hypothese dans la salle : " + salle);
        Suspects suspect = choisirSuspect();
        Armes arme = choisirArme();
        Lieux lieu = partie.lieuPourSalle(salle);

        dire("Tu proposes : " + suspect + " avec " + arme + " dans " + lieu + ".");

        CarteSuspect carteSuspect = new CarteSuspect(suspect);
        CarteArme carteArme = new CarteArme(arme);
        CarteLieu carteLieu = new CarteLieu(lieu);

        boolean quelquUnAMontre = false;
        for (Joueur autre : partie.getJoueurs()) {
            // On ne demande jamais au joueur qui vient de faire l'hypothese
            if (autre == joueur) {
                continue;
            }
            Carte montree = carteDansInventaire(autre, carteSuspect, carteArme, carteLieu);
            if (montree != null) {
                rendu.getPanneau().getCarnet().cocherCarte(nomPourCarnet(montree));
                dire(autre.getPersonnage() + " te montre une carte : " + montree
                        + " (cochee dans ton carnet).");
                quelquUnAMontre = true;
                break;
            }
        }

        if (!quelquUnAMontre) {
            dire("Personne ne peut montrer de carte... tres interessant !");
        }
    }

    /** Retourne la premiere carte de l'inventaire qui correspond a l'hypothese ou null. */
    private Carte carteDansInventaire(Joueur joueur, Carte... recherchees) {
        for (Carte carte : joueur.getCardInventory()) {
            for (Carte recherchee : recherchees) {
                if (carte.equals(recherchee)) {
                    return carte;
                }
            }
        }
        return null;
    }

    // Accusation finale

    private void faireAccusation(Joueur joueur) {
        System.out.println("ACCUSATION FINALE ! Bonne reponse = victoire, mauvaise = defaite.");
        String reponse = saisie.lireLigne("Tu confirmes ? (oui/non) > ");
        if (!reponse.equalsIgnoreCase("oui")) {
            dire("Accusation annulee.");
            return;
        }

        Suspects suspect = choisirSuspect();
        Armes arme = choisirArme();
        Lieux lieu = choisirLieu();

        System.out.println("Tu accuses : " + suspect + " avec " + arme + " dans " + lieu + ".");

        boolean correcte = solutionContient(new CarteSuspect(suspect))
                && solutionContient(new CarteArme(arme))
                && solutionContient(new CarteLieu(lieu));

        if (correcte) {
            partie.terminer("Bravo " + joueur.getPersonnage() + ", tu as resolu l'enquete !\n" + solutionEnTexte());
        } else {
            partie.terminer("Rate, " + joueur.getPersonnage() + "... Tu es elimine !\nLa solution etait :\n" + solutionEnTexte());
        }
    }

    private boolean solutionContient(Carte carte) {
        for (Carte cachee : partie.getCartesCachees()) {
            if (cachee.equals(carte)) {
                return true;
            }
        }
        return false;
    }

    private String solutionEnTexte() {
        StringBuilder sb = new StringBuilder();
        for (Carte carte : partie.getCartesCachees()) {
            sb.append("  - ").append(carte).append('\n');
        }
        return sb.toString();
    }

    // Choix numerotes

    private Suspects choisirSuspect() {
        System.out.println("Quel suspect ?");
        Suspects[] valeurs = Suspects.values();
        for (int i = 0; i < valeurs.length; i++) {
            System.out.println("  " + (i + 1) + " - " + valeurs[i]);
        }
        int numero = saisie.lireNumero("> ", 1, valeurs.length);
        return valeurs[numero - 1];
    }

    private Armes choisirArme() {
        System.out.println("Quelle arme ?");
        Armes[] valeurs = Armes.values();
        for (int i = 0; i < valeurs.length; i++) {
            System.out.println("  " + (i + 1) + " - " + valeurs[i]);
        }
        int numero = saisie.lireNumero("> ", 1, valeurs.length);
        return valeurs[numero - 1];
    }

    private Lieux choisirLieu() {
        System.out.println("Dans quelle salle ?");
        for (int i = 0; i < SALLES.length; i++) {
            System.out.println("  " + (i + 1) + " - " + SALLES[i]);
        }
        int numero = saisie.lireNumero("> ", 1, SALLES.length);
        return partie.lieuPourSalle(SALLES[numero - 1]);
    }

    // Tour d'un bot : il lance le de et se deplace au hasard

    private void jouerTourBot(Bot bot) {
        int valeur = rendu.getPanneau().lancerDe();
        rendu.getPanneau().revenirMap();

        int pasRestants = valeur;
        while (pasRestants > 0) {
            boolean aBouge = deplacerBotAuHasard(bot);
            if (!aBouge) {
                pasRestants = 0;
                continue;
            }
            pasRestants = pasRestants - 1;
            if (partie.salleDuJoueur(bot) != null) {
                pasRestants = 0;
            }
        }

        String salle = partie.salleDuJoueur(bot);
        String arrivee;
        if (salle == null) {
            arrivee = "dans le couloir";
        } else {
            arrivee = "dans la salle : " + salle;
            faireHypotheseBot(bot);
        }
        dire(bot.getPersonnage().pastille() + " " + bot.getPersonnage()
                + " lance le de (" + valeur + ") et s'arrete " + arrivee + ".");
    }

    private void faireHypotheseBot(Bot joueur){
        String salle = partie.salleDuJoueur(joueur);

        System.out.println("Hypothese dans la salle : " + salle + " par "+joueur.getPersonnage());
        Suspects suspect = Suspects.values()[1+hasard.nextInt(5)];
        Armes arme = Armes.values()[1 + hasard.nextInt(5)];
        Lieux lieu = partie.lieuPourSalle(salle);

        dire(joueur.getPersonnage()+" proposes : " + suspect + " avec " + arme + " dans " + lieu + ".");

        CarteSuspect carteSuspect = new CarteSuspect(suspect);
        CarteArme carteArme = new CarteArme(arme);
        CarteLieu carteLieu = new CarteLieu(lieu);

        boolean quelquUnAMontre = false;    
        for (Joueur autre : partie.getJoueurs()) {
            // On ne demande jamais au joueur (bot) qui vient de faire l'hypothese
            if (autre == joueur) {
                continue;
            }
            Carte montree = carteDansInventaire(autre, carteSuspect, carteArme, carteLieu);
            if (montree != null) {
                rendu.getPanneau().getCarnet().cocherCarte(nomPourCarnet(montree));
                quelquUnAMontre = true;
                break;
            }
        }
    }


    /** Essaie quelques directions au hasard ; retourne false si le bot est coince. */
    private boolean deplacerBotAuHasard(Bot bot) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int essai = 0; essai < 10; essai++) {
            int[] direction = directions[hasard.nextInt(directions.length)];
            boolean deplace = partie.deplacer(bot, direction[0], direction[1]);
            if (deplace) {
                return true;
            }
        }
        return false;
    }

    // Affichage : on efface le terminal puis on redessine tout l'ecran

    private void afficherEcran() {
        effacerEcran();

        List<Marqueur> marqueurs = new ArrayList<>();
        for (Joueur joueur : partie.getJoueurs()) {
            Suspects personnage = joueur.getPersonnage();
            marqueurs.add(new Marqueur(Suspects.SYMBOLE_PASTILLE, joueur.getX(), joueur.getY(),
                    personnage.getCouleur()));
        }
        for (Pnj pnj : partie.getPnjs()) {
            int[] position = AffichageMap.pointCentreSalle(pnj.getSalle());
            marqueurs.add(new Marqueur('P', position[0], position[1], "\u001B[96m")); // cyan
        }

        rendu.afficher(marqueurs);
        System.out.println(legendeDesPastilles());
        System.out.println();

        // Les messages depuis le dernier rafraichissement, puis on repart a zero
        for (String message : messages) {
            System.out.println(message);
        }
        messages.clear();
    }

    /**
     * Efface le terminal : le premier code remet le curseur en haut a gauche,
     * le second efface tout ce qui est affiche.
     */
    private void effacerEcran() {
        System.out.print("\u001B[H\u001B[2J");
    }

    /** Une ligne qui rappelle quelle pastille correspond a quel personnage. */
    private String legendeDesPastilles() {
        StringBuilder sb = new StringBuilder("Pastilles : ");
        for (Joueur joueur : partie.getJoueurs()) {
            Suspects personnage = joueur.getPersonnage();
            sb.append(personnage.pastille()).append(' ').append(personnage);
            if (joueur == partie.getHumain()) {
                sb.append(" (toi)");
            }
            sb.append("   ");
        }
        return sb.toString();
    }

    // Etat FIN

    private void faireFin() {
        effacerEcran();
        System.out.println("================ FIN DE LA PARTIE ================");
        System.out.println(partie.getMessageFin());
        System.out.println("==================================================");
        saisie.lireLigne("Appuie sur Entree pour revenir au menu... ");
        etat = Etat.MENU;
    }

}