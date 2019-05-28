package es.ucm.gdv.danis.bombardero.bombardero;

import android.content.res.AssetManager;
import android.view.SurfaceView;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;


/*
    Clase encargada de mantener las instancias de graphics e input
    Para que las usen los distintos estados de juego.
*/
public class GameAndroid implements Game {


    private GraphicsAndroid _androidGraphics;
    private InputAndroid    _androidInput;


    public  GameAndroid(SurfaceView surfaceView, AssetManager assetManager){

        _androidGraphics = new GraphicsAndroid(surfaceView, assetManager);
        _androidInput = new InputAndroid();
    }
    @Override
    public Graphics GetGraphics() {
        return _androidGraphics;
    }

    @Override
    public Input GetInput() {
        return (Input)_androidInput;
    }


}
