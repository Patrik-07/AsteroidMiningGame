package Graphics;

import Entities.Settler;
import Places.Teleport;
import Resources.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SettlerDataPanel extends JPanel {
    private JLabel steps, teleportGates;
    private JLabel iron, carbon, iceWater, uranium;
    private JComboBox<Teleport> teleportCB;
    private JComboBox<Resource> inventoryCB;
    private final Color textColor = Color.white;

    /**
     * Alapértelmezett konstruktor. Inicializáltatja a komponenseket.
     */
    public SettlerDataPanel(){
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

        teleportCB = new JComboBox<>();
        inventoryCB = new JComboBox<>();

        steps = newCustomJLabel("-");
        teleportGates = newCustomJLabel("-");
        iron = newCustomJLabel("-");
        carbon = newCustomJLabel("-");
        iceWater = newCustomJLabel("-");
        uranium = newCustomJLabel("-");

        JPanel typePane = newCustomJPanel();
        JPanel stepsPane = newCustomJPanel();
        JPanel teleportsPane = newCustomJPanel();
        JPanel inventoryPane = newCustomJPanel();
        JPanel ironPane = newCustomJPanel();
        JPanel carbonPane = newCustomJPanel();
        JPanel iceWaterPane = newCustomJPanel();
        JPanel uraniumPane = newCustomJPanel();

        typePane.add(newCustomJLabel("Settler:"));
        stepsPane.add(newCustomJLabel("Steps:"));
        stepsPane.add(steps);
        teleportsPane.add(newCustomJLabel("Teleport gates:"));
        teleportsPane.add(teleportGates);
        teleportsPane.add(teleportCB);
        inventoryPane.add(newCustomJLabel("Inventory:"));
        inventoryPane.add(inventoryCB);
        ironPane.add(newCustomJLabel("Iron"));
        ironPane.add(iron);
        carbonPane.add(newCustomJLabel("Carbon"));
        carbonPane.add(carbon);
        iceWaterPane.add(newCustomJLabel("IceWater"));
        iceWaterPane.add(iceWater);
        uraniumPane.add(newCustomJLabel("Uranium"));
        uraniumPane.add(uranium);

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
        add(stepsPane, c);
        c.gridy = 3;
        c.ipadx = indentation*indentationAmount;
        add(teleportsPane, c);
        c.gridy = 4;
        c.ipadx = indentation*indentationAmount;
        add(inventoryPane, c);
        indentation += 1;
        c.gridy = 5;
        c.ipadx = indentation*indentationAmount;
        add(ironPane, c);
        c.gridy = 6;
        c.ipadx = indentation*indentationAmount;
        add(carbonPane, c);
        c.gridy = 7;
        c.ipadx = indentation*indentationAmount;
        add(iceWaterPane, c);
        c.gridy = 8;
        c.ipadx = indentation*indentationAmount;
        add(uraniumPane, c);
    }

    /**
     * Frissíti az információt megjelenítő JLabel-ek tartalmát
     * @param settler az a telepes aminek az adatait meg kell jeleníteni
     */
    public void updateData(Settler settler){
        teleportCB.removeAllItems();
        for (Teleport teleport : settler.getTeleports()) {
            teleportCB.addItem(teleport);
        }
        inventoryCB.removeAllItems();
        for (Resource resource : settler.getInventory().getList()){
            inventoryCB.addItem(resource);
        }
        steps.setText(String.valueOf(settler.getSteps()));
        teleportGates.setText(String.valueOf(settler.getTeleports().size()));
        ArrayList<Resource> inventory = settler.getInventory().getList();
        iron.setText(String.valueOf(inventory.stream().filter(p -> p.typeString().equals("Iron")).count()));
        carbon.setText(String.valueOf(inventory.stream().filter(p -> p.typeString().equals("Carbon")).count()));
        iceWater.setText(String.valueOf(inventory.stream().filter(p -> p.typeString().equals("IceWater")).count()));
        uranium.setText(String.valueOf(inventory.stream().filter(p -> p.typeString().equals("Uranium")).count()));
    }

    /**
     * Visszaadja a telepes rakteréből aktuálisan kiválasztott nyersanyagot.
     * @return kiválasztott nyersanyag referenciája
     */
    public Resource getSelectedResource(){
        return (Resource) inventoryCB.getSelectedItem();
    }

    /**
     * Visszaadja a telepes rakteréből aktuálisan kiválasztott teleport indexét.
     * @return kiválasztott teleport indexe
     */
    public int getSelectedTeleportIndex(){
        return teleportCB.getSelectedIndex();
    }
}
