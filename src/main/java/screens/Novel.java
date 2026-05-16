package screens;

import java.util.List;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

import core.Engine;
import managers.ChoiceOption;
import managers.ScriptManager;
import managers.ScriptManager.Chara;
import screens.components.Choice;
import screens.components.LogsMenu;
import screens.components.TextBox;


public class Novel extends JPanel {

    TextBox textBox;
    Choice choice;
    ScriptManager scriptManager;
    public LogsMenu logs;

    private JPanel darkbg;
    private Choice currentChoice = null;

    public Novel() {

        setLayout(null);

        darkbg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (Engine.getInstance().darkBackground) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(0, 0, 0, Engine.getInstance().darkbackgroundOpacity));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        darkbg.setOpaque(false);

        textBox = new TextBox(1000, 100, 70, Engine.getInstance().topTextBox, Engine.getInstance().textColor);


        add(textBox);
        add(darkbg);
        setBackground(Color.BLACK);

        logs = new LogsMenu(0, 0, getWidth(), getHeight());
        add(logs);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                if (!textBox.isTop) {
                    int textBoxWidth = (int) (width * 0.8);
                    int textBoxHeight = (int) (height * 0.25);
                    int textBoxX = ((width - textBoxWidth) / 2) - 10;
                    int textBoxY = height - textBoxHeight - 20;
                    textBox.setBounds(textBoxX, textBoxY, textBoxWidth, textBoxHeight);
                } else {
                    int textBoxWidth = (int) (width * 0.8);
                    int textBoxHeight = (int) (height * 0.25);
                    int textBoxX = ((width - textBoxWidth) / 2) - 10;
                    int textBoxY = 0;
                    textBox.setOpaque(false);
                    textBox.setBounds(textBoxX, textBoxY, textBoxWidth, textBoxHeight);
                }

                int logsWidth = (int) (width * 0.97);
                int logsHeight = (int) (height * 0.935);
                logs.setBounds(10, 10, logsWidth, logsHeight);
                darkbg.setBounds(0, 0, width, height);


                //temporaire car pas fiable
                choice.setBounds(width / 4, height / 4, 650, 700);
            }
        });
    }

    public void displayChoices(List<ChoiceOption> options, String question, java.util.function.Consumer<Integer> onChoiceSelected) {
        hideChoices();

        currentChoice = new Choice(options, question, (selectedIndex) -> {

            hideChoices();

            Engine.getInstance().canInteract = true;

            onChoiceSelected.accept(selectedIndex);

            refreshImages();
            repaint();
        });


        int x = (getWidth() - 600) / 2;
        int y = (getHeight() - currentChoice.height) / 2;


        currentChoice.setBounds(x, y, 600, currentChoice.height);


        add(currentChoice);
        setComponentZOrder(currentChoice, 0);

        revalidate();
        repaint();
    }

    public void hideChoices() {
        if (currentChoice != null) {
            remove(currentChoice);
            currentChoice = null;
            revalidate();
            repaint();
        }
    }

    public void refreshImages() {
        for (screens.components.Image img : Engine.getInstance().images) {
            remove(img);
        }

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        Dimension parentSize = new Dimension(panelWidth, panelHeight);

        for (int i = Engine.getInstance().images.size() - 1; i >= 0; i--) {
            screens.components.Image img = Engine.getInstance().images.get(i);

            img.updateParentSize(parentSize);

            img.setBounds(img.x, img.y, img.getPreferredSize().width, img.getPreferredSize().height);
            add(img);
            img.repaint();
        }

        setComponentZOrder(textBox, 0);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void speak(String text, Chara charName){
        textBox.updateLine(text, charName);
        repaint();
    }

    public void changeTextboxState(){
        textBox.setVisible(!textBox.isVisible());
        repaint();
    }

    public void changeLogsState() {
        logs.setVisible(!logs.isVisible());

        if (logs.isVisible()) {
            logs.render(Engine.getInstance().logs);
            setComponentZOrder(logs, 0);
        }

        textBox.setVisible(false);

        revalidate();
        repaint();
    }

    public void forceChangeTextboxState(){
        if (!textBox.isVisible()) {
            textBox.setVisible(true);
        }
    }
}