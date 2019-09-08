
// Si changement de cap de plus de 20° on ajoute un point GPS

package gpsbom.plectre.com.gpsbom.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class Cap {
    private Context context;

    private float oldCap = 0;
    // si le cap change de 20°
    public boolean delta(float curentCap) {

        float diff = Math.abs(curentCap - oldCap);
        boolean isCapChange = false;

        if(diff > 20) {
            Log.i(String.valueOf(diff), "Changement de cap ! detecté");
            isCapChange = true;
            oldCap = curentCap;
            Toast.makeText(context.getApplicationContext(),"Changement de cap !",Toast.LENGTH_SHORT).show();
        }

        return isCapChange;
    }
}
