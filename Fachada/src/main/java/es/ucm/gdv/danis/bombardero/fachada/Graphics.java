package es.ucm.gdv.danis.bombardero.fachada;



public interface Graphics {

    Image newImage(String name);                //Carga una imagen
    void clear(int color);                      //Limpia la pantalla, con el color dado por parámetro
    void drawImage(Image image, int x, int y);  //Imagen y coordenadas de pintado

    //Prueba de pintar imagen a traves de un spritesheet
    //The src parameters represent the area of the image to copy and draw.
    //The dst parameters display the area of the destination to cover by the the source
    void drawImageFromSpritesheet(Image image, int x, int y, int tamTileX, int tamTileY, int imgX, int imgY);

    //Getters de la ventana
    int getWidth();                             //Ancho
    int getHeight();                            //Alto


}
