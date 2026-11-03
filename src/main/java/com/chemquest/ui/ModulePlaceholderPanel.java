package com.chemquest.ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;

/**
 * Honest "not built yet" page used by modules scheduled for later phases.
 * It states what is coming and offers a working way back to the dashboard.
 */
public class ModulePlaceholderPanel extends JPanel {
    public ModulePlaceholderPanel(String title, String description, String phase, Runnable backToDashboard) {
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        RoundedPanel card = new RoundedPanel(24, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        UiKit.addRow(card, UiKit.label(title, 28, Theme.MUSTARD, true), 0);
        UiKit.addRow(card, UiKit.label("<html><div style='width:420px'>" + description + "</div></html>", 15, Theme.TEXT, false), 12);
        UiKit.addRow(card, UiKit.label("Scheduled for " + phase + " of the build.", 13, Theme.TEAL, true), 14);
        UiKit.addRow(card, new RoundedButton("\u2190 Back to Dashboard", false), 22);
        ((RoundedButton) card.getComponent(card.getComponentCount() - 1))
                .addActionListener(e -> backToDashboard.run());
        add(card);
    }
}
