package euiti.mariobros.entities.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Coin extends Item {


    public Coin(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);

        setRegion(new TextureRegion(mainScreen.getTextureAtlas().findRegion("Monedas"), 14, 0, 13, 15));
        setBounds(body.getPosition().x, body.getPosition().y, 14 / MarioBros.PPM, 14 / MarioBros.PPM);

        setSize(16 / MarioBros.PPM, 16 / MarioBros.PPM);

        body.applyLinearImpulse(new Vector2(0, 12.0f), body.getWorldCenter(), true);
    }

    @Override
    public void use() {

    }

    @Override
    protected void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MarioBros.NOTHING_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void update(float delta) {
        if (!super.checkDestroyed())
            return;

        setPosition(body.getPosition().x - 8 / MarioBros.PPM, body.getPosition().y - 8 / MarioBros.PPM);
    }
}
