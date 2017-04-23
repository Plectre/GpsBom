package gpsbom.plectre.com.gpsbom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Created by plectre on 20/03/17.
 * Activitée principale
 */

public class MainActivity extends AppCompatActivity {

    public TextView txt_status_gps;
    public static TextView txt_lat;
    public static TextView txt_lon;
    public String gpsStatus = "Ready to go !";
    public Button btn_rec;
    public Button btn_stop;
    public RadioGroup rd_group;
    public String lat = "--°.---";
    public String lon = "--°.---";
    public static boolean recIsOn = false;
    protected String typeCollectte = "HLP";
    protected ImageView img_sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_lat = (TextView) findViewById(R.id.txt_latitude);
        txt_lon = (TextView) findViewById(R.id.txt_longitude);
        rd_group = (RadioGroup) findViewById(R.id.radio_group);
        img_sat = (ImageView) findViewById(R.id.img_satellite);
        txt_status_gps = (TextView) findViewById(R.id.txt_satus_gps);
        btn_rec = (Button) findViewById(R.id.btn_rec);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        img_sat.setImageResource(R.drawable.gps_off);

        AfficheGpsStatus(gpsStatus);
        setLat(lat, lon);

        onRadioGroupChange();

        // Gestion ds boutons stop rec et pause
        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inverser recIsOn
                recIsOn ^= true;

                if (recIsOn == true)
                {
                    btn_rec.setText("PAUSE");
                    Log.e(String.valueOf(recIsOn),"REC IS ON");
                    img_sat.setImageResource(R.drawable.gps_on);
                    gpsStatus = "Tracking en cours...";

                }
                    else if (recIsOn == false)
                {
                    btn_rec.setText("REC");
                    Log.e(String.valueOf(recIsOn),"REC IS OFF");
                    gpsStatus = "Tracking en pause";
                    img_sat.setImageResource(R.drawable.gps_off);
                }
                AfficheGpsStatus(gpsStatus);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On stoppe l'enregistrement
                recIsOn = false;
                // On ferme le fichier kml en appelant le footer
                stopTracking();
                stopGpsService();
            }
        });
    }

    // Methode qui ecoute si on a un changement d'état du radioGroup
    protected void onRadioGroupChange() {

        //RadioGroup rd_group = (RadioGroup) findViewById(R.id.radio_group);
        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                    case R.id.radio_biLat:
                        RadioButton rbt_bilat = (RadioButton) findViewById(R.id.radio_biLat);
                        typeCollectte = String.valueOf(rbt_bilat.getText());
                        Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte);
                        break;
                    case R.id.radio_hlp:
                        RadioButton rbt_hlp = (RadioButton) findViewById(R.id.radio_hlp);
                        typeCollectte = String.valueOf(rbt_hlp.getText());
                        Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte);
                        break;
                    case R.id.radio_m_a:
                        RadioButton rbt_ma = (RadioButton) findViewById(R.id.radio_m_a);
                        typeCollectte = String.valueOf(rbt_ma.getText());
                        Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte);
                        break;
                    case R.id.radio_ulat:
                        RadioButton rbt_ulat = (RadioButton) findViewById(R.id.radio_ulat);
                        typeCollectte = String.valueOf(rbt_ulat.getText());
                        Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte);
                        break;
                    default:
                        Log.i("Default", "");
                        break;
                }
            }
        });
    }
    // Appel fonction d'enregistrement des coordonnées
    public void typeCollectte(String pTypeCollecte) {

       //Log.e("MainActivity 1 --> MyReceiver", pTypeCollecte);
       KmlFactory kmlFactory = new KmlFactory();
       kmlFactory.setKml(pTypeCollecte);
   }
    // Appel du footer kml
   public void stopTracking() {
       // une boucle sur les enfants du radioGroup et desactivation de
       // tous
       for (int i = 0; i< rd_group.getChildCount(); i++)
       {
           rd_group.getChildAt(i).setEnabled(false);
       }
       KmlFactory kmlFactory = new KmlFactory();
       kmlFactory.footerKml();
   }
    // Fonction qui stoppe le service GPS
   public void stopGpsService() {
       stopService(new Intent(MainActivity.this, GpsService.class));
       btn_stop.setEnabled(false);
       img_sat.setImageResource(R.drawable.gps_off);
       btn_rec.setEnabled(false);
       gpsStatus = "Fin du tracking !";
       AfficheGpsStatus(gpsStatus);

   }
   public void setLat(String pLat, String pLon) {
       lat = pLat;
       lon = pLon;
       txt_lon.setText("Lon: " + lon);
       txt_lat.setText("Lat: " + lat);
   }

   public void AfficheGpsStatus(String pGpsStatus) {
       txt_status_gps.setText(pGpsStatus);
   }
}