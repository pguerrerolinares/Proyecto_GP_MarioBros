package euiti.mariobros.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import euiti.mariobros.entities.enemies.Enemy;
import euiti.mariobros.entities.enemies.Goomba;
import euiti.mariobros.entities.mapObjects.*;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class WorldCollision {

    private Array<MapTileObject> mapTileObjects;
    private Array<Enemy> enemies;

    private Vector2 startPosition;

    // TODO: añadir flag final
    private Vector2 flagPosition;

    public WorldCollision(MainScreen mainScreen, TiledMap tiledMap) {

        mapTileObjects = new Array<MapTileObject>();
        enemies = new Array<Enemy>();

        MapLayer mapLayer = tiledMap.getLayers().get("ground");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new Rock(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }

        mapLayer = tiledMap.getLayers().get("metal_block");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new MetalBlock(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }

        mapLayer = tiledMap.getLayers().get("pipe");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new Pipe(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }

        mapLayer = tiledMap.getLayers().get("brick");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new Brick(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }

        mapLayer = tiledMap.getLayers().get("coin_block");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new CoinBlock(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }

        mapLayer = tiledMap.getLayers().get("goomba");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                enemies.add(new Goomba(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM));
            }
        }


        mapLayer = tiledMap.getLayers().get("start");
        if (mapLayer != null) {
            if (mapLayer.getObjects().getCount() > 0) {
                float x = ((TiledMapTileMapObject) mapLayer.getObjects().get(0)).getX();
                float y = ((TiledMapTileMapObject) mapLayer.getObjects().get(0)).getY();

                startPosition = new Vector2(x, y);
            }
        }


        mapLayer = tiledMap.getLayers().get("stick");
        if (mapLayer != null) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                float x = ((TiledMapTileMapObject) mapObject).getX();
                float y = ((TiledMapTileMapObject) mapObject).getY();

                mapTileObjects.add(new Stick(mainScreen, (x + 8) / MarioBros.PPM, (y + 8) / MarioBros.PPM, (TiledMapTileMapObject) mapObject));
            }
        }


        mapLayer = tiledMap.getLayers().get("flag");
        if (mapLayer != null) {
            if (mapLayer.getObjects().getCount() > 0) {
                float x = ((TiledMapTileMapObject) mapLayer.getObjects().get(0)).getX();
                float y = ((TiledMapTileMapObject) mapLayer.getObjects().get(0)).getY();

                flagPosition = new Vector2(x, y);
            }
        }

    }

    public Vector2 getStartPosition() {
        return startPosition;
    }


    public Array<MapTileObject> getMapTileObject() {
        return mapTileObjects;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Vector2 getFlagPosition() {
        return flagPosition;
    }
}
