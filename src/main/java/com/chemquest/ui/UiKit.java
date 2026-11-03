package com.chemquest.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/** Small reusable factory methods so screens stay short and consistent. */
public final class UiKit {
    private UiKit() { }

    public static JLabel label(String text, int size, Color color, boolean bold) {
        JLabel l = new JLabel(text);
        l.setFont(bold ? Theme.bold(size) : Theme.font(size));
        l.setForeground(color);
        return l;
    }

    public static JTextField textField() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    public static JLabel wrap(String text, int width, int size, Color color) {
        JLabel label = label("<html><div style='width:" + width + "px'>" + esc(text) + "</div></html>", size, color, false);
        label.setVerticalAlignment(JLabel.TOP);
        return label;
    }

    public static JScrollPane scroller(JComponent content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    public static void onClick(Component c, Runnable action) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
    }

    public static String esc(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static void styleField(JTextComponent f) {
        f.setFont(Theme.font(15));
        f.setBackground(Theme.FIELD_BG);
        f.setForeground(Theme.TEXT);
        f.setCaretColor(Theme.MUSTARD);
        f.setBorder(fieldBorder(Theme.SURFACE_ALT));
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                f.setBorder(fieldBorder(Theme.TEAL));
            }

            @Override
            public void focusLost(FocusEvent e) {
                f.setBorder(fieldBorder(Theme.SURFACE_ALT));
            }
        });
    }

    private static javax.swing.border.Border fieldBorder(Color c) {
        return BorderFactory.createCompoundBorder(new LineBorder(c, 1, true), new EmptyBorder(9, 12, 9, 12));
    }

    /** Text-style link button (e.g. "Create an account"). */
    public static JButton link(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setFont(Theme.bold(13));
        b.setForeground(Theme.TEAL);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());
        return b;
    }

    /** Adds a component as a new full-width row in a GridBagLayout panel. */
    public static void addRow(JPanel panel, Component c, int topGap) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = panel.getComponentCount();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(topGap, 0, 0, 0);
        panel.add(c, gbc);
    }

    /** Wraps a component so it is centred instead of stretched. */
    public static JPanel centered(Component c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    /** Shows a wrapped message (HTML-escaped) in a label with the given colour. */
    public static void setMessage(JLabel label, String text, Color color) {
        String safe = esc(text);
        label.setForeground(color);
        label.setText("<html><div style='width:320px'>" + safe + "</div></html>");
    }
}
