package euiti.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import euiti.mariobros.screens.MainScreen;

public class MarioBros extends Game {
    private final int WIDTH = 340;
    private final int HEIGHT = 200;
    private static MarioBros myMarioBros;
    private SpriteBatch batch;


    public static MarioBros getMarioBros() {
        if (myMarioBros == null)
            myMarioBros = new MarioBros();
        return myMarioBros;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
