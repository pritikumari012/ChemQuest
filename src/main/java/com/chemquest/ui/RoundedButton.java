package com.chemquest.ui;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Rounded button with hover and pressed feedback. primary = mustard, otherwise dark with teal outline. */
public class RoundedButton extends JButton {
    private boolean primary;

    public RoundedButton(String text, boolean primary) {
        super(text);
        this.primary = primary;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(Theme.bold(14));
        setForeground(primary ? Theme.BG_DEEP : Theme.TEXT);
        setBorder(new EmptyBorder(10, 22, 10, 22));
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
        setForeground(primary ? Theme.BG_DEEP : Theme.TEXT);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color base = primary ? Theme.MUSTARD : Theme.SURFACE_ALT;
        if (getModel().isPressed()) {
            base = base.darker();
        } else if (getModel().isRollover()) {
            base = primary ? Theme.MUSTARD_LIGHT : Theme.TEAL_DARK;
        }
        g2.setColor(base);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
        if (!primary) {
            g2.setColor(Theme.TEAL);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
