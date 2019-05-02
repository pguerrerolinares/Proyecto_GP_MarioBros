package euiti.mariobros.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.system.MarioBros;


public class LayoutScreen implements Disposable {


    private Stage stage;

    private int worldTimer;

    private Label scoreLb;
    private Label countLb;

    private float accumulator;


    public LayoutScreen(SpriteBatch batch) {

        Viewport viewport = new FitViewport(MarioBros.WINDOW_WIDTH, MarioBros.WINDOW_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        worldTimer = 300;

        Table infoLayout = new Table();
        infoLayout.top();

        //tamaño del stage
        infoLayout.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label timeLb = new Label("TIME", labelStyle);
        Label levelLb = new Label("1-1", labelStyle);
        Label worldLb = new Label("WORLD", labelStyle);
        Label marioLb = new Label("MARIO", labelStyle);


        scoreLb = new Label("", labelStyle);
        countLb = new Label(Integer.toString(worldTimer), labelStyle);

        int padTop = 10;
        infoLayout.add(marioLb).expandX().padTop(padTop);
        infoLayout.add(worldLb).expandX().padTop(padTop);
        infoLayout.add(timeLb).expandX().padTop(padTop);

        infoLayout.row();

        infoLayout.add(scoreLb).expandX();
        infoLayout.add(levelLb).expandX();
        infoLayout.add(countLb).expandX();

        stage.addActor(infoLayout);

        accumulator = 0;
    }


    public int getWorldTimer() {
        return worldTimer;
    }


    public void draw() {
        scoreLb.setText(Integer.toString(MarioBros.getScore()));
        stage.draw();

    }

    public void update(float delta) {
        accumulator += delta;

        if (accumulator > 1.0f) {
            if (worldTimer > 0)
                worldTimer -= 1;
            accumulator -= 1.0f;
            countLb.setText(Integer.toString(worldTimer));
        }


        stage.act(delta);

    }


    @Override
    public void dispose() {
        stage.dispose();

    }
}
