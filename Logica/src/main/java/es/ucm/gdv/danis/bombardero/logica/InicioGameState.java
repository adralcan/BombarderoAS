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

    //Texto
    LinkedList<Tile> tilesTexto;
    LinkedList<String> cadenasTexto;

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

        tilesTexto = new LinkedList<>();
        cadenasTexto = new LinkedList<>();

        _isStateOver = false;
        _textosCargados = false;

        _estadoActual = estadoMenu.inicio;
        initTexto();

    }

    private void calculateTileDimensions(){

        int anchoTexto = 0;
        int altoTexto = cadenasTexto.size();
        for (String str:cadenasTexto) {
            if(str.length() > anchoTexto){
                anchoTexto = str.length();
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

        cadenasTexto.clear();
        tilesTexto.clear();


        switch (_estadoActual){
            case inicio:
                cadenasTexto.add("");
                cadenasTexto.add("Usted esta pilotando un avion sobre una");
                cadenasTexto.add("ciudad desierta y tiene que pasar sobre");
                cadenasTexto.add("los edificios para aterrizar y repos-");
                cadenasTexto.add("tar. Su avion se mueve de izquierda a");
                cadenasTexto.add("derecha.");
                cadenasTexto.add(" ");
                cadenasTexto.add("Al llegar a la derecha, el avion vuelve");
                cadenasTexto.add("a salir por la izquierda, pero MAS BAJO.");
                cadenasTexto.add("Dispone de un numero limitado de bombas");
                cadenasTexto.add("y puede hacerlas caer sobre los edifi-");
                cadenasTexto.add("cios pulsando sobre la pantalla.");
                cadenasTexto.add(" ");
                cadenasTexto.add("Cada vez que aterriza, sube la altura");
                cadenasTexto.add("de los edificios y la velocidad.");
                cadenasTexto.add(" ");
                cadenasTexto.add("UNA VEZ DISPARADA UNA BOMBA, YA NO PUEDE");
                cadenasTexto.add("DISPARAR OTRA MIENTRAS NO HAYA EXPLOSIO-");
                cadenasTexto.add("NADO LA PRIMERA!!!!");
                cadenasTexto.add("");
                cadenasTexto.add("Pulse para empezar");

                break;
            case dificultad:

                cadenasTexto.add("Elija nivel: 0 (AS) a 5 (principiante)");
                cadenasTexto.add("0 1 2 3 4 5");

                break;
            case velocidad:

                break;
        }

        calculateTileDimensions();
        initTiles();
    }

    private void initTiles(){
        for(int i = 0; i < cadenasTexto.size(); i++) {
            String str = cadenasTexto.get(i);
            char[] chars = str.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                Tile tmpTile = new Tile(_resourceManager, chars[j], Logica.Colores.verde, 0, 0, _TileSizeX * j, _TileSizeY*i +_marginY/2, _TileSizeX, _TileSizeY);
                if(i ==cadenasTexto.size()-1)
                    tmpTile.setTile(Logica.Colores.rojo, chars[j]);
                tilesTexto.add(tmpTile);
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

    }
    private void tickVelocidad(){

    }

    @Override
    public void render() {
        if(_textosCargados) {
            for (int i = 0; i < tilesTexto.size(); i++) {
                tilesTexto.get(i).drawTile(_juego.GetGraphics());
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
