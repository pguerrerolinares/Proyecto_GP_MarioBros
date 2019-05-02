package euiti.mariobros.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import euiti.mariobros.screens.MainScreen;


public abstract class RigidBody extends Sprite {
    protected MainScreen mainScreen;
    protected World world;
    protected Body body;

    protected boolean toBeDestroyed;
    protected boolean destroyed;

    public RigidBody(MainScreen mainScreen, float x, float y) {
        this.mainScreen = mainScreen;
        this.world = mainScreen.world;

        toBeDestroyed = false;
        destroyed = false;

        setPosition(x, y);
        defineBody();
    }

    protected abstract void defineBody();

    public abstract void update(float delta);

    public void onCollide(Collider other) {

    }

    public void onTrigger(Collider other) {

    }

    public void queueDestroy() {
        toBeDestroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    protected boolean checkDestroyed() {
        if (destroyed) {
            return false;
        }

        if (toBeDestroyed) {
            setBounds(0, 0, 0, 0);
            world.destroyBody(body);
            destroyed = true;
            return false;
        }
        return true;
    }

    protected boolean checkDirection(boolean direction) {
        if ((body.getLinearVelocity().x < -0.01f || !direction)) {
            flip(true, false);
            direction = false;
        }

        if (body.getLinearVelocity().x > 0.01f) {
            direction = true;
        }
        return direction;
    }
}
