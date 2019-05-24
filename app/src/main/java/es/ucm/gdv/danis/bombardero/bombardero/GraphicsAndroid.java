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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

public class GraphicsAndroid implements Graphics {

    private SurfaceView _surfaceView;       //Ventana para android
    private AssetManager _assetManager;     //Carga de imagenes
    private Canvas _canvas;                 //Viewport. Aquí se pinta.
    private SurfaceHolder _surfaceHolder;

    GraphicsAndroid(SurfaceView surfaceView, AssetManager assetManager) {
        _surfaceView = surfaceView;
        _assetManager = assetManager;
        _surfaceHolder = _surfaceView.getHolder();
        _canvas = _surfaceView.getHolder().lockCanvas();

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
        //finally hace que se ejecute siempre, para cerrar el fichero independientemente del error
        finally {
            if (rutaImage != null) {
                try {
                    rutaImage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageAndroid;
    }

    @Override
    public void clear(int color) {
        while (!_surfaceHolder.getSurface().isValid()) {
        }
        _canvas = _surfaceView.getHolder().lockCanvas();
        _canvas.drawColor(color);
    }

    @Override
    public void drawImage(Image image, int x, int y) {
        //Poner que si no es null, se pinta en canvas
        if (image != null) {
            ImageAndroid moc = (ImageAndroid) image;
            Bitmap bm = moc.getBitmap();
            _canvas.drawBitmap(bm, x, y, null);
        }
    }

    @Override
    public void drawImageFromSpritesheet(Image image, int coordX, int coordY, int tam, int destX, int destY) {
        if (image != null) {
            ImageAndroid moc = (ImageAndroid) image;
            Bitmap bm = moc.getBitmap();
            int tamSprite = 16;

            // Dos rectangulos
            //Los sprites tienen un tamaño de 16 pixeles, por lo que las coordenadas son multiplos de 16
            //Primero el source que es el rectangulo que queremos usar y lueog el destino que es donde lo vamos a pintar
            Rect sourceRect = new Rect(coordX*16, coordY*16, coordX*16 + tamSprite, coordY*16 + tamSprite);
            Rect destinyRect = new Rect(
                    destX, destY, destX + tam, destY + tam);

            _canvas.drawBitmap(bm, sourceRect, destinyRect, null);
        }
    }

    @Override
    public int getWidth() {
        return _surfaceView.getWidth();
    }

    @Override
    public int getHeight() {
        return _surfaceView.getHeight();
    }


    public void present() {
        _surfaceHolder.unlockCanvasAndPost(_canvas);
    }
}
