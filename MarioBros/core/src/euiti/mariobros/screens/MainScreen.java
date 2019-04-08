package euiti.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.MarioBrosGP;

public class MainScreen implements Screen {
    // TODO: Tipos de viewport: https://github.com/libgdx/libgdx/wiki/Viewports
    // TODO: Colisión: Box2DDebugRenderer
    private MarioBrosGP gameMain;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private LayoutScreen layoutScreen;
    private World world;

    // backgroup - mapa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public MainScreen(MarioBrosGP marioBrosGP) {
        gameMain = marioBrosGP;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBrosGP.WIDTH, MarioBrosGP.HEIGHT, gameCam);
        layoutScreen = new LayoutScreen(gameMain.getBatch());


        world = new World(new Vector2(0, -10), true);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("core/assets/marioMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);


        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // primero render luego layout, sino render sobreescrite al layout
        renderer.render();

        gameMain.getBatch().setProjectionMatrix(layoutScreen.stage.getCamera().combined);
        layoutScreen.stage.draw();


    }

    public void update(float dt) {
        handleInput(dt);
        gameCam.update();

        renderer.setView(gameCam);
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameCam.position.x += 1000 * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameCam.position.x -= 1000 * dt;
        }

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    }
}
