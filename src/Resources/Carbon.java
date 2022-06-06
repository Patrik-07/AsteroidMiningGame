package Resources;

public class Carbon extends Resource {
    /**
     * A szén esetében nem csinál semmit.
     */
    @Override
    public void expose() {}

    @Override
    public String toString(){
        return "Carbon";
    }
}