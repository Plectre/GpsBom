package gpsbom.plectre.com.gpsbom;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by plectre on 20/03/17.
 */

public class MainActivity extends AppCompatActivity {

    //public Button btn_stop = (Button) findViewById(R.id.btn_stop);
    public TextView txt_gps_status;
    public RadioGroup rd_group;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        txt_gps_status = (TextView) findViewById(R.id.txt_satus_gps);
        // Radio Group Bouton type collectte
        rd_group = (RadioGroup) findViewById(R.id.radio_group);


        // On récupére L'ID du bouton radio checker
        int selectBouttonId = rd_group.getCheckedRadioButtonId();  // getChekckedRadioButtonId renvoi un integer
        RadioButton rd_button = (RadioButton) findViewById(selectBouttonId);
        //Toast.makeText(getBaseContext(),rd_button.getText(),Toast.LENGTH_SHORT);

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