package es.ucm.gdv.danis.bombardero.bombardero;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;

public class GameAndroid implements Game {

    private GraphicsAndroid _graphicsAndroid;
    private InputAndroid _inputAndroid; //TODO: implementar inputAndroid

    long lastFrameTime = System.nanoTime();
    long currentTime, nanoElapsedTime;
    double elapsedTime;

    public  GameAndroid(){


        //Implementa los motores
        //_graphicsAndroid = new GraphicsAndroid();
    }
    @Override
    public Graphics GetGraphics() {
        return _graphicsAndroid;
    }

    @Override
    public Input GetInput() {
        return null;
    }

    @Override
    public void setCurrentGameState(GameState c) {

    }
}
