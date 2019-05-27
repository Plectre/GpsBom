package gpsbom.plectre.com.gpsbom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import gpsbom.plectre.com.gpsbom.saves.SaveBox;
import gpsbom.plectre.com.gpsbom.saves.SaveFiles;

/**
 * Activitée Launcher
 * Instanciaion des Views et appel des méthodes de démarrage du GPS
 * Demarrage de l'activitée principale si le signal du GPS est acqui
 */

public class LauncherActivity extends AppCompatActivity {
    public String status_coord = "";
    private String signal;
    private Boolean fileOk;
    private TextView txt_status_gps;
    private TextView txt_location;
    private Boolean fileCreate = false;
    private Button btnSave;
    private Button btn_find_position;
    private SeekBar sb_location;
    private TextView txt_titre;
    private float updateLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // Vérouillage de la vue en position portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialisation des views
        btnSave = (Button) findViewById(R.id.btn_save);
        btn_find_position = (Button) findViewById(R.id.btn_fin_position);
        txt_titre = (TextView) findViewById(R.id.txt_titre);
        txt_status_gps = (TextView) findViewById(R.id.txt_gps_status);
        txt_location = (TextView) findViewById(R.id.txt_location);
        //txt_time = (TextView) findViewById(R.id.txt_time);
        sb_location = (SeekBar) findViewById(R.id.sb_location);
        //sb_time = (SeekBar) findViewById(R.id.sb_time);

        init();

        // Manipulation de la seekBar location
        sb_location.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float minSeekValue = 30; // Valeur minimum de la seekBar
                updateLocation = (float) progress + minSeekValue;
                // Récupération de la string formatée dans R.string
                // string>%progress km/h</string>
                typeDeCircuit(progress);
                //txt_location.setText(getString(R.string.concat_km_h, progress));
                Log.d("progress bar", String.valueOf(progress));
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Manipulation SeekBar time

        /**       sb_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Pas de 0.5 soit 1/2 seconde
        updateTime = (long) progress * 500;
        float prog = (float) progress / 2;
        txt_time.setText(String.valueOf(prog));

        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override public void onStopTrackingTouch(SeekBar seekBar) {

        }
        });
         **/

        // Appel de la boite de dialogue sauvegarde
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveBox sBox = new SaveBox(LauncherActivity.this);
                sBox.show();
                btn_find_position.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
            }
        });

        // Bouton demarrageService GPS
        btn_find_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveFiles sf = new SaveFiles();
                fileCreate = sf.getIsCreate();
                btnSave.setEnabled(false);
                if (!fileCreate) {
                    return;
                } else {
                    StartGps();
                }
                txt_status_gps.setText(R.string.status_gps);
                btnSave.setVisibility(View.INVISIBLE);
                btn_find_position.setEnabled(false);
                //sb_time.setEnabled(false);
                sb_location.setEnabled(false);
            }
        });

    }

    @Override
    protected void onStart() {
        Log.e("Launcher onStart", String.valueOf(fileCreate));
        super.onStart();
        Log.i("Launcher on start", String.valueOf(fileCreate));
        coordOk();
    }

    public void StartGps() {
        /**
         *  Demarrage du service GPS et passage des valeurs de la SeekBar vers
         *  update_time et update_location
         */

        Log.e("Launcher iscreate,String", String.valueOf(fileCreate));
        GpsService gpsService = new GpsService();
        signal = gpsService.getIscoorOk();
        Intent intent = new Intent(this, GpsService.class);
        //intent de a vitesse de collecte
        intent.putExtra("update_location", updateLocation);
        startService(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ACTIVITY ", "Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stopper le service GPS
        stopService(new Intent(LauncherActivity.this, GpsService.class));
    }

    /**
     * Reception de l'intent de GpsService afin de savoir si
     * * la position est aquise
     */

    public String coordOk() {
        status_coord = "---------------";
        SaveFiles sf = new SaveFiles();
        this.fileOk = sf.getIsCreate();
        //fileOk = true;
        Log.e(String.valueOf(fileOk), "COOR DOK");
        Intent intent = getIntent();
        /** Si les coordonnées sont aquises et le fichier sauvgardé
         // Démarrage de l'activitée principale */

        if (intent.getStringExtra("txt_status_gps") != null) {
            status_coord = intent.getStringExtra("txt_status_gps");
            Log.e(status_coord, "isatatus coord");

            // Démmarage activitée principale
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else {
            return status_coord = "Position en cours d'acquisition ...";
        }
        return status_coord;
    }

    public void vibrator() {
        Log.i("vibrator", "VIBRATOR");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void init() {

        txt_status_gps.setText(status_coord);
        btn_find_position.setVisibility(View.INVISIBLE);
        txt_titre.setText(R.string.app_name);
        txt_location.setText(R.string.hyper_urbain);
        //txt_time.setText("0");
    }

    public void typeDeCircuit(int i) {
        if (i >= 0 && i <= 12) {
            txt_location.setText(R.string.hyper_urbain);
        } else if (i > 12 && i <= 24) {
            txt_location.setText(R.string.urbain);
        } else if (i > 24 && i <= 36) {
            txt_location.setText(R.string.rural);
        } else if (i > 36) {
            txt_location.setText(R.string.hyper_rural);
        } else {
            txt_location.setText("Type de circuit");
        }
    }
}