package euiti.mariobros.entities;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import euiti.mariobros.MarioBros;
import euiti.mariobros.screens.MainScreen;


public class Player extends Sprite {

    private World world;


    private TextureRegion mario;

    private Body body;

    private Vector2 centerMass;
    private Vector2 impulseUp;
    private Vector2 impulseRight;
    private Vector2 impulseLeft;

    public enum State {JUMPING, STANDING}

    public State actualState;


    public Player(World world, MainScreen screen) {
        super(screen.getTextureAtlas().findRegion("walk_right1"));
        this.world = world;


        defineMario();
        defineMov();


        mario = new TextureRegion(getTexture(), 2, 188, 36, 64);

        setBounds(0, 0, 12 / MarioBros.PPM, 24 / MarioBros.PPM);
        setRegion(mario);

        actualState = State.STANDING;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(mario, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private void defineMov() {
        centerMass = this.body.getWorldCenter();
        impulseUp = new Vector2(0, 4f);
        impulseRight = new Vector2(0.1f, 0);
        impulseLeft = new Vector2(-0.1f, 0);
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MarioBros.PPM);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public Body getBody() {
        return body;
    }


    public void jump() {
        if (getActualState() == State.STANDING)
            body.applyLinearImpulse(impulseUp, centerMass, true);
    }

    public void moveRight() {
        if (body.getLinearVelocity().x <= 2)
            body.applyLinearImpulse(impulseRight, centerMass, true);
    }

    public void moveLeft() {
        if (body.getLinearVelocity().x >= -2)
            body.applyLinearImpulse(impulseLeft, centerMass, true);

    }

    public State getActualState() {
        if (body.getLinearVelocity().y > 0 || body.getLinearVelocity().y < 0) {
            return State.JUMPING;
        } else {
            return State.STANDING;
        }
    }
}
