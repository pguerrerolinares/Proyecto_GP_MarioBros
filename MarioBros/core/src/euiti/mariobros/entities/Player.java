package euiti.mariobros.entities;


import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.utils.Array;
import euiti.mariobros.system.MarioBros;
import euiti.mariobros.screens.MainScreen;


public class Player extends Sprite {

    private World world;
    private Body body;

    private Vector2 centerMass;
    private Vector2 impulseUp;
    private Vector2 impulseRight;
    private Vector2 impulseLeft;

    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;

    private TextureRegion marioNoMove;

    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private boolean movDirection;
    private float stateTimer;

    public enum State {JUMPING, STANDING, FALLING, RUNNING}

    private State actualState;
    private State prevState;


    public Player(MainScreen screen) {
        movDirection = true;

        this.world = screen.getWorld();


        defineMarioBody();
        defineMov();

        Array<TextureRegion> walkAnimation = new Array<TextureRegion>();
        //walkAnimation.add(new TextureRegion(createRegion(screen, "walk_right1"), 108, 0, 16, 32));
        walkAnimation.add(new TextureRegion(createRegion(screen, "jump_right"), 0, 0, 16, 32));
        walkAnimation.add(new TextureRegion(createRegion(screen, "jump_right"), 39, 2, 16, 32));

        // walkAnimation.add(new TextureRegion(createRegion(screen, "walk_right1"), 261, 2, 35, 60));
        //walkAnimation.add(new TextureRegion(createRegion(screen, "walk_right2"), 76, 2, 35, 60));
        //walkAnimation.add(new TextureRegion(createRegion(screen, "walk_right2"), 76, 2, 35, 60));

        marioRun = new Animation<TextureRegion>(0.1f, walkAnimation);


        marioJump = new TextureRegion(createRegion(screen, "jump_right"), 0, 0, 16, 32);


        marioNoMove = new TextureRegion(createRegion(screen, "still_right"), 0, 0, 16, 32);

        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioNoMove);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(marioNoMove, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private void defineMov() {
        stateTimer = 0;
        actualState = State.STANDING;
        prevState = State.STANDING;

        centerMass = this.body.getWorldCenter();
        impulseUp = new Vector2(0, 4f);
        impulseRight = new Vector2(0.1f, 0);
        impulseLeft = new Vector2(-0.1f, 0);
    }

    private void defineMarioBody() {
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

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(dt));
    }


    private TextureRegion getFrame(float dt) {
        actualState = getActualState();

        TextureRegion region;
        switch (actualState) {
            case JUMPING:
                region = marioJump;
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioNoMove;
                break;
        }


        stateTimer = actualState == prevState ? stateTimer + dt : 0;
        prevState = actualState;
        return region;
    }

    private TextureAtlas.AtlasRegion createRegion(MainScreen screen, String name) {
        return screen.getTextureAtlas().findRegion(name);
    }


    public void jump() {
        if (getActualState() == State.STANDING)
            body.applyLinearImpulse(impulseUp, centerMass, true);
    }


    public void moveRight() {
        if (body.getLinearVelocity().x <= 1.7)
            body.applyLinearImpulse(impulseRight, centerMass, true);
    }

    public void moveLeft() {
        if (body.getLinearVelocity().x >= -1.7)
            body.applyLinearImpulse(impulseLeft, centerMass, true);

    }

    private State getActualState() {
        if (body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }


    public Body getBody() {
        return body;
    }
}
