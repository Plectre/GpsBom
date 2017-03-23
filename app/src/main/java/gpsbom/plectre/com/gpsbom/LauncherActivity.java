package gpsbom.plectre.com.gpsbom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    private String status_coord = "Position en cours d'aquisition ..";
    private  Boolean signal = false;
    private Boolean fileOk;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // Vérification des fichiers d'enregistrement

        final Button btnSave = (Button) findViewById(R.id.btn_save);
        final TextView status_gps = (TextView) findViewById(R.id.txt_satus_gps);


        //status_gps.setText(status_coord);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SaveBox sBox = new SaveBox(LauncherActivity.this);
                sBox.show();
                signal = sBox.receptData;
                Log.i(String.valueOf(signal), "SIGNAL");
                coordOk();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        coordOk();
        if (signal == false) {
            // Demarrage du service GPS
            startService(new Intent(LauncherActivity.this, GpsService.class));
            Log.i ("demarrage", "Gps service");
        }

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
        SaveFiles sf = new SaveFiles();
        this.fileOk = sf.getIsCreate();
        //fileOk = true;
        Log.e(String.valueOf(fileOk),"");
        Intent intent = getIntent();
            // Si les coordonnées sont aquises et le fichier sauvgardé
            // Démarrage de l'activitée principale
            if (intent.getStringExtra("txt_status_gps") != null && fileOk == true) {
                String isCoorOk = intent.getStringExtra("txt_status_gps");
                Log.e(isCoorOk, "intent de onLocationChanged");
                status_coord = isCoorOk;
                // Démmarage activitée principale
                Intent i = new Intent(this, MainActivity.class );
                startActivity(i);
        } else {
                status_coord = "Attente position !";
            }
        return status_coord;
    }

}