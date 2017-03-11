package gpsbom.plectre.com.gpsbom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by plectre on 09/03/17.
 */

public class SaveCoord extends BroadcastReceiver {

    // Abonnement au Broadcast
    public void onReceive(Context context, Intent intent) {

        // Récuperation des coorddonnées envoyé par GpsService
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("lon");
        Log.e("longitude", lon);
        Log.e("latitude", lat);
    }
    public void saveCoor(String lat, String lon) {

    }
}
