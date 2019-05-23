package es.ucm.gdv.danis.bombardero.bombardero;
//Imports del proyecto
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;
//Imports de android
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

public class GraphicsAndroid implements Graphics {

    private SurfaceView _surfaceView;   //Ventana para android
    private AssetManager _assetManager; //Carga de imagenes
    private Canvas _canvas;             //Viewport. Aquí se pinta.

    GraphicsAndroid(SurfaceView surfaceView, AssetManager assetManager){
        _surfaceView = surfaceView;
        _assetManager = assetManager;
    }

    public void startFrame(Canvas c) {
        _canvas = c;
    }

    @Override
    public Image newImage(String name) {
        ImageAndroid imageAndroid = null;
        InputStream rutaImage = null;
        try {
            rutaImage = _assetManager.open(name);
            Bitmap sprite = BitmapFactory.decodeStream(rutaImage); //Decode el asset manager
            imageAndroid = new ImageAndroid(sprite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //finally hace que se ejecute siempre, para cerrar el fichero
        finally {
            if (rutaImage != null) {
                try {
                    rutaImage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return imageAndroid;
        }
    }

    @Override
    public void clear(int color) {

    }

    @Override
    public void drawImage(Image image, int x, int y) {
        //Poner que si no es null, se pinta en canvas
        if(image != null){
            ImageAndroid moc = (ImageAndroid) image;
            Bitmap bm = moc.getBitmap();
            _canvas.drawBitmap(bm, x, y, null);
        }
    }

    @Override
    public void drawImageFromSpritesheet(Image image, int x, int y, int tam, int imgX, int imgY) {

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
