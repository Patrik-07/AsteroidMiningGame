package Graphics;

import Places.Place;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Egy valamilyen Place grafikus reprezentációja
 * A játék helyeit által alkotott gráf egy csúcsaként fog működni
 */
public abstract class BaseGraphics extends JPanel {
    private final JLabel imageLabel;
    private float virtualX, virtualY; //0 és 1 közötti koordináták, ami alapján
                                      //a valós koordináták lesznek kiszámolva a megjelenítésnél
    private final EntitiesPanel entitiesPanel;

    public static JLabel positionMarker;

    /**
     * Elsédleges paraméteres konstruktor. A nem paraméteres tagváltozókat 0-ra incializálja
     * @param img Place-t reprezentáló kép
     */
    public BaseGraphics(Icon img){
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        ((FlowLayout)getLayout()).setVgap(0);
        ((FlowLayout)getLayout()).setHgap(10);
        imageLabel = new JLabel("", img, JLabel.CENTER);
        setBounds(0, 0, img.getIconWidth()+100, img.getIconHeight());

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setThisAsSelectedPlace();
            }
        });

        entitiesPanel = new EntitiesPanel();

        add(imageLabel, 0);
        add(entitiesPanel, 1);
    }

    /**
     * Megjelöli a grafikus reprezentáló pozícióját, az aktív telepes helyét jelölő aurával.
     */
    public void markPosition() {
        int x = this.getImageX() + this.getImageWidth()/2 - positionMarker.getWidth()/2;
        int y = this.getImageY() + this.getImageHeight()/2 - positionMarker.getHeight()/2;
        positionMarker.setLocation(x, y);
    }

    public void updateEntitiesPanel(){
        entitiesPanel.update(this.getPlace());
    }

    public void setVirtualLocation(float x, float y){
        virtualX = x;
        virtualY = y;
    }

    public float getVirtualX() {
        return virtualX;
    }

    public float getVirtualY() {
        return virtualY;
    }

    /**
     * Eltolja a JLabel x értékét úgy hogy a kép x értékét adja ki
     * @return a kép x értéke
     */
    public int getImageX(){
        return super.getX() + imageLabel.getX();
    }

    /**
     * Eltolja a JLabel y értékét úgy hogy a kép y értékét adja ki
     * @return a kép y értéke
     */
    public int getImageY(){
        return super.getY() + imageLabel.getY();
    }

    /**
     * Eltolja a JLabel width értékét úgy hogy a kép width értékét adja ki
     * @return a kép width értéke
     */
    public int getImageWidth() {
        return imageLabel.getWidth();
    }

    /**
     * Eltolja a JLabel height értékét úgy hogy a kép height értékét adja ki
     * @return a kép height értéke
     */
    public int getImageHeight() {
        return imageLabel.getHeight();
    }

    /**
     * Eltolja a kapott koordinátákat úgy mintha a kép koordinátáit állítaná be
     */
    public void setImageLocation(int x, int y) {
        setLocation(x-imageLabel.getX(), y-imageLabel.getY());
    }

    /**
     * Megadja hogy melyik játékbeli helyet reprezentálja a grafikus elem
     * @return játékbeli hely
     */
    public abstract Place getPlace();

    protected abstract void setThisAsSelectedPlace();
}
