package Graphics;

import Places.Place;

import javax.swing.*;
import java.awt.*;

public class EntitiesPanel extends JPanel {
    JLabel settlerCountLB = new JLabel("-");
    JLabel robotCountLB = new JLabel("-");
    JLabel ufoCountLB = new JLabel("-");

    public EntitiesPanel(){
        FlowLayout layout = new FlowLayout();
        layout.setHgap(2);
        layout.setVgap(0);
        setLayout(layout);

        setBackground(new Color(69, 69, 69));

        JLabel settlerIconLB = new JLabel("S");
        settlerIconLB.setForeground(Color.yellow);
        JLabel robotIconLB = new JLabel("R");
        robotIconLB.setForeground(Color.blue);
        JLabel ufoIconLB = new JLabel("U");
        ufoIconLB.setForeground(Color.red);

        settlerCountLB.setForeground(Color.yellow);
        robotCountLB.setForeground(Color.blue);
        ufoCountLB.setForeground(Color.red);

        add(settlerIconLB);
        add(settlerCountLB);
        add(robotIconLB);
        add(robotCountLB);
        add(ufoIconLB);
        add(ufoCountLB);
    }

    public void update(Place place){
        if(place == null)
            return;

        long settlerCount = place.getEntities().stream().filter(p -> p.toString().equals("Settler")).count();
        long robotCount = place.getEntities().stream().filter(p -> p.toString().equals("Robot")).count();
        long ufoCount = place.getEntities().stream().filter(p -> p.toString().equals("Ufo")).count();

        settlerCountLB.setText(String.valueOf(settlerCount));
        robotCountLB.setText(String.valueOf(robotCount));
        ufoCountLB.setText(String.valueOf(ufoCount));
    }
}
