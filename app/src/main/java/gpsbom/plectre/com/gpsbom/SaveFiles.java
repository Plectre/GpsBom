package gpsbom.plectre.com.gpsbom;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;


/**
 * Created by plectre on 09/03/17
 * Classe de Gestion de dossier
 * E//storage/emulated/0/Gps Bom
 */

public class SaveFiles {

    private String path;
    private static Boolean isCreate = false;
    final String DIR = "/Gps Bom";
    private File fFilePath;
    private File fichier;
    private static String sFilePath;
    private static String fName;

    public String getfName() {return fName;}
    public String getFilePath() {return sFilePath;}


    public Boolean getIsCreate() {return isCreate;}

    public void testCarteSd(String pName) {
        // Test Si la carte est presente
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.i("la carte", "presente !");
            // On recupére le chemin du Dossier
                path = Environment.getExternalStorageDirectory().getPath();
                //Log.e("path..", path);

            // Appel method de création du dossier
                createDir(pName);
                return;
        } else {

            Log.e("Carte", "absente");
        }
    }

    // Création du Dossier GpsBom
    public void createDir(String pName) {
        this.fName = pName;
        this.fFilePath = new File(path + DIR);
        this.sFilePath = String.valueOf(fFilePath);
        fichier = new File(fFilePath + "/"+pName+".txt");
        if (!fFilePath.exists()) {
            fFilePath.mkdir();
            Log.i("Dossier créer", "");
        }
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
                Log.e(pName, "Créer");
                isCreate = true;
                this.fName = pName+".txt";
                //Log.e(fName, "fName");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(sFilePath, "Existe déjà");
            return;
        }
            sFilePath = String.valueOf(fFilePath);
            //Log.e(DIR, "Existe");
            Log.e(String.valueOf(fFilePath), "fFilePath");

    }
}
