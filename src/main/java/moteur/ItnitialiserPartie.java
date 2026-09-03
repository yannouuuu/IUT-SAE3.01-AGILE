package moteur;

import modele.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItnitialiserPartie {

    public Joueur[] creerJoueurs(List<List<Carte>> inventaires) {

        List<Suspects> suspects = new ArrayList<>(Arrays.asList(Suspects.values()));
        Collections.shuffle(suspects);

        // Chaque joueur incarne un personnage tire au hasard, qui donne sa pastille
        Joueur joueur = new Joueur(suspects.get(0), inventaires.get(0).toArray(new Carte[0]));
        Bot b1 = new Bot(suspects.get(1), inventaires.get(1).toArray(new Carte[0]));
        Bot b2 = new Bot(suspects.get(2), inventaires.get(2).toArray(new Carte[0]));
        Bot b3 = new Bot(suspects.get(3), inventaires.get(3).toArray(new Carte[0]));

        return new Joueur[]{joueur, b1, b2, b3};
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

    public Object[] systemeDistribution() {
        List<Carte> toutes = creerToutesLesCartes();

        Carte[] cachees = tirerCartesCachees();

        // On retire les cartes cachÃ©es de la liste avec une boucle classique
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

        return new Object[]{inventaires, cachees};
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