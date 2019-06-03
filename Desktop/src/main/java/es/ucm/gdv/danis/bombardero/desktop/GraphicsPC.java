package es.ucm.gdv.danis.bombardero.desktop;


import javax.swing.*;
import java.awt.*;
import java.io.*;



import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Image;

 class GraphicsPC implements Graphics  {

     java.awt.Graphics g;
     JFrame _frame;

     GraphicsPC(JFrame jf){
         _frame = jf;
     }


    @Override
    //Carga la imagen
    public Image newImage(String name){

        java.awt.Image _img = null;
        Image ret = null;

         try {
             System.out.println(System.getProperty("user.dir"));
             _img = javax.imageio.ImageIO.read( new java.io.File("images/"+name));

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
     public void drawImageFromSpritesheet(Image image, int coordX, int coordY, int tamTileX, int tamTileY, int destX, int destY) {

        if(image != null) {
            ImagePC img = (ImagePC) image;
            java.awt.Image img2draw = img.getImage();
            int tamSprite = 16;

            //The src parameters represent the area of the image to copy and draw. The dst parameters display the area of the destination to cover by the the source area.
            g.drawImage(img2draw, destX, destY, destX + tamTileX, destY + tamTileY,
                    coordX * 16, coordY * 16, ((coordX * 16) + tamSprite), ((coordY * 16) + tamSprite),
                    null);
        }
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
        return _frame.getWidth();
    }
    //Alto de la ventana
    public int getHeight(){
        return _frame.getHeight();
    }

    public void setGraphics(java.awt.Graphics graphics) {
         this.g = graphics;
     }

 }
