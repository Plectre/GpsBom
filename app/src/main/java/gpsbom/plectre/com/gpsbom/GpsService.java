package gpsbom.plectre.com.gpsbom;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by plectre on 08/03/17.
 */

public class GpsService extends Service {

    private LocationManager locationMgr = null;
    //public String str_lat;
    private double latitude;
    private double longitude;

    private LocationListener onLocationChange = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            /*Toast.makeText(getBaseContext(),
                    "lat: " + latitude + " lon: " + longitude, Toast.LENGTH_SHORT).show();*/

            // Envoyer les donnée aux classe abonnées (SaveCoord.class) par l'intermediare
            // d'un Broadcast
            Intent intent = new Intent("broadcast_coor");
            String str_lat = String.valueOf(latitude);
            String str_lon = String.valueOf(longitude);
            intent.putExtra("lat", str_lat);
            intent.putExtra("lon", str_lon);
            sendBroadcast(intent);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(),"GPS actif",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(),"Vérifier les settings",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        Toast.makeText(getBaseContext(),
                "Service démarré \"GPS\" ", Toast.LENGTH_LONG).show();
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, onLocationChange);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(),
                "Fin de service \"GPS\" ", Toast.LENGTH_LONG).show();
        locationMgr.removeUpdates(onLocationChange);
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("Appel Service","GPS");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
