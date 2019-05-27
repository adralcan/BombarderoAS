package es.ucm.gdv.danis.bombardero.logica;

import java.util.ArrayList;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;

/*"ESTADO DE JUEGO" EN EL QUE CARGAMOS LOS RECURSOS, para tenerlos disponibles*/
public class ResourceManager {

    private ArrayList<Image> gameImages;
    private ArrayList<ArrayList<Sprite>>gameSprites;
    private Graphics graphics;

    private int anchoImg, altoImg;

    final int NumImages = 16;
    final int SpritesPorColumna = 16;
    final int SpritesPorFila = 16;


    public ResourceManager(Graphics g) {
        gameImages = new ArrayList<>();
        gameSprites = new ArrayList<ArrayList<Sprite>>();
        graphics = g;
        System.out.println("AAAAAA - RESOURCE MANAGER A TOPE");
        CargaImagenes();
    }

    public Image GetImageFromColor(Logica.Colores color) {
        return null;
    }

    //Carga las imagenes base y establece las constantes del tamaño
    private void CargaImagenes() {
        for (int i = 0; i < NumImages; i++) {
            Image spriteSheet = graphics.newImage("ASCII_" + i + ".png");
            System.out.println("AAAAAA- SPRITESHEET CARGADA NUMERO " + i);
            gameImages.add(spriteSheet);

            CargaSprites(spriteSheet);
        }
    }

    //Crea una matriz de sprites por cada una de las imagenes
    private void CargaSprites(Image img) {
        ArrayList<Sprite> SpritesAux = new ArrayList<>();

        for (int i = 0; i < SpritesPorFila; i++) { //256 iteraciones sobre c/u de los colores
            for (int j = 0; j < SpritesPorColumna; j++) {
                Sprite temp = new Sprite(img, j, i, 16);
                SpritesAux.add(temp);
                //System.out.println("AAAAAA- IMAGEN CARGADA NUMERO " + (i+j))
            }
        }

        gameSprites.add(SpritesAux);
        System.out.println("AAAAAA- TODO TERMINADO Y CARGADO");

    }

    public Sprite GetSpriteAPartirDeAscii(Logica.Colores c, int x){ //TODO: cambiar x e y a un int de numero
        int color = c.ordinal();

        Sprite aaa = gameSprites.get(color).get(x);

        return aaa;
    }
}
