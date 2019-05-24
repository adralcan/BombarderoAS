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

    //AVION
    private int xAvion = 0;
    private int yAvionMorro = 0; int yAvionCola = 0;
    private double xIncr = 1, yIncr = 1;

    private int posFinalX = 18, posFinalY = 22;
    //BOMBA
    public int numBombas = 0;
    private int xBomba = 0;
    private int yBomba = 0;

    //ATRIBUTOS
    private Game juego = null;
    private Image spriteSheetNegra, javaTest=null;

    private info [][] tablero;

    private static final int Ancho_Tablero = 20;
    private static final int Alto_Tablero = 25;
    private static final int numeroEdificios = 11;

    private boolean gameOver = false;

    //Pruebas
    private int posPruebaX = 20;
    private int posPruebaY = 20;

    //Start
    public Logica(Game juego, int d){
        this.juego = juego;
        System.out.println("AAAAAAAA - VOY A CARGAR IMAGEN");
        spriteSheetNegra = juego.GetGraphics().newImage("ASCII_03.png");
        System.out.println("AAAAAAA - IMAGEN CARGADA!!");
        init(d);
    }

    void init(int d){
        _dificultad = d;
        _frameRate = (float) (d + 1) / 30f;
        tablero = new info[Ancho_Tablero][Alto_Tablero];
        fillTablero();
    }

    void fillTablero(){
        for (int i = 0; i < Ancho_Tablero ; i++) {
            for (int j = 0; j < Alto_Tablero ; j++) {
                tablero[i][j] = info.nada;
            }
        }

        //Inicia los edificios
        //initEdificios(tablero);

        //Crea el avión y define la posición final
        //initAvion(tablero);

    }

    void initEdificios(info [][] _tablero){
        //Primero calculamos la altura base (5-d) y le añadimos un valor aleatorio 0-7
        //Y al edificio le añades el tejado si su altura es mayor a 0
        int alturaMinima = (5 - _dificultad );
        int posYedfif = 5;

        //FOR
        for (int i = 0; i < numeroEdificios ; i++) {

            java.util.Random Random = new Random();
            int alturaExtra = Random.nextInt(7);
            int alturaTotal = alturaMinima  + alturaExtra;

            final int Base = 18;

            if(alturaTotal > 0 ) {

                int altEdif = 0;
                for (int alt = 0; alt <= alturaTotal; alt++) {

                    int newAltura = Base - alt; //Y tal
                    _tablero[newAltura][posYedfif] = info.edificio;

                    if(alt == alturaTotal){
                        //tejado
                        _tablero[newAltura-1][posYedfif] = info.tejado;
                    }
                }
            }
            //Siguiente edificio
            posYedfif++;
        }
    }



    void initAvion(info [][] _tablero){
        xAvion = 2;
        yAvionMorro = 3;
        yAvionCola = 2;

        _tablero[xAvion][yAvionCola] = info.avionCola;
        _tablero[xAvion][yAvionMorro] = info.avionMorro;

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


            System.out.println("AAAAA - X "+ posPruebaX + "Y: " + posPruebaY);

        }
    }


    void tickBomba(double elapsedTime){
        //Bomba
        //Pide el input y revisa si se ha pulsado la pantalla.
        //Si se ha pulsado y no hubiera bomba, crea una
        List<TouchEvent> listaEventos = juego.GetInput().getTouchEvents();
        int indiceEventos = 0;

       while(!listaEventos.isEmpty()){
            TouchEvent touchEvent = listaEventos.get(indiceEventos);
            //Click izquierdo
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click){
                //Creamos una bomba!
                if(numBombas == 0 && !gameOver){
                    xBomba = xAvion+1;
                    yBomba = yAvionCola;
                    tablero[xBomba][yBomba] = info.bomba;
                    numBombas++;
                }
            }

            listaEventos.remove(indiceEventos);
            indiceEventos++;
        }

        //Movimiento de la bomba y revisa colisiones con edificios
        if(  numBombas!= 0 &&
                tablero[xBomba+1][yBomba] == info.tejado || tablero[xBomba+1][yBomba] == info.edificio)
        {
            java.util.Random Random = new Random();
            int nPisos;

            nPisos = (Random.nextInt(3)) + 2;

            System.out.printf("Numero de pisos " + nPisos);

            //reventamos nPisos y la bomba
            tablero[xBomba][yBomba] = info.nada;
            numBombas = 0;
            int indicePiso = 0;
            while(indicePiso < nPisos){
                //Compruebas si has llegado al final
                if (tablero[xBomba + indicePiso][yBomba] == info.tejado || tablero[xBomba + indicePiso][yBomba] == info.edificio) {
                    tablero[xBomba + indicePiso][yBomba] = info.nada;
                }

                else {}
                indicePiso++;
            }
        }

        else {
            if(xBomba + 1 < 18) {
                tablero[xBomba][yBomba] = info.nada;
                if (numBombas != 0) {
                    tablero[++xBomba][yBomba] = info.bomba;
                }
            }
            else  {
                tablero[xBomba][yBomba] = info.nada;
                numBombas = 0;
            }

        }
    }
    void tickAvion(double elapsedTime){
        //Mueve el avión e interpreta colisiones, de haberlas
        //Movimiento horizontal y comprobacion
        if(tablero[xAvion][yAvionMorro+1] == info.nada){

            //Si se sale de la matriz por la derecha
            if(yAvionMorro+1 > Ancho_Tablero){
                //Reventamos esas localizaciones y movemos el avión
                tablero[xAvion][yAvionMorro] = info.nada;
                tablero[xAvion][yAvionCola]   = info.nada;

                //Sumo la X y cambio la Y
                xAvion++;
                yAvionCola = 0;
                yAvionMorro = 1;

                tablero[xAvion][yAvionMorro]  = info.avionMorro;
                tablero[xAvion][yAvionCola]   = info.avionCola;

            }
            //Comprueba si el avión ha llegado a la zona destino
            if(xAvion == posFinalX){
                //HAS GANAO HOSTIA
                tablero[xAvion][yAvionMorro] = info.explosion;
                tablero[xAvion][yAvionCola] = info.nada;
            }
            //Movimiento lateral básico
            else {
                tablero[xAvion][yAvionMorro] = info.avionCola;
                tablero[xAvion][yAvionCola] = info.nada;

                //Ahora la cola es el morro
                yAvionCola = yAvionMorro;
                //Aumentamos la Y y definimos la nueva posicion
                yAvionMorro++;
                tablero[xAvion][yAvionMorro] = info.avionMorro;
                System.out.printf("x: " + xAvion + " y: " + yAvionMorro);
                System.out.println();
            }
        }

        else {
            gameOver = true;
            //Renderiza una explosion en lugar del avión
            tablero[xAvion][yAvionMorro] = info.explosion;
            tablero[xAvion][yAvionCola] = info.nada;

        }


    }

    @Override
    public void render() {
        //juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, 20, 20, 200,1,15);
        juego.GetGraphics().clear(0xFF000000);
        //juego.GetGraphics().drawImage(spriteSheetNegra, posPruebaX,posPruebaY);

        int acumX = 0, acumY = 0;
        int t = 16;
        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, 12, 15,100, posPruebaX, posPruebaY);
    }
 /*
        @Override
    public void render() {

        //int imgw = spriteSheetNegra.getWidth(), imgh = spriteSheetNegra.getHeight();
        //juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x, y, 200,1,15);
        //juego.GetGraphics().drawImage(spriteSheetNegra, x, y);

        //DEBUG DE LA MATRIZ
        //pintaMatrix(tablero);

        int acumX = 0, acumY = 0;
        int t = 30;
        for (int x = 0; x <  tablero.length  ; x++) {
            for (int y = 0; y < tablero[x].length; y++) {
                switch (tablero[x][y]) {
                    case edificio:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 9, 14);
                        break;
                    case tejado:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 4, 15);
                        break;
                    case avionCola:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 1, 15);
                        break;
                    case avionMorro:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 2, 15);
                        break;
                    case bomba:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 12, 15);
                        break;
                    case nada:
                        //juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 1, 16);
                        break;
                    case explosion:
                        juego.GetGraphics().drawImageFromSpritesheet(spriteSheetNegra, x + acumX, y + acumY, t, 14, 14);
                        break;

                }
                acumX += t;
            }
            //Reset de la X y aumentamos Y
            acumX = 0;
            acumY += t;
        }
    }
*/
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


