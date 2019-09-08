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
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
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
    private String str_bearing;

    private int currentStep = 0;
    private int oldStep;
    private float vitesseDeCollecte;


    private double latitude;
    private double longitude;
    private float bearing;
    private float accuracy;

    private String isCoordOK = "Position en cours !";

    private Boolean isFirstime = false; // boolean qui verifie si le toast dispo à etait affiché
//    private long time_update_gps = 0; // Delta entre chaque enregistrement de coord en millisecondes
//    // ou
//    private float location_update_gps = 0; // Delta entre chaque enregistrement de coord en Métres

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
            KmlFactory kml = new KmlFactory();
            kml.headerKml(String.valueOf(latitude), String.valueOf(longitude));

            Log.i("Appel", "Intent");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("lati", String.valueOf(latitude));
            intent.putExtra("longi", String.valueOf(longitude));

            startActivity(intent);
        }
        firstCoorInbound = false;
    }


    private LocationListener onLocationChange = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            // Test du seekbar des step de plot de coordonnées
            if (vitesseDeCollecte <= 0) {
                vitesseDeCollecte = 30; // Valeur minimum de la seekBar
            }

            //Log.e("LocationUpdate", String.valueOf(vitesseDeCollecte));
            float newStep = oldStep + (vitesseDeCollecte/2);
            currentStep += 1;
            //Log.e("currentStep", String.valueOf(currentStep));
            //Log.e("newStep", String.valueOf(newStep));
            MainActivity ma = new MainActivity();
            if (currentStep >= newStep && ma.recIsOn == true) {
                //Log.e("Step", String.valueOf(newStep));
                oldStep = currentStep;

                String typeDeCollecte = ma.typeCollectte;
                String typeOfCollect = ma.getTypeOfCollect();
                Log.i("Type", typeOfCollect);
                sendToSave(typeDeCollecte);
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            // Manipulation des getAccuracy et getBearing
            // Afin de les caster en int pour ne pas afficher les decimales
            accuracy = Math.round(location.getAccuracy());
            int intAccuracy = (int) accuracy;
            bearing = Math.round(location.getBearing());
            int intBearing = (int) bearing;
            // A la premiere aquisition de la position
            // Appel de la méthode intentStatusPosition
            if (firstCoorInbound) {
                //Log.e("Premiere", "Reception");
                intentStatusPosition();
            }


            /** Envoyer les données aux classe abonnées (MyReciever.class) par l'intermediare
             *   d'un Broadcast
             * Envoyer les données avec un put StringExtra à l'activitée principale afin de renseigner les TextView
             * txt_lat et txt_lon
             */

            Intent intent = new Intent(getApplicationContext(), MyReciever.class);
            intent.setAction("com.bom.service");
            //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            String str_lat = String.valueOf(latitude);
            String str_lon = String.valueOf(longitude);
            String str_accuracy = String.valueOf(intAccuracy);
            // Appel methode pour formatage du cap pour affichage...
            str_bearing = formatBearing(bearing);
            intent.putExtra("lat", str_lat);            // Latitude
            intent.putExtra("lon", str_lon);            // longitude
            intent.putExtra("accuracy", str_accuracy);  // Précision
            intent.putExtra("bearing", str_bearing);    // Cap pour affichage
            //intent.putExtra("float_bearing", bearing);  // Cap
            Log.e("GpsService_Location Changed", str_lat);
            Log.e("GpsService_precision", str_accuracy);
            sendBroadcast(intent);
        }

        // Methode appelée par onLocationChanged si le step entre deux points et depassé
        private void sendToSave(String typeDeCollecte) {
            KmlFactory kmlFactory = new KmlFactory();
            kmlFactory.setKml(typeDeCollecte, String.valueOf(latitude), String.valueOf(longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String newStatus = null;

            switch (status) {
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    newStatus = " temporairement innactif";
                    isFirstime = false;
                    break;
                case LocationProvider.AVAILABLE:
                    newStatus = " disponible";
                    isFirstime = true;
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    newStatus = " hors service";
                    isFirstime = false;
                    break;
            }
            if (isFirstime = false) {
                Toast.makeText(getBaseContext(), provider + newStatus, Toast.LENGTH_SHORT).show();
            }
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
    // Methode appellée au demarrage du service
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Récupération des données envoyées par launcherActivity
        // Mise à jour du timeUpdate et vitesseDeCollecte

        final float ARCMAP_REF = 1200;
        float location_update_gps;
        if (intent != null) {
            //time_update_gps = intent.getLongExtra("update_time", 1500);
            location_update_gps = intent.getFloatExtra("update_location", 3);
            vitesseDeCollecte = Math.round(location_update_gps / 3.6f);
            Log.i("LOCATIONUPDATE", String.valueOf(location_update_gps));
            Log.i("VITESSEDECOLLECTTE", String.valueOf(vitesseDeCollecte));

            // Si la vitesse de collecte n'est pas renseignée par defaut on donne 30
            if (vitesseDeCollecte <= 0) {
                vitesseDeCollecte = 30;
            }
            int timeToTick = Math.round(500 * vitesseDeCollecte); // set tick / vitesse de collecte
            Log.i("TIMETOTICK", String.valueOf(timeToTick));
            // Appel abonementGps(timer ms, distance parcourue avant update)
            abonementGps(timeToTick, vitesseDeCollecte);
            //vitesseDeCollecte = location_update_gps;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void abonementGps(long time, float location) {

        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // gestion des permissions utilisateur
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.i("APP", "Time update " + time);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, time,
                location, onLocationChange);
    }

    public String formatBearing(float bearing) {
        //Log.i("GPS", String.valueOf(bearing));
        float formatBearing = bearing;

        if (formatBearing == 0.0) {
            str_bearing = "--";
        }

        if (formatBearing > 0 && formatBearing <= 22.5) {
            str_bearing = "N";
        }
        if (formatBearing > 22.5 && formatBearing <= 67.5) {
            str_bearing = "NNE";
        }
        if (formatBearing > 67.5 && formatBearing <= 112.5) {
            str_bearing = "E";
        }
        if (formatBearing > 112.5 && formatBearing <= 157.5) {
            str_bearing = "SSE";
        }
        if (formatBearing > 157.55 && formatBearing <= 202.5) {
            str_bearing = "S";
        }
        if (formatBearing > 202.5 && formatBearing <= 247.5) {
            str_bearing = "SSW";
        }
        if (formatBearing > 247.5 && formatBearing <= 292.5) {
            str_bearing = "W";
        }
        if (formatBearing > 292.5 && formatBearing <= 337.5) {
            str_bearing = "NNW";
        }

        if (formatBearing > 337.5 && formatBearing <= 359.9) {
            str_bearing = "N";
        }

        return str_bearing;
    }
}
