package gpsbom.plectre.com.gpsbom;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by plectre on 08/03/17.
 * Classe Service se charge du GPS et envoi
 * les positions au Broadcast reciever qui les enregistre
 * dans la mémoire Externe du device
 */

public class GpsService extends Service {

    private LocationManager locationMgr = null;
    private Boolean firstCoorInbound = true;
    private double latitude;
    private double longitude;
    private String isCoordOK = "Position en cours !";
    private long TIME_UPDATE_GPS = 2000; // Delta entre chaque enregistrement de point en millisecondes
                                        // ou
    private float LOCATION_UPDATE_GPS = 5; // Delta entre chaque enregistrement de point en Métres

    public String getIscoorOk() {
        return isCoordOK;
    }

    /**
     * Intent se chargeant d'envoyer à MainActivity
     * le fait d'avoir recus les premiéres Coordonnées
     */
    private void intentStatusPosition() {
        if (firstCoorInbound) {
            isCoordOK = "Position aquise !";
            Log.i("Appel", "Intent");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("txt_status_gps", isCoordOK);
            intent.putExtra("lati", String.valueOf(latitude));
            intent.putExtra("longi", String.valueOf(longitude));
            startActivity(intent);
        }
        firstCoorInbound = false;
    }


    private LocationListener onLocationChange = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            // A la premiere aquisition de la position
            // Appel de la méthode intentStatusPosition
            if (firstCoorInbound) {
                //Log.e("Premiere", "Reception");
                intentStatusPosition();
            }


            /** Envoyer les données aux classe abonnées (MyReciever.class) par l'intermediare
             // d'un Broadcast
             * Envoyer les données avec un put StringExtra à l'activitée principale afin de renseigner les TextView
             * txt_lat et txt_lon
             */

            Intent intent = new Intent("broadcast_coor");

            String str_lat = String.valueOf(latitude);
            String str_lon = String.valueOf(longitude);

            intent.putExtra("lat", str_lat);
            intent.putExtra("lon", str_lon);

            sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String newStatus = null;

            switch (status) {
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    newStatus = " Localisation temporairement innactive";
                    break;
                case LocationProvider.AVAILABLE:
                    newStatus = " Localisation disponible";
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    newStatus = " Localisation hors service";
                    break;
            }
            Toast.makeText(getBaseContext(), provider + newStatus, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "GPS actif", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(), "GPS innactif", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // Abonement au service GPs du device
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // gestion de la permission utilisateur
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // requestLocationMananger(String PROVIDER, long minTime "miliseconds", float minDistance "metre", listener)
//
//        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
//                0, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATE_GPS,
                LOCATION_UPDATE_GPS, onLocationChange);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(),
                "Fin de service \"GPS\" ", Toast.LENGTH_LONG).show();
        Log.e("Appel fin service", "GPS");
        // On se desabonne du service GPS
        locationMgr.removeUpdates(onLocationChange);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        
        Log.i("Appel Service", String.valueOf(TIME_UPDATE_GPS));
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}