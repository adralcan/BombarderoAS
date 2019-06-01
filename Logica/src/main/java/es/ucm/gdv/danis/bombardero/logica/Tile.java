package es.ucm.gdv.danis.bombardero.logica;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;

public class Tile {

    private ResourceManager _resourceManager;

    private Logica.info _infoTile;                  //InfoTile
    private Sprite _TileSprite;                     //Sprite asociado al tile
    private char _charTile;                         //Char asociado al tile

    private int _PosMatrizX, _PosMatrizY;           //Posicion en la matriz de Tiles

    private int _coordX, _coordY;
    private int _DestPosX, _DestPosY;
    private int  _TamDestX, _TamDestY;


    public Tile(ResourceManager res, int x , int y, int coordX, int coordY, int tamDestX, int tamDestY,  Logica.Colores color, Logica.info info){

        //Multipicamos por 16 para el pintado logico de matriz a pantalla

        _PosMatrizX = x;
        _PosMatrizY = y;

        _coordX = coordX;
        _coordY = coordY;
        _TamDestX = tamDestX;
        _TamDestY = tamDestY;

        _infoTile = info;

        _resourceManager = res;
        _TileSprite = _resourceManager.GetSpriteAPartirDeAscii(color,  interpretaTipo(info));

    }

    public Tile(ResourceManager res, char c, Logica.Colores color, int x, int y, int coordX, int coordY, int tamX, int tamY){

        _PosMatrizX = x;
        _PosMatrizY = y;
        _coordX = coordX;
        _coordY = coordY;
        _TamDestX = tamX;
        _TamDestY = tamY;

        _resourceManager = res;
        _TileSprite = _resourceManager.GetSpriteAPartirDeAscii(color, (int)c);
        _charTile = c;

    }

    public void setTile(Logica.Colores color, Logica.info info){
        _TileSprite = _resourceManager.GetSpriteAPartirDeAscii(color,  interpretaTipo(info));
        _infoTile = info;
    }
    public void setTile(Logica.Colores color, char c){
        _TileSprite = _resourceManager.GetSpriteAPartirDeAscii(color, (int)c);
    }


    private int interpretaTipo(Logica.info info){

        //TODO: Cargar aqui las posiciones de la imagen en spritesheet en función del tipo
        int indice = 0;
        switch (info) {
            case nada:
                indice = 32;
                break;
            case edificio:
                indice = 233;
                break;
            case tejado:
                indice = 244;
                break;
            case avionMorro:
                indice =  242;
                break;
            case avionCola:
                indice =  241;
                break;
            case bomba:
                indice = 252;
                break;
            case explosion1:
                indice =  188;
                break;
            case explosion2:
                indice =  238;
                break;
            case explosion3:
                indice =  253;
                break;

        }
        return indice;
    }

    public void drawTile(Graphics graphics){

        _TileSprite.drawSprite(graphics, _coordX , _coordY, _TamDestX, _TamDestY);
    }

    //Devuelve true si (x,y) está dentro del tile en la pantalla
    public boolean clickOnTile(int x, int y){
        return ((x > _coordX && x < (_coordX + _TamDestX) && y > _coordY && y < (_coordY+_TamDestY)));
    }

    public char get_charTile(){
        return _charTile;
    }


    //GETTERS
    public int get_PosMatrizX() {
        return _PosMatrizX;
    }

    public int get_PosMatrizY() {
        return _PosMatrizY;
    }

    public int get_DestPosX() {
        return _DestPosX;
    }

    public int get_DestPosY() {
        return _DestPosY;
    }

    public Logica.info get_infoTile() {
        return _infoTile;
    }

}
