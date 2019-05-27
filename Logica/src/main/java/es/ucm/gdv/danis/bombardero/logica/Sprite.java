package es.ucm.gdv.danis.bombardero.logica;

import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;



public class Sprite {

    private Image _imagenSprite;        //Imagen de la que sacas el sprite(color)
    //private Graphics _graphics;         //Los graphics que vas a usar para pintar

    private int posImgX;                //PosX de la imagen en el spritesheet
    private int posImgY;                //PosY de la imagen en el spritesheet
    private int tamImg;                 //Tam de la imagen en el spritesheet(Son cuadrados)


    public Sprite(Image img, int x, int y, int tam){

        _imagenSprite = img;
        posImgX = x;
        posImgY = y;
        tamImg = tam;
    }

    public void drawSprite(Graphics g, int posDestX, int posDestY, int tamTileX, int tamTileY){
        g.drawImageFromSpritesheet(_imagenSprite, posImgX, posImgY, tamTileX, tamTileY, posDestX, posDestY);
    }
}
