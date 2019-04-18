package euiti.mariobros.system;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import euiti.mariobros.screens.MainScreen;

public class MarioBros extends Game {

    public final static float WIDTH = 300;
    public final static float HEIGHT = 160;

    public final static Vector2 GRAVITY = new Vector2(0, -10);

    public final static float PPM = 100;


    public SpriteBatch batch;


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
        super.dispose();

    }

}
