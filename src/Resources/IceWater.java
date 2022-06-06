package Resources;

public class IceWater extends Resource {
    /**
     * A jégvíz elpárolog az aszteroidája belsejéből.
     */
    @Override
    public void expose() {
        asteroid.setContains(null);
    }

    @Override
    public String toString(){
        return "IceWater";
    }
}
