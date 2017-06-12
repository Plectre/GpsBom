package gpsbom.plectre.com.gpsbom;

import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plectre on 31/03/17.
 * Classe servant à enregistrer sur le fichier les coordonnées
 * et appellé par MyReciever
 **/

public class SaveCoordinates extends MainActivity{
    private String fName;
    private String tCollecte; // type de collecte par defaut au demarrage
    private String path;
    private String NEW_LINE = System.lineSeparator();
    public String saveLat;
    private String saveLon;
    private boolean recIsOn;

    public String getLat() {
        return saveLat;
    }

    public String getLon() {
        return saveLon;
    }

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();
    }

    public void saveCoor(String pLat, String pLon) {

        this.saveLat = pLat;
        this.saveLon = pLon;

        // Appel de la fonction qui récupére le chemin et le nom du fichier
        getDirPath();

        Log.i("Save latitude", pLat);
        Log.i("Save longitude", pLon);

        // Ecriture des coordonnées sur le fichier
        File file = new File(path, fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(pLat);
            output.write(" , ");
            output.append(pLon);
            output.write(NEW_LINE);

            //Log.i("Enregistrement Ok", String.valueOf(fName));
            output.close();
        } catch (IOException ex) {
            //Log.e("Enregistrement fail", String.valueOf(ex));
        }
    }
}