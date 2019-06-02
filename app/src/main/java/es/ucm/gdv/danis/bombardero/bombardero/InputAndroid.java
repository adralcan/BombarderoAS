package es.ucm.gdv.danis.bombardero.bombardero;

//TODO: Implementar lo de la interfaz aqui

import android.view.MotionEvent;
import android.view.View;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import es.ucm.gdv.danis.bombardero.fachada.Input;
import es.ucm.gdv.danis.bombardero.fachada.TouchEvent;

public class InputAndroid implements Input, View.OnTouchListener {

    //Atributos
    LinkedList<TouchEvent> listaEventos;

    public InputAndroid(){
        listaEventos = new LinkedList<>();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            LinkedList<TouchEvent> aux = new LinkedList<>(listaEventos);
            listaEventos.clear();
            return aux;
        }
    }

    public void addEvent(TouchEvent event)
    {
        synchronized (this) {
            listaEventos.add(event);
        }
    }

    public void Clear() {
        listaEventos.clear();
    }

    public boolean onTouch(View view, MotionEvent event)
    {
        TouchEvent tipo = null;
        boolean tocado = false;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                tipo = new TouchEvent((int)event.getX(), (int)event.getY(), TouchEvent.TouchType.release, event.getActionIndex());
                addEvent(tipo);
                tocado = true;
                break;
            case MotionEvent.ACTION_DOWN:
                tipo = new TouchEvent((int)event.getX(), (int)event.getY(), TouchEvent.TouchType.click, event.getActionIndex());
                addEvent(tipo);
                tocado = true;
                break;

        }
        return tocado;
    }


}
