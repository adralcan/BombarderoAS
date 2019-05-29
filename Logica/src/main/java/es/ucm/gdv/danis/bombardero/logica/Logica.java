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

    public enum info {avionCola, avionMorro, bomba, tejado, edificio, nada, explosion1, explosion2, explosion3, character};
    public enum Colores { negro, verde, rojo, morado, verdeOscuro, naranja, azulOscuro,
                            amarillo, rosa, blanco, aguamarina, azulClaro, caqui, naranjaClaro, azul, verdeClaro}

    private Game juego = null;

    //<ESTADOS>
    private BombarderoGameState _BombarderoGameState;

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

        _BombarderoGameState = new BombarderoGameState(_resourceManager, juego);

        //TODO: calcular dificultad
        init();
    }

    void init(){
        _dificultad = 3;
        _frameRate = (float) (3 + 1) / getVelocity();

        //tablero = new info[Ancho_Tablero][Alto_Tablero];
        //fillTablero();
    }

    //Update
    @Override
    public void tick(double elapsedTime){

        _time += elapsedTime;
        if(_time > _lastFrame) {
            _lastFrame = _time + _frameRate;
            //tickBomba();
            //tickAvion();
            _BombarderoGameState.tick(elapsedTime);
            // tickPrueba(elapsedTime);
        }
    }

    void tickPrueba(double elapsedTime) {
        _time += elapsedTime;
        if (_time  > _lastFrame) {
            _lastFrame = _time  + _frameRate;


            posPruebaX += 15;
            posPruebaY += 15;

        }
    }


    @Override
    public void render() {

        juego.GetGraphics().clear(0xFF000000);
        _BombarderoGameState.render();
    }

    @Override
    public float getVelocity() {
       return _BombarderoGameState.getVelocity(); //TODO: _actualEstate.getVelocity();
    }

    void CreaMatrixPrueba(){
        final info [][] test = {
                { info.nada, info.avionCola, info.avionMorro, info.nada, info.nada},
                { info.nada, info.nada,info.nada, info.nada, info.nada},
                { info.nada, info.nada, info.tejado, info.nada, info.nada},
                { info.nada, info.tejado, info.edificio, info.nada, info.nada},
                { info.nada, info.edificio, info.edificio, info.nada, info.nada}

        };

        pintaMatrix(test);
    }

    void pintaMatrix(info [][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}


