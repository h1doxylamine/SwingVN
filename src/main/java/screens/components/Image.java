package screens.components;

import managers.ResourceRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Image extends JPanel {

    public String name;
    public int x, y;
    double scale = 1f;

    private int baseWidth;
    private int baseHeight;

    public boolean visible = false;

    public float opacity = 1f;

    int w, h;

    private boolean useRelativePositioning = true;
    private double relativeX = 0.5;
    private double relativeY = 0.5;
    private Dimension parentSize = null;

    private final ScheduledExecutorService showFadeTimer = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService hideFadeTimer = Executors.newSingleThreadScheduledExecutor();


    public Image(String name) {
        this.name = name;

        java.awt.Image img = ResourceRegister.getImage(name);
        if (img != null) {
            baseWidth = img.getWidth(null);
            baseHeight = img.getHeight(null);

            w = (int) (baseWidth * scale);
            h = (int) (baseHeight * scale);

            this.x = 0;
            this.y = 0;

            setPreferredSize(new Dimension(w, h));
            setSize(w, h);
        }

        setOpaque(false);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.useRelativePositioning = false;
        updateBounds();
    }

    public void updateParentSize(Dimension newSize) {
        this.parentSize = newSize;
        if (useRelativePositioning) {
            updatePositionFromRelative();
        }
    }

    private void updatePositionFromRelative() {
        if (parentSize != null) {

            this.x = (int)((parentSize.width - w) * relativeX);
            this.y = (int)((parentSize.height - h) * relativeY);
            updateBounds();
        }
    }

    public void setScale(double scale) {
        this.scale = scale;

        int oldCenterX = x + w / 2;
        int oldCenterY = y + h / 2;

        w = (int) (baseWidth * scale);
        h = (int) (baseHeight * scale);

        if (useRelativePositioning) {
            updatePositionFromRelative();
        } else {

            this.x = oldCenterX - w / 2;
            this.y = oldCenterY - h / 2;
            updateBounds();
        }
    }

    private void updateBounds() {
        setBounds(x, y, w, h);
        setPreferredSize(new Dimension(w, h));
        setSize(w, h);

        revalidate();
        repaint();
    }

    public void fadeShow(int milliseconds) {
        opacity = 0f;
        visible = true;
        int ms = 10;
        int steps = Math.max(1, milliseconds / ms);
        float increment = 1f / steps;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                opacity += increment;
                repaint();
                if (opacity >= 1f) {
                    opacity = 1f;
                    showFadeTimer.shutdown();
                }
            }
        };
        showFadeTimer.scheduleAtFixedRate(task, 0, ms, TimeUnit.MILLISECONDS);

    }

    public void fadeHide(int milliseconds) {
        opacity = 1f;
        visible = true;
        int ms = 10;
        int steps = Math.max(1, milliseconds / ms);
        float increment = 1f / steps;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                opacity -= increment;
                repaint();
                if (opacity <= 0f) {
                    opacity = 0f;
                    visible = false;
                    hideFadeTimer.shutdown();
                }
            }
        };
        hideFadeTimer.scheduleAtFixedRate(task, 0, ms, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, opacity));

        java.awt.Image img = ResourceRegister.getImage(name);
        if (img != null && visible) {

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.translate(w / 2, h / 2);

            g2d.drawImage(img, -w / 2, -h / 2, w, h, this);
        }
    }
}