package Historique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Sauvegarde extends Exception{

    private String resultat;

    public Sauvegarde(String resultat){
         this.resultat = resultat;
    }


    public void sauvegrade(String chemin){
        try(FileWriter file = new FileWriter(new File(chemin))){
            file.write(this.resultat);
            file.write("\n");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static String importer(String chemin) {
        StringBuilder resultat = new StringBuilder();

        try(BufferedReader ficher = new BufferedReader(new FileReader(chemin))){
            String ligne;

            while((ligne = ficher.readLine())!= null){
                resultat.append(ligne).append("\n");
            }
            
        } catch ( IOException e){
            e.printStackTrace();
        }
        return resultat.toString();
    }
}