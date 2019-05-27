package es.ucm.gdv.danis.bombardero.logica;
import es.ucm.gdv.danis.bombardero.fachada.Image;

public class Tile {

    private Logica.Colores _colorImg; //Seguramente no haga falta
    private Logica.info _infoTile;
    private Sprite _TileSprite;

    private int _PosX, _PosY;

    public Tile(int x, int y, Logica.Colores color, Logica.info info){
    //TODO: construir el sprite a partir del interpretacolor, y las posiciones generadas en interpretatipo
    //Es mejor construir el sprite fuera, por el Resource manager se encuenta en los gamestates

    }

    private Image interpretaColor(Logica.Colores color){

        //TODO: cargar aqui las imagenes que va a usar
        Image img = null;
        switch (color){
            case rojo:
                //spriteSheetNegra = juego.GetGraphics().newImage("ASCII_03.png"); //TODO: quitar esto de aqui, ahora está en Tile
                break;
        }

        return img;
    }

    private void interpretaTipo(Logica.info info){

        //TODO: Cargar aqui las posiciones de la imagen en spritesheet en función del tipo
        switch (info) {
            case nada:
                break;
            case edificio:
                break;
            case tejado:
                break;
            case avionMorro:
                break;
            case avionCola:
                break;
            case bomba:
                break;
            case explosion:
                break;

        }
    }


    public int get_PosX() {
        return _PosX;
    }

    public int get_PosY() {
        return _PosY;
    }

}
