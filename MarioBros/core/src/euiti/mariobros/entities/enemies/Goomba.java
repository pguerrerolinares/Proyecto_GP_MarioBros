package euiti.mariobros.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import euiti.mariobros.entities.Player;
import euiti.mariobros.entities.items.Item;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;


public class Goomba extends Enemy {

    private boolean facingRight;

    public enum State {
        WALKING,
        STOMPED,
        DYING,
    }

    private Animation<TextureRegion> walking;
    private float stateTime;

    private boolean movingRight;
    private float speed;

    private State currentState;

    private boolean walk;
    private boolean stomped;
    private boolean die;

    public Goomba(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);

        Array<TextureRegion> keyFrames = new Array<TextureRegion>();

        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("GoombaDerecha"), (72 * i), 0, 20, 24));
        }
        walking = new Animation<TextureRegion>(0.05f, keyFrames);

        setRegion(keyFrames.get(0));
        setBounds(getX() - 8.0f / MarioBros.PPM, getY() - 8.0f / MarioBros.PPM, 16.0f / MarioBros.PPM, 16.0f / MarioBros.PPM);

        movingRight = false;
        speed = 3.2f;
        stateTime = 0;

        facingRight = false;
        walk = true;
        stomped = false;
        die = false;
    }

    private void checkMovingDirection() {
        Vector2 p1;
        Vector2 p2;

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() == this || fixture.getUserData() instanceof Item) {
                    return 1;
                }
                if (fraction < 1.0f && fixture.getUserData().getClass() != Player.class) {
                    movingRight = !movingRight;
                }
                return 0;
            }
        };

        if (movingRight) {
            p1 = new Vector2(body.getPosition().x + 8.0f / MarioBros.PPM, body.getPosition().y);
            p2 = new Vector2(p1).add(0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        } else {
            p1 = new Vector2(body.getPosition().x - 8.0f / MarioBros.PPM, body.getPosition().y);
            p2 = new Vector2(p1).add(-0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
    }

    @Override
    public void update(float delta) {
        if (destroyed) {
            return;
        }

        if (toBeDestroyed) {
            world.destroyBody(body);
            setBounds(0, 0, 0, 0);
            destroyed = true;
            return;
        }

        if (mainScreen.getMarioPosition().x + MarioBros.V_WIDTH / 2 + 4 > body.getPosition().x)
            active = true;

        if (!active) {
            return;
        }

        State previousState = currentState;

        if (stomped) {
            stomped = false;
            currentState = State.STOMPED;
            becomeStomped();

            MarioBros.getAssetManager().get("sound/thwomp.wav", Sound.class).play();
            MarioBros.addScore(250);
        } else if (die) {
            die = false;
            currentState = State.DYING;

            body.applyLinearImpulse(new Vector2(0.0f, 7.2f), body.getWorldCenter(), true);
            becomeDead();

            float cameraX = mainScreen.getCamera().position.x;
            float distanceRatio = (body.getPosition().x - cameraX) / MarioBros.V_WIDTH * 2;
            float pan = MathUtils.clamp(distanceRatio, -1, 1);
            float volume = MathUtils.clamp(2.0f - (float) Math.sqrt(Math.abs(distanceRatio)), 0, 1);
            MarioBros.getAssetManager().get("sound/thwomp.wav", Sound.class).play(volume, 1.0f, pan);

            MarioBros.addScore(250);
        } else if (walk) {
            walk = false;
            currentState = State.WALKING;
        }

        if (previousState != currentState) {
            stateTime = 0;
        }

        switch (currentState) {
            case STOMPED:
                setRegion(new TextureRegion(textureAtlas.findRegion("goombas"), 33, 0, 15, 17));
                if (stateTime > 1.0f) {
                    queueDestroy();
                }
                break;

            case DYING:
                setRegion(walking.getKeyFrame(stateTime, true));
                setFlip(false, true);
                if (stateTime > 2.0f) {
                    queueDestroy();
                }

                break;

            case WALKING:
            default:
                setRegion(walking.getKeyFrame(stateTime, true));
                checkMovingDirection();

                float velocityY = body.getLinearVelocity().y;
                if (movingRight) {
                    body.setLinearVelocity(new Vector2(speed, velocityY));
                } else {
                    body.setLinearVelocity(new Vector2(-speed, velocityY));
                }
                break;
        }


        facingRight = checkDirection(facingRight);

        stateTime += delta;

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }


    private void becomeStomped() {
        Filter filter = new Filter();
        filter.maskBits = MarioBros.GROUND_BIT;
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    private void becomeDead() {
        Filter filter;
        for (Fixture fixture : body.getFixtureList()) {
            filter = fixture.getFilterData();
            filter.categoryBits = MarioBros.NOTHING_BIT;
            filter.maskBits = MarioBros.NOTHING_BIT;
            fixture.setFilterData(filter);
        }
    }

    @Override
    public void getDamage(int damage) {
        if (toBeDestroyed || currentState == State.STOMPED || currentState == State.DYING || !active) {
            return;
        }

        // hit by Player on head
        if (damage == 1) {
            stomped = true;
        } else {
            die = true;
        }

    }

    @Override
    protected void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        // feet
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(
                new Vector2(-7.0f, -7.0f).scl(1 / MarioBros.PPM),
                new Vector2(7.0f, -7.0f).scl(1 / MarioBros.PPM)
        );

        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_LETHAL_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.MARIO_BIT | MarioBros.WEAPON_BIT;
        body.createFixture(fixtureDef).setUserData(this);


        // lethal
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2.0f / MarioBros.PPM);
        circleShape.setPosition(new Vector2(-6, 0).scl(1 / MarioBros.PPM));

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_LETHAL_BIT;
        fixtureDef.filter.maskBits = MarioBros.MARIO_BIT | MarioBros.WEAPON_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        circleShape.setPosition(new Vector2(6, 0).scl(1 / MarioBros.PPM));
        body.createFixture(fixtureDef).setUserData(this);

        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertice);

        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_WEAKNESS_BIT;
        fixtureDef.filter.maskBits = MarioBros.MARIO_BIT | MarioBros.WEAPON_BIT;

        body.createFixture(fixtureDef).setUserData(this);

        circleShape.dispose();
        edgeShape.dispose();
        polygonShape.dispose();

    }

}
