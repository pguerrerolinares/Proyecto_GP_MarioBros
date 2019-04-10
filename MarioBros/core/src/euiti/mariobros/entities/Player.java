package euiti.mariobros.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {

    private static World myWorld;

    private Body body;

    private Vector2 centerMass;
    private Vector2 impulseUp;
    private Vector2 impulseRight;
    private Vector2 impulseLeft;

    private static Player myPlayer;

    private Player() {
    }

    public static Player getMyPlayer() {
        if (myPlayer == null)
            myPlayer = new Player();
        return myPlayer;
    }

    public void setWorld(World world) {
        myWorld = world;
        defineMario();
        defineMov();
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32, 32);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = myWorld.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

    }

    private void defineMov() {
        centerMass = this.body.getWorldCenter();
        impulseUp = new Vector2(0, 4f);
        impulseRight = new Vector2(0.1f, 0);
        impulseLeft = new Vector2(-0.1f, 0);
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getCenterMass() {
        return centerMass;
    }

    public Vector2 getImpulseUp() {
        return impulseUp;
    }

    public Vector2 getImpulseRight() {
        return impulseRight;
    }

    public Vector2 getImpulseLeft() {
        return impulseLeft;
    }
}
