package ui;

import model.EventLog;
import model.LogPrinter;
import ui.panels.GamePanel;
import ui.panels.SplashScreenPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Main class; displays splash screen and then instantiates and runs Game
public class Main {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 450;
    private static final String FRAME_TITLE = "Lucky Dungeon";

    public static void main(String[] args) {
        JFrame splashFrame = new JFrame(FRAME_TITLE);
        SplashScreenPanel splashScreenPanel = new SplashScreenPanel(WIDTH, HEIGHT);
        splashFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        splashFrame.setSize(WIDTH, HEIGHT);
        splashFrame.add(splashScreenPanel);

        JFrame gameFrame = new JFrame(FRAME_TITLE);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Game game = new Game();
        GamePanel gamePanel = new GamePanel(game);
        gameFrame.setSize(WIDTH, HEIGHT);
        gameFrame.add(gamePanel);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logEvents();
            }
        });

        doIntroSequence(splashFrame, gameFrame);
    }

    // EFFECTS: shows splash screen and then goes to game frame
    private static void doIntroSequence(JFrame splashFrame, JFrame gameFrame) {
        splashFrame.setVisible(true);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // pass
        }

        splashFrame.setVisible(false);
        gameFrame.setVisible(true);
    }

    // EFFECTS: prints all Events in EventLog
    private static void logEvents() {
        LogPrinter.printLog(EventLog.getInstance());
    }
}
