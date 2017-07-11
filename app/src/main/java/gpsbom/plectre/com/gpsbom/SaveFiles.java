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

public class SaveFiles extends Activity {

    private String path;
    public static Boolean isCreate = false;
    public final String DIR = "/Service Bom";
    private File fFilePath;
    private File fichier;
    private File fichierPoints;
    private static String sFilePath;
    private static String fName;
    private String pNoirs;
    private static String nomFichierPoints;

    public String getNomFichierPoints() {
        return nomFichierPoints;
    }
    public String getfName() {
        return fName;
    }

    public String getFilePath() {
        return sFilePath;
    }

    public Boolean getIsCreate() {
        return isCreate;
    }

    public void testCarteSd(String pName) {
        // Test Si la carte est presente
        String directory = Environment.getExternalStorageState();
        if (directory.equals(Environment.MEDIA_MOUNTED)) {
            //Log.i("la carte", "presente !");
            // On recupére le chemin du Dossier
            path = Environment.getExternalStorageDirectory().getPath();
            // Appel method de création du dossier
            createDir(pName);
            return;
        } else {

            Toast.makeText(this, "Pas de carte",Toast.LENGTH_SHORT).show();
        }
    }

    // Création du Dossier Service Bom
    public void createDir(String pName) {
        this.fName = pName;
        this.pNoirs = pName;
        this.fFilePath = new File(path + DIR);
        sFilePath = String.valueOf(fFilePath);

        if (!fFilePath.exists()) {
            fFilePath.mkdir();
            //Log.i("Dossier créer", "");
        }
        fichier = new File(fFilePath + "/" + pName + ".kml");
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
                //Log.e(pName, "Créer");
                fName = pName + ".kml";
                Log.e("isCreate SaveFile", String.valueOf(isCreate));
                isCreate = true;
                //GpsService gps = new GpsService();
                Log.e("isCreate SaveFile", String.valueOf(isCreate));
                filePoints(pName);
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

    // Creation du fichier points noir
    public void filePoints(String pNoirs) {
        nomFichierPoints = pNoirs + "_Poi.kml";
        fichierPoints = new File(fFilePath + "/" + nomFichierPoints);
        try {
            fichierPoints.createNewFile();
            KmlFactory kmlFactory = new KmlFactory();
            kmlFactory.headerPoints(pNoirs);
        } catch (IOException e) {
            Log.e("APP", "Erreur ecriture filePoints " + e);
            e.printStackTrace();
        }
    }

}
