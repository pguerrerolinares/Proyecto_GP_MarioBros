package euiti.mariobros.entities.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Coin extends Item {

    private Animation<TextureRegion> flipping;
    private float stateTime;

    public Coin(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);
        Array<TextureRegion> keyFrames = new Array<TextureRegion>();
        keyFrames.add(new TextureRegion(mainScreen.getTextureAtlas().findRegion("Monedas"), 0, 0, 14, 16));
        keyFrames.add(new TextureRegion(mainScreen.getTextureAtlas().findRegion("Monedas"), 16, 0, 16, 16));
        keyFrames.add(new TextureRegion(mainScreen.getTextureAtlas().findRegion("Monedas"), 32, 0, 16, 16));
        keyFrames.add(new TextureRegion(mainScreen.getTextureAtlas().findRegion("Monedas"), 48, 0, 16, 16));


        flipping = new Animation<TextureRegion>(0.03f, keyFrames);


        setSize(12 / MarioBros.PPM, 14 / MarioBros.PPM);
        stateTime = 0;

        body.applyLinearImpulse(new Vector2(0, 12.0f), body.getWorldCenter(), true);
    }

    @Override
    public void use() {
    }

    @Override
    protected void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + 0.3f, getY());

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MarioBros.ITEM_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void update(float delta) {
        if (!super.checkDestroyed())
            return;

        stateTime += delta;
        if (stateTime > 0.5f) {
            queueDestroy();
        }
        setRegion(flipping.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - 8 / MarioBros.PPM, body.getPosition().y - 8 / MarioBros.PPM);
    }
}
