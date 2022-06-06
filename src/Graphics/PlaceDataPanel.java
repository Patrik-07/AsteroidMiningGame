package Graphics;

import Places.Asteroid;
import Places.Place;
import Resources.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlaceDataPanel extends JPanel {
    private JLabel type, thickness, resource, sunCloseness;
    private JLabel baseIronReq, baseCarbonReq, baseIceWaterReq, baseUraniumReq;
    private final Color textColor = Color.white;

    /**
     * Alapértelmezett konstruktor. Inicializáltatja a komponenseket.
     */
    public PlaceDataPanel(){
        super();
        initComponents();
    }

    /**
     * Létrehoz egy új JLabel objektumot, aminek beállítja a szövegét
     * és a betűszínét.
     * @param text az új JLabel szövege
     * @return az új JLabel objektum
     */
    private JLabel newCustomJLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));
        return label;
    }

    /**
     * Létrehoz egy új JPanel objektumot, aminek beállítja az áttetszőségét
     * és hogy ne legyen margója.
     * @return az új JPanel objektum
     */
    private JPanel newCustomJPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        panel.setLayout(layout);
        return panel;
    }

    /**
     * A komponensek létrehozása, beállítása, és elhelyezése.
     */
    private void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        type = newCustomJLabel("-");
        thickness = newCustomJLabel("-");
        resource = newCustomJLabel("-");
        sunCloseness = newCustomJLabel("-");
        baseIronReq = newCustomJLabel("-");
        baseCarbonReq = newCustomJLabel("-");
        baseIceWaterReq = newCustomJLabel("-");
        baseUraniumReq = newCustomJLabel("-");

        JPanel typePane = newCustomJPanel();
        JPanel thicknessPane = newCustomJPanel();
        JPanel resourcePane = newCustomJPanel();
        JPanel sunClosenessPane = newCustomJPanel();
        JPanel baseResReqPane = newCustomJPanel();
        JPanel ironReqPane = newCustomJPanel();
        JPanel carbonReqPane = newCustomJPanel();
        JPanel iceWaterReqPane = newCustomJPanel();
        JPanel uraniumReqPane = newCustomJPanel();

        typePane.add(type);
        thicknessPane.add(newCustomJLabel("Thickness:"));
        thicknessPane.add(thickness);
        resourcePane.add(newCustomJLabel("Resource:"));
        resourcePane.add(resource);
        sunClosenessPane.add(sunCloseness);
        baseResReqPane.add(newCustomJLabel("Base res. required:"));
        ironReqPane.add(newCustomJLabel("Iron"));
        ironReqPane.add(baseIronReq);
        carbonReqPane.add(newCustomJLabel("Carbon"));
        carbonReqPane.add(baseCarbonReq);
        iceWaterReqPane.add(newCustomJLabel("IceWater"));
        iceWaterReqPane.add(baseIceWaterReq);
        uraniumReqPane.add(newCustomJLabel("Uranium"));
        uraniumReqPane.add(baseUraniumReq);

        int indentation = 0;
        int indentationAmount = 100;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 0;
        add(typePane, c);
        indentation += 1;
        c.gridy = 2;
        c.ipadx = indentation*indentationAmount;
        add(thicknessPane, c);
        c.gridy = 3;
        c.ipadx = indentation*indentationAmount;
        add(resourcePane, c);
        c.gridy = 4;
        c.ipadx = indentation*indentationAmount;
        add(sunClosenessPane, c);
        c.gridy = 5;
        c.ipadx = indentation*indentationAmount;
        add(baseResReqPane, c);
        indentation += 1;
        c.gridy = 6;
        c.ipadx = indentation*indentationAmount;
        add(ironReqPane, c);
        c.gridy = 7;
        c.ipadx = indentation*indentationAmount;
        add(carbonReqPane, c);
        c.gridy = 8;
        c.ipadx = indentation*indentationAmount;
        add(iceWaterReqPane, c);
        c.gridy = 9;
        c.ipadx = indentation*indentationAmount;
        add(uraniumReqPane, c);
    }

    /**
     * Frissíti az információt megjelenítő JLabel-ek tartalmát
     * @param place az a hely aminek az adatait meg kell jeleníteni
     */
    public void updateData(Place place){
        if(place.getThickness() == -1){ //teleportra értelmezett
            type.setText("Teleport:");
            thickness.setText("-");
            sunCloseness.setText("-");
            baseIronReq.setText("-");
            baseCarbonReq.setText("-");
            baseIceWaterReq.setText("-");
            baseUraniumReq.setText("-");
        } else { //asteroidára értelmezett
            Asteroid asteroid = (Asteroid) place;
            type.setText("Asteroid:");
            thickness.setText(String.valueOf(place.getThickness()));

            if(asteroid.getSunClose()){
                sunCloseness.setText("Close to sun.");
            } else {
                sunCloseness.setText("Far from sun.");
            }
            ArrayList<Resource> baseReqResources = asteroid.getBaseRequiredResources();
            long iron = baseReqResources.stream().filter(p -> p.typeString().equals("Iron")).count();
            long carbon = baseReqResources.stream().filter(p -> p.typeString().equals("Carbon")).count();
            long iceWater = baseReqResources.stream().filter(p -> p.typeString().equals("IceWater")).count();
            long uranium = baseReqResources.stream().filter(p -> p.typeString().equals("Uranium")).count();
            baseIronReq.setText(String.valueOf(iron));
            baseCarbonReq.setText(String.valueOf(carbon));
            baseIceWaterReq.setText(String.valueOf(iceWater));
            baseUraniumReq.setText(String.valueOf(uranium));
        }
        //teleportra és aszteroidára is értelmezett
        String contains = "-";
        if(place.getContains() != null) {
            contains = place.getContains().toString();
        }
        resource.setText(contains);
    }
}
