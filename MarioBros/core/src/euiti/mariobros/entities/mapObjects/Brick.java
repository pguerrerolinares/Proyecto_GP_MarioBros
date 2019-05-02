package euiti.mariobros.entities.mapObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import euiti.mariobros.entities.Collider;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Brick extends MapTileObject {

    private final TextureRegion unhitableTextureRegion;
    private boolean hitable;
    private boolean hit;

    private boolean multihit = false;
    private int hitCount = 0;


    private Vector2 originalPosition;
    private Vector2 movablePosition;
    private Vector2 targetPosition;


    public Brick(MainScreen mainScreen, float x, float y, TiledMapTileMapObject mapObject) {
        super(mainScreen, x, y, mapObject);

        originalPosition = new Vector2(x, y);
        movablePosition = new Vector2(x, y + 0.2f);

        targetPosition = originalPosition;

        TiledMap tiledMap = mainScreen.getTiledMap();
        unhitableTextureRegion = tiledMap.getTileSets().getTileSet(0).getTile(28).getTextureRegion();


        if (mapObject.getProperties().containsKey("multihit")) {
            hitCount = Integer.parseInt(mapObject.getProperties().get("multihit", String.class));
            if (hitCount > 0) {
                multihit = true;
            }
        }

        hitable = true;
        hit = false;
    }


    @Override
    public void update(float delta) {
        if (!super.checkDestroyed())
            return;

        float x = body.getPosition().x;
        float y = body.getPosition().y;
        Vector2 dist = new Vector2(x, y).sub(targetPosition);
        if (dist.len2() > 0.0001f) {
            body.setTransform(new Vector2(x, y).lerp(targetPosition, 0.6f), 0);
        } else {
            body.setTransform(targetPosition, 0);
            if (hit || !hitable) {
                hit = false;
                targetPosition = originalPosition;
            }
        }


        if (!hitable) {
            setRegion(unhitableTextureRegion);
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

    }


    @Override
    public void onTrigger(Collider other) {

        if (other.getFilter().categoryBits == MarioBros.MARIO_HEAD_BIT) {

            if (!hitable) {
                MarioBros.getAssetManager().get("sound/bump.wav", Sound.class).play();
                return;
            }

            targetPosition = movablePosition;

            if (multihit) {
                if (hitCount > 0) {
                    MarioBros.getAssetManager().get("sound/coin.wav", Sound.class).play();
                    MarioBros.addScore(200);
                    hitCount--;
                    hit = true;
                } else {
                    hitable = false;
                }
            } else {
                MarioBros.getAssetManager().get("sound/bump.wav", Sound.class).play();
                hit = true;
            }

        }
    }
}
