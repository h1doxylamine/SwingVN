import screens.MainMenu;
import screens.Novel;

import javax.swing.*;

import core.Engine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Main {

    static int HEIGHT = 900;
    static int WIDTH = 1250;

    public static void main(String[] args) {

        Script script;

        JFrame frame = new JFrame("SwingVN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);

        // Panel conteneur avec letterboxing
        JPanel containerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Peindre le fond en noir
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        containerPanel.setLayout(null);
        containerPanel.setBackground(Color.BLACK);


        ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();


        JMenuBar menuBar = new JMenuBar();
        JMenuItem auto = new JMenuItem("Auto");
        JMenuItem skip = new JMenuItem("Skip");
        JMenuItem logs = new JMenuItem("Logs");
        JMenuItem saves = new JMenuItem("Save");

        menuItems.add(auto);
        menuItems.add(skip);
        menuItems.add(logs);
        menuItems.add(saves);

        for (JMenuItem menu : menuItems) {
            menuBar.add(menu);
            menu.setMargin(new java.awt.Insets(0, 2, 0, 2));
        }

        menuBar.add(Box.createHorizontalGlue());

        auto.addActionListener(e  ->  {
            Engine.getInstance().isAutoOn = !Engine.getInstance().isAutoOn;
            System.out.println("Auto mode :" + Engine.getInstance().isAutoOn);
        });

        skip.addActionListener(e -> {
            Engine.getInstance().isSkipOn = !Engine.getInstance().isSkipOn;
            System.out.println("Skip mode :" + Engine.getInstance().isAutoOn);
        });

        logs.addActionListener(e -> {
            Engine.getInstance().logs();
        });





        Novel novelPanel = new Novel();
        novelPanel.setBounds(0, 0, WIDTH, HEIGHT);
        containerPanel.add(novelPanel);
        frame.setJMenuBar(menuBar);
        frame.add(containerPanel);
        frame.setVisible(true);

        Engine.getInstance().novel = novelPanel;
        Engine.getInstance().play(new Script());

    }
}