package gpsbom.plectre.com.gpsbom;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;


/**
 * Created by plectre on 09/03/17.
 */

public class SaveFiles extends Activity{
    private String path;
    private String DIR = "/GpsBom";
    private File dossier;
    private String data = "test écriture fichier";

    public void testDeLaCarte() {
        // Test Si la carte est presente
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.e("la carte", "presente !");
                path = Environment.getExternalStorageDirectory().getPath();
                Log.e("Chemin", path);
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
        dossier = new File(path+DIR);
        if(!dossier.exists()){
            dossier.mkdir();
            Log.e("Dossier créer","");
        } else {
            Log.e("Le dossier existe déja","");
        }
    }
}
