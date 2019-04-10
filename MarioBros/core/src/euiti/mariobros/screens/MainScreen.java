package euiti.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.MarioBros;
import euiti.mariobros.entities.Player;

public class MainScreen implements Screen {
    // TODO: Tipos de viewport: https://github.com/libgdx/libgdx/wiki/Viewports
    // TODO: Colisión: Box2DDebugRenderer
    private MarioBros gameMain;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private LayoutScreen layoutScreen;


    // backgroup - mapa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Colisiones
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    // Mario


    public MainScreen(MarioBros marioBros) {
        gameMain = marioBros;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.getMarioBros().getWIDTH(), MarioBros.getMarioBros().getHEIGHT()
                , gameCam);
        layoutScreen = new LayoutScreen(gameMain.getBatch());


        world = new World(new Vector2(0, -10), true);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("core/assets/marioMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);


        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // colisiones
        // In Box2D 1 unit = 1 meter.
        int gravityX = 0;
        int gravityY = -10;
        world = new World(new Vector2(gravityX, gravityY), true);
        box2DDebugRenderer = new Box2DDebugRenderer();


        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // suelo
        // mirar el id del tited map
        int fixId = -1;
        int idGround = 3 + fixId;
        for (MapObject mapObject : map.getLayers().get(idGround).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        int idPipe = 4 + fixId;
        for (MapObject mapObject : map.getLayers().get(idPipe).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        int idBrick = 6 + fixId;
        for (MapObject mapObject : map.getLayers().get(idBrick).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);

            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }


        // marios ?
        Player.getMyPlayer();
        Player.getMyPlayer().setWorld(world);

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

        // dar colisiones
        box2DDebugRenderer.render(world, gameCam.combined);

        // pintar layout
        gameMain.getBatch().setProjectionMatrix(layoutScreen.stage.getCamera().combined);
        layoutScreen.stage.draw();


    }

    private void update(float dt) {
        handleInput(dt);


        //  Stepping the simulation
        world.step(1 / 60f, 6, 2);


        // mover la camara con marios
        gameCam.position.x = Player.getMyPlayer().getBody().getPosition().x;

        gameCam.update();

        renderer.setView(gameCam);
    }

    private void handleInput(float dt) {
        Vector2 centerMassPlayer = Player.getMyPlayer().getCenterMass();
        Vector2 movUp = Player.getMyPlayer().getImpulseUp();
        Vector2 movRight = Player.getMyPlayer().getImpulseRight();
        Vector2 movLeft = Player.getMyPlayer().getImpulseLeft();
        Body playerBody = Player.getMyPlayer().getBody();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            playerBody.applyLinearImpulse(movUp, centerMassPlayer, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerBody.applyLinearImpulse(movRight, centerMassPlayer, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
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
