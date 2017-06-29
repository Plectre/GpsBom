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

public class KmlFactory extends Activity {

    private String fName;
    private String path;
    private String NEW_LINE = System.lineSeparator();
    private String kml;


    // Header kml
    public void headerKml(String lat, String lon) {
        getDirPath();
        // "<kml xmlns:kml=\"http://www.opengis.net/kml/2.2\">"
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE
                + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" + NEW_LINE
                + "<Document>" + NEW_LINE
                + "<name>" + fName + "</name>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                //+ "<name>debut</name>" + NEW_LINE
                //+ "<styleUrl></styleUrl>" + NEW_LINE
                //+ "<LineString>" + NEW_LINE
                //+ "<tesselate>1</tesselate>" + NEW_LINE
                //+ "<altitudeMode>clampToGround</altitudeMode>" + NEW_LINE
                // + <coordinates> + NEW_LINE;
                //+ "<Placemark>" + NEW_LINE
                + "<name>" + "debut" + "</name>" + NEW_LINE
                + "<Point>" + NEW_LINE
                + "<coordinates>" + NEW_LINE
                + lon + "," +lat + NEW_LINE
                + "</coordinates>" + NEW_LINE
                + "</Point>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "<altitudeMode>clampToGround</altitudeMode>" + NEW_LINE
                + "<styleUrl></styleUrl>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                + "<LineString>" + NEW_LINE
                + "<coordinates>";
        saveKml(header);
    }


    // Body kml
    public void setKml(String typeCollecte, String lat, String lon) {
        // Recuperation des coordonnées afin d'eviter les
        // /coupures du circuit lors du changement de type de collecte


        kml =   "</coordinates>"
                + NEW_LINE
                + "</LineString>"
                + NEW_LINE
                + "</Placemark>"
                + NEW_LINE
                + "<Placemark>"
                + NEW_LINE
                + "<name>" + typeCollecte + "</name>"
                + NEW_LINE +
                "<styleUrl>" + "</styleUrl>"
                + NEW_LINE +
                "<LineString>"
                + NEW_LINE +
                "<tesselate>1</tesselate>"
                + NEW_LINE
                + "<coordinates>"
                + NEW_LINE
                + lon + "," + lat;
        saveKml(kml);
        Log.i("KML_Factory", lon + "," + lat);
    }

    // Footer
    public void footerKml(String lat, String lon) {
        String footer = "</coordinates>"+ NEW_LINE
                + "</LineString>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                + "<name>" + "fin" + "</name>" + NEW_LINE
                + "<Point>" + NEW_LINE
                + "<coordinates>" + NEW_LINE
                + lon +","+ lat + NEW_LINE
                + "</coordinates>" + NEW_LINE
                + "</Point>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "</Document>" + NEW_LINE
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
        File file = new File(path, fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(kmlpart);
            output.write(NEW_LINE);

            //Log.i("Enregistrement Ok",String.valueOf(fName));
            output.close();
        } catch (IOException ex) {
            Log.e("Enregistrement fail", String.valueOf(ex));
        }
    }
}