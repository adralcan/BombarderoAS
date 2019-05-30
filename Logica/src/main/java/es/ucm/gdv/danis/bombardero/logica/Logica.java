package es.ucm.gdv.danis.bombardero.logica;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import sun.java2d.Surface;


public class Logica implements GameState {

    //Frames
    private float _lastFrame = 0;       //ultimo frame para el tiempo
    private float _frameRate;           //frames por segundo
    private float _time = 0;             //Contador de tiempo



    //UTILS
    private int _dificultad;

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

        _currentGameState = new InicioGameState(_resourceManager, juego);
        //_currentGameState = new BombarderoGameState(_resourceManager, juego);

        //TODO: calcular dificultad
        init();
    }

    void init(){
        _dificultad = 3;
        //tablero = new info[Ancho_Tablero][Alto_Tablero];
        //fillTablero();
    }

    //Update
    @Override
    public void tick(double elapsedTime){

        _time += elapsedTime;
        if(_time > _lastFrame) {
            _frameRate = (float) (_dificultad + 1) / getVelocity();
            _lastFrame = _time + _frameRate;

            if(_currentGameState.getStateOver()){
                changeCurrentState();
            }
            else
                _currentGameState.tick(elapsedTime);
        }
    }

    private void changeCurrentState(){
        switch (_estadoActual) {
            case menu:
                //_dificultad = _currentGameState.getDificultad();
                 _currentGameState = new BombarderoGameState(_resourceManager, juego);
                break;
            case bombardero:
                break;
            case gameover:
                break;
        }
    }

    @Override
    public void render() {

        juego.GetGraphics().clear(0xFF000000);
        _currentGameState.render();
    }

    @Override
    public float getVelocity() {
       return _currentGameState.getVelocity(); //TODO: _actualEstate.getVelocity();
    }

    @Override
    public boolean getStateOver() {
        return false;
    }

}


