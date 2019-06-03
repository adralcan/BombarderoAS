package es.ucm.gdv.danis.bombardero.logica;

import java.util.LinkedList;
import java.util.List;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class InicioGameState implements GameState {

    private Game _juego;
    ResourceManager _resourceManager;
    Logica _logica;

    Boolean _isStateOver;

    //Texto
    LinkedList<Parrafo> parrafos;

    //Atributos
    private static int _TileSizeX, _TileSizeY;
    private static int _anchoTexto, _altoTexto;

    int _marginX;
    int _marginY;

    boolean _textosCargados;


    public InicioGameState(ResourceManager res, Game juego, Logica l) {
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

        _TileSizeX = (_juego.GetGraphics().getWidth()-1) / (_anchoTexto);
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

    private void initTexto() {

        parrafos = new LinkedList<>(); //Clear no funciona como queremos
        LinkedList<String> listaAux = new LinkedList<>();
        Parrafo aux = null;


        listaAux.add("");
        listaAux.add("Usted esta pilotando un avion sobre una");
        listaAux.add("ciudad desierta y tiene que pasar sobre");
        listaAux.add("los edificios para aterrizar y repos-");
        listaAux.add("tar. Su avion se mueve de izquierda a");
        listaAux.add("derecha.");
        listaAux.add(" ");
        listaAux.add("Al llegar a la derecha, el avion vuelve");
        listaAux.add("a salir por la izquierda, pero MAS BAJO.");
        listaAux.add("Dispone de un numero limitado de bombas");
        listaAux.add("y puede hacerlas caer sobre los edifi-");
        listaAux.add("cios pulsando sobre la pantalla.");
        listaAux.add(" ");
        listaAux.add("Cada vez que aterriza, sube la altura");
        listaAux.add("de los edificios y la velocidad.");
        listaAux.add(" ");
        listaAux.add("UNA VEZ DISPARADA UNA BOMBA, YA NO PUEDE");
        listaAux.add("DISPARAR OTRA MIENTRAS NO HAYA EXPLOSIO-");
        listaAux.add("NADO LA PRIMERA!!!!");
        listaAux.add("");
        aux = new Parrafo(listaAux, Logica.Colores.verde);
        parrafos.add(aux);

        listaAux = new LinkedList<>();
        listaAux.add("Pulse para empezar");
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

                    Tile tmpTile = new Tile(_resourceManager, chars[j], parrafos.get(i).color, i, j, (_TileSizeX * j) + 15, _TileSizeY * (i + contY) + _marginY / 2, _TileSizeX, _TileSizeY);
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
        return 1;
    }

    @Override
    public boolean getStateOver() {
        return _isStateOver;
    }
}
