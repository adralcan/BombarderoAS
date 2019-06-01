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
    enum Estados {
        CINEMATICA, JUEGO, WIN, LOSE
    }

    Estados estadoActual;
    boolean _isStateOver;

    private float _velocidadJuego;
    private int _dificultadJuego;

    private final float velocidadBase = 30.0f;
    private final float velocidadCinematica = 250.0f;

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
    private class Edificio {
        Edificio(int a, int c) {
            _alturaEdificio = a;
            _color = c;
        }

        public int _alturaEdificio;
        public int _color;
    }

    private Edificio[] edificios;
    private ResourceManager _resourceManager = null;

    private final int NumEdificios = 11;
    private Image spriteSheetNegra, javaTest = null;

    //atributos cinematica
    private int i = 0;
    private int j = 22;
    private Random rnd;

    //

    //< TABLERO >
    private Tile[][] tablero;

    private static final int Ancho_Tablero = 20;
    private static final int Alto_Tablero = 25;
    private static final int numeroEdificios = 11;

    private static int _TileSizeX, _TileSizeY;
    private static int _OffsetX, _OffsetY;

    private Game _juego;
    private Graphics _graphics;
    private Logica _logica;

    private boolean gameOver = false;

    public BombarderoGameState(ResourceManager res, Game juego, Logica logica) {
        _resourceManager = res;
        _juego = juego;
        _graphics = _juego.GetGraphics();
        _logica = logica;

        _isStateOver = false;

        tablero = new Tile[Ancho_Tablero][Alto_Tablero];
        estadoActual = Estados.CINEMATICA;
        _velocidadJuego = 1 / velocidadCinematica;


        edificios = new Edificio[NumEdificios];
        listaExplosiones = new LinkedList<>();
        initCuidad();

    }

    //Init de todos los elementos de la ciudad
    private void initCuidad() {
        initMatriz();
        initAlturaEdificios();
        initAvion(xAvion, yAvion);
    }

    void initMatriz() {
        _TileSizeX = _graphics.getWidth() / (Ancho_Tablero);
        _TileSizeY = _graphics.getHeight() / (Alto_Tablero + 2);

        _OffsetX = _TileSizeX + (_graphics.getWidth() % (Ancho_Tablero)) / 2;
        _OffsetY = _TileSizeY + (_graphics.getHeight() % (Alto_Tablero + 10)) / 2;

        Tile edifTemp = null;
        rnd = new Random(); //Para generar alturas aleatorias

        for (int i = 0; i < Ancho_Tablero; i++) {
            for (int j = 0; j < Alto_Tablero; j++) {
                edifTemp = new Tile(_resourceManager, i, j, i * _OffsetX, j * _OffsetY, _TileSizeX, _TileSizeY, Logica.Colores.azulClaro, Logica.info.nada);
                tablero[i][j] = edifTemp;
            }
        }
    }

    void initAlturaEdificios() {
        //Tomamos la dificultad de la logica
        _dificultadJuego = _logica.getDificultad();
        for (int j = 0; j < numeroEdificios; j++) {
            int _alturaEdificio = (5 - _dificultadJuego) + rnd.nextInt(7);
            int _color;
            do {
                _color = rnd.nextInt(16);
            } while (_color == 0);
            edificios[j] = new Edificio(_alturaEdificio, _color);
        }

        initIU();
    }

    void initIU() {
        for (int i = 0; i < Ancho_Tablero; i++) {
            tablero[i][Alto_Tablero - 2].setTile(Logica.Colores.blanco, '_');
        }

        String puntos = "Puntos: ";
        char[] aux = puntos.toCharArray();

        for (int j = 0; j < aux.length; j++) {
            tablero[j][Alto_Tablero - 1].setTile(Logica.Colores.rojo, aux[j]);
        }
    }


    boolean initEdificio(int x, int y) {

        Logica.Colores color = Logica.Colores.values()[edificios[x]._color];
        tablero[x + 4][y].setTile(color, Logica.info.edificio);

        if (y - 1 == 22 - edificios[x]._alturaEdificio) {
            tablero[x + 4][y - 1].setTile(color, Logica.info.tejado);
            return true;
        } else return false;
    }


    void initAvion(int x, int y) {

        //Son dos tiles que siempre van juntos lmao
        //Pero las variables si que las vamos a mantener
        tablero[x - 1][y].setTile(Logica.Colores.rojo, Logica.info.avionCola);
        tablero[x][y].setTile(Logica.Colores.rojo, Logica.info.avionMorro);

    }


    @Override
    public void tick(double elapsedTime) {

        switch (estadoActual) {

            case CINEMATICA:
                //Construye edificio
                if (i < numeroEdificios) {
                    if (edificios[i]._alturaEdificio > 0 && !initEdificio(i, j)) {
                        //Si no ha terminado, resto para subir la altura
                        j--;
                    } else {
                        i++;
                        j = 22;
                    }
                } else {
                    estadoActual = Estados.JUEGO;
                    _velocidadJuego = (_logica.getVelocidad() + 1) / velocidadBase;
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
                _isStateOver = true;
                break;
            case LOSE:
                _isStateOver = true;
                break;
        }
    }

    //Mueve el avión e interpreta colisiones, de haberlas
    //Movimiento horizontal y comprobacion
    private void tickAvion(double elapsedTime) {

        //Comprobar colisiones con la casilla siguiente.
        Tile Avion = tablero[xAvion][yAvion];

        if (tablero[xAvion + 1][yAvion].get_infoTile() == Logica.info.nada) {

            tablero[xAvion][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
            tablero[xAvion - 1][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);

            //Si se sale por la derecha
            if (xAvion + 1 > Ancho_Tablero - 2) {
                xAvion = 3;
                yAvion++;
            } else {
                xAvion++;
            }
            tablero[xAvion][yAvion].setTile(Logica.Colores.rojo, Logica.info.avionMorro);
            tablero[xAvion - 1][yAvion].setTile(Logica.Colores.rojo, Logica.info.avionCola);
        } else {
            if (tablero[xAvion + 1][yAvion].get_infoTile() == Logica.info.tejado || tablero[xAvion + 1][yAvion].get_infoTile() == Logica.info.edificio) {
                tablero[xAvion][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
                tablero[xAvion - 1][yAvion].setTile(Logica.Colores.azulClaro, Logica.info.nada);
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

    private void tickBomba(double elapsedTime) {
        List<TouchEvent> touchEvents = _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent : touchEvents) {
            if (touchEvent.get_touchEvent() == TouchEvent.TouchType.click && numBombas < 1) {
                numBombas++;
                xBomba = xAvion;
                yBomba = yAvion + 1;
                tablero[xBomba][yBomba].setTile(Logica.Colores.rojo, Logica.info.bomba);
            }
        }

        if (numBombas >= 1) {
            //Comprobamos colisiones
            if (tablero[xBomba][yBomba + 1].get_infoTile() == Logica.info.tejado || tablero[xBomba][yBomba + 1].get_infoTile() == Logica.info.edificio) {
                Random rnd = new Random();

                int edificiosDestruidos = 4;//rnd.nextInt(3)+2;
                int i = 1;
                while (i <= edificiosDestruidos && yBomba + i <= 22) {
                    tablero[xBomba][yBomba + i - 1].setTile(Logica.Colores.rojo, Logica.info.nada);
                    tablero[xBomba][yBomba + i].setTile(Logica.Colores.rojo, Logica.info.explosion1);
                    i++;
                }

                //Resgistramos la última explosion
                Tile expl = tablero[xBomba][yBomba + (i - 1)];
                listaExplosiones.add(expl);
                numBombas--;

            }

            else if (yBomba+1 >= 23) {
                tablero[xBomba][yBomba].setTile(Logica.Colores.rojo, Logica.info.explosion1);
                Tile expl = tablero[xBomba][yBomba];
                listaExplosiones.add(expl);
                numBombas--;
            }
            else {
                tablero[xBomba][yBomba].setTile(Logica.Colores.rojo, Logica.info.nada);
                yBomba++;
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
        return _velocidadJuego;
    }

    @Override
    public boolean getStateOver() {
        return _isStateOver;
    }


}
