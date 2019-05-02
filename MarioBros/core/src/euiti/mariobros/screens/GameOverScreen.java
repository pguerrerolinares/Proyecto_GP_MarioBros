package euiti.mariobros.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import euiti.mariobros.system.MarioBros;

/**
 * GameOverScreen
 */
public class GameOverScreen implements Screen {

    private MarioBros game;
    private Stage stage;

    private float countDown;

    public GameOverScreen(Game game) {
        this.game = (MarioBros) game;
        stage = new Stage(new FitViewport(MarioBros.WINDOW_WIDTH / 2, MarioBros.WINDOW_HEIGHT / 2));

        Label gameOverTextLabel = new Label("Game Over", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(gameOverTextLabel).expand();

        stage.addActor(table);

        countDown = 4.5f;

        MarioBros.getAssetManager().finishLoading();
    }

    @Override
    public void show() {
        MarioBros.playMusic("game_over.wav");

    }

    public void update(float delta) {
        countDown -= delta;

        if (countDown < 0.0f) {
            game.setScreen(new MainScreen(game));
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        update(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
