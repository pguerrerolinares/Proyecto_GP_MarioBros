package euiti.mariobros.entities.mapObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Flag extends Actor {

    Sprite flagSprite;

    public Flag(MainScreen playScreen, float x, float y) {
        flagSprite = new Sprite(new TextureRegion(playScreen.getTextureAtlas().findRegion("bandera"), 0, 0, 16, 16));
        flagSprite.setBounds(x, y, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setBounds(flagSprite.getX(), flagSprite.getY(), flagSprite.getWidth(), flagSprite.getHeight());
    }

    @Override
    protected void positionChanged() {
        flagSprite.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        flagSprite.draw(batch);

    }
}
