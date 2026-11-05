package moteur;

import modele.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Distribution des cartes et creation des joueurs au debut d'une partie. */
public class InitialiserPartie {

    /** Resultat de la distribution : les inventaires des joueurs et les trois cartes cachees. */
    public record Distribution(List<List<Carte>> inventaires, Carte[] cartesCachees) {
    }

    /**
     * Cree les 4 joueurs de la partie : les nbHumains premiers sont controles au clavier,
     * les autres sont des bots. Chacun incarne un personnage tire au hasard.
     */
    public Joueur[] creerJoueurs(List<List<Carte>> inventaires, int nbHumains) {

        List<Suspects> suspects = new ArrayList<>(Arrays.asList(Suspects.values()));
        Collections.shuffle(suspects);

        Joueur[] joueurs = new Joueur[4];
        for (int i = 0; i < joueurs.length; i++) {
            Carte[] inventaire = inventaires.get(i).toArray(new Carte[0]);
            if (i < nbHumains) {
                joueurs[i] = new Joueur(suspects.get(i), inventaire);
            } else {
                joueurs[i] = new Bot(suspects.get(i), inventaire);
            }
        }
        return joueurs;
    }

    public static List<List<Carte>> distribuerCartes(List<Carte> cartes) {
        Collections.shuffle(cartes);

        int[] tailles = {5, 5, 4, 4};

        List<Integer> ordre = new ArrayList<>();
        for (int t : tailles) ordre.add(t);
        Collections.shuffle(ordre);

        List<List<Carte>> inventaires = new ArrayList<>();
        for (int i = 0; i < 4; i++) inventaires.add(new ArrayList<>());

        int index = 0;
        for (int i = 0; i < 4; i++) {
            int nb = ordre.get(i);
            for (int j = 0; j < nb; j++) {
                inventaires.get(i).add(cartes.get(index));
                index++;
            }
        }
        return inventaires;
    }

    public static Carte[] tirerCartesCachees() {
        Random rand = new Random();

        CarteArme armeCachee = new CarteArme(
            Armes.values()[rand.nextInt(Armes.values().length)]
        );

        CarteLieu lieuCache = new CarteLieu(
            Lieux.values()[rand.nextInt(Lieux.values().length)]
        );

        CarteSuspect suspectCache = new CarteSuspect(
            Suspects.values()[rand.nextInt(Suspects.values().length)]
        );

        return new Carte[]{armeCachee, lieuCache, suspectCache};
    }

    public Distribution systemeDistribution() {
        List<Carte> toutes = creerToutesLesCartes();

        Carte[] cachees = tirerCartesCachees();

        List<Carte> restantes = new ArrayList<>();
        for (Carte c : toutes) {
            boolean estCachee = c.equals(cachees[0])
                    || c.equals(cachees[1])
                    || c.equals(cachees[2]);
            if (!estCachee) {
                restantes.add(c);
            }
        }

        List<List<Carte>> inventaires = distribuerCartes(restantes);

        return new Distribution(inventaires, cachees);
    }


    public static List<Carte> creerToutesLesCartes() {
        List<Carte> cartes = new ArrayList<>();

        for (Armes a : Armes.values()) {
            cartes.add(new CarteArme(a));
        }

        for (Lieux l : Lieux.values()) {
            cartes.add(new CarteLieu(l));
        }

        for (Suspects s : Suspects.values()) {
            cartes.add(new CarteSuspect(s));
        }

        return cartes;
    }
}
