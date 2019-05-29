package es.ucm.gdv.danis.bombardero.logica;

import java.sql.Struct;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

//TODO: ORGANIZAR LAS VARIABLES
public class BombarderoGameState implements GameState {

    //ESTADOS BOMBARDERO GAME STATE
    enum Estados{ CINEMATICA, JUEGO, WIN, LOSE}

    Estados estadoActual;

    private float _velocidadActual;
    private final float velocidadCinematica = 250.0f;
    private final float velocidadJuego = 30.0f;

    //Atributos Avión
    private int xAvion = 3;
    private int yAvion = 2;
    private double xIncr = 1, yIncr = 1;

    private int posFinalX = 18, posFinalY = 22;

    //BOMBA
    public int numBombas = 0;
    private int xBomba = 0;
    private int yBomba = 0;

    LinkedList<Tile> listaExplosiones;

    //ATRIBUTOS
    private class Edificio{
        Edificio(int a, int c){
            _alturaEdificio = a;  _color = c;
        }
        public int _alturaEdificio;
        public int _color;
    }

    private Edificio [] edificios;
    private ResourceManager _resourceManager = null;

    private final int NumEdificios = 11;
    private Image spriteSheetNegra, javaTest=null;

    //atributos cinematica
    private int dificultad;
    private int i = 0;
    private int j = 22;
    private Random rnd;

    //

    //< TABLERO >
    private Tile [][] tablero;

    private static final int Ancho_Tablero = 20;
    private static final int Alto_Tablero = 25;
    private static final int numeroEdificios = 11;

    private static int _TileSizeX, _TileSizeY;
    private static int _OffsetX, _OffsetY;

    private Game _juego;
    private Graphics _graphics;

    private boolean gameOver = false;

    public BombarderoGameState (ResourceManager res, Game juego){
        _resourceManager = res;
        _juego = juego;
        _graphics = _juego.GetGraphics();

        tablero = new Tile[Ancho_Tablero][Alto_Tablero];
        estadoActual = Estados.CINEMATICA;
        _velocidadActual = velocidadCinematica;


        edificios = new Edificio[NumEdificios];
        listaExplosiones = new LinkedList<>();
        initCuidad();

    }

    //Init de todos los elementos de la ciudad
    private void initCuidad(){
        initMatriz();
        initAlturaEdificios();
        initAvion(xAvion,yAvion);
    }

    void initMatriz(){
        _TileSizeX = _graphics.getWidth() / (Ancho_Tablero);
        _TileSizeY = _graphics.getHeight()/ (Alto_Tablero+2);

        _OffsetX =  _TileSizeX + (_graphics.getWidth() % (Ancho_Tablero))/ 2;
        _OffsetY =  _TileSizeY + (_graphics.getHeight() % (Alto_Tablero+10))/ 2;

        dificultad = 2; //Provisional hasta que nos la pase el gamestate de seleccionar dificultad
        Tile edifTemp = null;
        rnd = new Random(); //Para generar alturas aleatorias

        for (int i = 0; i < Ancho_Tablero ; i++) {
            for (int j = 0; j < Alto_Tablero ; j++) {
                edifTemp = new Tile(_resourceManager, i, j, i * _OffsetX, j * _OffsetY, _TileSizeX, _TileSizeY, Logica.Colores.azulClaro, Logica.info.nada);
                tablero[i][j] = edifTemp;
            }
        }
    }

    void initAlturaEdificios(){
        for(int j = 0; j < numeroEdificios; j++ ){
            int _alturaEdificio = (5 - dificultad)  + rnd.nextInt(7);
            int _color;
            do{
                _color = rnd.nextInt(16);
            }while(_color == 0);
            edificios[j] = new Edificio(_alturaEdificio, _color);
        }
    }


    boolean initEdificio(int x, int y){

            Logica.Colores color = Logica.Colores.values()[edificios[x]._color];
            tablero[x+4][y].setTile(color, Logica.info.edificio);

            if(y-1 ==  22 - edificios[x]._alturaEdificio) {
                tablero[x+4][y - 1].setTile(color, Logica.info.tejado);
                return true;
            }
            else return false;
    }


    void initAvion(int x, int y){


        //Son dos tiles que siempre van juntos lmao
        //Pero las variables si que las vamos a mantener
        tablero[x-1][y].setTile(Logica.Colores.rojo, Logica.info.avionCola);
        tablero[x][y].setTile(Logica.Colores.rojo, Logica.info.avionMorro);

    }



    @Override
    public void tick(double elapsedTime) {

        switch (estadoActual){

            case CINEMATICA:
                //Construye edificio
                if (i < numeroEdificios) {
                    if(edificios[i]._alturaEdificio > 0  && !initEdificio(i,j)  ) {
                        //Si no ha terminado, resto para subir la altura
                        j--;
                    }
                    else {
                        i++;
                        j = 22;
                    }

                }
                else{
                    estadoActual = Estados.JUEGO;
                    _velocidadActual = velocidadJuego;
                }

                break;
            case JUEGO:
                if (!gameOver) {
                    tickAvion(elapsedTime);
                    tickBomba(elapsedTime);
                    tickExplosion();
                }
                break;
            case WIN:
                break;
            case LOSE:
                break;
        }
    }

    //Mueve el avión e interpreta colisiones, de haberlas
    //Movimiento horizontal y comprobacion
    private void tickAvion(double elapsedTime){

        //Comprobar colisiones con la casilla siguiente.
       Tile Avion =  tablero[xAvion][yAvion];

        if( tablero[xAvion+1][yAvion].get_infoTile() == Logica.info.nada){

            tablero[xAvion][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
            tablero[xAvion-1][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);

            //Si se sale por la derecha
            if(xAvion+1 > Ancho_Tablero-2) {
                xAvion = 3;
                yAvion++;
            }
            else{
                xAvion++;
            }
            tablero[xAvion][yAvion].setTile(Logica.Colores.rojo, Logica.info.avionMorro);
            tablero[xAvion-1][yAvion].setTile(Logica.Colores.rojo, Logica.info.avionCola);
        }

        else {
            if (tablero[xAvion + 1][yAvion].get_infoTile() == Logica.info.tejado || tablero[xAvion + 1][yAvion].get_infoTile() == Logica.info.edificio) {
                tablero[xAvion][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
                tablero[xAvion-1][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
                xAvion++;
                tablero[xAvion][yAvion].setTile(Logica.Colores.rojo, Logica.info.explosion1);
                Tile expl = tablero[xAvion][yAvion];
                listaExplosiones.add(expl);
                tablero[xAvion - 1][yAvion].setTile(Logica.Colores.rojo, Logica.info.nada); //borrar la cola
                //Cambio de estado
                gameOver = true;
            }

        }
    }

    private void tickBomba(double elapsedTime)
    {
        List<TouchEvent> touchEvents =  _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent:touchEvents) {
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click && numBombas < 1){
                numBombas++;
                xBomba = xAvion; yBomba=yAvion+1;
                tablero[xAvion][yAvion].setTile(Logica.Colores.rojo, Logica.info.bomba);
            }
        }

        if(numBombas >= 1){
            tablero[xBomba][yBomba].setTile(Logica.Colores.blanco, Logica.info.nada);
            yBomba++;

            //Comprobamos colisiones
            if(tablero[xBomba][yBomba].get_infoTile() == Logica.info.tejado || tablero[xBomba][yBomba].get_infoTile() == Logica.info.edificio){
                Random rnd = new Random();

                int edificiosDestruidos = 3; /*rnd.nextInt(3)+1;*/
                for(int i = 0; i < edificiosDestruidos; i++){
                    tablero[xBomba][yBomba+(i-1)].setTile(Logica.Colores.rojo, Logica.info.nada);
                  tablero[xBomba][yBomba+i].setTile(Logica.Colores.rojo, Logica.info.explosion1);

                  if(i == edificiosDestruidos-1){
                      Tile expl = tablero[xBomba][yBomba+edificiosDestruidos-1];
                      listaExplosiones.add(expl);
                  }
                }
                numBombas--;
            }
            else if (yBomba >= 22){
                tablero[xBomba][yBomba].setTile(Logica.Colores.rojo, Logica.info.explosion1);
                Tile expl = tablero[xBomba][yBomba];
                listaExplosiones.add(expl);
                numBombas--;
            }
            else{
                tablero[xBomba][yBomba].setTile(Logica.Colores.rojo, Logica.info.bomba);
            }
        }


    }

    private void tickExplosion(){
        for (Tile expl: listaExplosiones) {

            int posExplX = expl.get_PosMatrizX();
            int posExplY = expl.get_PosMatrizY();

            switch (expl.get_infoTile()){
                case explosion1:
                   tablero[posExplX][posExplY].setTile(Logica.Colores.rojo, Logica.info.explosion2);
                    break;
                case explosion2:
                    tablero[posExplX][posExplY].setTile(Logica.Colores.rojo, Logica.info.explosion3);
                    break;
                case explosion3:
                    tablero[posExplX][posExplY].setTile(Logica.Colores.rojo, Logica.info.nada);
                    break;

            }
        }
    }

    @Override
    public void render() {

        for (int i = 0; i < Ancho_Tablero ; i++) {
            for (int j = 0; j < Alto_Tablero ; j++) {
                tablero[i][j].drawTile(_graphics);
            }
        }
    }

    @Override
    public float getVelocity() {
        return _velocidadActual;
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
