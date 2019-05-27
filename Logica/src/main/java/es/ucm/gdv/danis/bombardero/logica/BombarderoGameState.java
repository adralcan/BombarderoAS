package es.ucm.gdv.danis.bombardero.logica;

import java.util.List;
import java.util.Random;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class BombarderoGameState implements GameState {

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
    private ResourceManager _resourceManager = null;

    private Image spriteSheetNegra, javaTest=null;

    //< TABLERO >
    private Tile [][] tablero;

    private static final int Ancho_Tablero = 20;
    private static final int Alto_Tablero = 25;
    private static final int numeroEdificios = 11;

    private static int _TileSizeX, _TileSizeY;
    private static int _OffsetX, _OffsetY;

    private Graphics _graphics;

    private boolean gameOver = false;

    public BombarderoGameState (ResourceManager res, Graphics graphics){
        _resourceManager = res;
        _graphics = graphics;
        tablero = new Tile[Ancho_Tablero][Alto_Tablero];

        System.out.println("AAAA -" + _graphics);

        _TileSizeX = _graphics.getWidth()/ (Ancho_Tablero);
        _TileSizeY = _graphics.getHeight()/ (Alto_Tablero);

        _OffsetX =  _TileSizeX + (_graphics.getWidth() % (Ancho_Tablero))/ 2;
        _OffsetY =  _TileSizeY + (_graphics.getHeight() % (Alto_Tablero))/ 2;
        initEdificios();
    }


    void initEdificios(){
        Tile edifTemp;
        for (int i = 0; i < Ancho_Tablero ; i++) {
            for (int j = 0; j < Alto_Tablero ; j++) {

                if (i>4 && i < 16) {
                    edifTemp = new Tile(_resourceManager, i, j, 75, Logica.Colores.azulClaro, Logica.info.edificio);
                }
                else {
                     edifTemp = new Tile(_resourceManager, i, j, 75, Logica.Colores.azulClaro, Logica.info.nada);
                }
                tablero[i][j] = edifTemp;
            }
        }




    }

  /*  void initEdificios(Logica.info[][] _tablero){
        //Primero calculamos la altura base (5-d) y le añadimos un valor aleatorio 0-7
        //Y al edificio le añades el tejado si su altura es mayor a 0
        int alturaMinima = (5 /*-dificultad);
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
                    _tablero[newAltura][posYedfif] = Logica.info.edificio;

                    if(alt == alturaTotal){
                        //tejado
                        _tablero[newAltura-1][posYedfif] = Logica.info.tejado;
                    }
                }
            }
            //Siguiente edificio
            posYedfif++;
        }
    }*/



    void initAvion(Logica.info[][] _tablero){
        xAvion = 2;
        yAvionMorro = 3;
        yAvionCola = 2;

        //Son dos tiles que siempre van juntos lmao
        //Pero las variables si que las vamos a mantener
        _tablero[xAvion][yAvionCola] = Logica.info.avionCola;
        _tablero[xAvion][yAvionMorro] = Logica.info.avionMorro;

    }



    @Override
    public void tick(double elapsedTime) {

    }

    @Override
    public void render() {

        for (int i = 0; i < Ancho_Tablero ; i++) {
            for (int j = 0; j < Alto_Tablero ; j++) {
                tablero[i][j].drawTile(juego.GetGraphics(),_TileSizeX, _TileSizeY);
            }
        }
    }


  /*  void tickBomba(double elapsedTime){
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
                    tablero[xBomba][yBomba] = Logica.info.bomba;
                    numBombas++;
                }
            }

            listaEventos.remove(indiceEventos);
            indiceEventos++;
        }

        //Movimiento de la bomba y revisa colisiones con edificios
        if(  numBombas!= 0 &&
                tablero[xBomba+1][yBomba] == Logica.info.tejado || tablero[xBomba+1][yBomba] == Logica.info.edificio)
        {
            java.util.Random Random = new Random();
            int nPisos;

            nPisos = (Random.nextInt(3)) + 2;

            System.out.printf("Numero de pisos " + nPisos);

            //reventamos nPisos y la bomba
            tablero[xBomba][yBomba] = Logica.info.nada;
            numBombas = 0;
            int indicePiso = 0;
            while(indicePiso < nPisos){
                //Compruebas si has llegado al final
                if (tablero[xBomba + indicePiso][yBomba] == Logica.info.tejado || tablero[xBomba + indicePiso][yBomba] == Logica.info.edificio) {
                    tablero[xBomba + indicePiso][yBomba] = Logica.info.nada;
                }

                else {}
                indicePiso++;
            }
        }

        else {
            if(xBomba + 1 < 18) {
                tablero[xBomba][yBomba] = Logica.info.nada;
                if (numBombas != 0) {
                    tablero[++xBomba][yBomba] = Logica.info.bomba;
                }
            }
            else  {
                tablero[xBomba][yBomba] = Logica.info.nada;
                numBombas = 0;
            }

        }
    }
    void tickAvion(double elapsedTime){
        //Mueve el avión e interpreta colisiones, de haberlas
        //Movimiento horizontal y comprobacion
        if(tablero[xAvion][yAvionMorro+1] == Logica.info.nada){

            //Si se sale de la matriz por la derecha
            if(yAvionMorro+1 > Ancho_Tablero){
                //Reventamos esas localizaciones y movemos el avión
                tablero[xAvion][yAvionMorro] = Logica.info.nada;
                tablero[xAvion][yAvionCola]   = Logica.info.nada;

                //Sumo la X y cambio la Y
                xAvion++;
                yAvionCola = 0;
                yAvionMorro = 1;

                tablero[xAvion][yAvionMorro]  = Logica.info.avionMorro;
                tablero[xAvion][yAvionCola]   = Logica.info.avionCola;

            }
            //Comprueba si el avión ha llegado a la zona destino
            if(xAvion == posFinalX){
                //HAS GANAO HOSTIA
                tablero[xAvion][yAvionMorro] = Logica.info.explosion;
                tablero[xAvion][yAvionCola] = Logica.info.nada;
            }
            //Movimiento lateral básico
            else {
                tablero[xAvion][yAvionMorro] = Logica.info.avionCola;
                tablero[xAvion][yAvionCola] = Logica.info.nada;

                //Ahora la cola es el morro
                yAvionCola = yAvionMorro;
                //Aumentamos la Y y definimos la nueva posicion
                yAvionMorro++;
                tablero[xAvion][yAvionMorro] = Logica.info.avionMorro;
                System.out.printf("x: " + xAvion + " y: " + yAvionMorro);
                System.out.println();
            }
        }

        else {
            gameOver = true;
            //Renderiza una explosion en lugar del avión
            tablero[xAvion][yAvionMorro] = Logica.info.explosion;
            tablero[xAvion][yAvionCola] = Logica.info.nada;

        }


    }*/

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
}
