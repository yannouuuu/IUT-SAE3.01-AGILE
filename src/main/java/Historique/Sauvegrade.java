package Historique;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Sauvegrade {
    private String resultat;

    public Sauvegrade(String resultat){
         this.resultat = resultat;
    }


    public void sauvegrade(String chemin) throws IOException{
        try(FileWriter file = new FileWriter(new File(chemin))){
            file.write(this.resultat);
            file.write("\n");
        }
    }
}