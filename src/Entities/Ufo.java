package Entities;

import Game.Game;
import Places.Place;
import java.lang.Math;

import java.util.ArrayList;

public class Ufo extends Entity{

    /**
     * Az ufó konstruktora.
     * @param place Egy Place referenciája.
     * @param game Egy Game referenciája.
     */
    public Ufo(Place place, Game game) {
        super(place, game);
        game.addEntity(this, false);
    }

    /**
     * Azon Place bányászása, ahol éppen tartózkodik az ufó
     */
    @Override
    public void mine() {
        if (steps < 0 || place.getContains() == null || place.getThickness() != 0)
            return;
        place.mine();
        steps--;
    }

    /**
     * Az Entity-beli fúrás metódus felülírása. Az ufó nem tud fúrni, így ez nem csinál semmit.
     */
    @Override
    public void drill() { }

    /**
     * Meghal az ufó, kiveszi a játékból.
     */
    public void die () {
        game.entityDied(this, false);
    }

    /**
     * Robbanás, amely következtében az ufó meghal.
     */
    public void explode () {
        die();
    }

    /**
     * Egy új kör leírása, ufó mögötti logika megvalósítása.
     */
    public void newTurn(){
        while (steps > 0) {
            mine();
            ArrayList<Place> neighbours = place.getNeighbours();
            if (neighbours.size() == 0)
                steps = 0;
            else {
                try {
                    move(neighbours.get((int)(Math.random()*(neighbours.size()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString(){
        return "Ufo";
    }
}
