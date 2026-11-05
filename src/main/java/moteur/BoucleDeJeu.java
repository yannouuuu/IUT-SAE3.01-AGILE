package moteur;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import historique.Sauvegarde;
import carnet.Carnet;
import enigmes.PortesFermees;
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
import ui.AffichageAttente;
import ui.AffichageFin;
import ui.AffichageMap;
import ui.Logo;
import ui.Marqueur;
import ui.Menu;
import ui.Rendu;
import ui.Terminal;

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

    private static final String VERT  = "\u001B[92m";
    private static final String ROUGE = "\u001B[91m";
    private static final String GRIS  = "\u001B[90m";
    private static final String DOSSIER_SAUVEGARDES = "sauvegarde";

    private final Scanner clavier;
    private final Saisie saisie;
    private final Rendu rendu = new Rendu();
    private final Random hasard = new Random();
    private final son.Musique musique = new son.Musique();

    /** Les messages a afficher sous la map au prochain rafraichissement. */
    private final List<String> messages = new ArrayList<>();

    /** Les joueurs humains deja accueillis (cartes de depart montrees) dans la partie en cours. */
    private final Set<Joueur> dejaAccueillis = new HashSet<>();

    private Etat etat = Etat.MENU;
    private EtatPartie partie;
    private TourManager tours;

    public BoucleDeJeu(Scanner clavier) {
        this.clavier = clavier;
        this.saisie = new Saisie(clavier);
    }

    public void lancer() {
        musique.demarrer();
        while (etat != Etat.ARRET) {
            if (etat == Etat.MENU) {
                faireMenu();
            } else if (etat == Etat.PARTIE) {
                faireUnTour();
            } else if (etat == Etat.FIN) {
                faireFin();
            }
        }
        musique.arreter();
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
            preparerPartie(1);
            etat = Etat.PARTIE;
        } else if (choix.equals("2")) {
            int nbHumains = saisie.lireNumero("Combien de joueurs humains (2 a 4) ? > ", 2, 4);
            preparerPartie(nbHumains);
            etat = Etat.PARTIE;
        } else if (choix.equals("3")) {
            if (chargerPartie()) {
                etat = Etat.PARTIE;
            }
        } else if (choix.equals("q")) {
            System.out.println("Au revoir !");
            etat = Etat.ARRET;
        } else {
            System.out.println("Choix inconnu, tape 1, 2, 3 ou q.");
        }
    }

    private void preparerPartie(int nbHumains) {
        InitialiserPartie init = new InitialiserPartie();

        InitialiserPartie.Distribution distribution = init.systemeDistribution();
        List<List<Carte>> inventaires = distribution.inventaires();
        Carte[] cartesCachees = distribution.cartesCachees();

        Joueur[] joueurs = init.creerJoueurs(inventaires, nbHumains);
        partie = new EtatPartie(joueurs, cartesCachees);
        tours = new TourManager(joueurs);
        dejaAccueillis.clear();

        // Chaque pion demarre sur un point de depart du bord de la map
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i].setPosition(DEPARTS[i][0], DEPARTS[i][1]);
        }

        // Chaque joueur humain a son propre carnet : on l'initialise et on y coche d'office ses cartes
        for (Joueur joueur : joueurs) {
            if (!(joueur instanceof Bot)) {
                joueur.getCarnet().reinitialiser();
                cocherCartesDansLeCarnet(joueur);
            }
        }
    }

    /** Accueil prive d'un joueur humain, a la premiere fois que son tour arrive. */
    private void accueillirJoueur(Joueur joueur) {
        Suspects personnage = joueur.getPersonnage();
        dire("Bienvenue ! Tu joues " + personnage + " " + personnage.pastille() + ".");
        dire("Tes " + joueur.getCardInventory().length
                + " cartes de depart sont deja cochees dans ton carnet (/carnet pour les voir).");

        // Avant meme d'afficher la map pour la premiere fois on montre ses cartes de depart au joueur
        afficherCartesJoueursTemporairement(joueur);
    }

    private void cocherCartesDansLeCarnet(Joueur joueur) {
        for (Carte carte : joueur.getCardInventory()) {
            joueur.getCarnet().marquerPossedee(nomPourCarnet(carte));
        }
    }

    // --- Sauvegarde et chargement ---

    private void sauvegarderPartie() {
        String nom = saisie.lireLigne("Nom de la sauvegarde > ");
        if (nom.isBlank()) {
            dire("Sauvegarde annulee (nom vide).");
            return;
        }

        new File(DOSSIER_SAUVEGARDES).mkdirs();
        String chemin = DOSSIER_SAUVEGARDES + "/" + nom + ".csv";
        try {
            Sauvegarde.ecrire(chemin, serialiserPartie());
            dire("Partie sauvegardee dans " + chemin + ".");
        } catch (UncheckedIOException e) {
            dire("Impossible de sauvegarder la partie : " + e.getMessage());
        }
    }

    /** Retourne true si une partie a bien ete chargee (l'appelant doit alors passer a l'etat PARTIE). */
    private boolean chargerPartie() {
        String nom = saisie.lireLigne("Nom de la sauvegarde a charger > ");
        if (nom.isBlank()) {
            return false;
        }

        String chemin = DOSSIER_SAUVEGARDES + "/" + nom + ".csv";
        if (!new File(chemin).exists()) {
            System.out.println("Aucune sauvegarde trouvee : " + chemin);
            saisie.lireLigne("Appuie sur Entree pour revenir au menu... ");
            return false;
        }

        try {
            restaurerPartie(Sauvegarde.lire(chemin));
            return true;
        } catch (RuntimeException e) {
            System.out.println("Sauvegarde illisible ou corrompue (" + e.getMessage() + ").");
            saisie.lireLigne("Appuie sur Entree pour revenir au menu... ");
            return false;
        }
    }

    /** Ecrit l'etat complet de la partie (joueurs, cartes, cles, tour) dans un texte a une ligne par info. */
    private String serialiserPartie() {
        StringBuilder sb = new StringBuilder();
        sb.append("NB_HUMAINS=").append(nombreHumains()).append('\n');
        sb.append("TOUR=").append(tours.getIndexCourant()).append('\n');
        sb.append("CLES=").append(clesTrouveesEnCsv()).append('\n');
        sb.append("CACHEES=").append(clesCsv(Arrays.asList(partie.getCartesCachees()))).append('\n');

        for (Joueur joueur : partie.getJoueurs()) {
            String typeJoueur;
            if (joueur instanceof Bot) {
                typeJoueur = "BOT";
            } else {
                typeJoueur = "HUMAIN";
            }
            sb.append("JOUEUR=")
                    .append(joueur.getPersonnage().name()).append(';')
                    .append(typeJoueur).append(';')
                    .append(joueur.getX()).append(';')
                    .append(joueur.getY()).append(';')
                    .append(clesCsv(Arrays.asList(joueur.getCardInventory()))).append(';')
                    .append(carnetCocheCsv(joueur))
                    .append('\n');
        }
        return sb.toString();
    }

    /** Reconstruit partie/tours a partir du texte ecrit par serialiserPartie(). */
    private void restaurerPartie(String contenu) {
        int tour = 0;
        List<String> clesTrouvees = new ArrayList<>();
        Carte[] cachees = new Carte[0];
        List<Joueur> joueurs = new ArrayList<>();

        for (String ligne : contenu.split("\n")) {
            if (ligne.isBlank()) {
                continue;
            }
            if (ligne.startsWith("TOUR=")) {
                tour = Integer.parseInt(rogner(ligne.substring(5)));
            } else if (ligne.startsWith("CLES=")) {
                String valeur = rogner(ligne.substring(5));
                if (!valeur.isEmpty()) {
                    clesTrouvees.addAll(Arrays.asList(valeur.split(",")));
                }
            } else if (ligne.startsWith("CACHEES=")) {
                cachees = cartesDepuisCsv(rogner(ligne.substring(8)));
            } else if (ligne.startsWith("JOUEUR=")) {
                joueurs.add(joueurDepuisLigne(ligne.substring(7)));
            }
        }

        if (joueurs.isEmpty()) {
            throw new IllegalArgumentException("aucun joueur dans la sauvegarde");
        }

        Joueur[] tableauJoueurs = joueurs.toArray(new Joueur[0]);
        partie = new EtatPartie(tableauJoueurs, cachees);
        tours = new TourManager(tableauJoueurs);
        tours.definirIndexCourant(tour);

        for (String salle : clesTrouvees) {
            partie.getPortes().marquerCleTrouvee(salle);
        }

        // Partie deja en cours : personne ne doit revoir l'accueil / ses cartes de depart
        dejaAccueillis.clear();
        dejaAccueillis.addAll(joueurs);

        messages.clear();
        dire("Partie chargee !");
    }

    private Joueur joueurDepuisLigne(String champs) {
        String[] morceaux = champs.split(";", -1);
        Suspects personnage = Suspects.valueOf(morceaux[0]);
        boolean estBot = morceaux[1].equals("BOT");
        int x = Integer.parseInt(morceaux[2]);
        int y = Integer.parseInt(morceaux[3]);
        Carte[] cartes = cartesDepuisCsv(morceaux[4]);

        Joueur joueur;
        if (estBot) {
            joueur = new Bot(personnage, cartes);
        } else {
            joueur = new Joueur(personnage, cartes);
        }
        joueur.setPosition(x, y);

        joueur.getCarnet().reinitialiser();
        cocherCartesDansLeCarnet(joueur);
        if (morceaux.length > 5 && !morceaux[5].isBlank()) {
            for (String nomCoche : morceaux[5].split(",")) {
                joueur.getCarnet().cocherCarte(nomCoche);
            }
        }
        return joueur;
    }

    /** Les noms des salles dont la cle a deja ete trouvee, separes par des virgules. */
    private String clesTrouveesEnCsv() {
        List<String> salles = new ArrayList<>();
        for (PortesFermees.Porte porte : partie.getPortes().getPortes()) {
            if (porte.estCleTrouvee()) {
                salles.add(porte.getSalleVerrouillee());
            }
        }
        return joindreParVirgule(salles);
    }

    /** Les entrees cochees dans le carnet du joueur (deductions), separees par des virgules. */
    private String carnetCocheCsv(Joueur joueur) {
        List<String> noms = new ArrayList<>();
        for (Carnet.Entree entree : joueur.getCarnet().getEntrees()) {
            if (!entree.estTitre() && entree.estCoche()) {
                noms.add(entree.getNom());
            }
        }
        return joindreParVirgule(noms);
    }

    /** Cles ('SUSPECT:MOUTARDE', ...) d'une liste de cartes, separees par des virgules. */
    private String clesCsv(List<Carte> cartes) {
        List<String> cles = new ArrayList<>();
        for (Carte carte : cartes) {
            cles.add(cleCarte(carte));
        }
        return joindreParVirgule(cles);
    }

    private String joindreParVirgule(List<String> valeurs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valeurs.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(valeurs.get(i));
        }
        return sb.toString();
    }

    private String rogner(String texte) {
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

    private Carte[] cartesDepuisCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            return new Carte[0];
        }
        String[] cles = csv.split(",");
        Carte[] cartes = new Carte[cles.length];
        for (int i = 0; i < cles.length; i++) {
            cartes[i] = carteDepuisCle(cles[i]);
        }
        return cartes;
    }

    /** L'inverse de cleCarte() : reconstruit une Carte a partir de sa cle 'TYPE:NOM'. */
    private Carte carteDepuisCle(String cle) {
        String[] parties = cle.split(":", 2);
        String type = parties[0];
        String nom = parties[1];
        if (type.equals("SUSPECT")) {
            return new CarteSuspect(Suspects.valueOf(nom));
        }
        if (type.equals("ARME")) {
            return new CarteArme(Armes.valueOf(nom));
        }
        return new CarteLieu(Lieux.valueOf(nom));
    }

    /** Nombre de joueurs humains dans la partie en cours (1 en solo, 2 a 4 en multijoueur). */
    private int nombreHumains() {
        int total = 0;
        for (Joueur joueur : partie.getJoueurs()) {
            if (!(joueur instanceof Bot)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Ecran de passation affiche en multijoueur avant le tour de chaque joueur humain :
     * il cache les infos du joueur precedent jusqu'a ce que le suivant confirme etre pret.
     */
    private void montrerEcranAttente(Joueur joueur) {
        effacerEcran();
        int largeur = Terminal.largeur();
        System.out.println(Logo.rendu(largeur));
        System.out.println();
        for (String ligne : AffichageAttente.rendu(
                joueur.getPersonnage().toString(), joueur.getPersonnage().pastille(), largeur)) {
            System.out.println(ligne);
        }
        System.out.println();
        saisie.lireLigne("> ");
        effacerEcran();
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
        if (nombreHumains() > 1) {
            montrerEcranAttente(joueur);
        }
        rendu.getPanneau().afficherCarnetDe(joueur);
        if (!dejaAccueillis.contains(joueur)) {
            dejaAccueillis.add(joueur);
            accueillirJoueur(joueur);
        }

        boolean tourFini = false;
        boolean deDejaLance = false;

        while (!tourFini && !partie.estTerminee()) {
            afficherEcran();
            System.out.println("--- A toi de jouer, " + joueur.getPersonnage() + " "
                    + joueur.getPersonnage().pastille() + " ! ---");
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
                partie.terminer("Partie abandonnee. La solution etait :\n" + solutionEnTexte(),
                        EtatPartie.Resultat.ABANDON);
            } else if (commande == Commandes.Type.CHEAT_MOUVEMENT){
                cheatMouvement(joueur);
            } else if (commande == Commandes.Type.SAVE) {
                sauvegarderPartie();
            } else if (commande == Commandes.Type.MUSIQUE) {
                musique.basculer();
                dire("Musique coupee ou remise.");
            } else {
                boolean consommee = rendu.getPanneau().traiter(texte);
                if (!consommee) {
                    dire("Commande inconnue. La liste est affichee a droite.");
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
        dire("  /de        lancer le de puis se deplacer (fleches, ou z haut, s bas, q gauche, d droite)");
        dire("  /carnet    ouvrir le carnet (Espace = cocher, z/s = naviguer)");
        dire("  /cartes    voir tes cartes");
        dire("  /map       revenir sur la map");
        dire("  /parler    discuter avec le PNJ present dans ta salle");
        dire("  /hypothese faire une hypothese dans la salle ou tu es");
        dire("  /passage   prendre le passage secret de la salle ou tu es");
        dire("  /accuser   accusation finale : bonne = victoire, mauvaise = defaite !");
        dire("  /fin       terminer ton tour");
        dire("  /save      sauvegarder la partie");
        dire("  q          quitter la partie");
    }

    // De et deplacement

    private int[] direction(String touche) {
        if (touche.equals("z")) {
            return new int[]{-1, 0};
        }
        if (touche.equals("s")) {
            return new int[]{1, 0};
        }
        if (touche.equals("q")) {
            return new int[]{0, -1};
        }
        if (touche.equals("d")) {
            return new int[]{0, 1};
        }
        return null;
    }

    private void lancerDeEtDeplacer(Joueur joueur) {
        int valeur = rendu.getPanneau().lancerDe();
        dire("Tu as fait " + valeur + " !");

        int pasRestants = valeur;
        while (pasRestants > 0 && !partie.estTerminee()) {
            afficherEcran();
            String touche = saisie.LireLaDirection(
                    "Direction (" + pasRestants + " pas restants, fleches ou z/q/s/d, x pour s'arreter) > ");

            if (touche.equals("x") || saisie.estTerminee()) {
                pasRestants = 0;
                continue;
            }

            int[] pas = direction(touche);
            if (pas == null) {
                continue;
            }

            boolean deplace = partie.deplacer(joueur, pas[0], pas[1]);
            if (!deplace) {
                dire("Impossible d'aller par la : mur, bord du plateau, ou porte fermee a cle.");
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
            String touche = saisie.LireLaDirection(
                    "Direction (" + pasRestants + " pas restants, fleches ou z/q/s/d, x pour s'arreter) > ");

            if (touche.equals("x") || saisie.estTerminee()) {
                pasRestants = 0;
                continue;
            }

            int[] pas = direction(touche);
            if (pas == null) {
                continue;
            }

            boolean deplace = partie.deplacer(joueur, pas[0], pas[1]);
            if (!deplace) {
                dire("Impossible d'aller par la : mur, bord du plateau, ou porte fermee a cle.");
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
            if (!autre.getPersonnage().equals(joueur.getPersonnage())){
                Carte montree = carteDansInventaire(autre, new Carte[]{carteSuspect, carteArme, carteLieu});
                if (montree != null) {
                    joueur.getCarnet().cocherCarte(nomPourCarnet(montree));
                    dire(autre.getPersonnage() + " te montre une carte : " + montree
                            + " (cochee dans ton carnet).");
                    quelquUnAMontre = true;
                    break;
                    }
            } else {
                continue;
            }
            }
        

        if (!quelquUnAMontre) {
            dire("Personne ne peut montrer de carte... tres interessant !");
        }
    }

    /** Retourne la premiere carte de l'inventaire qui correspond a l'hypothese ou null. */
    private Carte carteDansInventaire(Joueur joueur, Carte[] recherchees) {
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
            partie.terminer("Bravo " + joueur.getPersonnage() + ", tu as resolu l'enquete !\n" + solutionEnTexte(),
                    EtatPartie.Resultat.VICTOIRE);
        } else {
            partie.terminer("Rate, " + joueur.getPersonnage() + "... Tu es elimine !\nLa solution etait :\n" + solutionEnTexte(),
                    EtatPartie.Resultat.DEFAITE);
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

    /**
     * Hypothese d'un bot : purement aleatoire, sans effet sur le carnet de personne
     * (au Cluedo, seul celui qui pose l'hypothese apprend la carte exacte montree).
     */
    private void faireHypotheseBot(Bot joueur){
        String salle = partie.salleDuJoueur(joueur);

        Suspects suspect = Suspects.values()[hasard.nextInt(Suspects.values().length)];
        Armes arme = Armes.values()[hasard.nextInt(Armes.values().length)];
        Lieux lieu = partie.lieuPourSalle(salle);

        dire(joueur.getPersonnage()+" propose : " + suspect + " avec " + arme + " dans " + lieu + ".");
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
            if (joueur == tours.joueurCourant() && !(joueur instanceof Bot)) {
                sb.append(" (toi)");
            }
            sb.append("   ");
        }
        return sb.toString();
    }

    // Etat FIN

    private void faireFin() {
        effacerEcran();
        int largeur = Terminal.largeur();
        System.out.println(Logo.rendu(largeur));
        System.out.println();

        String titre;
        String couleur;
        switch (partie.getResultat()) {
            case VICTOIRE -> {
                titre = "VICTOIRE !";
                couleur = VERT;
            }
            case DEFAITE -> {
                titre = "DEFAITE...";
                couleur = ROUGE;
            }
            default -> {
                titre = "PARTIE ABANDONNEE";
                couleur = GRIS;
            }
        }

        for (String ligne : AffichageFin.rendu(titre, couleur, partie.getMessageFin(), largeur)) {
            System.out.println(ligne);
        }
        System.out.println();

        saisie.lireLigne("Appuie sur Entree pour revenir au menu... ");
        etat = Etat.MENU;
    }

}