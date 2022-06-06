package Graphics;

import Places.Place;
import Places.Teleport;

import javax.swing.*;
import java.awt.*;

/**
 * Teleportot reprezentáló grafikus egység
 */
public class TeleportGraphics extends BaseGraphics{
    private Teleport teleport;

    public TeleportGraphics(Teleport teleport, Icon img) {
        super(img);
        this.teleport = teleport;
    }

    @Override
    protected void setThisAsSelectedPlace() {
        JPanel mapPanel = (JPanel)getParent();
        //todo ez is nagyon rákos
        MainFrame mainFrame = (MainFrame)mapPanel.getParent().getParent().getParent().getParent();
        mainFrame.setCurrentSelectedPlace(this);
    }

    @Override
    public Place getPlace() {
        return teleport;
    }
}
