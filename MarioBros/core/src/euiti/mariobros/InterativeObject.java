package euiti.mariobros;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class InterativeObject {

    World world;

    TiledMap map;

    Rectangle rect;

    Body body;

    MainScreen screen;
    MapObject object;
    Fixture fixture;

    public InterativeObject(MainScreen screen, MapObject object) {
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.rect = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

}
