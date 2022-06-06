package Game;

import Entities.Settler;

public interface Buildable {
    ResourceList getPrice();
    boolean build(Settler settler);
}
