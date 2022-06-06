package Entities;

import Game.Buildable;
import Game.Game;
import Game.ResourceList;
import Places.Place;
import Places.Teleport;
import Resources.Resource;

import java.util.ArrayList;


public class Settler extends Entity {
    private final ArrayList<Teleport> teleports = new ArrayList<>(2);
    private final ResourceList inventory = new ResourceList();

    /***
     * A Settler konstruktora, inicializálja a Settler a paraméterként kapott értékekkel.
     * @param place Annak a helynek a referenciája, ahol a Settler tartózkodik.
     * @param game Egy Game osztály referencia, amit beállít magának.
     */
    public Settler(Place place, Game game) {
        super(place, game);
        game.addEntity(this, true);
    }

    /**
     * Ezzel a paranccsal tudja a telepes kinyerni a nyersanyagot egy aszteroidából.
     * A nyersanyag automatikusan bekerűl a telepes inventory-ba.
     */
    @Override
    public void mine() throws Exception {
        if (steps != 0 && place.getContains() != null && place.getThickness() == 0 && inventory.resourceCount() < 10) {
            inventory.addResource(place.mine());
            steps--;
        }else{
            if(steps == 0)
                throw new Exception("A telepesnek nincs több lépése");
            if(place.getThickness() != 0)
                throw new Exception("Az aszteroida kérge nem nulla");
            if(place.getContains() == null)
                throw new Exception("Az aszteroida üreges");
            if(inventory.resourceCount() >= 10)
                throw new Exception("A telepesnél nincs több hely");
        }
    }

    /**
     * Ezzel a paranccsal a telepes beleépítí az öszzes olyan nyesanyagát a bázisba ami szükséges.
     */
    public void depositBase() {
        place.depositBase(inventory);
    }

    /**
     * A telepes megépít egy építhető dolgot.
     * @param buildable A dolog amit megépít. pl.: Robot, Teleport
     */
    public void build(Buildable buildable) throws Exception {
        if (inventory.isCompatible(buildable.getPrice())) {
            if (buildable.build(this))
                inventory.subtraction(buildable.getPrice());
        }else{
            throw new Exception("Nincs elég nyersanyag a telepesnél.");
        }
    }

    /**
     * A telepes leteszi a teleportkapu egyik felét annak a helynek a szomszédságába, ahol éppen áll.
     * @param index a lehelyezendő teleport raktérbeli endexe
     */
    public void placeTeleport(int index) throws Exception {
        if (steps == 0)
            return;
        if (teleports.size() == 0) {
            throw new Exception("A telepsenek nincs teleportja, így nincs mit elhelyezni.");
        }
        Teleport t = teleports.get(index);
        game.addTeleport(t);
        for (Place p : place.getNeighbours()){
            t.addNeighbour(p);
            p.addNeighbour(t);
        }
        t.addNeighbour(place);
        place.addNeighbour(t);
        teleports.remove(index);
        steps--;
    }

    /**
     * A telepes visszatesz egy nyersanyagot a saját inventori-ból
     * annak az aszteroidának az üres magjába, amin éppen áll.
     * @param resource A nyersanyag amit vissza szeretne rakni.
     */
    public void putBackResource(Resource resource) throws Exception {
        if (steps != 0 && place.getContains() == null && place.getThickness() == 0 && resource != null) {
            place.addResource(resource);
            ResourceList temp = new ResourceList();
            temp.addResource(resource);
            inventory.subtraction(temp);
            steps--;
        }else{
            if(steps == 0)
                throw new Exception("A telepesnek nincs több lépése");
            if(place.getThickness() != 0)
                throw new Exception("Az aszteroida kérge nem nulla");
            if(place.getContains() != null)
                throw new Exception("Az aszteroida nem üreges");
        }
    }

    /**
     * A telepes meghal.
     */
    @Override
    public void die() {
        game.entityDied(this, true);
        place.removeEntity(this);
    }

    /**
     * Az aszteroida amin a telepes áll felrobbant.
     */
    @Override
    public void explode() {
        die();
    }

    /**
     * Kijelöli a telepest, hogy az ő köre van.
     */
    @Override
    public void newTurn() {
        //ide nem is kell semmi
    }

    /**
     * Visszaadja a telepes nyersanyagait tároló listát.
     * @return A telepes nyersanyaglistája.
     */
    public ResourceList getInventory() {
        return inventory;
    }

    /**
     * Visszaadja a telepesnél lévő teleportok listáját.
     * @return A telepes teleportjainak listája.
     */
    public ArrayList<Teleport> getTeleports() {
        return teleports;
    }

    /**
     * Hozzáad egy teleportot a telepes rakteréhez
     * @param teleport hozzáadandó teleport
     */
    public void addTeleport(Teleport teleport){
        teleports.add(teleport);
    }

    @Override
    public String toString(){
        return "Settler";
    }
}
