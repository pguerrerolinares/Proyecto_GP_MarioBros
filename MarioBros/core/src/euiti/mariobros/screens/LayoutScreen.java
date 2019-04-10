package euiti.mariobros.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.MarioBros;

public class LayoutScreen {
    // TODO: https://github.com/libgdx/libgdx/wiki/Table
    Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private int score;

    private Label scoreLb;
    private Label levelLb;
    private Label worldLb;
    private Label marioLb;
    private Label countLb;
    private Label joke;

    public LayoutScreen(SpriteBatch spriteBatch) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        viewport = new FitViewport(MarioBros.getMarioBros().getWIDTH(), MarioBros.getMarioBros().getHEIGHT(), new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table infoLayout = new Table();
        infoLayout.top();

        //tamaño del stage
        infoLayout.setFillParent(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        countLb = new Label(Integer.toString(worldTimer), labelStyle);
        scoreLb = new Label(Integer.toString(score), labelStyle);
        Label timeLb = new Label("TIME", labelStyle);
        levelLb = new Label("1-1", labelStyle);
        worldLb = new Label("WORLD", labelStyle);
        marioLb = new Label("MARIO", labelStyle);
        joke = new Label("FULL KUNVIA", labelStyle);

        final int padTop = 10;

        // repartir de forma equitativa, 33,3% cada uno
        infoLayout.add(marioLb).expandX().padTop(padTop);
        infoLayout.add(worldLb).expandX().padTop(padTop);
        infoLayout.add(timeLb).expandX().padTop(padTop);

        // bajar
        infoLayout.row();
        infoLayout.add(joke).expandX();
        infoLayout.add(levelLb).expandX();
        infoLayout.add(countLb).expandX();


        stage.addActor(infoLayout);
    }
}
