package es.ucm.gdv.danis.bombardero.fachada;


import java.awt.event.MouseEvent;

public class TouchEvent {
    public enum TouchType {
        click, release, drag
    }

    private int _x;
    private int _y;
    private int _inputID;
    private TouchType _touchEvent;

    //Constructora de TouchEvent para PC
    public TouchEvent(MouseEvent mouseEvent, TouchType touchType) {
        _x = mouseEvent.getX();
        _y = mouseEvent.getY();
        _touchEvent = touchType;
        _inputID = mouseEvent.getID(); //mouseEvent.getButton()-1?
    }

    //Constructora de TouchEvent para Android
    public TouchEvent(int x, int y, TouchType touchEvent, int inputID) {
        _x = x;
        _y = y;
        _touchEvent = touchEvent;
        _inputID = inputID;
    }

    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }

    public int get_inputID() {
        return _inputID;
    }

    public TouchType get_touchEvent() {
        return _touchEvent;
    }
}

