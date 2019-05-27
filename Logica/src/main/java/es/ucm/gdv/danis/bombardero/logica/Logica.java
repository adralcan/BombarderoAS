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
    public enum Colores { Negro, azul, rojo }

    private Game juego = null;

    //Pruebas
    private int posPruebaX = 20;
    private int posPruebaY = 20;
    private Sprite pruebaSprite;

    private ResourceManager _resourceManager;

    //Start
    public Logica(Game juego, int d){
        this.juego = juego;
        _resourceManager = new ResourceManager(juego.GetGraphics());
        init(d);
    }

    void init(int d){
        _dificultad = d;
        _frameRate = (float) (d + 1) / 30f;
        //tablero = new info[Ancho_Tablero][Alto_Tablero];
        //fillTablero();

        pruebaSprite = _resourceManager.GetSpriteAPartirDeAscii(Colores.rojo, 65);
    }




    //Update
    @Override
    public void tick(double elapsedTime){
        //tickBomba();
        //tickAvion();
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
        //juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, 20, 20, 200,1,15);
        juego.GetGraphics().clear(0xFF000000);
        //juego.GetGraphics().drawImage(spriteSheetNegra, posPruebaX,posPruebaY);

        int acumX = 0, acumY = 0;
        int t = 16;
        pruebaSprite.drawSprite(juego.GetGraphics(), 20, 20, 100);
        //juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, 12, 15,100, posPruebaX, posPruebaY);
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


