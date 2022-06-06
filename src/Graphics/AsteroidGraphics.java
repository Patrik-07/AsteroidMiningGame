package Graphics;

import Places.Asteroid;
import Places.Place;

import javax.swing.*;

/**
 * Aszteroidát reprezentáló grafikus egység
 */
public class AsteroidGraphics extends BaseGraphics{
    private final Asteroid asteroid;

    public AsteroidGraphics(Asteroid asteroid, Icon img) {
        super(img);
        this.asteroid = asteroid;
    }

    @Override
    protected void setThisAsSelectedPlace() {
        JPanel mapPanel = (JPanel)getParent();
        //todo ezt a rákot meg kell fixelni xd
        MainFrame mainFrame = (MainFrame)mapPanel.getParent().getParent().getParent().getParent();
        mainFrame.setCurrentSelectedPlace(this);
    }

    @Override
    public Place getPlace() {
        return asteroid;
    }
}
