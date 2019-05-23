package es.ucm.gdv.danis.bombardero.fachada;

import java.awt.event.MouseEvent;
import java.util.List;

//TODO: REFACTORIZAR EL INPUT

public interface Input {
    List<MouseEvent> getTouchEvents();
}

