package gpsbom.plectre.com.gpsbom.utils;

import android.util.Log;

public class Cap {

    private float oldCap = 0;
    // si le cap change de 20°
    public boolean delta(float curentCap) {

        float diff = Math.abs(curentCap - oldCap);
        boolean isCapChange = false;

        if(diff > 20) {
            Log.i(String.valueOf(diff), "Changement de cap ! detecté");
            isCapChange = true;
            oldCap = curentCap;

        }

        return isCapChange;
    }
}
