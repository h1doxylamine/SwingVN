package core;

import managers.ChoiceOption;
import managers.LogData;
import managers.ResourceRegister;
import managers.ScriptManager;
import managers.ScriptManager.Chara;
import screens.Novel;
import screens.components.Image;
import screens.components.SoundManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {

    //TODO: créer un menu paramètres

       /* =========================
              OPTIONS DU VN
        ========================= */

    public int autoPlayerDelay = 400; //multiplié par 10 plus tard pour éviter de se prendre la tête avec les millisecondes
    public int textWritingSpeed = 20; //en millisecondes

    //mettre la texbox en haut façon higurashi
    public boolean topTextBox = true;

    /**
     * si la textbox est mise en haut façon Higurashi, elle n'a pas d'arrière plan
     * ces valeurs permettent de rendre le texte plus lisible
     */

    public boolean darkBackground = true;
    public int darkbackgroundOpacity = 120;


    public Color textColor = Color.WHITE;    // pour une couleur 100% personalisée en rgb :  public Color textColor = new Color(x, x, x)
    public boolean isBold = true; //mettre les textes de la texbox en gras ou non

    public static final Engine INSTANCE = new Engine();

    private final Map<String, Color> characters = new HashMap<>();
    public ArrayList<LogData> logs = new ArrayList<>();
    public ScriptManager currentScript;
    public boolean isAutoOn = false;
    public boolean isSkipOn = false;
    public boolean isLogsOn = false;

    public boolean canInteract = true;


    public static Engine getInstance() {
        return INSTANCE;
    }

    public List<Image> images = new ArrayList<>();
    public Novel novel;


    public void registerImage(Image img, boolean isBackground) {
        if (isBackground) {
            images.add(0, img);
        } else {
            images.add(img);
        }
    }

    public void displayDialogue(Chara character, String text) {
        novel.speak(text, character);
        logs.add(new LogData(character, text));
    }

    public void displayChoices(List<ChoiceOption> options, String question, java.util.function.Consumer<Integer> onChoiceSelected) {
        canInteract = false;

        novel.displayChoices(options, question, onChoiceSelected);
    }

    public void showImage(Image img) {
        img.visible = true;
        img.repaint();
    }

    public void fadeShowImage(Image img, int milliseconds) {
        img.fadeShow(milliseconds);
        img.repaint();
    }

    public void fadeHideImage(Image img, int milliseconds) {
        img.fadeHide(milliseconds);
        img.repaint();
    }

    public void hideImage(Image img) {
        img.visible = false;
        img.repaint();
    }

    public void playAudio(String name) {
        InputStream audio = ResourceRegister.getAudio(name);
        SoundManager.PlaySound(audio);
    }

    public void logs() {
        novel.changeLogsState();
        canInteract = !canInteract;

    }


    public void registerCharacter(String name, Color color) {
        characters.put(name, color);
    }

    public void play(ScriptManager script) {
        this.currentScript = script;
        script.run();

        if (currentScript.hasNext()) {
            currentScript.next();
            novel.refreshImages();
            novel.repaint();
        }

        novel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (currentScript.hasNext() && canInteract) {
                        currentScript.next();
                        novel.forceChangeTextboxState();
                        novel.refreshImages();
                        novel.repaint();
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (!novel.logs.isVisible()) novel.changeTextboxState();
                }
            }
        });
    }
}