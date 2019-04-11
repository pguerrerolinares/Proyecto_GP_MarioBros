package euiti.mariobros.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import euiti.mariobros.MarioBros;
import euiti.mariobros.screens.MainScreen;

public class Player extends Sprite {
    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, 32, 32);
    }

    private static World myWorld;

    private Body body;

    private Vector2 centerMass;
    private Vector2 impulseUp;
    private Vector2 impulseRight;
    private Vector2 impulseLeft;

    private TextureRegion marioStand;

    private static Player myPlayer;

    private Sprite sprite;


    public Player() {
        super(new Sprite(new Texture(Gdx.files.internal("core/assets/walk_right1.png"))));
    }

    public static Player getMyPlayer() {
        if (myPlayer == null)
            myPlayer = new Player();
        return myPlayer;
    }

    public void setWorld(MainScreen screen) {
        myWorld = screen.world;

        //Array<TextureRegion> frames = new Array<TextureRegion>();
        //marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"));
        defineMario();
        defineMov();
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(32 / MarioBros.getMarioBros().getPPM(), 32 / MarioBros.getMarioBros().getPPM());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = myWorld.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.getMarioBros().getPPM());
        fixtureDef.filter.categoryBits = 2;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

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
