package Resources;

import Places.Asteroid;

public abstract class Resource {
    protected Asteroid asteroid;
    public abstract void expose();

    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    public String typeString(){
        return toString();
    }
}
