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
    private int _velocidad = 0;

    private int puntosActuales = 0;
    private int puntosMaximos = 0;

    public enum estados {inicio, setting, bombardero, gameover}
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

        _currentGameState = new InicioGameState(_resourceManager, juego, this);
        _estadoActual = estados.inicio;

    }



    //Update
    public void tick(double elapsedTime){

        _time += elapsedTime;
        if(_time > _lastFrame) {
            _frameRate = getVelocity();
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
            case inicio:
                _currentGameState = new SettingsGameState(_resourceManager, juego, this);
                _estadoActual = estados.setting;
                break;
            case setting:
                _currentGameState = new BombarderoGameState(_resourceManager, juego, this);
                _estadoActual = estados.bombardero;
                break;
            case bombardero:
                _currentGameState = new GameOverGameState(_resourceManager, juego, this);
                _estadoActual = estados.gameover;
                break;
            case gameover:
                _currentGameState = new SettingsGameState(_resourceManager, juego, this);
                _estadoActual = estados.setting;
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

    public void setVelocidadMenu(int velocidadMenu){
        _velocidad = velocidadMenu;
        if(_velocidad < 0) _velocidad = 0;
    }

    public void setDificultad(int dificultad){
        _dificultad = dificultad;
        if(_dificultad < 0) _dificultad = 0;
    }

    public void restaVelocidadMenu(){
        _velocidad--;
        if (_velocidad < 0) _velocidad = 0;
    }

    public void restaDificultad(){
        _dificultad--;
        if (_dificultad < 0) _dificultad = 0;
    }

    public int getVelocidadMenu(){return _velocidad;}
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


