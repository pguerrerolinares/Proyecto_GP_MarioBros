package euiti.mariobros.entities.items;


import euiti.mariobros.entities.RigidBody;
import euiti.mariobros.screens.MainScreen;

public abstract class Item extends RigidBody {

    String name = "item";

    Item(MainScreen mainScreen, float x, float y) {
        super(mainScreen, x, y);
    }

    public String getName() {
        return name;
    }

    public abstract void use();

}
