package gpsbom.plectre.com.gpsbom;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import static gpsbom.plectre.com.gpsbom.R.id.txt_titre;

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
    private TextView txt_time;
    private Boolean fileCreate = false;
    private Button btnSave;
    private Button btn_find_position;
    private SeekBar sb_location;
    private SeekBar sb_time;
    private TextView txt_titre;

    private long updateTime;
    private float updateLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // Vérouillage de la vue en position portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnSave = (Button) findViewById(R.id.btn_save);
        btn_find_position = (Button) findViewById(R.id.btn_fin_position);
        txt_titre = (TextView) findViewById(R.id.txt_titre);
        txt_status_gps = (TextView) findViewById(R.id.txt_gps_status);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_time = (TextView) findViewById(R.id.txt_time);
        sb_location = (SeekBar) findViewById(R.id.sb_location);
        sb_time = (SeekBar) findViewById(R.id.sb_time);

        init();

        // Manipulation de la seekBar location
        sb_location.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateLocation = (float) progress;
                txt_location.setText(String.valueOf(progress));
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Manipulation SeekBar time
        sb_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Pas de 0.5 soit 1/2 seconde
                updateTime = (long) progress * 500;
                float prog = (float) progress / 2;
                txt_time.setText(String.valueOf(prog));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // appel de la boite de dialogue sauvgarde
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
                sb_time.setEnabled(false);
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
        //txt_status_gps.setText(status_coord);
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
        intent.putExtra("update_time", updateTime);
        intent.putExtra("update_location", updateLocation);

        startService(intent);
        //startService(servicenew Intent(LauncherActivity.this, GpsService.class));


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

    public void init() {

        txt_status_gps.setText(status_coord);
        btn_find_position.setVisibility(View.INVISIBLE);
        txt_titre.setText(R.string.app_name);
        txt_location.setText("0");
        txt_time.setText("0");
    }
}