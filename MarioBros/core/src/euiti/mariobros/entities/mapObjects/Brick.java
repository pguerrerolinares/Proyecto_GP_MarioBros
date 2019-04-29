package euiti.mariobros.entities.mapObjects;

import com.badlogic.gdx.maps.MapObject;
import euiti.mariobros.entities.Player;
import euiti.mariobros.screens.MainScreen;
import euiti.mariobros.system.MarioBros;

public class Brick extends InteractiveObject {
    public Brick(MainScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Player mario) {
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
