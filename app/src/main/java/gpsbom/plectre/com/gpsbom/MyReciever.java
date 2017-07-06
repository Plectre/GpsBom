package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by plectre on 09/03/17.
 * Classe etendue de BroadcastReceiver qui récupére les positions et
 * appel de la class SaveCoordinates
 * qui se charge de les enregistrer sur
 * la mémoire Externe du device
 */


public class MyReciever extends BroadcastReceiver {

    private String lat;
    private String lon;
    private Boolean fileIsOk;
    private Boolean recIsOn;

    public MyReciever() {
    }

    public void isFileOk() {
        SaveFiles sf = new SaveFiles();
        this.fileIsOk = sf.getIsCreate();
    }

    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {
        this.recIsOn = MainActivity.recIsOn;

        // Si le bouton de MAinActivity en sur enregistrement
        // on ecrit les coordonnées sur le fichier
        if (recIsOn == true) {
            isFileOk();

            // Récuperation des coordonnées envoyé par GpsService
            this.lat = intent.getStringExtra("lat");
            this.lon = intent.getStringExtra("lon");


            // Si le fichier est sauvegarder on enregistre les
            // cocrdonnées
            if (fileIsOk) {

                //Log.i("File OK", String.valueOf(fileIsOk));
                SaveCoordinates sc = new SaveCoordinates();
                sc.saveCoor(lon, lat);

                MainActivity mainActivity = new MainActivity();
                mainActivity.setLat(lat, lon);
            }
        } else {
            return;
        }
    }
}