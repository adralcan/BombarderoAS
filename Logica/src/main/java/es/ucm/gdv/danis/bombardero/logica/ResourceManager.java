package es.ucm.gdv.danis.bombardero.logica;

import java.util.ArrayList;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Image;

/*"ESTADO DE JUEGO" EN EL QUE CARGAMOS LOS RECURSOS, para tenerlos disponibles*/
public class ResourceManager {

    private Image[] gameImages;
    private Sprite[][] gameSprites;
    private Game juego;

    private int anchoImg, altoImg;

    final int NumImages = 16;
    final int SpritesPorColumna = 16;
    final int SpritesPorFila = 16;


    public ResourceManager(Game j) {
        gameImages = new Image[NumImages];
        gameSprites = new Sprite[NumImages][SpritesPorFila * SpritesPorColumna];
        juego = j;
    }

    public Image GetImageFromColor(Logica.Colores color) {
        return null;
    }

    //Carga las imagenes base y establece las constantes del tamaño
    private void CargaImagenes() {
        for (int i = 0; i < NumImages; i++) {
            Image spriteSheet = juego.GetGraphics().newImage("ASCII_03.png");
            gameImages[i] = spriteSheet;
        }

        anchoImg = gameImages[0].getWidth();
        altoImg = gameImages[0].getHeight();
    }

    //Crea una matriz de sprites por cada una de las imagenes
    private void cargaSprites() {
        for (int i = 0; i < NumImages; i++) {
            for (int j = 0; j < SpritesPorColumna * SpritesPorFila; j++) {
                gameSprites[i][j] = new Sprite(gameImages[i], j*SpritesPorColumna, i*SpritesPorFila, 16);
            }
        }
    }
}
