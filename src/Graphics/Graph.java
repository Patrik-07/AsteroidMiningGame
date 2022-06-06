package Graphics;

import Places.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gráf reprezentáló osztály
 * A csúcsokat nyilvántartja,
 * és a megjelenítéshez egy koordinátarendszerben rendezi őket
 */
public class Graph{
    private final ArrayList<BaseGraphics> nodes;
    private final Container container;
    private final Icon asteroidIcon, teleportIcon;

    /**
     * Alapértelmezett konstruktor. Inicializálja az tagváltozókat
     * Létrehozza a telepes helyét jelző aurát
     * @param container A csúcsok kirajzolásához használt rajzterület
     */
    public Graph(Container container){
        nodes = new ArrayList<>();
        this.container = container;
        asteroidIcon = new ImageIcon("Images//asteroid.png");
        teleportIcon = new ImageIcon("Images//teleport.png");
        JLabel markerLabel = new JLabel("", new ImageIcon("Images//positionMarker.png"), JLabel.CENTER);
        markerLabel.setBounds(0, 0, markerLabel.getIcon().getIconWidth(), markerLabel.getIcon().getIconHeight());
        BaseGraphics.positionMarker = markerLabel;
        container.add(BaseGraphics.positionMarker);
    }

    /**
     * Frissíti a gráfot a kapott csúcsok alapján
     * @param asteroids aszteroidaként megjelenítendő csúcsok
     * @param teleports teleportként megjelenítendő csúcsok
     */
    public void update(ArrayList<Asteroid> asteroids, ArrayList<Teleport> teleports){
        if(nodes.size() != asteroids.size() + teleports.size()) {
            container.removeAll();
            container.add(BaseGraphics.positionMarker);
            nodes.clear();
            for (Asteroid asteroid : asteroids) {
                nodes.add(new AsteroidGraphics(asteroid, asteroidIcon));
            }
            for (Teleport teleport : teleports) {
                nodes.add(new TeleportGraphics(teleport, teleportIcon));
            }
            for (BaseGraphics node : nodes) {
                container.add(node);
                container.setComponentZOrder(node, 0);
            }
            if(nodes.size() >= 1){
                container.setComponentZOrder(BaseGraphics.positionMarker, 1);
            }
            sort();
        }
    }

    /**
     * A csúcsok erő alapú rendezése egy 0-1 közötti koordinátarendszerben
     * A koordináták a megjelenített képek KÖZEPÉT kell jelöljék.
     */
    public void sort(){
        int sqNum = 1;
        while ((sqNum * sqNum) < nodes.size())
            sqNum += 1;
        //ez egy ideiglenes megjelenítés az ideiglenes pálya fájlhoz
        //példát adhat, de törölni kell!
        float delta = 1f/(sqNum-1);
        Random random = new Random();
        for(int i = 0; i < nodes.size(); i++){
            //noinspection IntegerDivisionInFloatingPointContext
            nodes.get(i).setVirtualLocation((i%sqNum)*delta + random.nextFloat()/20f -0.025f, (i/sqNum)*delta + random.nextFloat()/20f -0.025f);
        }
        container.repaint();
    }

    /**
     * A csúcsok megjelenítési pozícióinak kiszámolása
     * A gráf csucsainak megjelenítése
     * @param activeSettlerPlace a soron levő telepse aktuális helye
     */
    public void draw(Place activeSettlerPlace){
        int widthOffset = nodes.get(0).getImageWidth()/2;
        int heightOffset = nodes.get(0).getImageHeight()/2;
        float marginTop = 0.1f;
        float marginBot = 0.1f;
        float marginLeft = 0.1f;
        float marginRight = 0.16f;
        for(BaseGraphics node : nodes){
            float vX = marginLeft+node.getVirtualX()*(1f-marginLeft-marginRight);
            float vY = marginTop+node.getVirtualY()*(1f-marginTop-marginBot);
            int maxWidth = container.getWidth();
            int maxHeight = container.getHeight();
            node.setImageLocation((int)(vX*maxWidth)-widthOffset, (int)(vY*maxHeight)-heightOffset);
            node.updateEntitiesPanel();
        }
        //noinspection SimplifyOptionalCallChains
        BaseGraphics activeSettlerGraphic = nodes.stream().filter(
                p -> p.getPlace() == activeSettlerPlace).findFirst().orElse(null);
        if(activeSettlerGraphic != null){
            activeSettlerGraphic.markPosition();
        }

    }

    /**
     * Visszaadja a csúcsok listáját
     * @return csúcsok listája
     */
    public ArrayList<BaseGraphics> getNodes(){
        return nodes;
    }

    /**
     * egy számpárt tárol el intekből
     */
    private class Vec2{
        public int x, y;
        public Vec2(int a, int b){x = a; y = b;}
    }

    /**
     * egy számpárt tárol el floatokból
     */
    private class Vec2f{
        public float x, y;
        public Vec2f(float a, float b){x = a; y = b;}
    }

    /**
     * Az erővezérelt rendezést megvalósító függvény
     * Gyakorlatban nagyon nem nézett ki jól, sokszor egymásra csúsztak egyes aszteroidák ezért nem használjuk
     */
    public void forceSort(){
        //élek kinyerése a nodeokból
        ArrayList<Vec2> edges = new ArrayList<Vec2>();
        for (BaseGraphics node : nodes) {
            ArrayList<Place> neighbors = node.getPlace().getNeighbours();
            for (Place p : neighbors) {
                for (BaseGraphics othernode : nodes) {
                    if (othernode.getPlace() == p) {
                        Vec2 potedge = new Vec2(nodes.indexOf(node), nodes.indexOf(othernode));
                        boolean uj = true;
                        for (Vec2 edge : edges) {
                            if ((edge.x == potedge.x && edge.y == potedge.y) || (edge.y == potedge.x && edge.x == potedge.y)) uj = false;
                        }
                        if (uj) edges.add(potedge);
                    }
                }
            }
        }

        for (int iteracio = 0; iteracio < 1; iteracio++) {
            ArrayList<Vec2f> ero = new ArrayList<Vec2f>();
            for (int i = 0; i < nodes.size(); i++) {
                Vec2f pontraHatoEro = new Vec2f(0f, 0f);
                for (int j = 0; j < nodes.size(); j++) {
                    if (i != j) {
                        boolean vanElKozte = false;
                        for (int k = 0; k < edges.size(); k++) {
                            if ((edges.get(k).x == i && edges.get(k).y == j) || (edges.get(k).y == i && edges.get(k).x == j)) vanElKozte = true;
                        }
                        Vec2f aPont = new Vec2f(nodes.get(i).getVirtualX(), nodes.get(i).getVirtualY());
                        Vec2f bPont = new Vec2f(nodes.get(j).getVirtualX(), nodes.get(j).getVirtualY());
                        if (vanElKozte) {
                            Vec2f irany = new Vec2f(bPont.x - aPont.x, bPont.y - aPont.y);
                            float tavolsag = (float)Math.sqrt(irany.x * irany.x + irany.y * irany.y);
                            pontraHatoEro.x = pontraHatoEro.x + 0.5f * (float)Math.log(tavolsag / 0.25) * irany.x;         // 0.5 = cspring
                            pontraHatoEro.y = pontraHatoEro.y + 0.5f * (float)Math.log(tavolsag / 0.25) * irany.y;         // 0.25 = l
                        }
                        else {
                            Vec2f irany = new Vec2f(aPont.x - bPont.x, aPont.y - bPont.y);
                            float tavolsag = (float)Math.sqrt(irany.x * irany.x + irany.y * irany.y);
                            pontraHatoEro.x = pontraHatoEro.x + 0.007f / (tavolsag * tavolsag) * irany.x;                    // 0.05 = crep
                            pontraHatoEro.y = pontraHatoEro.y + 0.007f / (tavolsag * tavolsag) * irany.y;
                        }
                    }
                }
                ero.add(pontraHatoEro);
            }

            for (int i = 0; i < nodes.size(); i++) {
                Vec2f force = ero.get(i);
                //force.x = force.x / (float) Math.sqrt(force.x * force.x + force.y * force.y);
                //force.y = force.y / (float) Math.sqrt(force.x * force.x + force.y * force.y);
                force.x = force.x * 0.1f;
                force.y = force.y * 0.1f;
                BaseGraphics node = nodes.get(i);
                float x = node.getVirtualX();
                float y = node.getVirtualY();
                if (x + force.x < 0 || x + force.x > 1 || y + force.y < 0 || y + force.y > 1) continue;
                node.setVirtualLocation(x + force.x, y + force.y);
            }
        }
    }
}
