package gpsbom.plectre.com.gpsbom;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


/**
 * Created by plectre on 09/03/17
 * Classe de Gestion de dossier et
 * du fichier Appelé par SaveBox
 * E//storage/emulated/0/Gps Bom
 */

public class SaveFiles extends Activity{

    private String path;
    public static Boolean isCreate = false;
    public  final String DIR = "/Traker Bom";
    private File fFilePath;
    private File fichier;
    private static String sFilePath;
    private static String fName;

    public String getfName() {return fName;}
    public String getFilePath() {return sFilePath;}
    public Boolean getIsCreate() {return isCreate;}

    public void testCarteSd(String pName) {
        // Test Si la carte est presente
        String directory = Environment.getExternalStorageState();
        if (directory.equals(Environment.MEDIA_MOUNTED)) {
                //Log.i("la carte", "presente !");
            // On recupére le chemin du Dossier
               path = Environment.getExternalStorageDirectory().getPath();
                //Log.e("path..", path);

            // Appel method de création du dossier
                createDir(pName);
            // 
                return;
        } else {

            //Log.e("Carte", "absente");
        }
    }

    // Création du Dossier GpsBom
    public void createDir(String pName) {
        fName = pName;
        this.fFilePath = new File(path + DIR);
        sFilePath = String.valueOf(fFilePath);

        if (!fFilePath.exists()) {
            fFilePath.mkdir();
            //Log.i("Dossier créer", "");
        }
        fichier = new File(fFilePath + "/"+pName+".kml");
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
                //Log.e(pName, "Créer");
                fName = pName+".kml";
                Log.e("isCreate SaveFile", String.valueOf(isCreate));
                isCreate = true;
                //GpsService gps = new GpsService();
                Log.e("isCreate SaveFile", String.valueOf(isCreate));

                // instanciation de la classe kmlFactory et appel de la methode header
                // qui sauvegarde l'entête du kml
                //KmlFactory kmlFactory = new KmlFactory();
                //kmlFactory.headerKml();

            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            //Log.e(sFilePath, "Existe déjà");
            return;
        }
            sFilePath = String.valueOf(fFilePath);
            //Log.e(DIR, "Existe");
            //Log.e(String.valueOf(fFilePath), "fFilePath");
    }
}
