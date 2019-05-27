package es.ucm.gdv.danis.bombardero.logica;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;

public class Tile {

    private ResourceManager _resourceManager;

    private Logica.Colores _colorImg; //Seguramente no haga falta
    private Logica.info _infoTile;
    private Sprite _TileSprite;

    private int _PosMatrizX, _PosMatrizY;
    private int _DestPosX, _DestPosY;
    private int  _TamDest;

    public Tile(ResourceManager res, int x, int y, int tamDest, Logica.Colores color, Logica.info info){

        //Multipicamos por 16 para el pintado logico de matriz a pantalla
        _PosMatrizX = (x * 60) - 100;
        _PosMatrizY = y * 60;
        _TamDest = tamDest;

        _resourceManager = res;
        _TileSprite = _resourceManager.GetSpriteAPartirDeAscii(color,  interpretaTipo(info));

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
            case explosion:
                indice =  238;
                break;

        }

        return indice;
    }

    public void drawTile(Graphics graphics, int tamTileX, int tamTileY){

        _TileSprite.drawSprite(graphics, _PosMatrizX , _PosMatrizY, tamTileX, tamTileY);
    }


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

}