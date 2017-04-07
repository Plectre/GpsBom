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
    public String gpsStatus = "Tracking en pause";
    public RadioButton hlp;
    public Button btn_rec;
    public Button btn_stop;

    public static boolean recIsOn = false;
    protected String typeCollectte = "HLP";
    protected ImageView img_sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hlp = (RadioButton) findViewById(R.id.radio_hlp);
        img_sat = (ImageView) findViewById(R.id.img_satellite);
        txt_status_gps = (TextView) findViewById(R.id.txt_satus_gps);
        btn_rec = (Button) findViewById(R.id.btn_rec);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        // Cheked hlp par default
        hlp.setChecked(true);
        img_sat.setImageResource(R.drawable.gps_on);
        txt_status_gps.setText(gpsStatus);

        onRadioGroupChange();

        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inverser recIsOn
                recIsOn ^= true;

                if (recIsOn == true)
                {
                    btn_rec.setText("PAUSE");
                    Log.e(String.valueOf(recIsOn),"REC IS ON");
                    gpsStatus = "Tracking en cours";
                    img_sat.setImageResource(R.drawable.gps_on);

                }
                    else if (recIsOn == false)
                {
                    btn_rec.setText("REC");
                    Log.e(String.valueOf(recIsOn),"REC IS OFF");
                    gpsStatus = "Tracking en pause";
                    img_sat.setImageResource(R.drawable.gps_off);
                }
                txt_status_gps.setText(gpsStatus);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On stoppe l'enregistrement
                recIsOn = false;
                // On ferme le fichier kml en appelant le footer
                stopTacking();
                stopGpsService();
            }
        });

    }

    // Methode qui ecoute si on a un changement d'état du radioGroup
    protected void onRadioGroupChange() {

        RadioGroup rd_group = (RadioGroup) findViewById(R.id.radio_group);
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
   public void stopTacking() {
       KmlFactory kmlFactory = new KmlFactory();
       kmlFactory.footerKml();
   }
    // Fonction qui stoppe le service GPS
   public void stopGpsService() {
       stopService(new Intent(MainActivity.this, GpsService.class));
       btn_stop.setEnabled(false);
       img_sat.setImageResource(R.drawable.gps_off);

   }
}