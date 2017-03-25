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
    private Boolean fileIsOk;
    private String fName;
    private String path;
    private String NEW_LINE = System.lineSeparator();


    public BroadcastCoord() {
        getDirPath();
    }

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        //this.fDossier = sf.getFilePath();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();
        this.fileIsOk = sf.getIsCreate();

        //Log.i(path, "path");
        //Log.i(fName, "fName");

    }


    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {
           // getDirPath();

            // Récuperation des coorddonnées envoyé par GpsService
            lat = intent.getStringExtra("lat");
            lon = intent.getStringExtra("lon");

            // Si le fichier est sauvegarder on enregistre les
            // cocrdonnées
        if (fileIsOk) {
            Log.i("Fil OK", String.valueOf(fileIsOk));
            saveCoor(lat, lon);
        }
    }

    public void saveCoor(String lat, String lon) {

        Log.i("Save latitude", lat);
        Log.i("Save longitude", lon);

        File file = new File(path,fName);
            try {
                FileWriter output = new FileWriter(file, true);
                output.append(lat);
                output.write(" , ");
                output.append(lon);
                output.write(NEW_LINE);

                Log.i("Enregistrement Ok",String.valueOf(fName));
                output.close();
            } catch (IOException ex){
                Log.e("Enregistrement fail",String.valueOf(ex));
            }
    }
}