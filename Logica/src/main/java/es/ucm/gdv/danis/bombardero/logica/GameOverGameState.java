package es.ucm.gdv.danis.bombardero.logica;

import java.util.LinkedList;
import java.util.List;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class GameOverGameState implements GameState {

    //Atributos
    private Game _juego;
    ResourceManager _resourceManager;
    Logica _logica;
    Boolean _isStateOver;

    //Texto
    LinkedList<Parrafo> parrafos;
    boolean _textosCargados;


    //Atributos tiles
    private static int _TileSizeX, _TileSizeY;
    private static int _anchoTexto, _altoTexto;

    int _marginX;
    int _marginY;

    public GameOverGameState(ResourceManager res, Game juego, Logica l){
        _resourceManager = res;
        _juego = juego;
        _logica = l;

        parrafos = new LinkedList<>();
        _isStateOver = false;
        _textosCargados = false;

        _marginY = _marginX = 16;

        initTexto();
    }


    private void calculateTileDimensions() {

        _anchoTexto = _altoTexto = 0;

        for (Parrafo parr : parrafos) {
            for (String str : parr.cadenas) {
                if (str.length() > _anchoTexto) {
                    _anchoTexto = str.length();
                }
                _altoTexto++;
            }
        }
        float tileAR = 0.0f;

        _TileSizeX = _juego.GetGraphics().getWidth() / (_anchoTexto);
        _TileSizeY = (_juego.GetGraphics().getHeight()) / (_altoTexto);

        tileAR = (float) _TileSizeX / (float) _TileSizeY;
        if (tileAR > 1.7f) {
            _TileSizeX = (int) (Math.floor((1.7f * _TileSizeY)));
            _marginX = _juego.GetGraphics().getWidth() - (_TileSizeX * _anchoTexto);

        } else if (tileAR < 0.8f) {
            _TileSizeY = (int) (Math.floor((_TileSizeX / 1.f)));
            _marginY = _juego.GetGraphics().getHeight() - (_TileSizeY * _altoTexto);

        }
    }

    public void initTexto(){

        parrafos = new LinkedList<>(); //Clear no funciona como queremos
        LinkedList<String> listaAux = new LinkedList<>();
        Parrafo aux = null;

        listaAux = new LinkedList<>();
        listaAux.add("Has conseguido " + _logica.getPuntosActuales());
        listaAux.add("aaaaaaaaaaaAAAAAAAAAAAA");
        aux = new Parrafo(listaAux, Logica.Colores.rojo);
        parrafos.add(aux);

        calculateTileDimensions();
        initTiles();
    }

    private void initTiles() {
        int contY = 0;
        for (int i = 0; i < parrafos.size(); i++) {

            for (String str : parrafos.get(i).cadenas) {
                contY++;


                char[] chars = str.toCharArray();

                for (int j = 0; j < chars.length; j++) {
                    int coordX = _TileSizeX * j;
                    Tile tmpTile = new Tile(_resourceManager, chars[j], parrafos.get(i).color, i, j, _TileSizeX * j, _TileSizeY * (i + contY) + _marginY / 2, _TileSizeX, _TileSizeY);
                    //Tile tmpTile = new Tile(_resourceManager, i, j, _TileSizeX * j,  _TileSizeY * (i + cont) + _marginY / 2, _TileSizeX, _TileSizeY, Logica.Colores.azulClaro, Logica.info.nada);

                    parrafos.get(i).tiles.add(tmpTile);
                }
            }
        }

        _textosCargados = true;
    }



    @Override
    public void tick(double elapsedTime) {
        List<TouchEvent> touchEvents = _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent : touchEvents) {
            if (touchEvent.get_touchEvent() == TouchEvent.TouchType.click) {
                _isStateOver = true;
            }
        }
    }

    @Override
    public void render() {
        if (_textosCargados) {
            for (Parrafo parr : parrafos) {
                for (int i = 0; i < parr.tiles.size(); i++) {
                    parr.tiles.get(i).drawTile(_juego.GetGraphics());
                }
            }
        }
    }

    @Override
    public float getVelocity() {
        return 0.5f;
    }

    @Override
    public boolean getStateOver() {
        return _isStateOver;
    }
}
