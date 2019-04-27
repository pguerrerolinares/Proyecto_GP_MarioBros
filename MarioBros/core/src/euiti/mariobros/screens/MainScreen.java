package euiti.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.CollisionWorld;
import euiti.mariobros.system.MarioBros;
import euiti.mariobros.entities.Player;

public class MainScreen implements Screen {
    private TiledMap map;
    private MarioBros gameMain;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private LayoutScreen layoutScreen;
    private long time = System.currentTimeMillis(); //Tiempo

    private OrthogonalTiledMapRenderer renderer;

    // Colisiones
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    // Mario
    private Player mario;


    // Sprites
    private TextureAtlas textureAtlas = new TextureAtlas("MarioAssets.atlas");


    public MainScreen(MarioBros game) {

        gameMain = game;
        // movimiento
        float PPM = MarioBros.PPM;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.WIDTH / PPM, MarioBros.HEIGHT / PPM
                , gameCam);
        layoutScreen = new LayoutScreen(game.batch);


        world = new World(new Vector2(0, -10), true);

        // backgroup - mapa
        TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load("marioMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);


        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // colisiones

        world = new World(MarioBros.GRAVITY, true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        new CollisionWorld(this);


        mario = new Player(this);
        mario.setPosition(mario.getBody().getPosition().x - mario.getWidth() / 2, mario.getBody().getPosition().y - mario.getHeight() / 2);


    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //CONTADOR
        long currentTime = System.currentTimeMillis();
        if (((currentTime - time) / 1000) == 1) {
            layoutScreen.addTime();
            time = currentTime;
        }


        update(delta);


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        box2DDebugRenderer.render(world, gameCam.combined);

        gameMain.batch.setProjectionMatrix(gameCam.combined);
        gameMain.batch.begin();
        mario.draw(gameMain.batch);
        gameMain.batch.end();


        gameMain.batch.setProjectionMatrix(layoutScreen.stage.getCamera().combined);
        layoutScreen.stage.draw();


    }

    private void update(float dt) {
        handleInput();

        world.step(1 / 60f, 6, 2);

        mario.update(dt);

        gameCam.position.x = mario.getBody().getPosition().x;

        gameCam.update();
        renderer.setView(gameCam);
    }


    private void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            mario.jump();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mario.moveRight();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mario.moveLeft();
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
        map.dispose();
        world.dispose();
        renderer.dispose();
        box2DDebugRenderer.dispose();
        layoutScreen.dispose();
    }

    public World getWorld() {
        return this.world;
    }

    public TiledMap getMap() {
        return this.map;
    }
}
