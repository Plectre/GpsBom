package gpsbom.plectre.com.gpsbom;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.R.color.black;


/**
 * Created by plectre on 20/03/17.
 * Activitée principale
 */

public class MainActivity extends AppCompatActivity {

    public TextView txt_status_gps;
    private TextView txt_typeDeCollecte;
    public  static TextView txt_lat;
    public static TextView txt_lon;
    public static TextView txt_plot;
    public String gpsStatus = "GAZZZzzzzzz !!!!";
    public Button btn_rec;
    public Button btn_stop;
    public RadioGroup rd_group;
    public static String lat;
    public static String lon;
    public static boolean recIsOn = false;
    protected String typeCollectte = "HLP";
    protected ImageView img_sat;
    private Animation fadeAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Vérouillage de la vue en position portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txt_typeDeCollecte = (TextView) findViewById(R.id.txt_typeDeCollecte);
        txt_lat = (TextView) findViewById(R.id.txt_latitude);
        txt_lon = (TextView) findViewById(R.id.txt_longitude);
        txt_plot = (TextView) findViewById(R.id.txt_plot);
        rd_group = (RadioGroup) findViewById(R.id.radio_group);

        txt_status_gps = (TextView) findViewById(R.id.txt_satus_gps);
        btn_rec = (Button) findViewById(R.id.btn_rec);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        fadeAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim);
        AfficheGpsStatus(gpsStatus);

        onRadioGroupChange();

        // Gestion des boutons stop rec et pause
        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enregistrementPositions();
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

        // Recuperation de l'Intent envoyé par gpsService
        Intent intent = getIntent();
        lat = intent.getStringExtra("lati");
        lon = intent.getStringExtra("longi");
        // Affichage des premiéres coordonnées
        txt_lat.setText(lat);
        txt_lon.setText(lon);
        // Demarrer Fade Animation
        txt_typeDeCollecte.startAnimation(fadeAnim);
    }

    // Methode qui ecoute si on a un changement d'état du radioGroup
    protected void onRadioGroupChange() {

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                    case R.id.radio_biLat:
                        RadioButton rbt_bilat = (RadioButton) findViewById(R.id.radio_biLat);
                        typeCollectte = String.valueOf(rbt_bilat.getText());
                        affichageTypeCollecte(typeCollectte);
                        //Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte, lat, lon);
                        break;
                    case R.id.radio_hlp:
                        RadioButton rbt_hlp = (RadioButton) findViewById(R.id.radio_hlp);
                        typeCollectte = String.valueOf(rbt_hlp.getText());
                        affichageTypeCollecte(typeCollectte);
                        //Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte, lat, lon);
                        break;
                    case R.id.radio_m_a:
                        RadioButton rbt_ma = (RadioButton) findViewById(R.id.radio_m_a);
                        typeCollectte = String.valueOf(rbt_ma.getText());
                        affichageTypeCollecte(typeCollectte);
                        //Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte, lat, lon);
                        break;
                    case R.id.radio_ulat:
                        RadioButton rbt_ulat = (RadioButton) findViewById(R.id.radio_ulat);
                        typeCollectte = String.valueOf(rbt_ulat.getText());
                        affichageTypeCollecte(typeCollectte);
                        //Log.i("Radio", typeCollectte);
                        typeCollectte(typeCollectte, lat, lon);

                        break;
                    default:
                        //Log.i("Default", "");
                        break;
                }
            }
        });
    }
    // Appel fonction d'enregistrement des coordonnées
    public void typeCollectte(String pTypeCollecte, String lat, String lon) {

       KmlFactory kmlFactory = new KmlFactory();
       kmlFactory.setKml(pTypeCollecte, lat, lon);
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

       btn_rec.setEnabled(false);
       gpsStatus = "!... Fin du tracking ...!";
       AfficheGpsStatus(gpsStatus);
       //img_sat.clearAnimation();
       txt_plot.setText("");

   }
   public void setLat(String pLat, String pLon) {
       this.lat = pLat;
       this.lon = pLon;

       txt_lon.setText(lon);
       txt_lat.setText(lat);
       txt_plot.setText("! ... Recording ... !");
   }
   public void AfficheGpsStatus(String pGpsStatus) {
       txt_status_gps.setText(pGpsStatus);

   }

   // Methode Appelée à l'appui sur le bouton enregistrement
   public void enregistrementPositions() {
       // inverser recIsOn
       recIsOn ^= true;

       if (recIsOn)
       {
           btn_rec.setText("PAUSE");
           Log.e(String.valueOf(recIsOn),"REC IS ON");
           //img_sat.clearAnimation();
           gpsStatus = "!... Tracking en cours ...!";
           txt_typeDeCollecte.clearAnimation();


       }
       else
       {
           btn_rec.setText("REC");
           Log.e(String.valueOf(recIsOn),"REC IS OFF");
           gpsStatus = "!... Tracking en pause ...!";
           txt_plot.setText("");
           txt_typeDeCollecte.startAnimation(fadeAnim);
       }
   }
   public void affichageTypeCollecte(String typeCollecte) {
       txt_typeDeCollecte.setTextColor(Color.rgb(0,0,0));
       txt_typeDeCollecte.setText(typeCollecte);
   }
}