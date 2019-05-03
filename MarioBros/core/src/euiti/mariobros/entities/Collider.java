package euiti.mariobros.entities;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;


public class Collider {

    private Fixture fixture;

    public Collider(Fixture fixture) {
        this.fixture = fixture;
    }

    public RigidBody getUserData() {
        return (RigidBody) fixture.getUserData();
    }


    public Filter getFilter() {
        return fixture.getFilterData();
    }


}
