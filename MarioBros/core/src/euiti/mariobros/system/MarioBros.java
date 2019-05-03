package euiti.mariobros.system;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import euiti.mariobros.screens.MainScreen;

public class MarioBros extends Game {
    public SpriteBatch batch;

    public static final float PPM = 16;

    public static final int WINDOW_WIDTH = 300;
    public static final int WINDOW_HEIGHT = 160;

    public static final float V_WIDTH = 20.0f;
    public static final float V_HEIGHT = 15.0f;

    public static final Vector2 GRAVITY = new Vector2(0.0f, -9.8f * 4);

    public static final float STEP = 1 / 60.0f;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short MARIO_HEAD_BIT = 4;
    public static final short ENEMY_LETHAL_BIT = 8;
    public static final short ENEMY_WEAKNESS_BIT = 16;
    public static final short ENEMY_INTERACT_BIT = 32;
    public static final short ITEM_BIT = 64;
    public static final short WEAPON_BIT = 128;
    public static final short FLAGPOLE_BIT = 256;

    private static final String musicPath = "sound/";

    private static AssetManager assetManager;

    private static int score;

    public static float timeScale = 1;


    @Override
    public void create() {
        batch = new SpriteBatch();

        assetManager = new AssetManager();

        loadAudio();

        score = 0;

        setScreen(new MainScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }


    private void loadAudio() {
        assetManager.load("sound/main_loop.ogg", Music.class);
        assetManager.load("sound/bigjump.wav", Music.class);
        assetManager.load("sound/jump.wav", Music.class);
        assetManager.load("sound/game_over.wav", Music.class);
        assetManager.load("sound/flagpole.wav", Music.class);
        assetManager.load("sound/break.wav", Sound.class);
        assetManager.load("sound/bump.wav", Sound.class);
        assetManager.load("sound/coin.wav", Sound.class);
        assetManager.load("sound/die.wav", Sound.class);
        assetManager.load("sound/power_up.wav", Sound.class);
        assetManager.load("sound/item.wav", Sound.class);

        assetManager.load("sound/thwomp.wav", Sound.class);
        assetManager.load("sound/kick.wav", Sound.class);

        assetManager.finishLoading();
    }


    public static void gameOver() {
        clearScore();
    }

    public static int getScore() {
        return score;
    }

    private static void clearScore() {
        score = 0;
    }

    public static void addScore(int value) {
        score += value;
    }

    public static void setTimeScale(float value) {
        timeScale = MathUtils.clamp(value, 0.0f, 2.0f);
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    private static String currentMusic = "";

    public static void playMusic(String filename) {
        playMusic(filename, true);
    }

    public static void playMusic(String filename, boolean loop) {
        if (!currentMusic.equals(filename)) {
            stopMusic();
            currentMusic = filename;
        }

        if (isPlayingMusic(currentMusic)) {
            return;
        }
        assetManager.get(musicPath + filename, Music.class).setLooping(loop);
        assetManager.get(musicPath + filename, Music.class).play();
    }

    public static boolean isPlayingMusic() {
        return isPlayingMusic(currentMusic);
    }

    public static void pauseMusic() {
        if (currentMusic.length() > 0) {
            assetManager.get(musicPath + currentMusic, Music.class).pause();
        }
    }

    public static void resumeMusic() {
        if (currentMusic.length() > 0) {
            if (!isPlayingMusic(currentMusic)) {
                playMusic(currentMusic);
            }
        }
    }

    public static void stopMusic() {
        if (currentMusic.length() > 0) {
            assetManager.get(musicPath + currentMusic, Music.class).stop();
        }
    }

    public static boolean isPlayingMusic(String filename) {
        return assetManager.get(musicPath + filename, Music.class).isPlaying();
    }


    @Override
    public void dispose() {
        assetManager.dispose();
    }

}
