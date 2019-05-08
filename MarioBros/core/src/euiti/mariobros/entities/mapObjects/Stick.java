package euiti.mariobros.entities.mapObjects;


import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import euiti.mariobros.entities.Collider;
import euiti.mariobros.entities.Player;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Stick extends MapTileObject {


    public Stick(MainScreen mainScreen, float x, float y, TiledMapTileMapObject mapObject) {
        super(mainScreen, x, y, mapObject);
    }

    @Override
    protected void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / MarioBros.PPM / 2, 16 / MarioBros.PPM / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = MarioBros.FLAGPOLE_BIT;
        fixtureDef.filter.maskBits = MarioBros.MARIO_BIT;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void onTrigger(Collider obj) {
        if (obj.getUserData() instanceof Player) {
            mainScreen.levelCompleted();
            ((Player) obj.getUserData()).levelCompleted();
            
        }

    }
}
