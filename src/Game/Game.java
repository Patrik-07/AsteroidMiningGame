package Game;

import Entities.Entity;
import Entities.Settler;
import Entities.Ufo;
import Places.Asteroid;
import Places.Place;
import Places.Teleport;
import Resources.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Runnable{
    private int rounds;
    private final int steps;
    private int settlerCount;
    private boolean solarStormIncoming;
    private Settler currentSettler;
    private boolean gameEnded = false;//ha vége van a játéknak akkor igaz lesz
    private String endResult = "";

    private ArrayList<Entity> entities;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Teleport> teleports;

    private float sunCloseProbability = 1f/3;
    private float solarStormProbability = 1f/5;

    private Random random = new Random();

    public final Object settlerTurnWaitLock = new Object();

    /**
     * Konstruktor. Beállítja a belső változóit a kapott paramáterek értékére.
     * @param steps hány lápást kapnak az entitások körönként
     * @param settlerCount telepesek aktuális darabszáma, új játék esetén ennyi telepes generálódik
     */
    public Game(int steps, int settlerCount) {
        this.rounds = 0;
        this.steps = steps;
        this.settlerCount = settlerCount;
        this.solarStormIncoming = false;
        entities = new ArrayList<>();
        asteroids = new ArrayList<>();
        teleports = new ArrayList<>();
    }

    /**
     * Új játékot indít.
     * Létrehozza az aszteroidaövet és a telepeseket.
     * @param settlerCount létrehozandó telepesek darabszáma
     */
    public void newGame(int settlerCount) {
        this.settlerCount = 0;
        loadMap("Maps\\map1.txt");
        for(int i = 0; i < settlerCount; i++){
            new Settler(asteroids.get(0), this);
        }
        for (int i = 0; i < 3; i++) {
            int r = ThreadLocalRandom.current().nextInt(0, asteroids.size() - 1);
            new Ufo(asteroids.get(r), this);
        }
        gameEnded = false;
    }

    /**
     * Pálya betöltése fájlból
     * @param fileName pályát tartalmazó fájl neve
     */
    private void loadMap(String fileName){
        boolean asteroidsRead = false;
        boolean neighboursRead = false;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            //A fájl végéig olvas
            while ((line = br.readLine()) != null) {
                //Üres sornál a következő műveletre ugrik
                //aszteroidák létrehozása -> aszteroidák szomszédosítása
                if(line.equals("")){
                    if(!asteroidsRead)
                        asteroidsRead = true;
                    else if(!neighboursRead)
                        neighboursRead = true;
                    continue;
                }
                //Komment sorok átugrása
                else if(line.charAt(0) == '#')
                    continue;
                //Aszteroidák létrehozása
                if(!asteroidsRead) {
                    String[] split = line.split(" ");
                    Resource contains = null;
                    int exposeCount = 0;
                    if(split.length >= 3)
                        exposeCount = Integer.parseInt(split[2]);
                    switch (split[1]) {
                        case "I" -> contains = new Iron();
                        case "W" -> contains = new IceWater();
                        case "C" -> contains = new Carbon();
                        case "U" -> {
                            contains = new Uranium();
                            ((Uranium)contains).setExposeCount(exposeCount);
                        }
                    }
                    addAsteroid(new Asteroid(contains, Integer.parseInt(split[0]), false, this));
                }
                //Aszteroidák szomszédosítása
                else if(!neighboursRead){
                    String[] split = line.split(" ");
                    if(split.length <= 1)
                        continue;
                    int asteroidIndex = Integer.parseInt(split[0]) - 1;
                    for (int i = 1; i < split.length; i++){
                        int neighbourIndex = Integer.parseInt(split[i]) - 1;
                        Place neighbour = asteroids.get(neighbourIndex);
                        asteroids.get(asteroidIndex).addNeighbour(neighbour);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Új kör kezdődik
     * Elősször a pálya műveletei történnek meg (napvihar, napközelség)
     * Aztán minden entitás kap új lépési lehetőségeket, majd egysével rájuk kerül a sor hogy lépjenek
     *
     */
    public void newRound() {
        //Új kör kezdése a pálya elemein
        if(solarStormIncoming)
            solarStorm();
        ArrayList<Asteroid> tempAsteroids = new ArrayList<>(asteroids);
        for (Asteroid a:tempAsteroids) {
            boolean sunClose = random.nextFloat() < sunCloseProbability;
            a.setSunClose(sunClose);
        }
        solarStormIncoming = random.nextFloat() < solarStormProbability;
        //Megkergült teleportok mozognak
        for(Teleport t : teleports){
            t.move();
        }
        //Új kör kezdése az entitásoknak
        entities.forEach(x -> x.setSteps(steps));
        for (int i = 0; i < entities.size(); i++){
            if(gameEnded){
                return;
            }
            if(i < settlerCount) {
                currentSettler = (Settler) entities.get(i);
                synchronized (settlerTurnWaitLock) {
                    settlerTurnWaitLock.notify();
                }
                currentSettler.newTurn();
                //addig vár amíg nem lesz vége az aktív telepes körének
                synchronized (settlerTurnWaitLock){
                    try {
                        settlerTurnWaitLock.wait();
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
                continue;
            }
            try {
                entities.get(i).newTurn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rounds++;
    }

    /**
     * A játék szálának elindítása.
     * Folyamatosan új köröket indít amíg vége nincs a játéknak.
     */
    @Override
    public void run() {
        while(!gameEnded)
            newRound();
    }

    /**
     * Véget vet a játéknak. A játék szála is vissza tér.
     */
    public void endGameThread() {
        this.gameEnded = true;
        synchronized (settlerTurnWaitLock) {
            this.settlerTurnWaitLock.notifyAll();
        }
    }

    /**
     * A játéknak vége.
     * @param playersWon a telepesek nyertek-e
     */
    public void endGame(boolean playersWon) {
        endResult = "A játéknak " + rounds + " kör után vége: ";
        if(playersWon)
            endResult += "A játékosok nyertek.";
        else
            endResult += "A játékosok vesztettek.";
        gameEnded = true;
        synchronized (settlerTurnWaitLock) {
            this.settlerTurnWaitLock.notifyAll();
        }
    }

    /**
     * Minden place-re véletlenszerűen választja ki hogy hív solar storm-ot vagy sem.
     * A kiválasztott place-eken véletlen mélységgel hív napvihart.
     */
    public void solarStorm() {
        if(solarStormIncoming) {
            for (Asteroid asteroid : asteroids) {
                if(random.nextFloat() < solarStormProbability){
                    asteroid.solarStorm(random.nextInt(3) + 1);
                }
            }
        }
    }

    /**
     * Új entitás hozzáadása a játékhoz.
     * @param entity hozzáadandó entitás
     */
    public void addEntity(Entity entity, boolean isSettler) {
        entities.add(entity);
        if(isSettler)
            settlerCount += 1;
    }

    /**
     * Egy entitás meghalt.
     * A halott entitást kitörli a nyilvántartásából.
     * Ha telepes halt meg akkor a telepesek számlálóját is csökkenti.
     * Ha minden telepes meghalt akkor vége a játéknak.
     * @param entity meghalt entitás
     * @param isSettler a halott entitás telepes-e
     */
    public void entityDied(Entity entity, boolean isSettler) {
        entities.remove(entity);
        if (isSettler) {
            settlerCount--;
            if(settlerCount == 0)
                endGame(false);

        }
    }

    /**
     * Egy aszeteroida eltörlése.
     * @param asteroid eltörlendő aszteroida
     */
    public void removeAsteroid(Asteroid asteroid) {
        asteroids.remove(asteroid);
    }

    /**
     * Beállítja a játékban résztvevő entitásokat
     * @param entities entitások listája
     */
    public void setEntities(ArrayList<Entity> entities){
        this.entities = entities;
    }

    /**
     * Visszaadja a játékban levő entitások darabszámát
     * @return entitások darabszámát
     */
    public int getEntityCount() {return entities.size();}

    /**
     * Visszaadja a játékban levő aszteroidák darabszámát
     * @return aszteroidák darabszámát
     */
    public int getAsteroidCount() {return asteroids.size();}

    /**
     * Új aszteroida hozzáadása a játékhoz.
     * @param asteroid hozzáadantó aszteorida
     */
    public void addAsteroid(Asteroid asteroid){
        asteroids.add(asteroid);
    }

    public void setAsteroids(ArrayList<Asteroid> asteroids){
        this.asteroids = asteroids;
    }

    /**
     * Új teleport hozzáadása a játékhoz.
     * @param teleport hozzáadantó teleport
     */
    public void addTeleport(Teleport teleport){
        teleports.add(teleport);
    }

    public int getRounds() {
        return rounds;
    }

    public int getSteps() {
        return steps;
    }

    public int getSettlerCount() {
        return settlerCount;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public ArrayList<Teleport> getTeleports() {
        return teleports;
    }

    public void setSunCloseProbability(float sunCloseProbability){
        this.sunCloseProbability = sunCloseProbability;
    }

    public Settler getCurrentSettler() {
        return currentSettler;
    }

    public boolean getGameEnded(){
        return gameEnded;
    }

    public String getEndResult(){
        return endResult;
    }
}
