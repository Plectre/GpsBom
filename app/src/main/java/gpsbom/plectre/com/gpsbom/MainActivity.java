package gpsbom.plectre.com.gpsbom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SaveFiles saveDirectory = new SaveFiles();
        saveDirectory.testDeLaCarte();

        final Button btnOn = (Button) findViewById(R.id.gpsOn);
        final Button btnOff = (Button) findViewById(R.id.gpsOff);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Demarrage du service GPS
                startService(new Intent(MainActivity.this, GpsService.class));
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, GpsService.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
