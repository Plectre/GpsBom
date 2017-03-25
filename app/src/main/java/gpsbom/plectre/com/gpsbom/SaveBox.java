package gpsbom.plectre.com.gpsbom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by plectre on 16/03/17.
 */


    public class SaveBox extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button save,cancel;
    public EditText edText;
    public Boolean receptData = false;
    private String fichierName;



    public SaveBox(Activity a) {
        super(a);
        this.c = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);
        save = (Button) findViewById(R.id.btn_saveFichierName);
        cancel = (Button) findViewById(R.id.btn_cancel);
        edText = (EditText) findViewById(R.id.etSave);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    // gestion des boutons cliqués
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_saveFichierName:
                // Test si le champs est rempli
                if (edText.getText().toString().equals("")) {
                    dialogIsEmpty();
                } else {
                    //Log.e("edText :", "!= null");
                    edTexSaveOK(v);
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                Log.e("Default", "Default");
                break;
        }
    }

    public void dialogIsEmpty() {
        Toast.makeText(getContext(), "Remplir tous les champs ...", Toast.LENGTH_LONG).show();
    }

    // Methode céation fichier
    public void edTexSaveOK(View v) {
                fichierName = String.valueOf(edText.getText());
                //Log.e("SAVE", fichierName);
                // Vérification des fichiers d'enregistrement
                SaveFiles saveDirectory = new SaveFiles();
                saveDirectory.testCarteSd(fichierName);
                dismiss();
                receptData = true;
        }

    }