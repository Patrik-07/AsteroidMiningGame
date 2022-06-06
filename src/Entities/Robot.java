package Entities;

import Game.Buildable;
import Game.Game;
import Game.ResourceList;
import Places.Place;
import Resources.Carbon;
import Resources.Iron;
import Resources.Uranium;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Robot extends Entity implements Buildable {
    /**
     * A robot konstruktora.
     * @param place Egy Place referenciája.
     * @param game Egy Game referenciája.
     */
    public Robot(Place place, Game game){
        this.game = game;
    }

    /**
     * Lekérdezi egy robot árát.
     * @return Egy robot árának megfelelő ResourceListet ad vissza
     */
    public ResourceList getPrice() {
        ResourceList price = new ResourceList();
        price.addResource(new Iron());
        price.addResource(new Carbon());
        price.addResource(new Uranium());
        return price;
    }

    /**
     * Meghal a robot, kiveszi a játékból.
     */
    public void die(){
        game.entityDied(this, false);
    }

    /**
     * Robbanáskor egy véletlen szomszédos aszteroidára mozog.
     */
    public void explode(){
        ArrayList<Places.Place> p = place.getNeighbours();
        int randomidx = ThreadLocalRandom.current().nextInt(0, p.size());
        try {
            this.move(p.get(randomidx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newTurn() {
        try {
            while (steps > 0) {
                while (place.getThickness() > 0 && steps > 0) {
                    drill();
                }
                ArrayList<Place> neighbours = place.getNeighbours();
                if (neighbours.size() == 0)
                    steps = 0;
                else
                    move(neighbours.get((int) (Math.random() * (neighbours.size()))));
            }
        }
        catch (Exception e) {

        }
    }

    /**
     * Megépíti a robotot.
     * @param settler az a settler aki a robotot megépíti
     * @return sikerült-e megépíteni a robotot
     */
    @Override
    public boolean build(Settler settler) {
        Place p = settler.getPlace();
        p.addEntity(this);
        place = p;
        this.steps = game.getSteps();
        game.addEntity(this, false);
        return true;
    }

    /**
     * A robot nem tud bányászni, ezért nem csinál semmit.
     */
    @Override
    public void mine(){}

    @Override
    public String toString(){
        return "Robot";
    }
}