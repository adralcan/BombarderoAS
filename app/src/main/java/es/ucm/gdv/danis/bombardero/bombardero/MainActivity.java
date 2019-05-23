package es.ucm.gdv.danis.bombardero.bombardero;


// IMPORTS
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Import del JUEGO
//Import del GM (lógica)
import es.ucm.gdv.danis.bombardero.logica.Logica;

// --
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

// Punto de entrada para Android //
public class MainActivity extends AppCompatActivity {
    View mainView;
    Bitmap _sprite;

    //TODO: Referencia al juegoAndroid


    /*
    * Lo primero a lo que llamamos.
    * Crea el juego, la lógica que se va a usar y
    * llamamos a "run"
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //TODO: ¿es necesario?
        //crear el motor

        //Llamar a logica
        //Logica logica = new Logica(game,3);

        /*
        Supongo que esto irá en la parte de gráficos o en la construccion del juego en si
         mainView = new View(this);

        try {
            AssetManager assetManager = this.getAssets(); //Assets está en app/build/intermediates/assets
            InputStream inputStream = assetManager.open("logo.png");
            _sprite = BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException ioe) {

        }*/
    }

   //Cuando la APP pasa a primer plano.
    @Override
    protected void onResume() {
        super.onResume();
        //juegoAndroid.resume();
    }

    //Cuando la App pasa a segundo plano.
    @Override
    protected void onPause() {
        super.onPause();
        //juegoAndroid.pause();
    }
}
