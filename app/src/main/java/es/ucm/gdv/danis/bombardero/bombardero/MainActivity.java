package es.ucm.gdv.danis.bombardero.bombardero;


// IMPORTS
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Import del JUEGO
//Import del GM (lógica)
import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;
import es.ucm.gdv.danis.bombardero.logica.Logica;

// --
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;

// Punto de entrada para Android //
public class MainActivity extends AppCompatActivity {

    //GameAndroid _juegoAndroid;

    private MyView _renderView;
    private GraphicsAndroid _androidGraphics;
    private InputAndroid    _androidInput;
    private Logica _logicaJuego;
    boolean IniciaLogica;


    /*
    * Lo primero a lo que llamamos.
    * Crea el juego, la lógica que se va a usar y
    * llamamos a "run"
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _renderView = new MyView(this);
        setContentView(_renderView);

        //_juegoAndroid = new GameAndroid(_renderView, this.getAssets());

        _androidGraphics = new GraphicsAndroid(_renderView, this.getAssets());
        _androidInput = new InputAndroid();

        //Input
        //_renderView.setOnTouchListener(Input);

        IniciaLogica = false;
        //_logicaJuego = new Logica(_juegoAndroid);
    }

   //Cuando la APP pasa a primer plano.
    @Override
    protected void onResume() {
        super.onResume();
        _renderView.Resume();
    }

    //Cuando la App pasa a segundo plano.
    @Override
    protected void onPause() {
        super.onPause();
        _renderView.Pause();
    }

    //Clase que recubre SurfaceView
    class MyView extends SurfaceView implements Runnable, Game {

        public MyView(Context context) {

            super(context);
        }


        public void Pause(){
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

        public void Resume(){
            if(!_running){
                _running=true;

                _runningThread = new Thread(this);
                _runningThread.start();

            }
        }

        @Override
        public void run() {

            while (getWidth()<=0){} //Espera a que se inicialice el surfaceView

            if(!IniciaLogica){
                _logicaJuego = new Logica(this);
                IniciaLogica = true;
            }
            else {
                //Calcular los tiles???
            }

            //Bucle principal
            while (_running){
                SurfaceHolder sh = getHolder();
                //Input->Logica->Pintado

                currentTime = System.nanoTime();
                nanoElapsedTime = currentTime - lastFrameTime;
                elapsedTime = (double) nanoElapsedTime / 1.0e9;
                lastFrameTime = currentTime;

                _logicaJuego.tick(elapsedTime);

                while (!sh.getSurface().isValid()) {
                }
                Canvas c = sh.lockCanvas();
                _androidGraphics.startFrame(c);
                _logicaJuego.render();
                sh.unlockCanvasAndPost(c);

            }
        }


        @Override
        public Graphics GetGraphics() {
            return _androidGraphics;
        }

        @Override
        public Input GetInput() {
            return (Input)_androidInput;
        }

        //Atributos
        long lastFrameTime = System.nanoTime();
        long currentTime, nanoElapsedTime;
        double elapsedTime;
        volatile boolean _running; //Volatile hace que no revise en memoria
        Thread _runningThread;     //Hilo de juego

    }
}
