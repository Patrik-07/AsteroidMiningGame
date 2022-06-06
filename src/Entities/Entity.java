package Entities;

import Game.Game;
import Places.Place;

public abstract class Entity {
    protected Place place;
    protected Game game;
    protected int steps;

    /**
     * Az entitás konstruktora, inicializálja az entitást a paraméterként kapott értékekkel.
     * @param place Annak a helynek a referenciája, ahol az entitás tartózkodik.
     * @param game Egy Game osztály referencia, amit beállít magának.
     */
    public Entity (Place place, Game game) {
        this.place = place;
        this.game = game;
        place.addEntity(this);
    }

    /**
     * Default konstruktor.
     */
    public Entity () { }

    /**
     * Az entitás elmozog egy másik helyre, amelyet ellenőriz először, hogy szomszédos-e.
     * Ha sikerült mozogni, akkor veszít a mozgás pontjaiból.
     * @param place Az a hely, ahova mozogni szeretne.
     */
    public void move(Place place) throws Exception {
        if (steps == 0) {
            throw new Exception("Nincs több lépése a telepesnek.");
        }
        if (this.place.isNeighbour(place)) {
            this.place.removeEntity(this);
            place.addEntity(this);
            this.place = place;
            steps--;
        }else{
            throw new Exception("A telepes egy nem szomszédos aszteroidára akar mozogni.");
        }
    }

    /**
     * Az adott helyen ahol tartózkodik megpróbál fúrni egyet az entitás, ha sikerült, akkor veszít a mozgás pontjaiból.
     */
    public void drill() throws Exception {
        if (steps == 0)
            throw new Exception("A telepesnek nincs több lépése");
        if (place.drill()) {
            steps--;
        }else {
            throw new Exception("Nem lehetséges fúrni");
        }
    }

    /**
     * Megvalósítja a napvihart az entitásokon. Ha az entitás átfúrt, üreges helyen van, akkor túléli, ha nem meghívja a die() függvényét.
     */
    public void solarStormStrike() {
        if (place.getThickness() == 0 && place.getContains() == null) {
            return;
        }
        this.die();
    }

    /**
     * @return Azt a helyet adja vissza ahol az entitás tartózkodik.
     */
    public Place getPlace() { return place; }

    /**
     * Beállítja az entitás lépésszámát a kapott értékre.
     * @param steps Beállítandó lépésszám érték.
     */
    public void setSteps(int steps) {this.steps = steps;}

    public int getSteps() {
        return steps;
    }

    public abstract void die();
    public abstract void explode() throws Exception;
    public abstract void newTurn() throws Exception;
    public abstract void mine() throws Exception;
}
