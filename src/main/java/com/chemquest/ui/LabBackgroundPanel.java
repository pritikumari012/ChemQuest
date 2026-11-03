package com.chemquest.ui;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Animated futuristic-lab backdrop painted with Java2D vector shapes (no image files needed):
 * glowing gradients, a faint grid, drifting molecules, rising particles and bubbling flasks.
 * The animation timer only runs while the panel is on screen.
 */
public class LabBackgroundPanel extends JPanel {
    private record Particle(double x, double y, double speed, double size, double phase, boolean gold) { }

    private record Molecule(double x, double y, double radius, double spin, double phase, double[][] atoms) { }

    private final List<Particle> particles = new ArrayList<>();
    private final List<Molecule> molecules = new ArrayList<>();
    private double time;
    private final Timer timer = new Timer(33, e -> {
        time += 0.033;
        repaint();
    });

    public LabBackgroundPanel() {
        Random rnd = new Random(7);
        for (int i = 0; i < 70; i++) {
            particles.add(new Particle(rnd.nextDouble(), rnd.nextDouble(), 0.01 + rnd.nextDouble() * 0.03,
                    1.5 + rnd.nextDouble() * 3, rnd.nextDouble() * 6.28, rnd.nextBoolean()));
        }
        for (int i = 0; i < 7; i++) {
            int n = 4 + rnd.nextInt(3);
            double[][] atoms = new double[n][2];
            for (int k = 0; k < n; k++) {
                double r = 0.6 + rnd.nextDouble() * 0.4;
                atoms[k][0] = Math.cos(2 * Math.PI * k / n) * r;
                atoms[k][1] = Math.sin(2 * Math.PI * k / n) * r;
            }
            molecules.add(new Molecule(0.08 + rnd.nextDouble() * 0.84, 0.1 + rnd.nextDouble() * 0.75,
                    40 + rnd.nextDouble() * 40, (rnd.nextDouble() - 0.5) * 0.4, rnd.nextDouble() * 6.28, atoms));
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, Theme.BG_DEEP, 0, h, new Color(0x23, 0x2B, 0x1E)));
        g2.fillRect(0, 0, w, h);
        glow(g2, w * 0.15f, h * 0.20f, Math.max(w, h) * 0.45f, new Color(47, 184, 168, 70));
        glow(g2, w * 0.85f, h * 0.85f, Math.max(w, h) * 0.45f, new Color(227, 181, 46, 55));

        g2.setColor(new Color(255, 255, 255, 10));
        g2.setStroke(new BasicStroke(1f));
        for (int x = 0; x < w; x += 60) {
            g2.drawLine(x, 0, x, h);
        }
        for (int y = 0; y < h; y += 60) {
            g2.drawLine(0, y, w, y);
        }

        for (Molecule m : molecules) {
            drawMolecule(g2, m, w, h);
        }
        for (Particle p : particles) {
            double py = (((p.y - time * p.speed) % 1.0) + 1.0) % 1.0;
            double px = p.x + Math.sin(time * 0.6 + p.phase) * 0.01;
            Color c = p.gold ? new Color(227, 181, 46, 110) : new Color(47, 184, 168, 110);
            g2.setColor(c);
            g2.fill(new Ellipse2D.Double(px * w, py * h, p.size, p.size));
        }

        double s = Math.max(0.7, Math.min(1.3, Math.min(w, h) / 800.0));
        drawFlask(g2, w * 0.10, h - 24, s, new Color(47, 184, 168));
        drawFlask(g2, w * 0.90, h - 24, s, new Color(227, 181, 46));

        g2.setPaint(new RadialGradientPaint(new Point2D.Float(w / 2f, h / 2f), Math.max(w, h) * 0.75f,
                new float[]{0.5f, 1f}, new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 150)}));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
    }

    private void glow(Graphics2D g2, float cx, float cy, float radius, Color c) {
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), radius, new float[]{0f, 1f},
                new Color[]{c, new Color(c.getRed(), c.getGreen(), c.getBlue(), 0)}));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawMolecule(Graphics2D g2, Molecule m, int w, int h) {
        double cx = m.x * w + Math.sin(time * 0.3 + m.phase) * 18;
        double cy = m.y * h + Math.cos(time * 0.25 + m.phase) * 14;
        double a = m.phase + time * m.spin;
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        int n = m.atoms.length;
        double[] px = new double[n];
        double[] py = new double[n];
        for (int i = 0; i < n; i++) {
            double ax = m.atoms[i][0];
            double ay = m.atoms[i][1];
            px[i] = cx + (ax * cos - ay * sin) * m.radius;
            py[i] = cy + (ax * sin + ay * cos) * m.radius;
        }
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(47, 184, 168, 80));
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            g2.drawLine((int) px[i], (int) py[i], (int) px[j], (int) py[j]);
        }
        for (int i = 0; i < n; i++) {
            g2.setColor(i % 2 == 0 ? new Color(227, 181, 46, 150) : new Color(47, 184, 168, 150));
            g2.fill(new Ellipse2D.Double(px[i] - 6, py[i] - 6, 12, 12));
        }
    }

    /** Draws an Erlenmeyer (conical) flask with a gently waving liquid and rising bubbles. */
    private void drawFlask(Graphics2D g2, double cx, double baseY, double s, Color liquid) {
        double neckW = 36 * s;
        double bodyW = 150 * s;
        double height = 190 * s;
        double neckH = 70 * s;
        double top = baseY - height;

        Path2D flask = new Path2D.Double();
        flask.moveTo(cx - neckW / 2, top);
        flask.lineTo(cx + neckW / 2, top);
        flask.lineTo(cx + neckW / 2, top + neckH);
        flask.lineTo(cx + bodyW / 2, baseY);
        flask.lineTo(cx - bodyW / 2, baseY);
        flask.lineTo(cx - neckW / 2, top + neckH);
        flask.closePath();

        g2.setColor(new Color(255, 255, 255, 16));
        g2.fill(flask);

        Shape oldClip = g2.getClip();
        g2.clip(flask);
        double levelY = baseY - height * 0.32;
        Path2D water = new Path2D.Double();
        water.moveTo(cx - bodyW / 2, baseY);
        for (double x = cx - bodyW / 2; x <= cx + bodyW / 2; x += 4) {
            water.lineTo(x, levelY + Math.sin(time * 2 + x * 0.05) * 3);
        }
        water.lineTo(cx + bodyW / 2, baseY);
        water.closePath();
        g2.setPaint(new GradientPaint(0, (float) levelY,
                new Color(liquid.getRed(), liquid.getGreen(), liquid.getBlue(), 150),
                0, (float) baseY, new Color(liquid.getRed(), liquid.getGreen(), liquid.getBlue(), 60)));
        g2.fill(water);
        for (int i = 0; i < 5; i++) {
            double progress = (time * 0.25 + i * 0.2) % 1.0;
            double bx = cx + Math.sin(i * 1.7) * bodyW * 0.22;
            double by = baseY - progress * (baseY - levelY);
            double r = 2 + i % 3;
            g2.setColor(new Color(255, 255, 255, 120));
            g2.draw(new Ellipse2D.Double(bx - r, by - r, r * 2, r * 2));
        }
        g2.setClip(oldClip);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 90));
        g2.draw(flask);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.drawLine((int) (cx - neckW / 2 + 6 * s), (int) (top + 8 * s), (int) (cx - neckW / 2 + 6 * s), (int) (top + neckH));
    }
}
