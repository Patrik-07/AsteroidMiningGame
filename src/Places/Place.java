package Places;

import Entities.Entity;
import Game.ResourceList;
import Resources.Resource;

import java.util.ArrayList;

public abstract class Place {
    protected ArrayList<Place> neighbours = new ArrayList<>();
    protected ArrayList<Entity> entities = new ArrayList<>();

    /**
     * Fúrás egy Place objektumon. Itt nem csinál semmit, fúrni csak aszteroidán lehet.
     * @return mindig false lesz.
     */
    public boolean drill() {
        return false;
    }

    /**
     * Bányászni csak aszteroidán lehet.
     * @return Mindig nullával tér vissza.
     */
    public Resource mine() {
        return null;
    }

    /**
     * Nem csinál semmit. Bázist csak aszteroidán lehet építeni.
     * @param resourceList A nyersanyagok, amiket fel akarnak használni a bázis építéséhez.
     */
    public void depositBase(ResourceList resourceList) { }

    /**
     * Hamissal tér vissza. Nyersanyagot lerakni csak aszteroidán lehet.
     * @param resource A nyersanyag, amit a Aszteroida magjába szeretne a Settler elhelyezni.
     * @return  Mindig false Place esetén.
     */
    public boolean addResource(Resource resource) {
        return false;
    }

    /**
     * Hozzáad egy új szomszédot a neighbours listához.
     * @param place Az új szomszéd.
     */
    public void addNeighbour(Place place) {
        if(neighbours.contains(place)) return;
        neighbours.add(place);
    }

    /**
     * Eltöröl egy szomszédot a neighbours listából.
     * @param place A szomszéd akit eltávolít.
     */
    public void removeNeighbour(Place place) {
        neighbours.remove(place);
    }

    /**
     * Megvizsgálja, hogy egy Place szomszédos-e.
     * @param place - Az a Place, akiről megmondja, hogy szomszédos-e.
     * @return Igazzal tér vissza, ha a kapott place benne van a neighbours szomszédsági listában. Hamissal egyébként.
     */
    public boolean isNeighbour(Place place) {
       return neighbours.contains(place);
    }

    /**
     * Új entitás érkezet az adott Place-re. Hozzáadja a kapott entity-t az entities listához.
     * @param entity Az entitás, aki érkezett.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Egy entitás elhagyta a Place-t. Kivonja a kapott entity-t az entities listából.
     * @param entity Az entitás, aki elhagyja az aditt helyet.
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * A kőzetréteg vastagságát adja vissza. kőzetrétege csak az Azteroidának van.
     * @return Mindig -1-el tér vissza.
     */
    public int getThickness() {
        return -1;
    }

    /**
     * Megmondaj, hogy az adott Place tartalmez-e nyersanyagot. Csak Aszteroida tartalmaz nyersanyagot.
     * @return - Mindig null-al tér vissza.
     */
    public Resource getContains() {
        return null;
    }

    /**
     * A szomszédok listáját adja vissza.
     * @return A neighbours listát adja vissza.
     */
    public ArrayList<Place> getNeighbours() {
        return neighbours;
    }

    /**
     * Lekérdezi hogy hány darab entitás van a helyen
     * @return az entities méretét adja vissza
     */
    public int entityCount() { return entities.size();}

    /**
     * Minden az aszteroidán levő entitásra meghívja a napvihar metódusát.
     * Ezután a szomszédaira is meghívja a napvihart, amíg a mélység el nem éri a nullát.
     * @param depth "mélységi bejárás" mértéke
     */
    public void solarStorm(int depth){
        ArrayList<Entity> tempEntities = new ArrayList<>(entities);
        for (Entity entity : tempEntities) {
            entity.solarStormStrike();
        }
        if(depth > 0) {
            for (Place neighbour : neighbours) {
                neighbour.solarStorm(depth - 1);
            }
        }
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
