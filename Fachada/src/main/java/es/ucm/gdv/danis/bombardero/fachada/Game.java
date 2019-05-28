package es.ucm.gdv.danis.bombardero.fachada;


import javax.xml.ws.handler.LogicalHandler;

//?Es public class o es INTERFACE?
public interface Game {

    //Contiene la instancia de Graphics
     Graphics GetGraphics();

    //Contiene la instancia de Input
     Input GetInput();

}
