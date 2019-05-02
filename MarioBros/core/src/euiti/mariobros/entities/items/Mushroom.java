package euiti.mariobros.entities.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import euiti.mariobros.entities.Player;
import euiti.mariobros.entities.enemies.Enemy;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;


public class Mushroom extends Item {
    private boolean movingRight;
    private float speed;

    public Mushroom(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);

        setRegion(new TextureRegion(mainScreen.getTextureAtlas().findRegion("seta"), 0, 1, 18, 14));
        setBounds(body.getPosition().x, body.getPosition().y, 14 / MarioBros.PPM, 14 / MarioBros.PPM);

        movingRight = true;
        speed = 3.2f;

        name = "seta";
    }

    private void checkMovingDirection() {

        Vector2 p1;
        Vector2 p2;

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() == this || fixture.getUserData().getClass() == Player.class || fixture.getUserData() instanceof Enemy) {
                    return 1;
                }
                if (fraction < 1.0f) {
                    movingRight = !movingRight;
                }
                return 0;
            }
        };

        if (movingRight) {
            p1 = new Vector2(body.getPosition().x + 6.0f / MarioBros.PPM, body.getPosition().y);
            p2 = new Vector2(p1).add(0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        } else {
            p1 = new Vector2(body.getPosition().x - 6.0f / MarioBros.PPM, body.getPosition().y);
            p2 = new Vector2(p1).add(-0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
    }

    @Override
    public void update(float delta) {
        if (!super.checkDestroyed())
            return;


        checkMovingDirection();

        float velocityY = body.getLinearVelocity().y;
        if (movingRight) {
            body.setLinearVelocity(new Vector2(speed, velocityY));
        } else {
            body.setLinearVelocity(new Vector2(-speed, velocityY));
        }

        setPosition(body.getPosition().x - 8 / MarioBros.PPM, body.getPosition().y - 8 / MarioBros.PPM);
    }

    @Override
    public void use() {
        queueDestroy();
    }

    public void bounce() {
        body.applyLinearImpulse(new Vector2(0.0f, 6.0f), body.getWorldCenter(), true);
    }

    @Override
    protected void defineBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(6.8f / MarioBros.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MarioBros.ITEM_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.MARIO_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-6.8f, -6.8f).scl(1 / MarioBros.PPM), new Vector2(6.8f, -6.8f).scl(1 / MarioBros.PPM));
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef).setUserData(this);

        edgeShape.dispose();
        shape.dispose();
    }
}
