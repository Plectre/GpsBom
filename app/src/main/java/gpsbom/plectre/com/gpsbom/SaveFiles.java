package gpsbom.plectre.com.gpsbom;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
    private String DIR = "/Gps Bom";
    private File dossier;
    private File fichier;
    private String fichierName;

    public String getDir() {return DIR;}
    public String getFile(){return fichierName;}

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
        dossier = new File(path + DIR);
        fichier = new File(dossier + "/"+pName+".txt");
        if (!dossier.exists()) {
            dossier.mkdir();
            Log.i("Dossier créer", "");
        }
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
                Log.e("track", "Créer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(fichierName, "Existe déjà");
            return;
        }
            fichierName = fichier.getPath();
            Log.e(DIR, "Existe");
            Log.e(fichierName, "existe");

    }
}
