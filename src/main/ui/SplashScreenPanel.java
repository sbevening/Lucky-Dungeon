package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// class for displaying a splash screen that shows when the game is launched
public class SplashScreenPanel extends JPanel {
    private Image image;
    private final int width;
    private final int height;

    // EFFECTS: constructs a panel containing splash screen image of given width and height
    public SplashScreenPanel(int width, int height) {
        setLayout(new BorderLayout());
        this.width = width;
        this.height = height;
        try {
            image = ImageIO.read(new File("./data/SplashScreen.png"));
        } catch (IOException e) {
            // leave null/blank
        }
    }

    // MODIFIES: this
    // EFFECTS: overrides super.paintComponent(Graphics) so that the image is scaled to proper width and height
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            g.drawImage(scaledImage, 0, 0, this);
        }
    }
}
