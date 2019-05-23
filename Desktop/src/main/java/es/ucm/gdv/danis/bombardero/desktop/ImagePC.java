package es.ucm.gdv.danis.bombardero.desktop;

import es.ucm.gdv.danis.bombardero.fachada.Image;
import javax.swing.*;
import java.awt.*;





public class ImagePC implements Image{

private  java.awt.Image _img;


    public ImagePC( java.awt.Image img)  {
        _img = img;
    }
    @Override
    //devuelve el ancho de la imagen.
    public int getWidth(){
        return _img.getWidth(null);
    };

    @Override
    //devuelve el alto de la imagen.
   public int getHeight(){
        return _img.getHeight(null);
    }

    public java.awt.Image getImage(){return _img;}
}
