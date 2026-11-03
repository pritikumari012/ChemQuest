package com.chemquest.ui;

import com.chemquest.app.AppState;
import com.chemquest.ui.NavigationPanel.Page;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;

/**
 * Dashboard home. Counts of modules come from the actual data library; quiz/XP/badge values
 * are 0 until the Quiz and Progress modules (Phase 5) record real activity.
 */
public class DashboardHomePanel extends JPanel {

    public DashboardHomePanel(AppState state, Consumer<Page> navigate) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(28, 32, 28, 32));

        box.add(UiKit.label("Welcome, " + state.currentUser().displayName(), 30, Theme.TEXT, true));
        box.add(UiKit.label("Pick a module to start exploring the lab.", 14, Theme.TEXT_MUTED, false));
        box.add(javax.swing.Box.createVerticalStrut(20));

        JPanel stats = new JPanel(new GridLayout(0, 3, 16, 16));
        stats.setOpaque(false);
        stats.add(statCard("Elements", String.valueOf(state.periodicTable().count()), "Available in Periodic Table"));
        stats.add(statCard("Apparatus", String.valueOf(state.apparatus().all().size()), "Available in Apparatus"));
        stats.add(statCard("Experiments", String.valueOf(state.experiments().count()), "Available in Experiments"));
        stats.add(statCard("Quizzes attempted", "0", "Tracked from Phase 5"));
        stats.add(statCard("XP", "0", "Earned from quizzes (Phase 5)"));
        stats.add(statCard("Badges", "0", "Awarded automatically (Phase 5)"));
        box.add(stats);

        box.add(javax.swing.Box.createVerticalStrut(24));
        box.add(UiKit.label("Quick access", 18, Theme.MUSTARD, true));
        box.add(javax.swing.Box.createVerticalStrut(10));
        JPanel quick = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        quick.setOpaque(false);
        for (Page p : new Page[]{Page.PERIODIC_TABLE, Page.APPARATUS, Page.EXPERIMENTS, Page.VIRTUAL_LAB, Page.QUIZ, Page.PROGRESS}) {
            RoundedButton b = new RoundedButton(p.icon() + "  " + p.title(), false);
            b.addActionListener(e -> navigate.accept(p));
            quick.add(b);
        }
        box.add(quick);

        box.add(javax.swing.Box.createVerticalStrut(24));
        box.add(UiKit.label("Recent activity", 18, Theme.MUSTARD, true));
        box.add(javax.swing.Box.createVerticalStrut(10));
        RoundedPanel activity = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
        activity.setLayout(new BorderLayout());
        activity.setBorder(new EmptyBorder(18, 22, 18, 22));
        activity.add(UiKit.label("No activity yet. Quiz results and explored experiments will appear here.", 14, Theme.TEXT_MUTED, false));
        box.add(activity);

        for (java.awt.Component c : box.getComponents()) {
            ((javax.swing.JComponent) c).setAlignmentX(LEFT_ALIGNMENT);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(box, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);
    }

    private RoundedPanel statCard(String caption, String value, String note) {
        RoundedPanel card = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.add(UiKit.label(caption, 13, Theme.TEXT_MUTED, false));
        card.add(UiKit.label(value, 34, Theme.MUSTARD, true));
        card.add(UiKit.label(note, 11, Theme.TEAL, false));
        return card;
    }
}
