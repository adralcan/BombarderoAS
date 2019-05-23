package es.ucm.gdv.danis.bombardero.fachada;



public class TouchEvent {
    public enum TouchType {
        click, release, drag
    }

    private int _x;
    private int _y;
    private int _inputID;
    private TouchEvent _touchEvent;

    public TouchEvent(int x, int y, TouchEvent touchEvent, int inputID) {
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

    public TouchEvent get_touchEvent() {
        return _touchEvent;
    }
}

