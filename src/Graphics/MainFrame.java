package Graphics;

import Entities.Robot;
import Entities.Settler;
import Game.Game;
import Places.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Ennek az osztálynak a segítségével a felhasználó a játék telepeseit tudja irányítani,
 * és visszajelzéseket kap a pálya eseményeiről
 */
public class MainFrame extends JFrame{
    private final Graph graph;
    private Game game;
    private Place currentSelectedPlace;
    private JPanel mapPanel;
    private PlaceDataPanel placeDataPanel;
    private SettlerDataPanel settlerDataPanel;
    private GameDataPanel gameDataPanel;
    private final ArrayList<Line2D.Double> neighbourLines = new ArrayList<>();

    private class MapPanel extends JPanel{
        Image backgroundImage;
        public MapPanel(){
            super();
            backgroundImage = new ImageIcon("images//nebula.png").getImage();
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            graph.draw(game.getCurrentSettler().getPlace());
            calculateNeighbourLines();
            drawNeighbourLines(g);
            drawSelectionCircle(g);
        }
    }

    /**
     * Alapértelmezett konstruktor
     * Beállítja a Frame alap működését, beállíttatja a komponenseket
     * @param game a játék amit a Frame megjeleníteni, és irányítani fog
     * @param opener az a JFrame ami megnyitotta ezt az ablakot
     */
    public MainFrame(Game game, JFrame opener){
        super();
        this.game = game;
        //A játék egy külön szálon "fut", de cask az a szerepe,
        //hogy az aktuális telepes lépését megvárják a körök(és közbe futhasson a UI)
        Thread gameThread = new Thread(game);
        gameThread.start();

        //this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                opener.setVisible(true);
                game.endGameThread();
            }
        });

        initComponents();
        graph = new Graph(mapPanel);//ez az initComponents után kell legyen
        this.pack();
        setSize(new Dimension(1300, 750));
        setLocationRelativeTo(null);
        this.setVisible(true);

        updateGameView();
        setCurrentSelectedPlace(graph.getNodes().get(0));
    }

    /**
     * A Frame komponenseinek létrehozása, elhelyezése, beállítása
     */
    private void initComponents(){
        Container mainPane = this.getContentPane();
        mainPane.setLayout(new BorderLayout());

        mapPanel = new MapPanel();
        JPanel infoPanel = new JPanel();
        placeDataPanel = new PlaceDataPanel();
        settlerDataPanel = new SettlerDataPanel();
        gameDataPanel = new GameDataPanel();
        JPanel buttonPanel = new JPanel();

        //--------------------------
        //setting up component value
        //--------------------------
        Timer timer = new Timer(33, updateAction);

        JButton buildRobotButton = new JButton("Build Robot");
        JButton depositBaseButton = new JButton("Deposit Base");
        JButton moveButton = new JButton("Move");
        JButton buildTeleportButton = new JButton("Build Teleport");
        JButton placeTeleportButton = new JButton("Place Teleport");
        JButton endTurnButton = new JButton("End Turn");
        JButton mineButton = new JButton("Mine");
        JButton drillButton = new JButton("Drill");
        JButton putBackResourceButton = new JButton("Put Back Resource");

        buildRobotButton.addActionListener(buildRobotAction);
        depositBaseButton.addActionListener(depositBaseAction);
        moveButton.addActionListener(moveAction);
        buildTeleportButton.addActionListener(buildTeleportAction);
        placeTeleportButton.addActionListener(placeTeleportAction);
        endTurnButton.addActionListener(endTurnAction);
        mineButton.addActionListener(mineAction);
        drillButton.addActionListener(drillAction);
        putBackResourceButton.addActionListener(putBackResourceAction);

        //mapPanel.setBackground(new Color(39, 96, 176));
        mapPanel.setLayout(null);
        infoPanel.setBackground(new Color(64, 64, 64));
        infoPanel.setPreferredSize(new Dimension(1, 1));//igen az 1, 1 működik és kell ide
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setPreferredSize(new Dimension(390, 100));

        gameDataPanel.setBackground(new Color(53,53,53));
        settlerDataPanel.setBackground(new Color(64,64,64));
        placeDataPanel.setBackground(new Color(80,80,80));
        buttonPanel.setBackground(new Color(120,120,120));
        buttonPanel.setLayout(new GridLayout(5, 2));

        //--------------------------
        //adding components to panels
        //--------------------------
        GridBagConstraints c = new GridBagConstraints();

        buttonPanel.add(buildRobotButton);
        buttonPanel.add(depositBaseButton);
        buttonPanel.add(moveButton);
        buttonPanel.add(buildTeleportButton);
        buttonPanel.add(placeTeleportButton);
        buttonPanel.add(endTurnButton);
        buttonPanel.add(mineButton);
        buttonPanel.add(drillButton);
        buttonPanel.add(putBackResourceButton);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.2;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(gameDataPanel, c);
        c.weighty = 1;
        c.gridy = 2;
        infoPanel.add(settlerDataPanel, c);
        c.gridy = 3;
        infoPanel.add(placeDataPanel, c);
        c.weighty = 1;//0.3;
        c.gridy = 4;
        infoPanel.add(buttonPanel, c);

        mainPane.add(mapPanel, BorderLayout.CENTER);
        mainPane.add(infoPanel, BorderLayout.EAST);
    }

    /**
     * A kapott paraméter elmentése, mint kiválasztott hely
     * Ezen a helyen/helyre fognak bizonyos műveletek elvégződni
     * @param selected kiválasztott hely
     */
    public void setCurrentSelectedPlace(BaseGraphics selected){
        currentSelectedPlace = selected.getPlace();
        placeDataPanel.updateData(currentSelectedPlace);
        calculateNeighbourLines();
        revalidate();
        repaint();
    }

    /**
     * Az aktuálisan kiválasztott hely, és szomszédai közötti vonalak kiszámolása.
     * A vonalakat a neighbourLines listában tárolja az osztály.
     */
    public void calculateNeighbourLines(){
        BaseGraphics selectedGraphic = graph.getNodes().stream().filter(p -> p.getPlace()==currentSelectedPlace)
                .findFirst().orElse(null);
        if(selectedGraphic == null)
            return;
        ArrayList<Place> neighbours = currentSelectedPlace.getNeighbours();
        //a szomszédok grafikus objektumainak megkeresése
        ArrayList<BaseGraphics> neighbourGraphics = new ArrayList<>();
        for(BaseGraphics node : graph.getNodes()){
            if(neighbours.contains(node.getPlace()))
                neighbourGraphics.add(node);
        }
        //előző vonalak törlése
        neighbourLines.clear();
        //vonalak kiszámolása
        for(BaseGraphics node : neighbourGraphics){
            int x1 = selectedGraphic.getImageX() + selectedGraphic.getImageWidth()/2;
            int y1 = selectedGraphic.getImageY() + selectedGraphic.getImageHeight()/2;
            int x2 = node.getImageX() + node.getImageWidth()/2;
            int y2 = node.getImageY() + node.getImageHeight()/2;
            //vonal mentése
            neighbourLines.add(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    /**
     * A jelenleg kiválasztott helyet a szomszédaival összekötő vonalak kirajzolása
     * @param graphics rajzterület
     */
    public void drawNeighbourLines(Graphics graphics){
        Graphics2D g2d = (Graphics2D) graphics.create();
        //élsimítás bekapcsolása
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke dashed = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1, new float[]{10, 10}, 0);
        g2d.setStroke(dashed);
        g2d.setColor(Color.white);
        for(Line2D.Double line : neighbourLines){
            g2d.draw(new Line2D.Double(line.x1, line.y1, line.x2, line.y2));
        }
        g2d.dispose();
    }

    /**
     * Az aktuálisan kiválasztott hely köré kirajzol egy kört
     * @param graphics a rajzfelület
     */
    public void drawSelectionCircle(Graphics graphics){
        BaseGraphics selectedGraphic = graph.getNodes().stream().
                filter(p -> p.getPlace()==currentSelectedPlace).findFirst().
                orElse(null);
        if(selectedGraphic == null)
            return;

        Graphics2D g2d = (Graphics2D) graphics.create();
        //élsimítás bekapcsolása
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(209, 29, 29));
        int diameter = (int)(selectedGraphic.getImageWidth() * 1.3);
        int x = selectedGraphic.getImageX() - ((diameter - selectedGraphic.getImageWidth())/2);
        int y = selectedGraphic.getImageY() - ((diameter - selectedGraphic.getImageHeight())/2);
        g2d.drawOval(x, y, diameter, diameter);
        g2d.dispose();
    }

    /**
     * Ennek az ablaknak a bezárása
     * Indirekt módon elindítja a főmenü megjelenítését
     */
    private void CloseMainFrame(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * dokumentációban: MainFrame.update()
     * A játék jelenlegi állapota alapján, frissíti a megjelenített pályát.
     */
    private void updateGameView(){
        graph.update(game.getAsteroids(), game.getTeleports());
        graph.draw(game.getCurrentSettler().getPlace());
        settlerDataPanel.updateData(game.getCurrentSettler());
        gameDataPanel.updateData(game);
        if(currentSelectedPlace != null)
            placeDataPanel.updateData(currentSelectedPlace);
    }

    /**
     * Lekérdezi a játék adatait, és az alapján frissíti a játékfelületet.
     */
    private final ActionListener updateAction= e ->
        this.updateGameView();

    /**
     * A soron levő telepes megpróbál építeni egy robotot
     */
    private final ActionListener buildRobotAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        Place place = settler.getPlace();
        try {
            settler.build(new Robot(place, game));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál az aktuális helyén bázist építeni
     */
    private final ActionListener depositBaseAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        settler.depositBase();
        if(game.getGameEnded()){
            this.setEnabled(false);
            JOptionPane.showMessageDialog(this, game.getEndResult());
            CloseMainFrame();
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál lépni az aktuálisan kiválasztott helyre
     */
    private final ActionListener moveAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.move(currentSelectedPlace);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál egy teleportkapu párt építeni
     */
    private final ActionListener buildTeleportAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.build(new Teleport());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál egy teleportkaput lehelyezni
     */
    private final ActionListener placeTeleportAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.placeTeleport(settlerDataPanel.getSelectedTeleportIndex());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes befejezi a körét
     */
    private final ActionListener endTurnAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        synchronized (game.settlerTurnWaitLock) {
            game.settlerTurnWaitLock.notify();
        }
        //megvárja amíg ténylegesen az következő telepes lesz soron
        synchronized (game.settlerTurnWaitLock){
            try {
                game.settlerTurnWaitLock.wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        if(game.getGameEnded()){
            this.setEnabled(false);
            JOptionPane.showMessageDialog(this, game.getEndResult());
            CloseMainFrame();
        }
        else
            updateGameView();
    };

    /**
     * A soron levő telepes megpróbál bányászni
     */
    private final ActionListener mineAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.mine();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál fúrni
     */
    private final ActionListener drillAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.drill();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };

    /**
     * A soron levő telepes megpróbál egy nyersanyagot visszatenni az helyre ahol áll
     */
    private final ActionListener putBackResourceAction = e -> {
        Settler settler = game.getCurrentSettler();
        if(settler == null)
            return;
        try {
            settler.putBackResource(settlerDataPanel.getSelectedResource());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
        updateGameView();
    };
}
