package es.ucm.gdv.danis.bombardero.desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import es.ucm.gdv.danis.bombardero.fachada.Input;


public class InputPC implements Input, MouseListener {
    List<MouseEvent> inputList;
    boolean pulsado;

    public InputPC(JFrame jFrame){
        inputList = new ArrayList<>();
        jFrame.addMouseListener(this);
    }

    @Override
    public synchronized List getTouchEvents() {
        List<MouseEvent> aux;
        synchronized (this) {
            aux = new ArrayList<>(inputList);
            inputList.clear();
        }
        return aux;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //Click izquierdo
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            inputList.add(mouseEvent);  //Ahora tenemos un click almacenado :3
            pulsado = true;
        }
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

