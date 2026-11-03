package com.chemquest.ui.apparatus;

import com.chemquest.model.Apparatus;
import com.chemquest.model.Experiment;
import com.chemquest.ui.RoundedButton;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

public class ApparatusDetailDialog extends JDialog {
    public ApparatusDetailDialog(JFrame owner, Apparatus apparatus, List<Experiment> experiments) {
        super(owner, "Apparatus detail", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        RoundedPanel content = new RoundedPanel(22, Theme.SURFACE, Theme.SURFACE_ALT);
        content.setBorder(new EmptyBorder(18, 20, 18, 20));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(UiKit.label(apparatus.name(), 26, Theme.MUSTARD, true));
        content.add(Box.createVerticalStrut(4));
        content.add(UiKit.label(apparatus.category(), 12, Theme.TEAL, true));
        content.add(Box.createVerticalStrut(10));
        content.add(new Apparatus3DView(apparatus.name(), 460, 210));
        content.add(Box.createVerticalStrut(12));
        content.add(UiKit.wrap(apparatus.description(), 420, 14, Theme.TEXT));
        content.add(Box.createVerticalStrut(12));
        content.add(UiKit.label("Common uses", 16, Theme.MUSTARD, true));
        content.add(UiKit.wrap(apparatus.uses(), 420, 13, Theme.TEXT));
        content.add(Box.createVerticalStrut(12));
        content.add(UiKit.label("Safety", 16, Theme.MUSTARD, true));
        content.add(UiKit.wrap(apparatus.safety(), 420, 13, Theme.TEXT));
        content.add(Box.createVerticalStrut(12));

        if (!experiments.isEmpty()) {
            content.add(UiKit.label("Used in experiments", 16, Theme.MUSTARD, true));
            for (Experiment e : experiments) {
                JLabel l = UiKit.label("• " + e.title(), 12, Theme.TEXT, false);
                content.add(l);
            }
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        RoundedButton close = new RoundedButton("Close", false);
        close.addActionListener(e -> dispose());
        buttons.add(close);
        content.add(Box.createVerticalStrut(12));
        content.add(buttons);

        setContentPane(content);
        setSize(new Dimension(520, 540));
        setLocationRelativeTo(owner);
    }
}
