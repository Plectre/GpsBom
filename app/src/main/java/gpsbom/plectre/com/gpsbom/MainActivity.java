package gpsbom.plectre.com.gpsbom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String status_coord = "Position en cours d'aquisition ..";
    private Context context = MainActivity.this;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Vérification des fichiers d'enregistrement
        //SaveFiles saveDirectory = new SaveFiles();
        //saveDirectory.testCarteSd();

        final Button btnSave = (Button) findViewById(R.id.btn_save);
        final TextView status_gps = (TextView) findViewById(R.id.txt_satus_gps);

        coordOk();
        status_gps.setText(status_coord);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SaveBox sBox = new SaveBox(MainActivity.this);
                sBox.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Demarrage du service GPS
        //startService(new Intent(MainActivity.this, GpsService.class));

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
        stopService(new Intent(MainActivity.this, GpsService.class));
    }

    // Reception de l'intent de GpsService afin de savoir si
    // la position est aquise
    public String coordOk() {
        Intent intent = getIntent();

            if (intent.getStringExtra("txt_status_gps") != null) {
                String isCoorOk = intent.getStringExtra("txt_status_gps");
                Log.e(isCoorOk, "intent de onLocationChanged");
                status_coord = isCoorOk;
        } else {
                status_coord = "Position en cours d'aquisition !";
            }
        return status_coord;
    }

}
