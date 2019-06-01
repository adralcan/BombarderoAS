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
    private static int _TileSizeX, _TileSizeY;
    private static int _anchoTexto, _altoTexto;

    int _marginX;
    int _marginY;

    boolean _textosCargados;

    private Game _juego;
    ResourceManager _resourceManager;

    //Speed and velocity
    private int _dificultad;
    private int _velocidad;


    public InicioGameState(ResourceManager res, Game juego){
        _resourceManager = res;
        _juego = juego;

        parrafos = new LinkedList<>();

        _isStateOver = false;
        _textosCargados = false;

        _estadoActual = estadoMenu.inicio;
        initTexto();

        _marginY = _marginX = 16;
    }

    private void calculateTileDimensions(){

        _anchoTexto = _altoTexto = 0;

        for (Parrafo parr:parrafos) {
            for (String str:parr.cadenas) {
                if (str.length() > _anchoTexto) {
                    _anchoTexto = str.length();
                }
                _altoTexto++;
            }
        }



        float tileAR = 0.0f;

        _TileSizeX = _juego.GetGraphics().getWidth() / (_anchoTexto);
        _TileSizeY = (_juego.GetGraphics().getHeight())/ (_altoTexto);

        tileAR = (float)_TileSizeX/(float)_TileSizeY;
        if(tileAR > 1.7f){
            _TileSizeX = (int)(Math.floor((1.7f*_TileSizeY)));
            _marginX = _juego.GetGraphics().getWidth()-(_TileSizeX*_anchoTexto);

        }
        else if (tileAR < 0.8f){
            _TileSizeY = (int)(Math.floor((_TileSizeX/1.f)));
            _marginY = _juego.GetGraphics().getHeight() -(_TileSizeY*_altoTexto);

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
        int contY = 0;
        for(int i = 0; i < parrafos.size(); i++) {

            for (String str : parrafos.get(i).cadenas) {
                contY++;


                char[] chars = str.toCharArray();

                for (int j = 0; j < chars.length; j++) {
                    int coordX = _TileSizeX * j;
                    if(coordX > _juego.GetGraphics().getWidth()-100){
                    }
                    Tile tmpTile = new Tile(_resourceManager, chars[j], parrafos.get(i).color, i, j, _TileSizeX *  j, _TileSizeY * (i + contY) + _marginY / 2, _TileSizeX, _TileSizeY);
                    //Tile tmpTile = new Tile(_resourceManager, i, j, _TileSizeX * j,  _TileSizeY * (i + cont) + _marginY / 2, _TileSizeX, _TileSizeY, Logica.Colores.azulClaro, Logica.info.nada);

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
                //Empieza en 1 porque el primer parrafo no nos interesa
                for(int i = 1; i < parrafos.size(); i++) {
                    int j = 0;
                    boolean stop = false;
                    while (!stop && j < parrafos.get(i).tiles.size()){
                        if(parrafos.get(i).tiles.get(j).clickOnTile(touchEvent.get_x(), touchEvent.get_y())){
                            stop = true;
                            char c = parrafos.get(i).tiles.get(j).get_charTile();

                            //Restamos por 48 = 0, para que quede el número exacto
                            _dificultad = (int)c - 48;
                            System.out.println("AAAAA - " + _dificultad);

                            _estadoActual = estadoMenu.velocidad;
                            initTexto();
                        }

                        j++;
                    }
                }
            }
        }

    }
    private void tickVelocidad(){
        List<TouchEvent> touchEvents =  _juego.GetInput().getTouchEvents();
        for (TouchEvent touchEvent:touchEvents) {
            if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click){
                //Empieza en 1 porque el primer parrafo no nos interesa
                for(int i = 1; i < parrafos.size(); i++) {
                    int j = 0;
                    boolean stop = false;
                    while (!stop && j < parrafos.get(i).tiles.size()){
                        if(parrafos.get(i).tiles.get(j).clickOnTile(touchEvent.get_x(), touchEvent.get_y())){
                            stop = true;
                            char c = parrafos.get(i).tiles.get(j).get_charTile();

                            //Restamos por 48 = 0, para que quede el número exacto
                            _velocidad = (int)c - 48;
                            System.out.println("AAAAA - " + _velocidad);

                            _isStateOver = true;
                            //initTexto();
                        }

                        j++;
                    }
                }

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

    public int get_dificultad(){
        return _dificultad;
    }

    public int get_velocidad(){
        return _velocidad;
    }
}
