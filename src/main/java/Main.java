import screens.MainMenu;
import screens.Novel;

import javax.swing.*;
import javax.swing.border.LineBorder;

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


        ArrayList<JButton> menuItems = new ArrayList<>();


        JMenuBar menuBar = new JMenuBar();
        JButton auto = new JButton("Auto");
        JButton skip = new JButton("Skip");
        JButton logs = new JButton("Logs");
        JButton saves = new JButton("Save");

        menuItems.add(auto);
        menuItems.add(skip);
        menuItems.add(logs);
        menuItems.add(saves);

        for (JButton menu : menuItems) {
            menu.setBackground(Color.white);
            menu.setBorder(new LineBorder(Color.white, 5));
            menuBar.add(menu);
        }

        menuBar.add(Box.createHorizontalGlue());
        menuBar.setBackground(Color.WHITE);

        auto.addActionListener(e  ->  {
            Engine.getInstance().isAutoOn = !Engine.getInstance().isAutoOn;

            if (Engine.getInstance().isAutoOn) {
                auto.setBackground(Color.lightGray);
                auto.setBorder(new LineBorder(Color.lightGray, 5));
            } else {
                auto.setBackground(Color.white);
                auto.setBorder(new LineBorder(Color.white, 5));
            }

            System.out.println("Auto mode :" + Engine.getInstance().isAutoOn);
        });

        skip.addActionListener(e -> {
            Engine.getInstance().isSkipOn = !Engine.getInstance().isSkipOn;

            if (Engine.getInstance().isSkipOn) {
                skip.setBackground(Color.lightGray);
                skip.setBorder(new LineBorder(Color.lightGray, 5));
            } else {
                skip.setBackground(Color.white);
                skip.setBorder(new LineBorder(Color.white, 5));
            }

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