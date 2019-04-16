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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.CollisionWorld;
import euiti.mariobros.MarioBros;
import euiti.mariobros.entities.Player;

public class MainScreen implements Screen {

    private MarioBros gameMain;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private LayoutScreen layoutScreen;


    private OrthogonalTiledMapRenderer renderer;

    // Colisiones
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    // Mario
    private Player mario;


    // Sprites
    TextureAtlas textureAtlas;


    public MainScreen(MarioBros game) {

        textureAtlas = new TextureAtlas("core/assets/MarioPacker.atlas");

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
        TiledMap map = mapLoader.load("core/assets/marioMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);


        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // colisiones
        // In Box2D 1 unit = 1 meter.
        int gravityX = 0;
        int gravityY = -10;

        world = new World(new Vector2(gravityX, gravityY), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        new CollisionWorld(world, map);


        mario = new Player(world, this);
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
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        stage.act();
        //      stage.draw();

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
        Vector2 centerMassPlayer = mario.getCenterMass();
        Vector2 movUp = mario.getImpulseUp();
        Vector2 movRight = mario.getImpulseRight();
        Vector2 movLeft = mario.getImpulseLeft();
        Body playerBody = mario.getBody();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            playerBody.applyLinearImpulse(movUp, centerMassPlayer, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playerBody.getLinearVelocity().x <= 2) {
            playerBody.applyLinearImpulse(movRight, centerMassPlayer, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playerBody.getLinearVelocity().x >= -2) {
            playerBody.applyLinearImpulse(movLeft, centerMassPlayer, true);
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
