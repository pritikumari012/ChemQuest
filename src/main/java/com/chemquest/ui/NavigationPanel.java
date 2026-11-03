package com.chemquest.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/** Persistent left sidebar. The selected page is highlighted in mustard. */
public class NavigationPanel extends JPanel {

    public enum Page {
        DASHBOARD("Dashboard", "\u2302"),
        PERIODIC_TABLE("Periodic Table", "\u269B"),
        APPARATUS("Apparatus", "\u2697"),
        EXPERIMENTS("Experiments", "\u2630"),
        VIRTUAL_LAB("Virtual Lab", "\u2726"),
        QUIZ("Quiz", "?"),
        PROGRESS("Progress", "\u25B2"),
        LOGOUT("Logout", "\u23FB");

        private final String title;
        private final String icon;

        Page(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }

        public String title() {
            return title;
        }

        public String icon() {
            return icon;
        }
    }

    private final Map<Page, NavButton> buttons = new EnumMap<>(Page.class);

    public NavigationPanel(String userName, Consumer<Page> onSelect) {
        setBackground(Theme.BG_DEEP);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(22, 16, 18, 16));
        setPreferredSize(new Dimension(240, 0));

        JLabel brand = UiKit.label("ChemQuest", 26, Theme.MUSTARD, true);
        JLabel tag = UiKit.label("Virtual Chemistry Lab", 12, Theme.TEXT_MUTED, false);
        brand.setAlignmentX(LEFT_ALIGNMENT);
        tag.setAlignmentX(LEFT_ALIGNMENT);
        add(brand);
        add(tag);
        add(Box.createVerticalStrut(26));

        for (Page page : Page.values()) {
            if (page == Page.LOGOUT) {
                add(Box.createVerticalGlue());
            }
            NavButton b = new NavButton(page);
            b.addActionListener(e -> onSelect.accept(page));
            buttons.put(page, b);
            add(b);
            add(Box.createVerticalStrut(6));
        }

        JLabel user = UiKit.label("Signed in: " + userName, 11, Theme.TEXT_MUTED, false);
        user.setAlignmentX(LEFT_ALIGNMENT);
        add(Box.createVerticalStrut(8));
        add(user);
    }

    public void setSelected(Page page) {
        buttons.forEach((p, b) -> b.setSelectedPage(p == page));
    }

    private static final class NavButton extends JButton {
        private boolean selected;

        NavButton(Page page) {
            super(page.icon() + "   " + page.title());
            setHorizontalAlignment(SwingConstants.LEFT);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setFont(Theme.bold(14));
            setForeground(Theme.TEXT);
            setBorder(new EmptyBorder(0, 16, 0, 10));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(LEFT_ALIGNMENT);
            setPreferredSize(new Dimension(208, 44));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }

        void setSelectedPage(boolean value) {
            selected = value;
            setForeground(value ? Theme.BG_DEEP : Theme.TEXT);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = null;
            if (selected) {
                fill = Theme.MUSTARD;
            } else if (getModel().isRollover()) {
                fill = Theme.SURFACE_ALT;
            }
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
