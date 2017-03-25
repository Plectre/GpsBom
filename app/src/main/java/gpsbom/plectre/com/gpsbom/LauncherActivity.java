package gpsbom.plectre.com.gpsbom;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    public String status_coord;
    private String signal;
    private Boolean fileOk;
    private TextView txt_status_gps;
    private Boolean fileCreate;
    private Button btnSave;


    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        btnSave = (Button) findViewById(R.id.btn_save);
        final TextView txt_titre = (TextView) findViewById(R.id.txt_titre);
        txt_status_gps = (TextView) findViewById(R.id.txt_gps_status);

        txt_titre.setText(R.string.app_name);
        txt_status_gps.setText(status_coord);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // appel de la boite de dialogue sauvgarde
                SaveBox sBox = new SaveBox(LauncherActivity.this);
                sBox.show();
                btnSave.setEnabled(false);
            }
        });

    }

    @Override
    protected void onStart() {
        Log.e("isCreate onStart", String.valueOf(fileCreate));
        super.onStart();
        SaveFiles sf = new SaveFiles();
        fileCreate = sf.getIsCreate();
        Log.i("Launcher on start", String.valueOf(fileCreate));
        if (!fileCreate) {return;} else {StartGps();}
        coordOk();
        txt_status_gps.setText(status_coord);
    }

        public void StartGps() {
        // Demarrage du service GPS

                Log.e("Launcher iscreate,String",String.valueOf(fileCreate));
                startService(new Intent(LauncherActivity.this, GpsService.class));
                Log.i("demarrage", "Gps service");
                GpsService gpsService = new GpsService();
                signal = gpsService.getIscoorOk();

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

    // Reception de l'intent de GpsService afin de savoir si
    // la position est aquise
    public String coordOk() {
        status_coord = "---------------";
        SaveFiles sf = new SaveFiles();
        this.fileOk = sf.getIsCreate();
        //fileOk = true;
        Log.e(String.valueOf(fileOk),"COOR DOK");
        Intent intent = getIntent();
            // Si les coordonnées sont aquises et le fichier sauvgardé
            // Démarrage de l'activitée principale
            if (intent.getStringExtra("txt_status_gps") != null) {
                status_coord= intent.getStringExtra("txt_status_gps");
                Log.e(status_coord, "isatatus coord");

                // Démmarage activitée principale
                Intent i = new Intent(this, MainActivity.class );
                startActivity(i);

        } else  {
                return status_coord = "Position en cours d'acquisition ...";
        }
        return status_coord;
    }
}