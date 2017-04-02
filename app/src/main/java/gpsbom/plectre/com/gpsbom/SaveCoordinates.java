package gpsbom.plectre.com.gpsbom;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plectre on 31/03/17.
 **/

public class SaveCoordinates {

    private String fName;
    private String path;
    private String NEW_LINE = System.lineSeparator();

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        //this.fDossier = sf.getFilePath();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();

    }

    public void saveCoor(String pLat, String pLon) {

        getDirPath();

        Log.i("Save latitude", pLat);
        Log.i("Save longitude", pLon);


        File file = new File(path,fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(pLat);
            output.write(" , ");
            output.append(pLon);
            output.write(NEW_LINE);

            Log.i("Enregistrement Ok",String.valueOf(fName));
            output.close();
        } catch (IOException ex){
            Log.e("Enregistrement fail",String.valueOf(ex));
        }
    }


    public void saveTypeCollectte(String tCollectte) {
        getDirPath();
        File file = new File(path,fName);
        try {
            FileWriter out = new FileWriter(file, true);
            out.append(tCollectte);
            out.write(NEW_LINE);

            Log.i("Enregistrement Ok",String.valueOf(tCollectte));
            out.close();
        } catch (IOException ex){
            Log.e("Enregistrement fail",String.valueOf(ex));
        }
    }
}