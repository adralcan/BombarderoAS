package es.ucm.gdv.danis.bombardero.desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import es.ucm.gdv.danis.bombardero.fachada.Input;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;


public class InputPC implements Input, MouseListener {
    List<TouchEvent> inputList;
    boolean pulsado;

    public InputPC(JFrame jFrame){
        inputList = new ArrayList<>();
        jFrame.addMouseListener(this);
    }

    @Override
    public synchronized List getTouchEvents() {
        List<TouchEvent> aux;
        synchronized (this) {
            aux = new ArrayList<TouchEvent>(inputList);
            inputList.clear();
        }
        return aux;
    }


    public void mouseClicked(TouchEvent touchEvent) {
        //Click izquierdo
        if(touchEvent.get_touchEvent() == TouchEvent.TouchType.click) {
            inputList.add(touchEvent);  //Ahora tenemos un click almacenado :3
            pulsado = true;
        }
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent(mouseEvent, TouchEvent.TouchType.click);
        synchronized (this){
            inputList.add((touchEvent));
        }
        //pulsado = true?
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent(mouseEvent, TouchEvent.TouchType.release);
        synchronized (this){
            inputList.add((touchEvent));
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

