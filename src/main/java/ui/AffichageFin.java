package ui;

import java.util.Arrays;
import java.util.List;

public final class AffichageFin {

    public static List<String> rendu(String titre, String couleurBord, String message, int largeurTotale) {
        List<String> contenu = Arrays.asList(message.split("\n"));
        List<String> boite = Cadre.dessiner(titre, contenu, couleurBord);
        return Cadre.centrerBloc(boite, largeurTotale);
    }
}
