package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import gpsbom.plectre.com.gpsbom.saves.SaveCoordinates;
import gpsbom.plectre.com.gpsbom.saves.SaveFiles;
import gpsbom.plectre.com.gpsbom.utils.Cap;

/**
 * Created by plectre on 09/03/17.
 * Classe etendue de BroadcastReceiver qui récupére les positions et
 * appel de la class SaveCoordinates
 * qui se charge elle de les enregistrer sur
 * la mémoire Externe du device
 */


public class MyReciever extends BroadcastReceiver {

    private String lat;
    private String lon;
    private String accuracy;
    private String bearing;
    private float float_bearing;
    private Boolean fileIsOk;
    private Boolean recIsOn;

    public MyReciever() {
    }

    public void isFileOk() {
        SaveFiles sf = new SaveFiles();
        this.fileIsOk = sf.getIsCreate();
    }

    private void saveCoordinates(String lon, String lat) {
        SaveCoordinates sc = new SaveCoordinates();
        sc.saveCoor(lon, lat);
    }

    private void changeCap() {
        Cap currentCap = new Cap();
        boolean isCapChange = currentCap.delta(float_bearing);
        if (isCapChange) {
            saveCoordinates(lon, lat);

        }
    }
    public void getIntent(Intent intent) {

        // Récuperation des coordonnées envoyé par GpsService
        this.lat = intent.getStringExtra("lat");
        this.lon = intent.getStringExtra("lon");
        this.accuracy = intent.getStringExtra("accuracy");
        this.bearing = intent.getStringExtra("bearing");
        //this.float_bearing = intent.getFloatExtra("float_bearing",0.0f);
        //changeCap();
    }
    
    // Methode implementée par BroadcastReciever
    public void onReceive(Context context, Intent intent) {
        Log.e("MyReceiver_Location Changed", String.valueOf(lat));
        this.recIsOn = MainActivity.recIsOn;

        // Si le bouton de MAinActivity en sur enregistrement
        // on ecrit les coordonnées sur le fichier
        if (recIsOn == true) {
            isFileOk();
            getIntent(intent);

            // Si le fichier est sauvegarder on enregistre les
            // coordonnées
            if (fileIsOk) {

                //Log.i("File OK", String.valueOf(fileIsOk));
                SaveCoordinates sc = new SaveCoordinates();
                sc.saveCoor(lon, lat);

                MainActivity mainActivity = new MainActivity();
                mainActivity.setLat(lat, lon, accuracy, bearing);
            }
        } else {
            return;
        }
    }
}