package es.ucm.gdv.danis.bombardero.bombarderopc;


import es.ucm.gdv.danis.bombardero.desktop.GamePC;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.logica.Logica;


public class BombarderoPC {
    public static void main(String[] args){

        GamePC game = new GamePC();
        Logica logica = new Logica(game);

        game.SetLogicaPc(logica);
        game.run();
    }
}
