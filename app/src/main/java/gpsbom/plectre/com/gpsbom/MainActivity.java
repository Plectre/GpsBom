package gpsbom.plectre.com.gpsbom;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.checked;
import static android.R.attr.type;


/**
 * Created by plectre on 20/03/17.
 */

public class MainActivity extends AppCompatActivity {


    public TextView txt_gps_status;
    public RadioButton hlp;
    protected String typeCollectte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cheked hlp par default
        hlp = (RadioButton) findViewById(R.id.radio_hlp);
        hlp.setChecked(true);
        onRadioGroupChange();
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

   public void typeCollectte(String pTypeCollecte) {
       Log.e("MainActivity 1 --> MyReceiver", pTypeCollecte);
       SaveCoordinates sc = new SaveCoordinates();
        sc.saveTypeCollectte(pTypeCollecte);
       Log.e("MainActivity 2 --> MyReceiver", pTypeCollecte);
   }
}