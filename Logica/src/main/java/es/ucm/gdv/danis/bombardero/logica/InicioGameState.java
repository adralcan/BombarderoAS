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
    private static int _OffsetX, _OffsetY;

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
        calculateTileDimensions();
        initTiles();
    }

    private void calculateTileDimensions(){

        int anchoTexto = 0;
        int altoTexto = cadenasTexto.size();
        for (String str:cadenasTexto) {
            if(str.length() > anchoTexto){
                anchoTexto = str.length();
            }
        }

        _TileSizeX = _juego.GetGraphics().getWidth() / (anchoTexto);
        _TileSizeY = (_juego.GetGraphics().getHeight()-300)/ (altoTexto);

        _OffsetX =  _TileSizeX + (_juego.GetGraphics().getWidth() % (anchoTexto))/ 2;
        _OffsetY =  _TileSizeY + ((_juego.GetGraphics().getHeight()) % (altoTexto))/ 2;

    }

    private void initTexto(){
        switch (_estadoActual){
            case inicio:
                cadenasTexto.clear();
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
                cadenasTexto.clear();
                break;
            case velocidad:
                cadenasTexto.clear();

                break;
        }
    }

    private void initTiles(){
        for(int i = 0; i < cadenasTexto.size(); i++) {
            String str = cadenasTexto.get(i);
            char[] chars = str.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                Tile tmpTile = new Tile(_resourceManager, chars[j], Logica.Colores.verde, 20 + (i*2), 20 + (i*2), _OffsetX * j, _OffsetY*i, _TileSizeX, _TileSizeY);
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
        return 1;
    }

    @Override
    public boolean getStateOver() {
        return _isStateOver;
    }

}
