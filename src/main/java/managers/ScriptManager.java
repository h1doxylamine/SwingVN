package managers;

import screens.components.Image;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import core.Engine;

public abstract class ScriptManager {

    public int dialogueIndex = 0;

    public ArrayList<CommandManager> commands = new ArrayList<>();

    public Map<String, Integer> labels = new HashMap<>();
    private Stack<Integer> returnStack = new Stack<>();

    public static class Chara {

        public final String name;
        public final Color color;
        public Chara(String name, Color color) {
            this.name = Objects.requireNonNull(name);
            this.color = Objects.requireNonNull(color);

        }

    }

    protected void label(String name) {
        labels.put(name, commands.size());

        CommandManager cmd = () -> {
            System.out.println("Label " + name);
            next();
        };
        commands.add(cmd);
    }

    protected void jump(String labelName) {
        CommandManager cmd = () -> {
            if (!labels.containsKey(labelName)) {
                System.out.println("Label " + labelName + " non trouvé");
                return;
            }

            dialogueIndex = labels.get(labelName);
            System.out.println("Saut vers " + labelName);

            if (dialogueIndex < commands.size()) {
                commands.get(dialogueIndex++).execute();
            }
        };
        commands.add(cmd);
    }


    protected Image background(String name) {
        Image img = new Image(name);
        Engine.getInstance().registerImage(img, true); // true = background
        return img;
    }

    protected Image image(String name) {
        Image img = new Image(name);
        Engine.getInstance().registerImage(img, false); // false = foreground
        return img;

    }

    protected void playSound(String name) {
        CommandManager cmd = () -> {
            Engine.getInstance().playAudio(name);
            next();
        };
        commands.add(cmd);
    }

    protected void stopSound(String name) {
        CommandManager cmd = () -> {
    //        Engine.getInstance().stopAudio(name);
            next();
        };
        commands.add(cmd);
    }

    protected void speak(Chara chr, String text) {
        CommandManager cmd = () -> Engine.getInstance().displayDialogue(chr, text);
        commands.add(cmd);
    }

    protected void hide(Image img) {
        CommandManager cmd = () -> Engine.getInstance().hideImage(img);
        commands.add(cmd);
    }

    protected void show(Image img) {
        CommandManager cmd = () -> Engine.getInstance().showImage(img);
        commands.add(cmd);
    }

    protected void fadeShow(Image img, int milliseconds) {
        CommandManager cmd = () -> Engine.getInstance().fadeShowImage(img, milliseconds);
        commands.add(cmd);
    }

    protected void fadeHide(Image img, int milliseconds) {
        CommandManager cmd = () -> Engine.getInstance().fadeHideImage(img, milliseconds);
        commands.add(cmd);
    }

    protected void updateScale(Image img, double scale) {
        CommandManager cmd = () -> img.setScale(scale);
        commands.add(cmd);
    }

    protected void updatePosition(Image img, int x, int y) {
        CommandManager cmd = () -> img.setPosition(x, y);
        commands.add(cmd);
    }

    protected ChoiceManager choice(String question) {
        return new ChoiceManager(this, question);
    }

    protected void instantPass() {
        CommandManager cmd = () -> {
            if (dialogueIndex < commands.size()) {
                commands.get(dialogueIndex++).execute();
                commands.get(dialogueIndex++).execute();
                System.out.println("Passage instantané");
            }
        };
        commands.add(cmd);
    }

    protected Chara character(String name, Color color) {
        Chara c = new Chara(name, color);
        Engine.getInstance().registerCharacter(c.name, c.color);
        return c;
    }

    protected void noAction() {
        CommandManager cmd = this::noAction;
        commands.add(cmd);
    }

    public void next() {
        if (dialogueIndex < commands.size()) {
            commands.get(dialogueIndex++).execute();
            System.out.println("Evenement suivant");
        }
    }

    //fonction de merde ne faites pas ça mais au moins IT WORKS
    public void initButtons(){
        ScheduledExecutorService autoPlayer = Executors.newSingleThreadScheduledExecutor();

        autoPlayer.scheduleAtFixedRate(() -> {
            //autplay
            while (Engine.getInstance().isAutoOn && Engine.getInstance().canInteract) {
                if (dialogueIndex < commands.size()) {
                    commands.get(dialogueIndex++).execute();
                    System.out.println("Evenement suivant en auto");
                    try {
                        Thread.sleep(Engine.getInstance().autoPlayerDelay * 10L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //skip
            while (Engine.getInstance().isSkipOn && Engine.getInstance().canInteract) {
                if (dialogueIndex < commands.size()) {
                    commands.get(dialogueIndex++).execute();
                    System.out.println("Skip");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);;
    }

    public boolean hasNext() {
        return dialogueIndex < commands.size();
    }

    public abstract void run();

}
