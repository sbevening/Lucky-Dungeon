package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

// Utility class holding methods for styling Swing components
public final class StylingUtilities {
    // REQUIRES: thickness > 0; margin > 0
    // EFFECTS: returns a border of a given colour and thickness with given margin
    public static CompoundBorder createBorderWithMargin(Color color, int thickness, int margin) {
        Border lineBorder = BorderFactory.createLineBorder(color, thickness);
        Border emptyBoarder = BorderFactory.createEmptyBorder(margin, margin, margin, margin);
        return BorderFactory.createCompoundBorder(lineBorder, emptyBoarder);
    }
}
