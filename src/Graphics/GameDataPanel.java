package Graphics;

import Game.Game;

import javax.swing.*;
import java.awt.*;

public class GameDataPanel extends JPanel {
    private JLabel settlerCount, roundCount;
    private final Color textColor = Color.white;

    /**
     * Alapértelmezett konstruktor. Inicializáltatja a komponenseket.
     */
    public GameDataPanel(){
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

        settlerCount = newCustomJLabel("");
        roundCount = newCustomJLabel("");

        JPanel roundPane = newCustomJPanel();
        JPanel settlersPane = newCustomJPanel();

        roundPane.add(newCustomJLabel("Round:"));
        roundPane.add(roundCount);
        settlersPane.add(newCustomJLabel("Settlers:"));
        settlersPane.add(settlerCount);

        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        add(roundPane, c);
        c.gridx = 2;
        add(settlersPane, c);
    }

    /**
     * Frissíti az információt megjelenítő JLabel-ek tartalmát
     * @param game az a játék aminek az adatait meg kell jeleníteni
     */
    public void updateData(Game game){
        settlerCount.setText(String.valueOf(game.getSettlerCount()));
        roundCount.setText(String.valueOf(game.getRounds()));
    }
}
