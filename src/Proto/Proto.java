package Proto;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import Entities.*;
import Game.*;
import Places.*;
import Resources.*;

public class Proto {

    static Game game = new Game(5, 0);

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        boolean running = true;
        while(running) {
            running = commandReader(s);
        }
        s.close();
    }

    static boolean commandReader(Scanner s) {
        String nextline = "";
        try {
            nextline = s.nextLine();
        }catch(Exception e) {System.out.println(e.getMessage());}
        if(nextline.length() <= 1) return false;
        String[] input = nextline.split(" ");

        if(input[0].equals("listEntities")) listEntities(input);
        else if(input[0].equals("listPlaces")) listPlaces(input);
        else if(input[0].equals("listNeighbour")) listNeighbour(input);
        else if(input[0].equals("listAsteroidData")) listAsteroidData(input);
        else if(input[0].equals("listEntityData")) listEntityData(input);
        else if(input[0].equals("test"))
            if(input[1].equals("full"))
                testFull(input);
            else
                test(input);
        else if(input[0].equals("setEntitySteps")) setEntitySteps(input);
        else if(input[0].equals("setSunClose")) setSunClose(input);
        else if(input[0].equals("startNewRound")) startNewRound(input);
        else if(input[0].equals("addAsteroid")) addAsteroid(input);
        else if(input[0].equals("addTeleports")) addTeleports(input);
        else if(input[0].equals("setPlaceNeighbour")) setPlaceNeighbour(input);
        else if(input[0].equals("addEntity")) addEntity(input);
        else if(input[0].equals("setEntityInventory")) setEntityInventory(input);
        else if(input[0].equals("entityMove")) entityMove(input);
        else if(input[0].equals("entityDrill")) entityDrill(input);
        else if(input[0].equals("entityMine")) entityMine(input);
        else if(input[0].equals("putBackResource")) putBackResource(input);
        else if(input[0].equals("buildRobot")) buildRobot(input);
        else if(input[0].equals("buildTeleport")) buildTeleport(input);
        else if(input[0].equals("placeTeleport")) placeTeleport(input);
        else if(input[0].equals("depositBase")) depositBase(input);
        else if(input[0].equals("solarStorm")) solarStorm(input);

        return true;
    }

    static String boolToString(boolean b) {
        if (b)
            return "T";
        return "F";
    }

    static boolean stringToBool (String s) {
        return s.equals("T");
    }

    static String resToString (Resource res) {
        if (res == null)
            return "N";
        else if (Uranium.class.equals(res.getClass())) {
            return "U";
        } else if (Iron.class.equals(res.getClass())) {
            return "I";
        } else if (IceWater.class.equals(res.getClass())) {
            return "W";
        } else
            return "C";
    }

    static Resource stringToRes (String s) {
        return switch (s) {
            case "N" -> null;
            case "U" -> new Uranium();
            case "I" -> new Iron();
            case "W" -> new IceWater();
            default -> new Carbon();
        };
    }

    static Place IDToPlace(String ID){
        ArrayList<Asteroid> asteroids = game.getAsteroids();
        ArrayList<Teleport> teleports = game.getTeleports();

        if(ID.charAt(0) == 'A'){
            return asteroids.get(Integer.parseInt(ID.substring(1)));
        }
        else if(ID.charAt(0) == 'T'){
            return teleports.get(Integer.parseInt(ID.substring(1)));
        }
        return null;
    }

    static String PlaceToID(Place place){
        ArrayList<Asteroid> asteroids = game.getAsteroids();
        ArrayList<Teleport> teleports = game.getTeleports();
        String placeType;
        int placeIndex;
        if(place instanceof Asteroid){
            placeType = "A";
            placeIndex = asteroids.indexOf(place);
        }
        else if(place instanceof Teleport){
            placeType = "T";
            placeIndex = teleports.indexOf(place);
        }
        else{
            placeType = "Unknown";
            placeIndex = -1;
        }
        return placeType + placeIndex;
    }

    static void listEntities(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            String entityType;
            if(entity instanceof Settler)
                entityType = "S";
            else if(entity instanceof Robot)
                entityType = "R";
            else if(entity instanceof Ufo)
                entityType = "U";
            else
                entityType = "Unknown";
            String placeID = PlaceToID(entities.get(i).getPlace());

            String out = String.format("ID:%d Type:%s Place:%s", i, entityType, placeID);
            System.out.println(out);
        }
    }

    static void listPlaces(String[] input){
        String output = "";
        int index = 0;
        for (int i = 0; i < game.getAsteroids().size(); i++) {
            output = output.concat("ID:A" + index + "\n");
            index++;
        }
        index = 0;
        for (int i = 0; i < game.getTeleports().size(); i++) {
            output = output.concat("ID:T" + index + "\n");
            index++;
        }
        System.out.print(output);
    }

    static void listNeighbour(String[] input){
        if(input.length < 2){
            System.out.println("Not enough arguments");
            return;
        }
        Place p;
        int index = Integer.parseInt(input[1].substring(1));
        if(input[1].charAt(0) == 'A') p = game.getAsteroids().get(index);
        else if(input[1].charAt(0) == 'T') p = game.getTeleports().get(index);
        else{
            System.out.println("Format error");
            return;
        }
        String output = "";
        for(int i = 0; i < p.getNeighbours().size(); i++){
            if(p.getNeighbours().get(i) instanceof Asteroid){
                output = output.concat("ID:A" + game.getAsteroids().indexOf(p.getNeighbours().get(i)) + "\n");
            }
            if(p.getNeighbours().get(i) instanceof Teleport){
                output = output.concat("ID:T" + game.getAsteroids().indexOf(p.getNeighbours().get(i)) + "\n");
            }
        }
        System.out.print(output);
    }

    static void listAsteroidData(String[] input){
        if (input.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }
        if (input[1].charAt(0) != 'A') {
            System.out.println("Invalid argument: it must be an asteroid");
        }
        String output = "";
        Asteroid current = (Asteroid)IDToPlace(input[1]);
        output = output.concat("Thickness:" + current.getThickness() + " SunClose:" + boolToString(current.getSunClose())
                + " ResourceID:" + resToString(current.getContains()));
        if (current.getContains() != null && current.getContains().getClass() == Uranium.class) {
            Uranium res = (Uranium) current.getContains();
            output = output.concat(" ExposeCount:" + res.getExposeCount());
        }
        output = output.concat("\n");
        System.out.print(output);
    }

    static void listEntityData(String[] input){
        Entity entity = game.getEntities().get(Integer.parseInt(input[1]));
        String inventory = "";
        String teleportInventory = "";

        if(entity instanceof Settler){
            Settler settler = (Settler)entity;
            inventory = "Inventory:";
            if(settler.getInventory().resourceCount() != 0){
                ArrayList<Resource> inv = settler.getInventory().getList();
                for (int i = 0; i < inv.size(); i++)
                    inventory += i + " " + resToString(inv.get(i)) + " ";
            }
            ArrayList<Teleport> teleports = settler.getTeleports();
            teleportInventory = "Teleport gates: ";
            if(teleports.size() != 0){
                for(int i = 0; i < teleports.size(); ++i){
                    teleportInventory = teleportInventory.concat("ID:"+i+"Pair:"+teleports.get(i).getPair());
                }
            }
        }
        String stepsCount = String.valueOf(entity.getSteps());
        String PlaceID = PlaceToID(entity.getPlace());
        String out = "";
        if(!inventory.equals(""))
            out = out.concat(inventory + "\n");
        if(!teleportInventory.equals(""))
            out = out.concat(teleportInventory + "\n");
        out = out.concat("StepsCount:" + stepsCount + "\n");
        out = out.concat("Place:" + PlaceID);

        System.out.println(out);
    }

    static void testFull(String[] input){
        //running 35 tests
        String tempInput[] = {"test","0","outoff"};
        for (int i = 1; i <= 35; i++) {
            System.out.println("\nStarting test " + i + ":");
            tempInput[1] = String.valueOf(i);
            test(tempInput);
        }
    }

    static void test(String[] input){
        boolean outoff = false;
        if(input.length >= 3 && input[2].equals("outoff"))
            outoff = true;

        //redirecting output stream to savable stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        if(input.length < 2){
            System.out.println("Not enough arguments");
            return;
        }
        String inputFileName = "Tests\\test" + Integer.parseInt(input[1]) + ".txt";
        Scanner s;
        String anticipatedOutput = "";
        try {
            game = new Game(5, 0);
            s = new Scanner(new FileInputStream(inputFileName));
            boolean running = true;
            while(running){
                running = commandReader(s);
            }
            while(s.hasNext()){
                anticipatedOutput = anticipatedOutput.concat(s.nextLine() + "\n");
            }
        }catch(Exception e){
            System.out.println("Couldn't open the input file");
            e.printStackTrace();
            return;
        }
        //resetting output to console
        System.out.flush();
        System.setOut(old);
        String realOutput = baos.toString();

        //some invisible character always generated an error
        //Testing the real output against the anticipated one
        String[] splitRealOut = realOutput.split("\n");
        String[] strippedRealOut = new String[splitRealOut.length];
        for (int i = 0; i < splitRealOut.length; i++) {
            strippedRealOut[i] = splitRealOut[i].strip();
        }
        String[] splitExpectedOut = anticipatedOutput.split("\n");
        String[] strippedExpectedOut = new String[splitExpectedOut.length];
        for (int i = 0; i < splitExpectedOut.length; i++) {
            strippedExpectedOut[i] = splitExpectedOut[i].strip();
        }
        boolean testSuccessful = true;
        for (int i = 0; i < strippedExpectedOut.length; i++) {
            if(!strippedExpectedOut[i].equals(strippedRealOut[i])){
                System.out.println("Error at line " + (i + 1) + ":");
                System.out.println("Expected: " + strippedExpectedOut[i]);
                System.out.println("Got:      " + strippedRealOut[i]);
                testSuccessful = false;
                break;
            }
        }
        if(outoff) {
            if (testSuccessful)
                System.out.println("Test " + input[1] + " successful!");
        }
        else{
            System.out.println(realOutput);
        }

        s.close();
    }

    static void setEntitySteps(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        entity.setSteps(Integer.parseInt(input[2]));
    }

    static void setSunClose(String[] input){
        Place place = IDToPlace(input[1]);
        if(place instanceof Asteroid){
            Asteroid asteroid = (Asteroid) place;
            asteroid.setSunClose(stringToBool(input[2]));
        }
    }

    static void startNewRound(String[] input){
        game.setSunCloseProbability(1);
        game.newRound();
    }

    static void addAsteroid(String[] input){
        int thickness = Integer.parseInt(input[1]);
        Resource resource = stringToRes(input[2]);
        boolean sunClose;
        if(input[2].equals("U")) {
            ((Uranium)resource).setExposeCount(Integer.parseInt(input[3]));
            sunClose = stringToBool(input[4]);
        }
        else{
            sunClose = stringToBool(input[3]);
        }

        game.addAsteroid(new Asteroid(resource, thickness, sunClose, game));
    }

    static void addTeleports(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Teleport t1 = new Teleport();
        Teleport t2 = t1.getPair();
        ArrayList<Teleport> teleports = new ArrayList<>();
        teleports.add(t1);
        teleports.add(t2);
        for (int i = 0; i < 2; ++i) {
            Teleport t = teleports.get(i);
            if (Character.isDigit(input[i+1].charAt(0))) {
                Entity e = entities.get(Integer.parseInt(input[i+1]));
                if (e instanceof Settler) {
                    ((Settler)e).addTeleport(t);
                }
            } else {
                Place p = IDToPlace(input[i+1]);
                if (p != null) {
                    p.addNeighbour(t);
                    t.addNeighbour(p);
                    game.addTeleport(t);
                }
            }
        }
    }

    static void setPlaceNeighbour(String[] input){
        Place p1 = IDToPlace(input[1]);
        Place p2 = IDToPlace(input[2]);
        if(p1 != null && p2 != null) {
            p1.addNeighbour(p2);
            p2.addNeighbour(p1);
        }
    }

    static void addEntity(String[] input){
        Place place = IDToPlace(input[1]);
        int stepsCount = Integer.parseInt(input[3]);
        boolean isSettler = false;
        if(input[2].equals("S"))
            isSettler = true;
        Entity e = switch (input[2]) {
            case "S" -> new Settler(place, game);
            case "R" -> new Robot(place, game);
            case "U" -> new Ufo(place, game);
            default -> null;
        };
        if(e != null){
            e.setSteps(stepsCount);
            game.addEntity(e, isSettler);
        }
    }

    static void setEntityInventory(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if (entity instanceof Settler) {
            for (int i = 2; i < input.length; i+=2)
                for (int j = 0; j < Integer.parseInt(input[i+1]); j++)
                    ((Settler)entity).getInventory().getList().add(stringToRes(input[i]));
        }
    }

    static void entityMove(String[] input){
        if (input.length != 3) {
            System.out.println("Nem megfelelő számú argumentum");
            return;
        }
        try {
            game.getEntities().get(Integer.parseInt(input[1])).move(IDToPlace(input[2]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void entityDrill(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        try {
            entity.drill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void entityMine(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        try {
            entity.mine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void putBackResource(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if (entity instanceof Settler) {
            try {
                ((Settler)entity).putBackResource(stringToRes(input[2]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void buildRobot(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if(entity instanceof Settler){
            try {
                ((Settler)entity).build(new Robot(entity.getPlace(), game));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void buildTeleport(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if(entity instanceof Settler){
            try {
                ((Settler)entity).build(new Teleport());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void placeTeleport(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if(entity instanceof Settler){
            try {
                ((Settler)entity).placeTeleport(Integer.parseInt(input[2]));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void depositBase(String[] input){
        ArrayList<Entity> entities = game.getEntities();
        Entity entity = entities.get(Integer.parseInt(input[1]));
        if(entity instanceof Settler){
            ((Settler)entity).depositBase();
        }
    }

    static void solarStorm(String[] input){
        Asteroid asteroid = (Asteroid)IDToPlace(input[1]);
        if(asteroid != null)
            asteroid.solarStorm(Integer.parseInt(input[2]));
    }
}
