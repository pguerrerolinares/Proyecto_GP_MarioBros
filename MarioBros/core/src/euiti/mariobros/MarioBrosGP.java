package euiti.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import euiti.mariobros.screens.MainScreen;

public class MarioBrosGP extends Game {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 208;



    private SpriteBatch batch;

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


    public SpriteBatch getBatch() {
        return batch;
    }
}
