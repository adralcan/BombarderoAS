package es.ucm.gdv.danis.bombardero.bombardero;

import android.graphics.Bitmap;
import es.ucm.gdv.danis.bombardero.fachada.Image;

//Hereda de Graphics
public class ImageAndroid implements Image {
    Bitmap _imageBitmap;

    ImageAndroid(Bitmap sprite) {
        _imageBitmap = sprite;
    }

    public Bitmap getBitmap() {
        return _imageBitmap;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }


}
