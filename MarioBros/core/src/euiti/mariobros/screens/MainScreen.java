package euiti.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import euiti.mariobros.entities.items.Coin;
import euiti.mariobros.entities.mapObjects.Flag;
import euiti.mariobros.entities.Player;
import euiti.mariobros.entities.enemies.Enemy;
import euiti.mariobros.entities.items.Item;
import euiti.mariobros.entities.items.Mushroom;
import euiti.mariobros.entities.items.SpawningItem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import euiti.mariobros.entities.mapObjects.MapTileObject;
import euiti.mariobros.system.MarioBros;
import euiti.mariobros.utils.SGBD;
import euiti.mariobros.utils.WorldCollision;
import euiti.mariobros.utils.WorldContactListener;

import java.io.IOException;

import java.util.LinkedList;


public class MainScreen implements Screen {

    private MarioBros gameMain;

    public World world;

    private float accumulator;

    private OrthographicCamera camera;
    private Viewport viewport;

    private float cameraLeftLimit;
    private float cameraRightLimit;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private float mapWidth;

    private TextureAtlas textureAtlas;

    private Box2DDebugRenderer box2DDebugRenderer;
    private boolean renderB2DDebug;

    private Array<MapTileObject> mapTileObjects;
    private Array<Enemy> enemies;

    private Array<Item> items;
    private LinkedList<SpawningItem> itemSpawnQueue;


    private Player player;

    private LayoutScreen layoutScreen;

    private boolean playMusic;

    private float countDown;

    private Stage levelCompletedStage;
    private boolean levelCompleted = false;
    private boolean flagpoleMusicPlay = false;
    private boolean levelCompletedMusicPlay = false;

    public MainScreen(MarioBros gameMain) {
        this.gameMain = gameMain;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();

        viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT);
        viewport.setCamera(camera);
        camera.position.set(MarioBros.V_WIDTH / 2, MarioBros.V_HEIGHT / 2, 0);


        layoutScreen = new LayoutScreen(gameMain.batch);


        textureAtlas = new TextureAtlas("imgs/actores.atlas");

        // create Box2D world
        world = new World(MarioBros.GRAVITY, true);
        world.setContactListener(new WorldContactListener());

        // load tmx tiled map
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("maps/marioMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MarioBros.PPM);

        mapWidth = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();

        // create world from TmxTiledMap
        WorldCollision worldCollision = new WorldCollision(this, tiledMap);
        mapTileObjects = worldCollision.getMapTileObject();
        enemies = worldCollision.getEnemies();
        player = new Player(this, (worldCollision.getStartPosition().x + 8) / MarioBros.PPM, (worldCollision.getStartPosition().y + 8) / MarioBros.PPM);


        // for spawning item
        items = new Array<Item>();
        itemSpawnQueue = new LinkedList<SpawningItem>();


        accumulator = 0;

        cameraLeftLimit = MarioBros.V_WIDTH / 2;
        cameraRightLimit = mapWidth - MarioBros.V_WIDTH / 2;

        box2DDebugRenderer = new Box2DDebugRenderer();
        renderB2DDebug = false;

        countDown = 3.0f;

        playMusic = true;


        Flag flag = new Flag(this, (worldCollision.getFlagPosition().x - 9) / MarioBros.PPM, worldCollision.getFlagPosition().y / MarioBros.PPM);
        MoveToAction flagSlide = new MoveToAction();
        flagSlide.setPosition((worldCollision.getFlagPosition().x - 9) / MarioBros.PPM, 3);
        flagSlide.setDuration(1.0f);
        flag.addAction(flagSlide);
        levelCompletedStage = new Stage(viewport, gameMain.batch);
        levelCompletedStage.addActor(flag);
        RunnableAction setLevelCompletedScreen = new RunnableAction();
        setLevelCompletedScreen.setRunnable(new Runnable() {
            @Override
            public void run() {

                int score = MarioBros.getScore();
                LinkedList<String[]> listPunt = new LinkedList<String[]>();

                SGBD mydb = SGBD.getMiSGBD();
                try {
                    mydb.insert(score);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    listPunt = mydb.readFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MarioBros.gameOver();

                gameMain.setScreen(new RankingScreen(gameMain, listPunt));
                MarioBros.RANKVISIBLE = true;
                dispose();

            }
        });
        levelCompletedStage.addAction(new SequenceAction(new DelayAction(8.0f), setLevelCompletedScreen));

    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }


    public void addSpawnItem(float x, float y, Class<? extends Item> type) {
        itemSpawnQueue.add(new SpawningItem(x, y, type));
    }

    private void handleSpawningItem() {
        if (itemSpawnQueue.size() > 0) {
            SpawningItem spawningItem = itemSpawnQueue.poll();

            if (spawningItem.type == Mushroom.class) {
                items.add(new Mushroom(this, spawningItem.x, spawningItem.y));
            } else if (spawningItem.type == Coin.class) {
                items.add(new Coin(this, spawningItem.x, spawningItem.y));

            }

        }
    }

    private void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (MarioBros.isPlayingMusic()) {
                MarioBros.pauseMusic();
                playMusic = false;
            } else {
                MarioBros.resumeMusic();
                playMusic = true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            renderB2DDebug = !renderB2DDebug;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            layoutScreen.help();
        }

    }

    private void handleMusic() {
        if (!playMusic) {
            return;
        }

        if (player.isDead()) {
            MarioBros.stopMusic();
        } else if (levelCompleted) {
            if (!flagpoleMusicPlay) {
                MarioBros.playMusic("flagpole.wav", false);
                flagpoleMusicPlay = true;
            } else if (!MarioBros.isPlayingMusic("flagpole.wav")) {
                if (!levelCompletedMusicPlay) {
                    MarioBros.playMusic("flagpole.wav", false);
                    levelCompletedMusicPlay = true;
                }
            }
        } else {
            MarioBros.playMusic("main_loop.ogg");
        }
    }

    public void update(float delta) {
        delta *= MarioBros.timeScale;
        float step = MarioBros.STEP * MarioBros.timeScale;

        handleInput();
        handleSpawningItem();
        handleMusic();

        if (layoutScreen.getWorldTimer() == 0) {
            player.killMario();
        }

        accumulator += delta;
        if (accumulator > step) {
            world.step(step, 8, 3);
            accumulator -= step;
        }

        for (MapTileObject mapTileObject : mapTileObjects) {
            mapTileObject.update(delta);
        }

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        for (Item item : items) {
            item.update(delta);
        }


        player.update(delta);

        float targetX = camera.position.x;
        if (!player.isDead()) {
            targetX = MathUtils.clamp(player.getPosition().x, cameraLeftLimit, cameraRightLimit);
        }

        camera.position.x = MathUtils.lerp(camera.position.x, targetX, 0.1f);
        if (Math.abs(camera.position.x - targetX) < 0.1f) {
            camera.position.x = targetX;
        }
        camera.update();

        mapRenderer.setView(camera);

        layoutScreen.update(delta);


        cleanUpDestroyedObjects();


        if (player.isDead()) {
            countDown -= delta;
            if (countDown < 0) {
                MarioBros.removeLife();
                if (MarioBros.getLife() < 1) {
                    System.out.println(MarioBros.getLife());
                    MarioBros.gameOver();
                    gameMain.setScreen(new GameOverScreen(gameMain));
                    dispose();
                } else {
                    MarioBros.clearScore();
                    gameMain.setScreen(new MainScreen(gameMain));

                }

            }
        }

        if (levelCompleted) {
            levelCompletedStage.act(delta);
        }
    }

    private void cleanUpDestroyedObjects() {

        for (int i = 0; i < items.size; i++) {
            if (items.get(i).isDestroyed()) {
                items.removeIndex(i);
            }
        }

    }

    public Vector2 getMarioPosition() {
        return player.getPosition();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        gameMain.batch.setProjectionMatrix(camera.combined);
        gameMain.batch.begin();

        for (MapTileObject mapTileObject : mapTileObjects) {
            mapTileObject.draw(gameMain.batch);
        }

        for (Item item : items) {
            item.draw(gameMain.batch);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(gameMain.batch);
        }


        player.draw(gameMain.batch);

        gameMain.batch.end();

        layoutScreen.draw();

        if (renderB2DDebug) {
            box2DDebugRenderer.render(world, camera.combined);
        }

        levelCompletedStage.draw();

        update(delta);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        layoutScreen.dispose();
        tiledMap.dispose();
        world.dispose();
        textureAtlas.dispose();
        box2DDebugRenderer.dispose();
        levelCompletedStage.dispose();
    }


    public void levelCompleted() {
        if (levelCompleted) {
            return;
        }
        levelCompleted = true;

    }
}
