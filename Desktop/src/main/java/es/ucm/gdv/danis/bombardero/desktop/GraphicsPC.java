package es.ucm.gdv.danis.bombardero.desktop;


import javax.swing.*;
import java.awt.*;
import java.io.*;


import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;

 class GraphicsPC implements Graphics  {

     java.awt.Graphics g;
     JFrame Frame;

     int anchoVentana;
     int altoVentana;

     GraphicsPC(JFrame jf){
         Frame = jf;
         anchoVentana = Frame.getWidth();
         altoVentana = Frame.getHeight();
     }


    @Override
    //Carga la imagen
    public Image newImage(String name){

        java.awt.Image _img = null;
        Image ret = null;

         try {
             _img = javax.imageio.ImageIO.read( new java.io.File(name));
             ret = new ImagePC(_img);

         } catch (IOException e) {
             e.printStackTrace();
         }

        return ret;
    };

    @Override
    //Dibuja una imagen dada
    public void drawImage(Image image, int x, int y){

        ImagePC moc = (ImagePC) image;
        java.awt.Image img2draw = moc.getImage();
        g.drawImage(img2draw, x, y, null);
    }

     @Override
     public void drawImageFromSpritesheet(Image image, int x, int y, int tam, int imgX, int imgY) {
         ImagePC img = (ImagePC) image;
         java.awt.Image img2draw = img.getImage();

         //The src parameters represent the area of the image to copy and draw. The dst parameters display the area of the destination to cover by the the source area.
        g.drawImage(img2draw, x, y, x+tam, y+tam,
                imgX * 16,  imgY * 16, ((imgX * 16) + (img.getWidth() / 16)), ((imgY*16) + (img.getHeight()/ 16)),
                null);
     }


     @Override
    //Pinta movidas
    public  void clear(int color){
        Color rgb = new Color(color);
        g.setColor(rgb);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    //Ancho de la ventana
    public int getWidth(){
        return anchoVentana;
    }
    //Alto de la ventana
    public int getHeight(){
        return altoVentana;
    }

    public void setGraphics(java.awt.Graphics graphics) {
         this.g = graphics;
     }

 }
