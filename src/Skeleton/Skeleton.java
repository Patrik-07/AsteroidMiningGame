package Skeleton;
import Entities.*;
import Game.*;
import Places.*;
import Resources.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/*
public class Skeleton {
    static Scanner in = new Scanner(System.in);
    public static String line = "+---------------------------------------------------------------+";

    public static void divingLine() {
        System.out.println(line);
    }

    public static void emptyLine(int n) {
        int l = line.length() - n;
        for (int i = 0; i < l - 3; i++)
            System.out.print(" ");
        System.out.println("|");
    }

    public static char inputLineChar(String question) {
        System.out.println(" --> " + question);
        System.out.print(" --> Input: ");
        return in.next().charAt(0);
    }

    public static int inputLineNumber(String question) {
        System.out.println(" --> " + question);
        System.out.print(" --> Input: ");
        return in.nextInt();
    }

    public static void stringLine(String string, int n) {
        System.out.print("| ");
        for (int i = 0; i < 3*n; i++)
            System.out.print(" ");
        System.out.print(string);
        emptyLine(string.length() + 3*n);
    }

    public static void titleLine(String title) {
        divingLine();
        stringLine(title, (line.length() - title.length())/6);
        divingLine();
    }

    public static void testEnd() throws IOException {
        System.out.println(" --> A tesztnek vége. Nyomj ENTER-t a visszalépéshez.");
        //noinspection ResultOfMethodCallIgnored
        System.in.read(new byte[2]);
    }

    public static void moveSettlerTest() throws IOException {
        System.out.println();
        titleLine("Test[1] - Move settler");

        stringLine("Kezdő állapot:", 0);

        Asteroid a1 = new Asteroid(null, 1, false, null);
        Asteroid a2 = new Asteroid(null, 1, false, null);
        Asteroid a3 = new Asteroid(null, 1, false, null);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("a2: " + a2, 2);
        stringLine("a3: " + a3, 2);

        a1.addNeighbour(a2);
        a2.addNeighbour(a1);
        stringLine("Szomszédságok beállítva:", 1);
        stringLine("a1 szomszéd: a2: " + a1.getNeighbours().get(0), 2);
        stringLine("a2 szomszéd: a1: " + a2.getNeighbours().get(0), 2);
        stringLine("a3 szomszéd: null", 2);

        Settler s = new Settler(a1, null);
        stringLine("Telepes létrehozva:", 1);
        stringLine("telepes: " + s, 2);
        stringLine("telepes aszteroida: a1: " + a1, 2);

        divingLine();

        int input = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("Szomszédos aszteroidára menjen a telepes? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                stringLine("A telepes szomszédos aszteroidára utazik...", 0);
                s.move(a2);
                stringLine("telepes: " + s, 1);
                stringLine("telepes aszteroida: a2: " + s.getPlace(), 1);
                stringLine("Az utazás sikeres volt.", 0);
            } else {
                stringLine("A telepes nem szomszédos aszteroidára próbál utazni...", 0);
                s.move(a3);
                stringLine("telepes: " + s, 1);
                stringLine("telepes aszteroida: a1: " + s.getPlace(), 1);
                stringLine("A telepes csak szomszédos aszteroidára tud mozogni.", 0);
            }
        } else {
            stringLine("A telepesnek nincs elég lépése, így nem tud mozogni.", 0);
        }
        divingLine();
        testEnd();
    }

    public static void moveRobotTest() throws IOException {
        System.out.println();
        titleLine("Test[2] - Move robot");

        stringLine("Kezdő állapot:", 0);

        Asteroid a1 = new Asteroid(null, 1, false, null);
        Asteroid a2 = new Asteroid(null, 1, false, null);
        Asteroid a3 = new Asteroid(null, 1, false, null);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("a2: " + a2, 2);
        stringLine("a3: " + a3, 2);

        a1.addNeighbour(a2);
        a2.addNeighbour(a1);
        stringLine("Szomszédságok beállítva:", 3);
        stringLine("a1 szomszéd: a2: " + a1.getNeighbours().get(0), 2);
        stringLine("a2 szomszéd: a1: " + a2.getNeighbours().get(0), 2);
        stringLine("a3 szomszéd: null", 6);

        Robot r = new Robot(a1, null);
        stringLine("Robot létrehozva:", 1);
        stringLine("robot: " + r, 2);
        stringLine("robot aszteroida: a1: " + a1, 2);

        divingLine();

        int input = inputLineChar("Van még elég lépése a robotnak? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("Szomszédos aszteroidára menjen a robot? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                stringLine("A robot szomszédos aszteroidára utazik...", 0);
                r.move(a2);
                stringLine("robot: " + r, 1);
                stringLine("robot aszteroida: a2: " + r.getPlace(), 1);
            } else {
                stringLine("A robot nem szomszédos aszteroidára próbál utazni...", 0);
                r.move(a2);
                stringLine("robot: " + r, 1);
                stringLine("robot aszteroida: a1: " + r.getPlace(), 1);
                stringLine("A robot csak szomszédos aszteroidára tud mozogni.", 0);
            }
        } else {
            stringLine("A robotnak nincs elég lépése, így nem tud mozogni.", 0);
        }
        divingLine();
        testEnd();
    }

    public static void placeTeleportTest() throws IOException {
        System.out.println();
        titleLine("Test[3] - PlaceTeleport");

        stringLine("Kezdő állapot:", 0);

        Asteroid a1 = new Asteroid(null, 1, false, null);
        Asteroid a2 = new Asteroid(null, 1, false, null);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("a2: " + a2, 2);

        a1.addNeighbour(a2);
        a2.addNeighbour(a1);
        stringLine("Szomszédságok beállítva:", 1);
        stringLine("a1 szomszéd: a2: " + a1.getNeighbours().get(0), 2);
        stringLine("a2 szomszéd: a1: " + a2.getNeighbours().get(0), 2);

        Settler s = new Settler(a1, null);
        s.setSteps(5);
        stringLine("Telepes létrehozva:", 1);
        stringLine("telepes: " + s, 2);
        stringLine("telepes aszteroida: a1: " + a1, 2);
        stringLine("telepes teleporta: null", 2);

        divingLine();

        int input = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("Van teleportkapuja a telepesnek? [I/N]?");
            divingLine();
            if (input == 'i' || input == 'I') {
                s.getInventory().addResource(new Iron(), 2);
                s.getInventory().addResource(new IceWater(), 1);
                s.getInventory().addResource(new Uranium(), 1);
                s.build(new Teleport());

                stringLine("Telepes teleportja:", 0);
                stringLine("t: " + s.getTeleports().get(0), 1);
                stringLine("t szomszédai: null", 1);

                Teleport t1 = s.getTeleports().get(0);
                try {
                    s.placeTeleport(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                stringLine("Telepes lehelyez egy teleportot...", 0);
                stringLine("lehelyezett teleport:", 1);
                stringLine("t: " + t1, 2);
                stringLine("t szomszédai: ", 2);
                stringLine(t1.getNeighbours().get(0).toString(), 3);
                stringLine(t1.getNeighbours().get(1).toString(), 3);
            } else {
                try {
                    s.placeTeleport(0);
                } catch (Exception e) {
                    stringLine(e.getMessage(), 0);
                }
            }
        } else {
            stringLine("A telepesnek nincs elég lépése,", 0);
            stringLine("így nem tud teleportot elhelyezni.", 0);
        }
        divingLine();
        testEnd();
    }

    private static void mineTest() throws IOException {
        Asteroid a1 = new Asteroid(new Iron(), 0, false, null);
        Settler s = new Settler(a1, null);

        System.out.println();
        titleLine("Test[4] - Mine");
        stringLine("Kezdő állapot:", 0);
        stringLine("Aszteroida legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("Telepes létrehozva:", 1);
        stringLine("telepes: " + s, 2);
        divingLine();

        int input = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("Van elég hely a settler inventoryjában? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                input = inputLineChar("A kőzetréteg nulla? [I/N]");
                divingLine();
                if(input == 'i' || input == 'I') {
                    stringLine("A telepes bányászik...", 0);
                    s.mine();
                    stringLine("telepes: " + s, 1);
                    stringLine("telepes inventoryja: " + s.getInventory(), 1);
                } else {
                    stringLine("Az aszteroida kőzetrétegét át kell fúrni bányászás előtt.", 0);
                }
            } else {
                stringLine("A telepesnek nincs elég helye a nyersanyag felvételéhez.", 0);
            }
        } else {
            stringLine("A telepesnek nincs elég lépése, így nem tud bányászni.", 0);
        }
        divingLine();
        testEnd();
    }

    public static void putBackResource() throws IOException {
        System.out.println();
        titleLine("Test[5] - PutBackResource");
        int input = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("Aszteroidán áll a telepes? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                input = inputLineChar("Az aszteroida közetrétege ki van fúrva? [I/N]");
                divingLine();
                if (input == 'i' || input == 'I') {
                    input = inputLineChar("Üreges az aszteroida? [I/N]");
                    divingLine();
                    if (input == 'i' || input == 'I') {
                        Asteroid asteroid = new Asteroid(new Iron(), 0, false, null);
                        Settler settler = new Settler(asteroid, null);

                        settler.setSteps(2);
                        settler.mine();
                        stringLine("Kezdő állapot:", 0);
                        stringLine("Settler ResourceCount: " + settler.getInventory().resourceCount(), 1);
                        if (asteroid.getContains() == null)
                            stringLine("Asteroid Resource: null ", 1);
                        stringLine("", 0);
                        settler.putBackResource(new Iron());
                        stringLine("Nyersanyag visszahelyezése...", 0);
                        stringLine("", 0);

                        stringLine("Visszehelyezés után:", 0);
                        stringLine("Settler ResourceCount: " + settler.getInventory().resourceCount(), 1);
                        stringLine("Asteroid Resource: " + asteroid.getContains().getClass(), 1);

                    } else {
                        stringLine("A telepese csak üreges aszteroidába", 0);
                        stringLine("helyezhet vissza nyersanyagot.", 0);
                    }
                } else {
                    stringLine("A telepese csak teljesesn kifúrt aszteroidába", 0);
                    stringLine("helyezhet vissza nyersanyagot.", 0);
                }
            } else {
                stringLine("A telepese csak aszteroidára rakhat vissza nyersanyagot.", 0);
            }
            divingLine();
        } else if (input == 'n' || input == 'N') {
            stringLine("A telepesnek nincsenek lépései, ezért nem tett vissza semmit.", 0);
            divingLine();
        }
        testEnd();
    }

    public static void drillTest() throws IOException{
        System.out.println();
        titleLine("Test[6] - Drill");
        int x = inputLineNumber("Milyen vastag a kéreg?");
        divingLine();
        if(x < 0) {
            stringLine("Negatív vastagsága nem lehet", 0);
        } else {
            Asteroid a = new Asteroid(new Resources.Iron(), x, false, null);
            Settler settler = new Settler(a, null);
            settler.setSteps(1);
            settler.drill();
            stringLine("Fúrás után a kéreg vastagsága: " + a.getThickness(), 0);
        }
        divingLine();
        testEnd();
    }

    public static void drillIcewaterTest() throws IOException {
        System.out.println();
        titleLine("Test[7] - DrillIceWater");
        int ch;
        ch = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (ch == 'i' || ch == 'I') {
            int x = inputLineNumber("Milyen vastag a kéreg?");
            divingLine();
            Asteroid a1 = new Asteroid(new IceWater(), x, false, null);
            stringLine("A kéreg vastagsága: " + a1.getThickness(), 0);
            if (a1.getThickness() == 0) {
                stringLine("A telepes nem tud fúrni, mert a kőzetréteg eleve ki van fúrva.", 0);
                divingLine();
                testEnd();
                return;
            }
            Settler s = new Settler(a1, null);
            s.setSteps(1);
            if (a1.getContains() != null)
                stringLine("Az aszteroida magjában Vízjég van.", 0);
            else
                stringLine("Az aszteroida belsejéből eltűnt a vízjég.", 0);
            divingLine();
            ch = inputLineChar("Napközelben van az aszteroida? [I/N]");
            divingLine();
            if (ch == 'i' || ch == 'I') {
                a1.setSunClose(true);
                stringLine("A telepes megkezdi a fúrást...", 0);
                s.drill();
                stringLine("A fúrás után a kéreg vastagsága: " + a1.getThickness(), 1);
                if (a1.getContains() == null) {
                    stringLine("Az aszteroida belsejéből eltűnt a vízjég", 1);
                } else {
                    stringLine("Az aszteroida belsejében megmaradt a vízjég.", 1);
                }
                divingLine();
            }
            else if (ch == 'n' || ch == 'N') {
                stringLine("A telepes megkezdi a fúrást...", 0);
                s.drill();
                stringLine("A fúrás után a kéreg vastagsága: " + a1.getThickness(), 1);
                if (a1.getContains() == null){
                    stringLine("Az aszteroida belsejéből eltűnt a vízjég.", 1);
                } else {
                    stringLine("Az aszteroida belsejében megmaradt a vízjég.", 1);
                }
                divingLine();
            }
        } else if (ch == 'n' || ch == 'N'){
            stringLine("A telepesnek nincs lépése, így nem tud fúrni.", 1);
            divingLine();
        }
        testEnd();
    }

    private static void drillSettlerUraniumTest() throws IOException{
        Game game = new Game(1, 1);
        Asteroid a1 = new Asteroid(new Uranium(), 1, false, game);
        Settler s = new Settler(a1, game);
        s.setSteps(1);
        a1.addEntity(s);
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        asteroids.add(a1);
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(s);
        game.setAsteroids(asteroids);
        game.setEntities(entities);

        System.out.println();
        titleLine("Test[8] - Drill settler uranium");
        stringLine("Kezdő állapot:", 0);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("Telepes létrehozva:", 1);
        stringLine("telepes: " + s, 2);
        stringLine("telepes aszteroidája: a1: " + s.getPlace(), 3);
        stringLine("Játék létrehozva:", 1);
        stringLine("játék: " + game, 2);
        stringLine("játékban levő entitások száma: " + game.getEntityCount(), 3);
        stringLine("játékban levő aszteroidák száma: " + game.getAsteroidCount(), 3);
        divingLine();

        int input = inputLineChar("Van még elég lépése a telepesnek? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("A kőzetréteg vastagabb mint egy? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                a1.setThickness(2);
                stringLine("Az aszteroida kőzetrétegének átállítása:", 0);
                stringLine("a1: " + a1, 1);
                stringLine("", 0);
                stringLine("A telepes fúrja az aszteroidát...", 0);
                s.drill();
                stringLine("telepes: " + s, 1);
                stringLine("telepes aszteroidája: a1: " + s.getPlace(), 2);
                stringLine("aszteroida: " + a1, 1);
            }
            else if(input == 'n' || input == 'N') {
                input = inputLineChar("Az aszteroida napközelben van? [I/N]");
                divingLine();
                if (input == 'i' || input == 'I') {
                    a1.setSunClose(true);
                    stringLine("Az aszteroida napközelségének átállítása:", 0);
                    stringLine("a1: " + a1, 1);
                    stringLine("", 0);
                }
                stringLine("A telepes fúrja az aszteroidát...", 0);
                s.drill();
                stringLine("telepes: " + s, 1);
                stringLine("telepes aszteroidája: a1: " + s.getPlace(), 2);
                stringLine("aszteroida: " + a1, 1);
                stringLine("játék: " + game, 1);
                stringLine("játékban levő entitások száma: " + game.getEntityCount(), 2);
                stringLine("játékban levő aszteroidák száma: " + game.getAsteroidCount(), 2);
            }
        } else {
            stringLine("A telepesnek nincs elég lépése, így nem tud fúrni.", 0);
        }
        divingLine();
        testEnd();
    }

    private static void drillRobotUraniumTest() throws IOException {
        Game game = new Game(1, 1);
        Asteroid a1 = new Asteroid(new Uranium(), 1, false, game);
        Asteroid a2 = new Asteroid(new Uranium(), 1, false, game);
        a1.addNeighbour(a2);
        a2.addNeighbour(a1);
        Robot r = new Robot(a1, game);
        r.setSteps(1);
        a1.addEntity(r);
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        asteroids.add(a1);
        asteroids.add(a2);
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(r);
        game.setAsteroids(asteroids);
        game.setEntities(entities);

        System.out.println();
        titleLine("Test[9] - Drill robot uranium");
        stringLine("Kezdő állapot:", 0);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("a2: " + a2, 2);
        stringLine("Robot létrehozva:", 1);
        stringLine("robot: " + r, 2);
        stringLine("robot aszteroidája: a1: " + r.getPlace(), 3);
        stringLine("Játék létrehozva:", 1);
        stringLine("játék: " + game, 2);
        stringLine("játékban levő entitások száma: " + game.getEntityCount(), 3);
        stringLine("játékban levő aszteroidák száma: " + game.getAsteroidCount(), 3);
        divingLine();

        int input = inputLineChar("Van még elég lépése a robotnak? [I/N]");
        divingLine();
        if (input == 'i' || input == 'I') {
            input = inputLineChar("A kőzetréteg vastagabb mint egy? [I/N]");
            divingLine();
            if (input == 'i' || input == 'I') {
                a1.setThickness(2);
                stringLine("Az aszteroida kőzetrétegének átállítása:", 0);
                stringLine("a1: " + a1, 1);
                stringLine("", 0);
                stringLine("A robot fúrja az aszteroidát...", 0);
                r.drill();
                stringLine("robot: " + r, 1);
                stringLine("robot aszteroidája: a1: " + r.getPlace(), 2);
                stringLine("aszteroida: " + a1, 1);
            }
            else if(input == 'n' || input == 'N') {
                input = inputLineChar("Az aszteroida napközelben van? [I/N]");
                divingLine();
                if (input == 'i' || input == 'I') {
                    a1.setSunClose(true);
                    stringLine("Az aszteroida napközelségének átállítása:", 0);
                    stringLine("a1: " + a1, 1);
                    stringLine("", 0);
                }
                stringLine("A robot fúrja az aszteroidát...", 0);
                r.drill();
                stringLine("robot: " + r, 1);
                stringLine("robot aszteroidája: a1: " + r.getPlace(), 2);
                stringLine("aszteroida: " + a1, 1);
                stringLine("játék: " + game, 1);
                stringLine("játékban levő entitások száma: " + game.getEntityCount(), 2);
                stringLine("játékban levő aszteroidák száma: " + game.getAsteroidCount(), 2);
            }
        } else {
            stringLine("A robotnak nincs elég lépése, így nem tud fúrni.", 0);
        }
        divingLine();
        testEnd();
    }

    public static void buildRobot() throws IOException{
        System.out.println();
        titleLine("Test[10] - BuildRobot");

        Asteroid a = new Asteroid(null, 10, false, null);
        Settler s = new Settler(a, null);
        Robot r = new Robot(null, null);
        if(r.build(s)){
            stringLine("A robot sikeresen megépült,", 0);
            stringLine("az aszteroidáján az entitások száma:" + r.getPlace().entityCount(), 0);
        }
        divingLine();
        testEnd();
    }

    public static void buildTeleportTest() throws IOException{
        System.out.println();
        titleLine("Test[11] - Build teleport");

        Game game = new Game(1, 1);
        Settler s = new Settler(null, game);
        Teleport t = new Teleport();
        stringLine("Kezdő állapot:", 0);
        stringLine("Telepes: s: " + s, 1);
        stringLine("tárolt teleportok: " + s.getTeleports().size(), 2);
        stringLine("Nyersanyagok:", 2);
        divingLine();
        int input = inputLineChar("A telepesnél van már teleport kapu? [I/N]");
        divingLine();
        if(input == 'i' || input == 'I'){
            Teleport t2 = new Teleport();
            t2.build(s);
            stringLine("Telepesnél levő teleportok beállítása:",0);
            t.build(s);
            stringLine("Telepes: s: " + s, 1);
            stringLine("tárolt teleportok: " + s.getTeleports().size(), 2);
            stringLine("A telepesnél nincs elég hely teleportok készítéséhez", 0);
        } else {
            if(t.build(s)){
                stringLine("A teleport sikeresen megépült", 0);
                stringLine("Telepes: s: " + s, 1);
                stringLine("tárolt teleportok: " + s.getTeleports().size(), 2);
                stringLine("Nyersanyagok:", 2);
            }
        }
        divingLine();
        testEnd();
    }

    public static void depositBase() throws IOException {
        Game game = new Game(1, 1);
        ArrayList<Asteroid> a = new ArrayList<>();
        Asteroid a1 = new Asteroid(new Uranium(), 1, false, game);
        a.add(a1);
        Settler s = new Settler(a1, game);
        game.addEntity(s, true);
        game.setAsteroids(a);
        titleLine("Test[12] - DepositBase");
        stringLine("Kezdő állapot:", 0);
        stringLine("Aszteroidák legenerálva:", 1);
        stringLine("a1: " + a1, 2);
        stringLine("Telepes létrehozva:", 1);
        stringLine("telepes: " + s, 2);
        stringLine("Játék létrehozva:", 1);
        stringLine("játék: " + game, 2);
        stringLine("játékban levő entitások száma: " + game.getEntityCount(), 3);
        stringLine("játékban levő aszteroidák száma: " + game.getAsteroidCount(), 3);
        divingLine();
        int ch;
        ch = inputLineChar("Minden szükséges nyersanyag bekerült a bázisba? [I/N]");
        divingLine();
        if (ch == 'i' || ch == 'I') {
            stringLine("Telepesnél lévő nyersanyagok száma:" + s.getInventory().resourceCount(), 0);
            stringLine("Inventory feltöltése...", 1);
            s.getInventory().addResource(new Carbon(), 3);
            s.getInventory().addResource(new IceWater(), 3);
            s.getInventory().addResource(new Iron(), 3);
            s.getInventory().addResource(new Uranium(), 3);
            stringLine("Telepesnél lévő nyersanyagok száma:" + s.getInventory().resourceCount(), 0);
            if(s.getInventory().isContain(new Carbon()))
                stringLine("Szén: " + 3, 1);
            if(s.getInventory().isContain(new IceWater()))
                stringLine("Vízjég: " + 3, 1);
            if(s.getInventory().isContain(new Iron()))
                stringLine("Vas: " + 3, 1);
            if(s.getInventory().isContain(new Uranium()))
                stringLine("Uránium: " + 3, 1);
            stringLine("", 0);
            stringLine("A telepes megkezdi a bázis építését...", 0);
            s.depositBase();
        } else if(ch == 'n' || ch == 'N') {
            stringLine("Telepesnél lévő nyersanyagok száma:" + s.getInventory().resourceCount(), 0);
            stringLine("Inventory feltöltése...", 1);
            s.getInventory().addResource(new Carbon(), 1);
            s.getInventory().addResource(new IceWater(), 3);
            s.getInventory().addResource(new Iron(), 2);
            s.getInventory().addResource(new Uranium(), 0);
            stringLine("Telepesnél lévő nyersanyagok száma:" + s.getInventory().resourceCount(), 0);
            if(s.getInventory().isContain(new Carbon()))
                stringLine("Szén: " + 1, 1);
            if(s.getInventory().isContain(new IceWater()))
                stringLine("Vízjég: " + 3, 1);
            if(s.getInventory().isContain(new Iron()))
                stringLine("Vas: " + 2, 1);
            stringLine("", 0);
            stringLine("A telepes megkezdi a bázis építését...", 0);
            s.depositBase();
            stringLine("Az építés még nem fejeződött be,", 1);
            stringLine("szükség van további nyersanyagokra.", 1);
        }
        divingLine();
        testEnd();
    }

    public static void solarStormStrikeTest() throws IOException {
        titleLine("Test[13] - Solar Storm Strike");
        Game game = new Game(1, 1);
        Asteroid asteroid;
        int drilled = inputLineChar("Az aszteroida kőzetrétege át van fúrva? [I/N]");
        divingLine();
        int hollow = inputLineChar("Az aszteroida üreges? [I/N]");
        divingLine();
        boolean drilled_bool = (drilled == 'i' || drilled == 'I'), hollow_bool = (hollow == 'i' || hollow == 'I');

        if (drilled_bool && hollow_bool) {
            stringLine("Aszteroida átfúrva, üreges.", 0);
            asteroid = new Asteroid(null, 0, false, null);
        }
        else if (drilled_bool) {
            stringLine("Aszteroida átfúrva, nem üreges.", 0);
            asteroid = new Asteroid(new Iron(), 0, false, null);
        }
        else if (hollow_bool) {
            stringLine("Aszteroida nem átfúrva, üreges.", 0);
            asteroid = new Asteroid(null, 1, false, null);
        }
        else {
            stringLine("Aszteroida nem átfúrva, nem üreges.", 0);
            asteroid = new Asteroid(new Iron(), 1, false, null);
        }
        stringLine("Aszteroida generálva:", 0);
        stringLine("asteroid: " + asteroid, 1);
        stringLine("aszteroida vastagsága: " + asteroid.getThickness(), 2);
        stringLine("aszteroida tartalma: " + asteroid.getContains(), 2);
        Settler settler = new Settler(asteroid, game);
        stringLine("Telepes generálva:", 0);
        stringLine("settler: " + settler, 1);
        stringLine("A továbbiakban szimulálódik a napvihar. Ha meghal a telepes", 0);
        stringLine("akkor vesztenek a játékosok és ez kijelzésre kerül. Ha ", 1);
        stringLine("életben marad folytatódik a játék, erről nincs jelzés.", 1);
        divingLine();
        settler.solarStormStrike();

        testEnd();
    }


    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        boolean loop = true;
        while (loop) {
            titleLine("AsteroidMining - Skeleton");
            stringLine("Tesztesetek:", 0);
            stringLine(" 1: Move settler", 1);
            stringLine(" 2: Move robot", 1);
            stringLine(" 3: Place teleport", 1);
            stringLine(" 4: Mine", 1);
            stringLine(" 5: Put back resource", 1);
            stringLine(" 6: Drill", 1);
            stringLine(" 7: Drill icewater", 1);
            stringLine(" 8: Drill settler uranium", 1);
            stringLine(" 9: Drill robot uranium", 1);
            stringLine("10: Build robot", 1);
            stringLine("11: Build teleport", 1);
            stringLine("12: Deposit base", 1);
            stringLine("13: Solar storm strike", 1);
            divingLine();
            stringLine(" 0: Exit", 1);
            divingLine();

            int input = inputLineNumber("Válassz egy menüpontot!");
            switch (input) {
                case 0 -> loop = false;
                case 1 -> moveSettlerTest();
                case 2 -> moveRobotTest();
                case 3 -> placeTeleportTest();
                case 4 -> mineTest();
                case 5 -> putBackResource();
                case 6 -> drillTest();
                case 7 -> drillIcewaterTest();
                case 8 -> drillSettlerUraniumTest();
                case 9 -> drillRobotUraniumTest();
                case 10 -> buildRobot();
                case 11 -> buildTeleportTest();
                case 12 -> depositBase();
                case 13 -> solarStormStrikeTest();
            }
        }
    }
}
*/