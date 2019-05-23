package es.ucm.gdv.danis.bombardero.desktop;



import java.awt.image.BufferStrategy;

import es.ucm.gdv.danis.bombardero.fachada.Game;
import es.ucm.gdv.danis.bombardero.fachada.GameState;
import es.ucm.gdv.danis.bombardero.fachada.Graphics;
import es.ucm.gdv.danis.bombardero.fachada.Input;
import javax.swing.JFrame;



public class GamePC implements Game, Runnable   {

    //Referencias a gráficos e input
    private GraphicsPC _graphicsPC;
    BufferStrategy bs;
    private InputPC _inputPC;
    private GameState currentGameState;

    long lastFrameTime = System.nanoTime();
    long currentTime, nanoElapsedTime;
    double elapsedTime;

    public GamePC() {
        JFrame frame = new JFrame();

        //Inicializa los motores
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        bs = frame.getBufferStrategy();
        while(bs == null){
            frame.createBufferStrategy(2);
            bs = frame.getBufferStrategy();
        }

        _graphicsPC = new GraphicsPC(frame);
        _inputPC = new InputPC(frame);
    }

    @Override
    public void setCurrentGameState(GameState curr){
        currentGameState = curr;
    }

    @Override
    public Graphics GetGraphics() {
        return _graphicsPC;
    }

    @Override
    public Input GetInput() {
        //Esto va regular, seguramente por dependencias
        return _inputPC;
    }

    @Override
    public void run() {
        int i = 0;
        while (true){
            //Input->Logica->Pintado

            currentTime = System.nanoTime();
            nanoElapsedTime = currentTime - lastFrameTime;
            elapsedTime = (double) nanoElapsedTime / 1E09;
            lastFrameTime = currentTime;


            currentGameState.tick(elapsedTime);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            java.awt.Graphics g = bs.getDrawGraphics();
            //Actualizas el graphics que va a usar GraphicsPC
            _graphicsPC.setGraphics(g);

            //Clear
            _graphicsPC.clear(Integer.MAX_VALUE);

            currentGameState.render(); //Pintar to-do

            g.dispose();

            bs.show();

            i++;
        }
    }
}


