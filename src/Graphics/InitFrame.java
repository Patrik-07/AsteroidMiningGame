package Graphics;
import Game.Game;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;

/**
 * A főmenü osztálya. Ez jelenik meg amikor elindul a program.
 * Feladata a játék elindítása, vagy kilépés az alkalmazásból.
 */
public class InitFrame extends JFrame {
    private JPanel startButtonPanel;
    private JButton startButton;
    private JPanel exitButtonPanel;
    private JButton exitButton;
    private JPanel titlePanel;
    private JLabel title;

    /**
     * Alapértelmezett konstruktor. Beállítja a Frame alap működését.
     * Beállíttatja a komponenseket.
     */
    public InitFrame(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Asteroid Mining");
        setResizable(false);
        setMinimumSize(new Dimension(350, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(64, 64, 64));

        initComponents();
        this.pack();
        this.setVisible(true);
    }

    /**
     * A Frame komponenseinek létrehozása, elhelyezése, beállítása.
     */
    private void initComponents(){
        this.setLayout(new GridLayout(3,1));

        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(64, 64, 64));
        title = new JLabel("Asteroid Mining", SwingConstants.CENTER);
        title.setFont(new Font("Arial Black", Font.PLAIN, 35));
        title.setForeground(Color.LIGHT_GRAY);
        titlePanel.add(title, BorderLayout.CENTER);

        startButtonPanel = new JPanel();
        startButtonPanel.setBackground(new Color(64, 64, 64));
        startButton = new JButton("Start");
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setPreferredSize(new Dimension(200, 75));
        startButtonPanel.add(startButton);

        exitButtonPanel = new JPanel();
        exitButtonPanel.setBackground(new Color(64, 64, 64));
        exitButton = new JButton("Exit");
        exitButton.setBackground(Color.LIGHT_GRAY);
        exitButton.setPreferredSize(new Dimension(200, 75));
        exitButtonPanel.add(exitButton);


        startButton.addActionListener(e -> startGame());
        exitButton.addActionListener(e -> System.exit(0));

        add(titlePanel);
        this.add(startButtonPanel);
        this.add(exitButtonPanel);
    }

    /**
     * Megnyit egy új játék ablakot, és elrejti a menü ablakát.
     */
    private void startGame(){
        Game game = new Game(5, 5);
        game.newGame(5);
        MainFrame mainFrame = new MainFrame(game, this);
        this.setVisible(false);
    }
}
