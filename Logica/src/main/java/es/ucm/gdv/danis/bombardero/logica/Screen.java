package es.ucm.gdv.danis.bombardero.logica;

import java.awt.Graphics;

import es.ucm.gdv.danis.bombardero.fachada.Game;

//Clase con los utils de la pantalla
public class Screen {

    Game _juego;

    char [][] pantalla;
    private int _filas;
    private int _columnas;

    private int _margenX;
    private int _margenY;

    private static int _TileSizeX, _TileSizeY;
    private static int _OffsetX, _OffsetY;


    public Screen(Game juego){
        _juego = juego;
        _margenX = _margenY = 16;
    }

    public void initScreen(int f, int c){
        _filas = f;
        _columnas = c;
        pantalla = new char[_filas][_columnas];

        for (int i = 0; i < _filas; i++){
            for(int j = 0; j < _columnas; j++){
                pantalla[i][j] = ' ';
            }
        }
    }

    private void setTileDimensions(){
        _TileSizeX = (_juego.GetGraphics().getWidth()-_margenX)/_filas;
        _TileSizeX = (_juego.GetGraphics().getHeight()-_margenY)/_columnas;

    }

/*
    //TODO: Metodo que imprime una serie de caracteres comenzando en una posición
    public void print(String toPrint, int xStart, int yStart, color color) {
        //TODO: Comprobar si la cadena de texto es más grande de lo que soporta la pantalla

        int n = 0;
        int i = yStart;
        int j = xStart;

        while (n < toPrint.length()) {
            if(toPrint.charAt(n) != '\n') {
                grid[i][j] = toPrint.charAt(n);
                _colors[i][j] = color.getValue();
                j++;
                if (j == _cols) {
                    j = 0;
                    i = (i + 1) % _rows;
                }
            }
            else{
                j = 0;
                i = (i + 1) % _rows;
            }

            n++;
        }


    }*/


}
