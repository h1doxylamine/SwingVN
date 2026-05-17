package screens.components;

import core.Engine;
import managers.LogData;
import managers.ScriptManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class LogsMenu extends JPanel {

    int x,y;
    int width,height;

    public LogsMenu(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setBackground(new Color(255, 255, 255, 200));
        setLayout(null);
        setVisible(false);
    }

    public void render(ArrayList<LogData> data) {
        removeAll();

        if (isVisible()) {
            int yPosition = 10;

            JPanel logsPanel = new JPanel();
            logsPanel.setLayout(null);
            logsPanel.setOpaque(false);

            for (LogData log : data) {
                ScriptManager.Chara chara = log.chr();
                String text = log.text();

                if (text == null) {
                    continue;
                }

                String charName = "";
                Color charColor = Color.BLACK;

                if (chara != null) {
                    charName = (chara.name != null) ? chara.name : "";
                    charColor = (chara.color != null) ? chara.color : Color.BLACK;
                }

                Text name = new Text(charName, 20, charColor);
                name.setBounds(10, yPosition, getWidth() - 20, 30);
                logsPanel.add(name);

                yPosition += 35;

                Text dialog = new Text(text, 16, Color.BLACK);
                dialog.setBounds(20, yPosition, getWidth() - 30, 50);
                logsPanel.add(dialog);

                yPosition += 60;
            }

            logsPanel.setPreferredSize(new Dimension(getWidth() - 20, yPosition));

            JScrollPane scroll = new JScrollPane(logsPanel);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            scroll.setBounds(0, 0, getWidth(), getHeight() - 80);

            scroll.setOpaque(false);


            JButton button = new JButton("Back");
            button.setBounds(10, getHeight() - 60, getWidth() - 30, 50);
            button.addActionListener(e -> {
                Engine.getInstance().logs();
                Engine.getInstance().novel.changeTextboxState();
            });


            add(scroll);
            add(button);

            SwingUtilities.invokeLater(() -> {
                JScrollBar bar = scroll.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }

        revalidate();
        repaint();
    }
}