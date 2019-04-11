package euiti.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import euiti.mariobros.screens.MainScreen;

public class MarioBros extends Game {
    private final float WIDTH = 400;
    private final float HEIGHT = 208;


    private final float PPM = 100;
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

    public float getWIDTH() {
        return WIDTH;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }

    public float getPPM() {
        return PPM;
    }


    public SpriteBatch getBatch() {
        return batch;
    }
}
