package es.ucm.gdv.danis.bombardero.logica;

import java.util.LinkedList;
import java.util.List;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class SettingsGameState implements GameState {

    //Speed and velocity
    private int _dificultad;
    private int _velocidad;

    //Estados menu
        enum estadoSetting{dificultad, velocidad}

        estadoSetting _estadoActual;
        Boolean _isStateOver;

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
        Logica _logica;



        public SettingsGameState(ResourceManager res, Game juego, Logica l){
            _resourceManager = res;
            _juego = juego;
            _logica = l;

            parrafos = new LinkedList<>();

            _isStateOver = false;
            _textosCargados = false;

            _estadoActual = estadoSetting.dificultad;
            _marginY = _marginX = 16;

            initTexto();


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

            _TileSizeX = (_juego.GetGraphics().getWidth()-1) / (_anchoTexto);
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
                case dificultad:
                    listaAux = new LinkedList<>();
                    listaAux.add("Elija nivel: 0 (AS) a 5 (principiante)");
                    aux = new Parrafo(listaAux, Logica.Colores.rojo);
                    parrafos.add(aux);

                    listaAux = new LinkedList<>();
                    listaAux.add("             0 1 2 3 4 5");
                    aux = new Parrafo(listaAux, Logica.Colores.blanco);
                    parrafos.add(aux);
                    break;
                case velocidad:
                    listaAux = new LinkedList<>();
                    listaAux.add("Elija velocidad: 0 (MAX) a 9 (MIN)");
                    aux = new Parrafo(listaAux, Logica.Colores.rojo);
                    parrafos.add(aux);

                    listaAux = new LinkedList<>();
                    listaAux.add("             0 1 2 3 4");
                    aux = new Parrafo(listaAux, Logica.Colores.blanco);
                    parrafos.add(aux);

                    listaAux = new LinkedList<>();
                    listaAux.add("             5 6 7 8 9");
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
                        Tile tmpTile = new Tile(_resourceManager, chars[j], parrafos.get(i).color, i, j, (_TileSizeX *  j) + 15, _TileSizeY * (i + contY) + _marginY / 2, _TileSizeX, _TileSizeY);
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

                case dificultad:
                    tickDificultad();
                    break;
                case velocidad:
                    tickVelocidad();
                    break;
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

                                char c = parrafos.get(i).tiles.get(j).get_charTile();

                                //Restamos por 48 = 0, para que quede el número exacto
                                _dificultad = (int) c - 48;
                                System.out.println(_dificultad);
                                if(_dificultad >= 0) {
                                    stop = true;
                                    _estadoActual = estadoSetting.velocidad;
                                    _logica.setDificultad(_dificultad);
                                    initTexto();
                                }
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
                                System.out.println(_velocidad);
                                if(_velocidad >= 0) {
                                    stop = true;
                                    _logica.setVelocidadMenu(_velocidad);
                                    _isStateOver = true;
                                }
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
            return 0.5f;
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
