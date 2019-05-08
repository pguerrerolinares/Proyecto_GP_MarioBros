package euiti.mariobros.screens;

import java.util.LinkedList;

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

public class RankingScreen implements Screen {
    private MarioBros game;
    private Stage stage;
    private LinkedList<String[]> listPunt;
    private float countDown;

    RankingScreen(Game game, LinkedList<String[]> listPunt) {
        this.listPunt = listPunt;
        this.game = (MarioBros) game;
        stage = new Stage(new FitViewport(MarioBros.WINDOW_WIDTH*1.5f, MarioBros.WINDOW_HEIGHT*1.5f));

        Table table = new Table();
        table.setFillParent(true);
        table.top();


        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label title = new Label("Ranking", labelStyle);
        table.add(title).expandX();

        table.row();
        Label blanc = new Label("", labelStyle);
        table.add(blanc).expandX();


        for (String[] s : listPunt) {

            table.row();
            Label p = new Label(s[0], labelStyle);
            Label p1 = new Label(s[1], labelStyle);
            table.add(p).expandX();
            table.add(p1).expandX();
        }
        stage.addActor(table);

        countDown = 6f;

    }

    @Override
    public void show() {
        MarioBros.playMusic("game_over.wav");

    }

    public void update(float delta) {

        //System.out.println(countDown);
        if (MarioBros.RANKVISIBLE) {
            countDown -= delta;
            if (countDown < 0.0f) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        } else {
            MarioBros.RANKVISIBLE = true;
            game.setScreen(new RankingScreen(game, this.listPunt));
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
