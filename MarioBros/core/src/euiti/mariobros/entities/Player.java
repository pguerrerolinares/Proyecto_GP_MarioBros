package euiti.mariobros.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import euiti.mariobros.entities.enemies.Enemy;
import euiti.mariobros.entities.items.Item;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;


public class Player extends RigidBody {
    private Animation<TextureRegion> winningSmall;
    private Animation<TextureRegion> winningBig;
    private boolean slide;
    private boolean winner;


    public enum State {
        STANDING,
        RUNNING,
        JUMPING,
        FALLING,
        GROWING,
        SHRINKING,
        DYING,
        SLIDING,
        WINNING
    }

    private final float radius = 7 / MarioBros.PPM;


    private State currentState;

    private float stateTime;

    private TextureRegion standingSmall;
    private TextureRegion jumpingSmall;
    private Animation<TextureRegion> runningSmall;

    private TextureRegion standingBig;
    private TextureRegion jumpingBig;
    private Animation<TextureRegion> runningBig;

    private TextureRegion dying;
    private Animation<TextureRegion> growing;
    private Animation<TextureRegion> shrinking;


    private boolean facingRight;

    private boolean isGrownUp;
    private boolean isDead;
    private boolean isLevelCompleted;

    private boolean ground;
    private boolean jump;
    private boolean die;
    private boolean growUp;
    private boolean shrink;


    private boolean smallJump = false;

    private float jumpSoundTimer = 0f;


    private AssetManager assetManager;

    public Player(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);
        TextureAtlas textureAtlas = new TextureAtlas("imgs/actores.atlas");

        standingSmall = new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 0, 0, 12, 19);
        standingBig = new TextureRegion(textureAtlas.findRegion("Mariogrande"), 0, 0, 14, 33);

        jumpingSmall = new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 45, 0, 16, 19);
        jumpingBig = new TextureRegion(textureAtlas.findRegion("Mariogrande"), 52, 0, 16, 33);


        // corré
        Array<TextureRegion> keyFrames = new Array<>();
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 14, 0, 12, 19));
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 14 * 2, 0, 14, 19));
        runningSmall = new Animation<>(0.15f, keyFrames);
        keyFrames.clear();

        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 16, 0, 16, 33));
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), (16 * 2) + 2, 0, 16, 33));
        runningBig = new Animation<>(0.15f, keyFrames);
        keyFrames.clear();


        // crecé puto
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 269, 0, 14, 33));
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 285, 0, 14, 33));
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 0, 0, 14, 33));
        }
        growing = new Animation<>(0.09f, keyFrames);

        keyFrames.clear();

        // pixeleate
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 0, 0, 14, 33));
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 285, 0, 14, 33));
        }
        shrinking = new Animation<>(0.1f, keyFrames);
        keyFrames.clear();

        dying = new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 160, 0, 16, 19);

        // ganá
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 178, 0, 16, 19));
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariopequeno"), 196, 0, 14, 19));
        winningSmall = new Animation<>(0.2f, keyFrames);
        keyFrames.clear();

        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 319, 0, 15, 33));
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 301, 0, 16, 32));
        keyFrames.add(new TextureRegion(textureAtlas.findRegion("Mariogrande"), 337, 0, 15, 33));
        winningBig = new Animation<>(0.2f, keyFrames);
        keyFrames.clear();


        setRegion(standingSmall);
        setBounds(getX(), getY(), 14 / MarioBros.PPM, 16 / MarioBros.PPM);

        currentState = State.STANDING;
        stateTime = 0;

        facingRight = true;
        isGrownUp = false;
        jump = false;
        die = false;
        shrink = false;
        growUp = false;

        isLevelCompleted = false;

        assetManager = MarioBros.getAssetManager();
    }


    @Override
    protected void defineBody() {

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());

        body = world.createBody(bodyDef);


        // create colisiones
        defineCollision();
    }

    private void defineCollision() {

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.ENEMY_WEAKNESS_BIT |
                MarioBros.ENEMY_INTERACT_BIT | MarioBros.ENEMY_LETHAL_BIT | MarioBros.ITEM_BIT |
                MarioBros.FLAGPOLE_BIT;

        body.createFixture(fixtureDef).setUserData(this);

        // pie
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-radius, -radius), new Vector2(radius, -radius));
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef).setUserData(this);

        // cabeza
        edgeShape.set(new Vector2(-radius / 6, radius), new Vector2(radius / 6, radius));
        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        edgeShape.dispose();
    }

    private void defineSmallMario() {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);
        body.setLinearVelocity(velocity);

        defineCollision();

    }


    private void handleInput() {
        float maxSpeed = 6.0f;
        float force = 20.0f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            growUp = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && ground) {
            body.applyLinearImpulse(new Vector2(0, 30.0f), body.getWorldCenter(), true);
            jumpSoundTimer = 0;
            jump = true;
            smallJump = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && ground) {
            body.applyLinearImpulse(new Vector2(0, 20.0f), body.getWorldCenter(), true);
            jumpSoundTimer = 0;
            jump = true;
            smallJump = true;
        }


        if (smallJump && jumpSoundTimer > 0.15f) {
            assetManager.get("sound/Jump.ogg", Sound.class).play();
            smallJump = false;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x > -maxSpeed) {
            body.applyForceToCenter(new Vector2(-force, 0), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x < maxSpeed) {
            body.applyForceToCenter(new Vector2(force, 0), true);
        }

    }

    public Vector2 getPosition() {
        return body.getPosition();
    }


    public boolean isDead() {
        return isDead;
    }

    public void killMario() {
        die = true;
    }


    private void handleLevelCompleted() {

        if (slide) {
            facingRight = true;
            body.setTransform(196.0f, body.getPosition().y, 0);
            body.setLinearVelocity(new Vector2(0, -9f));
        } else {
            if (getX() < 200.97f)
                body.applyLinearImpulse(new Vector2(body.getMass() * (4.0f - body.getLinearVelocity().x), 0f), body.getWorldCenter(), true);
        }
    }

    private void checkGrounded() {
        ground = false;

        Vector2 p1;
        Vector2 p2;

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData().getClass() == Player.class) {
                    return 1;
                }

                if (fraction < 1) {
                    ground = true;
                    return 0;
                }
                return 0;
            }
        };

        for (int i = 0; i < 3; i++) {
            p1 = new Vector2(body.getPosition().x - radius * (1 - i), body.getPosition().y - radius);
            p2 = new Vector2(p1.x, p1.y - 0.05f);
            world.rayCast(rayCastCallback, p1, p2);
        }

    }

    @Override
    public void update(float delta) {
        checkGrounded();

        jumpSoundTimer += delta;

        // si cae
        if (body.getPosition().y < -2.0f) {
            die = true;
        }

        if (!isDead && !isLevelCompleted) {
            handleInput();
        } else if (isLevelCompleted) {
            //
            handleLevelCompleted();
        }

        State previousState = currentState;

        if (die) {

            if (!isDead) {
                assetManager.get("sound/die.wav", Sound.class).play();
                body.applyLinearImpulse(new Vector2(0.0f, body.getMass() * (12f - body.getLinearVelocity().y)), body.getWorldCenter(), true);
            }
            isDead = true;
            // do not collide with anything anymore
            for (Fixture fixture : body.getFixtureList()) {
                Filter filter = fixture.getFilterData();
                filter.maskBits = MarioBros.NOTHING_BIT;
                fixture.setFilterData(filter);
            }

            if (stateTime < 0.2f) {
                MarioBros.setTimeScale(0.1f);
            } else {
                MarioBros.setTimeScale(1.0f);
            }

            currentState = State.DYING;
        } else if (shrink) {
            currentState = State.SHRINKING;
            isGrownUp = false;
        } else if (growUp) {
            currentState = State.GROWING;
            isGrownUp = true;
            setBounds(body.getPosition().x, body.getPosition().y, 16 / MarioBros.PPM, 32 / MarioBros.PPM);
        } else if (!ground) {
            if (jump) {
                currentState = State.JUMPING;
            } else {
                currentState = State.FALLING;
            }
        } else if (slide) {
            currentState = State.SLIDING;
        } else if (winner) {
            currentState = State.WINNING;
        } else {
            if (currentState == State.JUMPING) {
                jump = false;
            }
            if (body.getLinearVelocity().x != 0) {
                currentState = State.RUNNING;
            } else {
                currentState = State.STANDING;
            }
        }

        float fastSpeedMax = 12.0f;
        float v = 1.0f + Math.abs(body.getLinearVelocity().x) / fastSpeedMax;
        stateTime = previousState == currentState ? stateTime + delta * v : 0;


        switch (currentState) {
            case DYING:
                setRegion(dying);
                setSize(16 / MarioBros.PPM, 16 / MarioBros.PPM);
                break;

            case SHRINKING:
                setRegion(shrinking.getKeyFrame(stateTime, false));
                // temporarily not collide with enemies
                for (Fixture fixture : body.getFixtureList()) {
                    Filter filter = fixture.getFilterData();
                    filter.maskBits = MarioBros.GROUND_BIT | MarioBros.ITEM_BIT;
                    fixture.setFilterData(filter);
                }

                if (shrinking.isAnimationFinished(stateTime)) {
                    setBounds(body.getPosition().x, body.getPosition().y, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
                    shrink = false;
                    defineSmallMario();
                }
                break;

            case GROWING:
                setRegion(growing.getKeyFrame(stateTime, false));
                if (growing.isAnimationFinished(stateTime)) {
                    growUp = false;
                }
                break;
            case RUNNING:
                if (isGrownUp) {
                    setRegion(runningBig.getKeyFrame(stateTime, true));
                } else {
                    setRegion(runningSmall.getKeyFrame(stateTime, true));
                }
                break;
            case JUMPING:
                if (isGrownUp) {

                    setRegion(jumpingBig);
                } else {
                    setRegion(jumpingSmall);
                }
                break;
            case SLIDING:

                if (stateTime > 1.0f) {
                    slide = false;
                }

                break;
            case WINNING:
                if (stateTime > 0.4f) {
                    if (isGrownUp) {
                        setRegion(winningBig.getKeyFrame(stateTime, true));
                    } else {
                        setRegion(winningSmall.getKeyFrame(stateTime, true));
                    }
                }
                break;
            case FALLING:
            case STANDING:
            default:
                if (isGrownUp) {
                    setRegion(standingBig);
                } else {
                    setRegion(standingSmall);
                }
                break;
        }


        facingRight = checkDirection(facingRight);


        if (body.getPosition().x < 0.5f) {
            body.setTransform(0.5f, body.getPosition().y, 0);
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        } else if (body.getPosition().x > mainScreen.getMapWidth() - 0.5f) {
            body.setTransform(mainScreen.getMapWidth() - 0.5f, body.getPosition().y, 0);
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - radius);
    }

    @Override
    public void onCollide(Collider other) {
        if (other.getFilter().categoryBits == MarioBros.ENEMY_WEAKNESS_BIT) {

            ((Enemy) other.getUserData()).getDamage(1);
            float force = body.getMass() * (8.0f - body.getLinearVelocity().y);
            body.applyLinearImpulse(new Vector2(0.0f, force), body.getWorldCenter(), true);

        } else if (other.getFilter().categoryBits == MarioBros.ENEMY_LETHAL_BIT) {


            if (shrink) {
                return;
            }
            if (!isGrownUp) {
                die = true;
            } else {
                shrink = true;
            }
        } else if (other.getFilter().categoryBits == MarioBros.ENEMY_INTERACT_BIT) {

            ((Enemy) other.getUserData()).interact(this);
            float force = body.getMass() * (8.0f - body.getLinearVelocity().y);
            body.applyLinearImpulse(new Vector2(0.0f, force), body.getWorldCenter(), true);
        } else if (other.getFilter().categoryBits == MarioBros.ITEM_BIT) {
            Item item = (Item) other.getUserData();
            item.use();
            if (item.getName().equals("seta")) {
                if (!isGrownUp) {
                    assetManager.get("sound/power_up.wav", Sound.class).play();
                    growUp = true;
                } else {
                    assetManager.get("sound/1up.wav", Sound.class).play();
                    MarioBros.addLife();
                }
            }

        }
    }

    public void levelCompleted() {
        if (isLevelCompleted) {
            return;
        }

        isLevelCompleted = true;
        slide = true;

        int point = (int) MathUtils.clamp(getY(), 2.0f, 10.0f) * 100;
        MarioBros.addScore(point);

        winner = true;

    }

}
