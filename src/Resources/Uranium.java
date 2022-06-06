package Resources;

public class Uranium extends Resource {
    int exposeCount = 0;

    /**
     * Az urán felrobbantja az aszteroidáját.
     */
    @Override
    public void expose() {
        exposeCount++;
        if (exposeCount == 3)
            asteroid.explosion();
    }

    /**
     * Getter az exposeCount-ra.
     * @return az exposeCount aktuális értéke.
     */
    public int getExposeCount() {
        return exposeCount;
    }

    public void setExposeCount(int exposeCount) {
        this.exposeCount = exposeCount;
    }

    @Override
    public String toString(){
        return ("Uranium exposed " + exposeCount + " times");
    }

    @Override
    public String typeString(){
        return "Uranium";
    }
}
