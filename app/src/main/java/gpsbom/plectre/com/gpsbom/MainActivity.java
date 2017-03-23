package gpsbom.plectre.com.gpsbom;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by plectre on 20/03/17.
 */

public class MainActivity extends AppCompatActivity {

    //public Button btn_stop = (Button) findViewById(R.id.btn_stop);
    public TextView txt_gps_status;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_gps_status = (TextView) findViewById(R.id.txt_satus_gps);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}