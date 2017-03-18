package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plectre on 09/03/17.
 * Classe qui récupére les position et se charge de les enregistrer sur
 * la mémoire Externe SD du device
 */

public class BroadcastCoord extends BroadcastReceiver {
    private String lat;
    private String lon;
    private String DIR;
    private String fichierName;
    private String path = Environment.getExternalStorageDirectory().getPath();
    private String NEW_LINE = System.lineSeparator();


    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sv = new SaveFiles();
        this.DIR = sv.getDir();
        this.fichierName = sv.getFile();
    }

    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {

        // Récuperation des coorddonnées envoyé par GpsService
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        //Log.e("latitude", lat);
        //Log.e("longitude", lon);
        getDirPath();

        // Si le fichier est sauvegarder on enregistre les
        // cocrdonnées
        saveCoor(lat, lon);
    }

    public void saveCoor(String lat, String lon) {

        Log.i("Save latitude", lat);
        Log.i("Save longitude", lon);

        File file = new File(path+DIR,fichierName+".txt");
            try {
                FileWriter output = new FileWriter(file, true);
                output.append(lat);;
                output.write(" , ");
                output.append(lon);
                output.write(NEW_LINE);

                Log.i("Enregistrement ","OK");
                output.close();
            } catch (IOException ex){
                Log.e("Enregistrement ",String.valueOf(ex));
            }
    }
}
