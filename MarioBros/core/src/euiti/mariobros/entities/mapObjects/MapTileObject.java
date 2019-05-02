package euiti.mariobros.entities.mapObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import euiti.mariobros.entities.RigidBody;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;


public abstract class MapTileObject extends RigidBody {

    TiledMapTileMapObject mapObject;

    MapTileObject(MainScreen mainScreen, float x, float y, TiledMapTileMapObject mapObject) {
        super(mainScreen, x, y);

        this.mapObject = mapObject;

        setRegion(mapObject.getTextureRegion());

        float width = 16 / MarioBros.PPM;
        float height = 16 / MarioBros.PPM;

        setBounds(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    protected void defineBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / MarioBros.PPM / 2, 16 / MarioBros.PPM / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = MarioBros.GROUND_BIT;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }


}
