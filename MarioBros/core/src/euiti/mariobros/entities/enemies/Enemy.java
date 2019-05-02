package euiti.mariobros.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import euiti.mariobros.entities.Player;
import euiti.mariobros.entities.RigidBody;
import euiti.mariobros.screens.MainScreen;

public abstract class Enemy extends RigidBody {

    TextureAtlas textureAtlas;

    boolean active = false;

    Enemy(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);
        this.textureAtlas = mainScreen.getTextureAtlas();
    }

    public abstract void getDamage(int damage);

    public void interact(Player player) {

    }

}
