package euiti.mariobros.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.system.MarioBros;


public class LayoutScreen implements Disposable {

	private boolean helpAux = false;
    private Stage stage;

    private int worldTimer;

    private Label scoreLb;
    private Label countLb;

    private float accumulator;
    
    private Table helpTable;


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
    public void help() {
    	if (helpAux) {
    		helpAux = false;
            printHelpClear();

    	}else {
    		helpAux = true;
            printHelp();
    	}
    }
    
    public void printHelpClear() {
    	helpTable.clear();
    }
    public void printHelp() {
  
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
        
        Label space = new  Label("Espacio", labelStyleContext);
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
