package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by plectre on 09/03/17.
 * Classe qui récupére les position et se charge de les enregistrer sur
 * la mémoire Externe SD du device
 */


public class MyReciever extends BroadcastReceiver {
    private String tCollectte;
    private String lat;
    private String lon;
    private Boolean fileIsOk;


    public void isFileOk() {
        SaveFiles sf = new SaveFiles();
        this.fileIsOk = sf.getIsCreate();
    }

    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {
           isFileOk();

            // Récuperation des coordonnées envoyé par GpsService
            lat = intent.getStringExtra("lat");
            lon = intent.getStringExtra("lon");

        // Si le fichier est sauvegarder on enregistre les
            // cocrdonnées
        if (fileIsOk) {
            Log.i("Fil OK", String.valueOf(fileIsOk));
            SaveCoordinates sc = new SaveCoordinates();
            sc.saveCoor(lon, lat);

        }
    }
}