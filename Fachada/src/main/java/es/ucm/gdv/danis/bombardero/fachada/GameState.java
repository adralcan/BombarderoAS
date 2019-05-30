package es.ucm.gdv.danis.bombardero.fachada;

//Estado del juego que define la lógica
public interface GameState {

    public void tick(double elapsedTime);

    public void render();

    public float getVelocity();

    public boolean getStateOver();
}
