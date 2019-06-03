package es.ucm.gdv.danis.bombardero.logica;

import java.util.LinkedList;

public class Parrafo{
    public Parrafo(LinkedList<String> s, Logica.Colores c){
        cadenas = s;
        color = c;
        tiles = new LinkedList<>();
    } //Los tiles se añaden despues

    public LinkedList<String> cadenas;
    public Logica.Colores color;
    public LinkedList<Tile> tiles;
}
