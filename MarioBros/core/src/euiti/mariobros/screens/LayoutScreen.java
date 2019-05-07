package euiti.mariobros.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.system.MarioBros;


public class LayoutScreen implements Disposable {


    private Label lifeCountLb;

    class LifeLayout extends Actor {

        private TextureRegion image;

        LifeLayout() {

            image = new TextureRegion(new TextureAtlas("imgs/actores.atlas")
                    .findRegion("Mariopequeno"), 178, 0, 16, 19);


            setSize(20, 12);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(image, getX(), getY());
        }


    }

    private boolean helpAux = false;
    private Stage stage;

    private int worldTimer;

    private Label scoreLb;
    private Label countLb;

    private float accumulator;

    private Table helpTable;


    LayoutScreen(SpriteBatch batch) {

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
        Label lifeLb = new Label("LIFE", labelStyle);


        scoreLb = new Label("", labelStyle);
        countLb = new Label(Integer.toString(worldTimer), labelStyle);

        int padTop = 8;
        infoLayout.add(marioLb).expandX().padTop(padTop);
        infoLayout.add(worldLb).expandX().padTop(padTop);
        infoLayout.add(timeLb).expandX().padTop(padTop);
        infoLayout.add(lifeLb).expandX().padTop(padTop);

        infoLayout.row();

        infoLayout.add(scoreLb).expandX();
        infoLayout.add(levelLb).expandX();
        infoLayout.add(countLb).expandX();

        stage.addActor(infoLayout);

        accumulator = 0;

        LifeLayout coin = new LifeLayout();

        // contador de vidas
        Table table1 = new Table();
        lifeCountLb = new Label("", labelStyle);
        table1.add(coin);
        table1.add(lifeCountLb);
        infoLayout.add(table1).expandX();
    }


    int getWorldTimer() {
        return worldTimer;
    }


    void draw() {
        scoreLb.setText(Integer.toString(MarioBros.getScore()));
        lifeCountLb.setText(Integer.toString(MarioBros.getLife()));
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

    void help() {
        if (helpAux) {
            helpAux = false;
            printHelpClear();

        } else {
            helpAux = true;
            printHelp();
        }
    }

    private void printHelpClear() {
        helpTable.clear();
    }

    private void printHelp() {

        helpTable = new Table();

        helpTable.top();

        //tamaño del stage
        helpTable.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        Label control = new Label("Control", labelStyle);
        Label description = new Label("Descripcion", labelStyle);

        Label.LabelStyle labelStyleContext = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label dch = new Label("->", labelStyleContext);
        Label movDch = new Label("Movimiento hacia delante", labelStyleContext);

        Label izq = new Label("<-", labelStyleContext);
        Label movIzq = new Label("Movimiento hacia atras", labelStyleContext);

        Label space = new Label("Espacio", labelStyleContext);
        Label movSpace = new Label("Saltar", labelStyleContext);


        int padTop = 45;
        helpTable.add(control).expandX().padTop(padTop);
        helpTable.add(description).expandX().padTop(padTop);

        helpTable.row();
        helpTable.add(dch).expandX();
        helpTable.add(movDch).expandX();

        helpTable.row();
        helpTable.add(izq).expandX();
        helpTable.add(movIzq).expandX();

        helpTable.row();
        helpTable.add(space).expandX();
        helpTable.add(movSpace).expandX();

        stage.addActor(helpTable);
    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
