package es.ucm.gdv.danis.bombardero.logica;

public class Tile {

    private Logica.Colores _colorImg;
    private Logica.info _infoTile;
    private Sprite _TileSprite;

    public Tile(Logica.Colores color, Logica.info info){
    //TODO: construir el sprite a partir del interpretacolor, y las posiciones generadas en interpretatipo
    }

    private void interpretaColor(Logica.Colores color){

        //TODO: cargar aqui las imagenes que va a usar
        switch (color){
            case rojo:
                break;
            case negro:
                break;
        }
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
}
