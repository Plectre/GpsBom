package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plectre on 09/03/17.
 */

public class SaveCoord extends BroadcastReceiver {
    private String lat;
    private String lon;
    private String path;

    SaveFiles sv = new SaveFiles();

    public SaveCoord(){
        String fichierName = sv.getFichierName();
        Log.e("getFichierName", fichierName);
    }
    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {

        // Récuperation des coorddonnées envoyé par GpsService
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        saveCoor(lat, lon);
    }
    public void saveCoor(String lat, String lon) {
        String fichierName = sv.getFichierName().toString();
        Log.e(fichierName, "");
        Log.e("longitude", lon);
        Log.e("latitude", lat);

        BufferedWriter bfWrite = null;
        FileWriter flWriter = null;
        try {
            flWriter = new FileWriter(fichierName, true);
            bfWrite = new BufferedWriter(flWriter);
            // Ecrire dans le fichier
            bfWrite.append(lat);
        } catch (IOException ex) {}

        }
}
