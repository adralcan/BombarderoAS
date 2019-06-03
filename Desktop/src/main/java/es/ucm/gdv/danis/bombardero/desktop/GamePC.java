package es.ucm.gdv.danis.bombardero.desktop;

import java.awt.image.BufferStrategy;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;
import es.ucm.gdv.danis.bombardero.logica.Logica;

import javax.swing.JFrame;

public class GamePC implements Game, Runnable   {

    //Referencias a gráficos e input
    BufferStrategy _bs;
    private GraphicsPC _graphicsPC;
    private InputPC _inputPC;
    private JFrame _frame;
    private Logica _logica;

    //Atributos de pantalla
    private final int _anchoPantalla = 400;
    private final int _altoPantalla = 711;

    //Ciclo de juego
    long lastFrameTime = System.nanoTime();
    long currentTime, nanoElapsedTime;
    double elapsedTime;

    public GamePC() {
        _frame = new JFrame();
        _frame.setVisible(true);
        _frame.setSize(_anchoPantalla, _altoPantalla);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        do {
            _bs = _frame.getBufferStrategy();
            _frame.createBufferStrategy(2);
            _bs = _frame.getBufferStrategy();
        }
        while(_bs == null);

        //Inicializa los motores
        _graphicsPC = new GraphicsPC(_frame);
        _inputPC = new InputPC(_frame);
    }

    @Override
    public Graphics GetGraphics() {
        return _graphicsPC;
    }

    @Override
    public Input GetInput() {
        return _inputPC;
    }

    public void SetLogicaPc(Logica logica){
        _logica = logica;
    }

    @Override
    public void run() {
        while (true){
            //Calculo de frames
            currentTime = System.nanoTime();
            nanoElapsedTime = currentTime - lastFrameTime;
            elapsedTime = (double) nanoElapsedTime / 1E09;
            lastFrameTime = currentTime;

            //Tick de la logica
            _logica.tick(elapsedTime);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Actualizas el graphics que va a usar GraphicsPC
            java.awt.Graphics g = _bs.getDrawGraphics();
            _graphicsPC.setGraphics(g);

            //Clear y render
            _logica.render();
            g.dispose();
            _bs.show();
        }
    }
}


