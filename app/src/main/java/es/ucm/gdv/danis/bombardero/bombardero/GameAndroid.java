package es.ucm.gdv.danis.bombardero.bombardero;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;

//Esta clase es el equivalente a MyView
public class GameAndroid implements Game, Runnable {

    private SurfaceView _surfaceView;

    private GraphicsAndroid _androidGraphics;
    private InputAndroid    _androidInput;
    private GameState _estadoJuego;

    volatile boolean _running; //Volatile hace que no revise en memoria
    Thread _runningThread;     //Hilo de juego

    long lastFrameTime = System.nanoTime();
    long currentTime, nanoElapsedTime;
    double elapsedTime;

    public  GameAndroid(AppCompatActivity context){

        _surfaceView = new SurfaceView(context);
        context.setContentView(_surfaceView);

        AssetManager assetManager = context.getAssets();

        _androidGraphics = new GraphicsAndroid(_surfaceView,assetManager);
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

    @Override
    public void setCurrentGameState(GameState c) {
        _estadoJuego = c;
    }

    @Override
    public void run() {
        int i = 0;

        while (_running) {
            //Input->Logica->Pintado

            currentTime = System.nanoTime();
            nanoElapsedTime = currentTime - lastFrameTime;
            elapsedTime = (double) nanoElapsedTime / 1.0e9;
            lastFrameTime = currentTime;

            _estadoJuego.tick(elapsedTime);

           /*try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //Necesitamos llamar a start frame para hacer un nuevo canvas??

            _estadoJuego.render();
            _androidGraphics.present();
            //Present por aquí o algo así

        }
    }

    public void OnPause(){
        _running = false;
        while (true) {
            //Tenemos que parar el hilo bien, antes de empezarlo de nuevo
            try {
                _runningThread.join();
                break;
            } catch (InterruptedException ie) {

            }
        }
    }

    public void OnResume(){
        if(!_running){
            _running=true;

            _runningThread = new Thread(this);
            _runningThread.start();

        }
    }
}
