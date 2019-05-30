package es.ucm.gdv.danis.bombardero.logica;

import java.util.LinkedList;
import java.util.List;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class InicioGameState implements GameState {

    //Estados menu
    enum estadoMenu{inicio, dificultad, velocidad}

    estadoMenu _estadoActual;
    Boolean _isStateOver;

    public class Parrafo{
        public Parrafo(LinkedList<String> s, Logica.Colores c){
            cadenas = s;
            color = c;
            tiles = new LinkedList<>();
        } //Los tiles se añaden despues

        public LinkedList<String> cadenas;
        public Logica.Colores color;
        public LinkedList<Tile> tiles;
    }

    //Texto
    LinkedList<Parrafo> parrafos;

    //Atributos
    //TODO: calcular cual es la cadena más larga para saber las dimensiones/escalado
    private static int _TileSizeX, _TileSizeY;
    int _marginY;

    boolean _textosCargados;

    private Game _juego;
    ResourceManager _resourceManager;


    public InicioGameState(ResourceManager res, Game juego){
        _resourceManager = res;
        _juego = juego;

        parrafos = new LinkedList<>();

        _isStateOver = false;
        _textosCargados = false;

        _estadoActual = estadoMenu.inicio;
        initTexto();

    }

    private void calculateTileDimensions(){

        int anchoTexto = 0;
        int altoTexto = 0;

        for (Parrafo parr:parrafos) {
            for (String str:parr.cadenas) {
                if (str.length() > anchoTexto) {
                    anchoTexto = str.length();
                }
                altoTexto++;
            }
        }

        float tileAR = 0.0f;

        _TileSizeX = _juego.GetGraphics().getWidth() / (anchoTexto);
        _TileSizeY = (_juego.GetGraphics().getHeight())/ (altoTexto);

        tileAR = (float)_TileSizeX/(float)_TileSizeY;
        if(tileAR > 1.7f){
            _TileSizeX = (int)(Math.floor((1.7f*_TileSizeY)));

        }
        else if (tileAR < 0.8f){
            _TileSizeY = (int)(Math.floor((_TileSizeX/1.f)));
            _marginY = _juego.GetGraphics().getHeight() -(_TileSizeY*altoTexto);

        }
    }

    private void initTexto(){

        parrafos = new LinkedList<>(); //Clear no funciona como queremos
        LinkedList<String> listaAux = new LinkedList<>();
        Parrafo aux = null;

        switch (_estadoActual){
            case inicio:
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
                break;
            case dificultad:
                listaAux = new LinkedList<>();
                listaAux.add("Elija nivel: 0 (AS) a 5 (principiante)");
                aux = new Parrafo(listaAux, Logica.Colores.rojo);
                parrafos.add(aux);

                listaAux = new LinkedList<>();
                listaAux.add("0 1 2 3 4 5");
                aux = new Parrafo(listaAux, Logica.Colores.blanco);
                parrafos.add(aux);
                break;
            case velocidad:
                listaAux = new LinkedList<>();
                listaAux.add("Elija velocidad: 0 (MAX) a 9 (MIN)");
                aux = new Parrafo(listaAux, Logica.Colores.rojo);
                parrafos.add(aux);

                listaAux = new LinkedList<>();
                listaAux.add("0 1 2 3 4");
                aux = new Parrafo(listaAux, Logica.Colores.blanco);
                parrafos.add(aux);

                listaAux = new LinkedList<>();
                listaAux.add("5 6 7 8 9");
                aux = new Parrafo(listaAux, Logica.Colores.blanco);
                parrafos.add(aux);

                break;
        }

        calculateTileDimensions();
        initTiles();
    }

    private void initTiles(){
        int cont = 0;
        for(int i = 0; i < parrafos.size(); i++) {

            for (String str : parrafos.get(i).cadenas) {
                cont++;

                char[] chars = str.toCharArray();

                for (int j = 0; j < chars.length; j++) {
                    Tile tmpTile = new Tile(_resourceManager, chars[j], parrafos.get(i).color, 0, 0, _TileSizeX * j, _TileSizeY * (i + cont) + _marginY / 2, _TileSizeX, _TileSizeY);

                    parrafos.get(i).tiles.add(tmpTile);
                }
            }
        }

        _textosCargados = true;
    }

    @Override
    public void tick(double elapsedTime) {

        switch (_estadoActual){
            case inicio:
                tickInicio();
                break;
            case dificultad:
                tickDificultad();
                break;
            case velocidad:
                tickVelocidad();
                break;
        }

    }

    private void tickInicio(){
        List<TouchEvent> touchEvents =  _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent:touchEvents) {
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click){
                _estadoActual = estadoMenu.dificultad;
                initTexto();
            }
        }
    }

    private void tickDificultad(){
        List<TouchEvent> touchEvents =  _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent:touchEvents) {
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click){
                _estadoActual = estadoMenu.velocidad;
                initTexto();
            }
        }

    }
    private void tickVelocidad(){
        List<TouchEvent> touchEvents =  _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent:touchEvents) {
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click){
                //_estadoActual = estadoMenu.dificultad;
                //initTexto();
                _isStateOver = true;
            }
        }

    }

    @Override
    public void render() {
        if(_textosCargados) {
            for (Parrafo parr:parrafos) {

                for (int i = 0; i < parr.tiles.size(); i++) {
                    parr.tiles.get(i).drawTile(_juego.GetGraphics());
                }
            }
        }
    }

    @Override
    public float getVelocity() {
        return 500;
    }

    @Override
    public boolean getStateOver() {
        return _isStateOver;
    }

}
