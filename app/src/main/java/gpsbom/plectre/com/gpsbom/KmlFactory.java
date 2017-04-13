package gpsbom.plectre.com.gpsbom;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plectre on 04/04/17.
 * Classe se chargeant de construire la syntaxe Kml
 */

public class KmlFactory extends Activity{

    private String fName;
    private String path;
    private String NEW_LINE = System.lineSeparator();
    private String kml;

    // Header kml
    public void headerKml() {
        getDirPath();
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<kml xmlns:kml=\"http://www.opengis.net/kml/2.2\">"
                        + "<Document>"
                        + "<name>" + fName + "</name>"
                        + "<Placemark>"
                            + "<name>--</name>"
                            + "<styleUrl></styleUrl>"
                            + "<LineString>"
                            + "<tesselate>1</tesselate>"
                            + "<coordinates>";
        saveKml(header);
    }

    // Body kml
    public void setKml(String typeCollectte) {
        kml = "</coordinates>"
                + NEW_LINE +
                "</LineString>"
                + NEW_LINE +
                "</Placemark>"
                + NEW_LINE
                + "<Placemark>"
                + NEW_LINE
                + "<name>" + typeCollectte +  "</name>"
                + NEW_LINE +
                "<styleUrl>" + "</styleUrl>"
                + NEW_LINE +
                "<LineString>"
                + NEW_LINE +
                "<tesselate>1</tesselate>"
                + NEW_LINE
                + "<coordinates>";
        saveKml(kml);
    }

    // Footer
    public void footerKml() {
        String footer = "</coordinates>"
                        +"</LineString>"
                        + "</Placemark>"
                        + "</Document>"
                        + "</kml>";
        saveKml(footer);
    }

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        //this.fDossier = sf.getFilePath();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();
    }

    public void saveKml(String kmlpart) {
        // Appel de la fonction qui récupére le chemin et le nom du fichier
        getDirPath();
        // Ecriture des coordonnées sur le fichier
        File file = new File(path,fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(kmlpart);
            output.write(NEW_LINE);

            //Log.i("Enregistrement Ok",String.valueOf(fName));
            output.close();
        } catch (IOException ex){
            Log.e("Enregistrement fail",String.valueOf(ex));
        }
    }
}