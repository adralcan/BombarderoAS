package es.ucm.gdv.danis.bombardero.bombardero;


// IMPORTS
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Import del JUEGO
//Import del GM (lógica)
import es.ucm.gdv.danis.bombardero.logica.Logica;

// --
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

// Punto de entrada para Android //
public class MainActivity extends AppCompatActivity {

    GameAndroid _juegoAndroid;
    /*
    * Lo primero a lo que llamamos.
    * Crea el juego, la lógica que se va a usar y
    * llamamos a "run"
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //crear el motor
        _juegoAndroid = new GameAndroid(this);

        //Llamar a logica
        Logica logica = new Logica(_juegoAndroid,3);

        _juegoAndroid.setCurrentGameState(logica);

    }

   //Cuando la APP pasa a primer plano.
    @Override
    protected void onResume() {
        super.onResume();
        _juegoAndroid.OnResume();
    }

    //Cuando la App pasa a segundo plano.
    @Override
    protected void onPause() {
        super.onPause();
        _juegoAndroid.OnPause();
    }
}
