package Places;

import Game.Game;
import Game.ResourceList;
import Resources.*;

import java.util.ArrayList;

public class Asteroid extends Place {
    private Resource contains;
    private final ResourceList baseResourceList;
    private final Game game;
    private int thickness;
    private boolean sunClose;

    /**
     * Az aszteroida konstruktora.
     * A kapott paramétereket beállítja, és a bázis építési költségeit.
     * @param contains aszteroida belsejében levő nyersanyag
     * @param thickness aszteroida kőzetrétegének vastagsága
     * @param sunClose aszteroida napközelsége
     * @param game játék irányítóra referencia
     */
    public Asteroid(Resource contains, int thickness, boolean sunClose, Game game) {
        this.contains = contains;
        this.thickness = thickness;
        this.sunClose = sunClose;
        this.game = game;

        baseResourceList = new ResourceList();
        baseResourceList.addResource(new Carbon());
        baseResourceList.addResource(new Carbon());
        baseResourceList.addResource(new Carbon());
        baseResourceList.addResource(new IceWater());
        baseResourceList.addResource(new IceWater());
        baseResourceList.addResource(new IceWater());
        baseResourceList.addResource(new Iron());
        baseResourceList.addResource(new Iron());
        baseResourceList.addResource(new Iron());
        baseResourceList.addResource(new Uranium());
        baseResourceList.addResource(new Uranium());
        baseResourceList.addResource(new Uranium());

        if(contains != null){
            this.contains.setAsteroid(this);
        }
    }

    /**
     * Aszteroida kőzetrétegének fúrása/csökkentése.
     * @return igaz ha sikerült a fúrás, hamis egyébként
     */
    public boolean drill() {
        if(thickness > 0){
            thickness--;
            if(thickness == 0 && getSunClose()){
                contains.expose();
            }
            return true;
        }
        return false;
    }

    /**
     * Aszteroida belsejében levő nyersanyag bányászása.
     * Csak akkor bányászható ha a kőzetréteg már elfogyott.
     * @return a kibányászott nyersanyag
     */
    public Resource mine() {
        if(thickness <= 0){
            Resource temp = contains;
            contains = null;
            return temp;
        }
        return null;
    }

    /**
     * Az aszteroida felrobban.
     * Minden rajta levő entitást felrobbant.
     * A benne levő nyersanyagot eltörli.
     * Értesíti a játékot, hogy felrobbant.
     */
    public void explosion() {
        contains = null;
        for(int i = 0; i < entities.size(); i++){
            try {
                entities.get(i).explode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        game.removeAsteroid(this);
    }

    /**
     * A paraméterként kapott nyersanyagok felhasználásával,
     * elkezdi vagy folytatja egy bázis építését.
     * @param resourceList A nyersanyagok, amiket fel akarnak használni a bázis építéséhez.
     */
    public void depositBase(ResourceList resourceList) {
        ResourceList temp = new ResourceList(baseResourceList);
        baseResourceList.subtraction(resourceList);
        resourceList.subtraction(temp);
        if (resourceList.resourceCount() == 0)
            game.endGame(true);
    }

    /**
     * Nyersanyag elhelyezése az aszteroidába
     * Csak akkor lehetséges ha már nincs kőzetréteg, és az aszteroida üres.
     * @param resource hozzáadandó nyersanyag
     * @return sikerült-e a hozzáadás
     */
    public boolean addResource(Resource resource) {
        if(contains == null && thickness == 0){
            contains = resource;
            return true;
        }
        return false;
    }

    /**
     * Megmondja, hogy milyen nyersanyag van egy aszteroida magjában.
     * @return aszteroidan lévő nyersanyag
     */
    @Override
    public Resource getContains() {
        return contains;
    }

    /**
     * Megmondja az aszteroida közetrétegének vastagságát.
     * @return közetréteg vastagsága
     */
    @Override
    public int getThickness() {
        return thickness;
    }

    /**
     * Beállítja a kőzetréteg vastagságát
     * @param t kőzetréteg vastagsága
     */
    public void setThickness(int t) {
        thickness = t;
    }

    /**
     * Beállítja az aszteroidába levő nyersanyagot
     * @param resource beállítandó nyersanyag
     */
    public void setContains(Resource resource) {
        contains = resource;
    }

    /**
     * Megmondja, hogy az aszteroid napközelben van-e.
     * @return napközelben van-e az aszteroida
     */
    public boolean getSunClose() {
        return sunClose;
    }

    /**
     * Beállítja az aszteroidában, hogy napközelben van-e.
     * Ha nincs kőzetrétege, és napközelbe kerül, akkor a belsejét károsodás érheti.
     * @param sunClose beállítandó naptávolság státusz
     */
    public void setSunClose(boolean sunClose) {
        this.sunClose = sunClose;
        if(sunClose && thickness == 0 && contains != null){
            contains.expose();
        }
    }

    public ArrayList<Resource> getBaseRequiredResources() {
        return baseResourceList.getList();
    }
}
