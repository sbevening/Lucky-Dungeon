package ui.panels;

import ui.StylingUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// panel that manages and displays a given number of the most recent messages at a time
// generated throughout playing game that operates like a circular queue
public class GameMessagesPanel extends JPanel implements Refreshable {
    private static final int FONT_SIZE = 20;
    private ArrayList<String> displayedMessages;

    // number of message strings to display at once
    private int numDisplayed;

    // REQUIRES: numDisplayed > 0
    // MODIFIES: this
    // EFFECTS: constructs a GameMessagesPanel that displays given number of messages at once
    // and is linked to the Game of a given GamePanel
    public GameMessagesPanel(int numDisplayed) {
        setLayout(new GridLayout(numDisplayed, 1));
        setBorder(StylingUtilities.createBorderWithMargin(Color.black, 2, 2));
        displayedMessages = new ArrayList<>();
        displayedMessages.add("You enter the dungeon");

        this.numDisplayed = numDisplayed;
        drawMessages();
    }

    // MODIFIES: this
    // EFFECTS: adds a message to displayedMessage. then if the new displayedMessage.size() > numDisplayed,
    // removes oldest (first) element from list
    public void addMessage(String message) {
        displayedMessages.add(message);
        int numMessages = displayedMessages.size();
        if (numMessages > numDisplayed) {
            displayedMessages.remove(0);
            refresh();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds text for each displayed message to panel
    private void drawMessages() {
        for (String message : displayedMessages) {
            Label messageLabel = new Label(message);
            messageLabel.setFont(new Font("Courier", 0, FONT_SIZE));
            add(messageLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes this panel, so it displays current state of displayedMessages
    @Override
    public void refresh() {
        removeAll();
        drawMessages();
        validate();
        updateUI();
    }
}
