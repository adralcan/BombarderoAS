package es.ucm.gdv.danis.bombardero.logica;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Image;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;




public class Logica implements GameState {

    //Frames
    private float _lastFrame = 0;       //ultimo frame para el tiempo
    private float _frameRate;           //frames por segundo
    private float _time = 0;             //Contador de tiempo

    //TABLERO
    private int _dificultad;

    public enum info {avionCola, avionMorro, bomba, tejado, edificio, nada, explosion};
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
    private Tile pruebaTile2;
    private Tile pruebaTile3;

    private ResourceManager _resourceManager;

    //Start
    public Logica(Game juego, int d){
        this.juego = juego;
        _resourceManager = new ResourceManager(juego.GetGraphics());
        _BombarderoGameState = new BombarderoGameState(_resourceManager, juego.GetGraphics());

        init(d);
    }

    void init(int d){
        _dificultad = d;
        _frameRate = (float) (d + 1) / 30f;

        //tablero = new info[Ancho_Tablero][Alto_Tablero];
        //fillTablero();
    }



    //Update
    @Override
    public void tick(double elapsedTime){
        //tickBomba();
        //tickAvion();
        _BombarderoGameState.tick(elapsedTime);
        tickPrueba(elapsedTime);
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


