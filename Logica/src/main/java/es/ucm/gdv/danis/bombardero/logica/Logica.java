package es.ucm.gdv.danis.bombardero.logica;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import sun.java2d.Surface;


public class Logica {

    //Frames
    private float _lastFrame = 0;       //ultimo frame para el tiempo
    private float _frameRate;           //frames por segundo
    private float _time = 0;             //Contador de tiempo



    //UTILS
    private int _dificultad;
    private float _velocidad = 0;

    private int puntosActuales = 0;
    private int puntosMaximos = 0;

    public enum estados {menu, bombardero, gameover}
    public enum info {avionCola, avionMorro, bomba, tejado, edificio, nada, explosion1, explosion2, explosion3};
    public enum Colores { negro, verde, rojo, morado, verdeOscuro, naranja, azulOscuro,
                            amarillo, rosa, blanco, aguamarina, azulClaro, caqui, naranjaClaro, azul, verdeClaro}

    private Game juego = null;

    //<ESTADOS>

    private GameState _currentGameState;
    private estados _estadoActual;
    private BombarderoGameState _BombarderoGameState;
    private InicioGameState _InicioGameState;

    //Pruebas
    private int posPruebaX = 20;
    private int posPruebaY = 20;
    private Sprite pruebaSprite;
    private Tile pruebaTile;

    private ResourceManager _resourceManager;

    //Start
    public Logica(Game juego){
        this.juego = juego;

        _resourceManager = new ResourceManager(juego.GetGraphics());
        //<Creamos los estados de juego>

        _currentGameState = new InicioGameState(_resourceManager, juego, this);
        _estadoActual = estados.menu;
        //_currentGameState = new BombarderoGameState(_resourceManager, juego);

    }



    //Update
    public void tick(double elapsedTime){

        _time += elapsedTime;
        if(_time > _lastFrame) {
            _frameRate = _currentGameState.getVelocity();
            _lastFrame = _time + _frameRate;

            if(_currentGameState.getStateOver()){
                changeCurrentState();
            }
            else
                _currentGameState.tick(elapsedTime);
        }
    }

    private void changeCurrentState(){
        juego.GetInput().Clear();
        switch (_estadoActual) {
            case menu:
                _currentGameState = new BombarderoGameState(_resourceManager, juego, this);
                _estadoActual = estados.bombardero;
                break;
            case bombardero:
                break;
            case gameover:
                break;
        }
    }

    public void render() {

        juego.GetGraphics().clear(0xFF000000);
        _currentGameState.render();
    }


    public float getVelocity() {
       return _currentGameState.getVelocity();
    }


    public void setVelocidad(float velocidad){
        if (velocidad >= 0)
            _velocidad = velocidad;
        else _velocidad = 0;
    }

    public void setDificultad(int dificultad){
        if (dificultad >= 0)
            _dificultad = dificultad;
        else _dificultad = 0;
    }

    public float getVelocidad(){return _velocidad;}
    public int getDificultad(){return _dificultad;}

    public int getPuntosActuales() {
        return puntosActuales;
    }

    public void setPuntosActuales(int puntosActuales) {
        this.puntosActuales = puntosActuales;
        if(this.puntosActuales >= this.puntosMaximos) {
            this.puntosMaximos = this.puntosActuales;
        }
    }

    public int getPuntosMaximos() {
        return puntosMaximos;
    }

    public void setPuntosMaximos(int puntosMaximos) {
        this.puntosMaximos = puntosMaximos;
    }
}


