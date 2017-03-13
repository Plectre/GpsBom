package gpsbom.plectre.com.gpsbom;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;


/**
 * Created by plectre on 09/03/17.
 */

public class SaveFiles extends Activity {
    private String path;
    private String DIR = "/Gps Bom";
    private File dossier;
    private File fichier;
    private String fichierName;
    private String data = "test écriture fichier";

    public String getFichierName() {
        return fichierName;
    }

    public void testCarteSd() {
        // Test Si la carte est presente
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.e("la carte", "presente !");
            // On recupére le chemin du Dossier
                path = Environment.getExternalStorageDirectory().getPath();
                //Log.e("path..", path);

            // Appel method de création du dossier
                createDir();
                return;
        } else {
            Toast.makeText(getBaseContext(),
                    "Vérifier votre carte SD" ,Toast.LENGTH_SHORT).show();
            Log.e("Carte", "absente");
        }
    }

    // Création du Dossier GpsBom
    public void createDir() {
        dossier = new File(path + DIR);
        fichier = new File(dossier + "/track.txt");
        if (!dossier.exists()) {
            dossier.mkdir();
            Log.e("Dossier créer", "");
        }
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
                Log.e("track", "Créer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            fichierName = fichier.getAbsolutePath();
            Log.e(DIR, "Existe");
            Log.e(fichierName, "existe");

        //Toast.makeText(getBaseContext(),"Le dossier existe déjà",Toast.LENGTH_SHORT);
    }
}
