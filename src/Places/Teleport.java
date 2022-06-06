package Places;

import Entities.Settler;
import Game.Buildable;
import Game.ResourceList;
import Resources.*;

import java.util.ArrayList;
import java.util.Random;

public class Teleport extends Place implements Buildable {
    private final Teleport pair;
    private boolean isMegkergult = false;

    /**
     * Teleportkapu konstruktora, amely létrehozza a másik teleportkapu-párt és beállítja azt a párjának.
     */
    public Teleport() {
        pair = new Teleport(this);
    }

    /**
     * Konstruktor, amely beállítja párnak a kapott teleportkaput.
     * @param pair Teleportkapu, amit be kell állítani párnak.
     */
    private Teleport(Teleport pair) {
        this.pair = pair;
    }

    /**
     * Visszaad egy ResourceList-et, ami a teleportkapu-pár építéséhez szükséges nyersanyagokat tartalmazza.
     * @return A teleportkapu-pár árát tartalmazó ResourceList.
     */
    @Override
    public ResourceList getPrice() {
        ResourceList price = new ResourceList();
        price.addResource(new Iron());
        price.addResource(new Iron());
        price.addResource(new IceWater());
        price.addResource(new Uranium());
        return price;
    }

    /**
     * Ha a telepesnél nincs teleportkapu, akkor odaadja a telepesnek a teleportkapu-párt.
     * @param settler A telepes, aki éppen építeni akar egy teleportkapu-párt.
     * @return Ha sikerült odaadni a telepesnek a kapu-párt, akkor igazzal tér vissza, amúgy hamissal.
     */
    @Override
    public boolean build(Settler settler) {
        if (settler.getTeleports().size() == 0) {
            settler.getTeleports().add(this);
            settler.getTeleports().add(pair);
            return true;
        }
        return false;
    }

    public Teleport getPair() {
        return pair;
    }

    /**
     * Ha solar storm éri a teleportot akkor megkergül
     * @param depth "mélységi bejárás" mértéke
     */
    public void solarStorm(int depth){
        super.solarStorm(depth);
        isMegkergult = true;
    }

    public void move(){
        if(!isMegkergult)
            return;
        Random random = new Random();
        Place next = neighbours.get(random.nextInt(neighbours.size()));
        for(Place p : neighbours){
            p.removeNeighbour(this);
        }
        neighbours = new ArrayList<Place>();
        for(Place p : next.getNeighbours()){
            neighbours.add(p);
            p.addNeighbour(this);
        }
    }
}
