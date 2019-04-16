package euiti.mariobros;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class CollisionWorld {


    public CollisionWorld(World world, TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        float PPM = MarioBros.PPM;

        // suelo
        // mirar el id del tited map
        int fixId = -1;
        int idGround = 3 + fixId;
        for (MapObject mapObject : map.getLayers().get(idGround).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PPM);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2 / PPM, rectangle.getHeight() / 2 / PPM);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        int idPipe = 4 + fixId;
        for (MapObject mapObject : map.getLayers().get(idPipe).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PPM);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2 / PPM, rectangle.getHeight() / 2 / PPM);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        int idBrick = 6 + fixId;
        for (MapObject mapObject : map.getLayers().get(idBrick).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PPM);
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2 / PPM, rectangle.getHeight() / 2 / PPM);

            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }
    }
}


