package com.chemquest.ui.experiments;

import com.chemquest.model.Apparatus;
import com.chemquest.model.Experiment;
import com.chemquest.service.ApparatusService;
import com.chemquest.ui.RoundedButton;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.ScrollPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;
import com.chemquest.ui.apparatus.ApparatusDetailDialog;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

/** Full experiment page: Aim, Theory, Equation, Apparatus, Reagents, Procedure, Observations, Result, Calculations, Precautions, Quiz. */
public class ExperimentDetailPanel extends JPanel {
    private static final int W = 600; // text width of the reading column

    private final ApparatusService apparatusService;
    private final JPanel column = new JPanel();

    public ExperimentDetailPanel(Experiment e, ApparatusService apparatusService,
                                 Runnable onBack, Runnable onTakeQuiz, Runnable onOpenLab) {
        this.apparatusService = apparatusService;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        // Breadcrumb
        JPanel crumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        crumbs.setOpaque(false);
        crumbs.add(UiKit.link("\u2190 Experiments", onBack));
        crumbs.add(UiKit.label("\u203A " + e.category().label() + " \u203A " + e.id(), 12, Theme.TEXT_MUTED, false));
        add(crumbs);
        column.add(left(crumbs));
        column.add(Box.createVerticalStrut(8));

        column.add(left(UiKit.wrap(e.title(), W, 28, Theme.MUSTARD)));
        ((JLabel) column.getComponent(column.getComponentCount() - 1)).setFont(Theme.bold(28));
        column.add(Box.createVerticalStrut(6));
        column.add(left(UiKit.label(e.category().level() + "  \u2022  " + e.difficulty() + "  \u2022  ~" + e.minutes()
                + " min  \u2022  " + e.apparatus().size() + " apparatus items", 13, Theme.TEAL, true)));
        column.add(Box.createVerticalStrut(8));
        column.add(left(UiKit.wrap(e.shortDesc(), W, 15, Theme.TEXT)));
        column.add(Box.createVerticalStrut(10));

        RoundedPanel notice = new RoundedPanel(16, new Color(0x3A, 0x32, 0x18), Theme.WARNING);
        notice.setLayout(new BorderLayout());
        notice.setBorder(new EmptyBorder(10, 14, 10, 14));
        String hazard = e.hazard().isBlank() ? "" : "Hazard: " + e.hazard() + "  ";
        notice.add(UiKit.wrap(hazard + "Educational notice: this page is a study guide and any simulated result is labelled as such. "
                + "Hazardous steps (flames, corrosive or toxic chemicals) belong in a supervised school or college laboratory, "
                + "never at home.", W - 30, 12, Theme.WARNING));
        column.add(left(notice));

        text("Aim", e.aim());
        text("Theory", e.theory());

        JPanel eq = section("Chemical Equation");
        for (String line : e.equationLines()) {
            JLabel l = new JLabel("<html><div style='width:" + (W - 20) + "px'>" + UiKit.esc(line) + "</div></html>");
            l.setFont(new Font("Monospaced", Font.BOLD, 15));
            l.setForeground(Theme.MUSTARD_LIGHT);
            add(eq, l);
        }

        JPanel app = section("Required Apparatus  (click an item for details)");
        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 8));
        grid.setOpaque(false);
        for (String name : e.apparatus()) {
            RoundedButton b = new RoundedButton(name, false);
            b.setFont(Theme.bold(13));
            b.addActionListener(ev -> openApparatus(name));
            grid.add(b);
        }
        add(app, grid);

        bullets("Reagents and Materials", e.reagents(), false);
        bullets("Procedure", e.steps(), true);
        bullets("Observations", e.observations(), false);
        text("Result", e.result());

        JPanel calc = section("Calculations");
        for (String line : e.calculations()) {
            JLabel l = UiKit.wrap(line, W - 20, 13, Theme.TEXT);
            l.setFont(new Font("Monospaced", Font.PLAIN, 13));
            add(calc, l);
            calc.add(Box.createVerticalStrut(4));
        }

        bullets("Precautions", e.precautions(), false);

        JPanel quiz = section("Test Your Understanding");
        add(quiz, UiKit.wrap("Answer questions on this level of chemistry and earn +10 XP for every correct answer (no negative marking).", W - 20, 13, Theme.TEXT));
        quiz.add(Box.createVerticalStrut(10));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setOpaque(false);
        RoundedButton take = new RoundedButton("Take Quiz \u2192   (+10 XP per correct answer)", true);
        take.addActionListener(ev -> onTakeQuiz.run());
        buttons.add(take);
        if (e.hasLab()) {
            RoundedButton lab = new RoundedButton("Open in Virtual Lab: " + e.lab(), false);
            lab.addActionListener(ev -> onOpenLab.run());
            buttons.add(Box.createHorizontalStrut(10));
            buttons.add(lab);
        }
        add(quiz, buttons);

        column.add(Box.createVerticalStrut(20));

        ScrollPanel scrollContent = new ScrollPanel(new GridBagLayout(), W + 120);
        scrollContent.setBorder(new EmptyBorder(22, 24, 30, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        scrollContent.add(column, gbc);
        removeAll();
        add(UiKit.scroller(scrollContent), BorderLayout.CENTER);
    }

    private JComponent left(JComponent c) {
        c.setAlignmentX(LEFT_ALIGNMENT);
        return c;
    }

    private JPanel section(String heading) {
        RoundedPanel p = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(14, 18, 14, 18));
        p.add(left(UiKit.label(heading, 17, Theme.MUSTARD, true)));
        p.add(Box.createVerticalStrut(8));
        column.add(Box.createVerticalStrut(14));
        column.add(left(p));
        return p;
    }

    private void add(JPanel section, JComponent c) {
        section.add(left(c));
    }

    private void text(String heading, String body) {
        JPanel s = section(heading);
        add(s, UiKit.wrap(body, W - 20, 14, Theme.TEXT));
    }

    private void bullets(String heading, List<String> items, boolean numbered) {
        JPanel s = section(heading);
        int i = 1;
        for (String item : items) {
            String prefix = numbered ? (i++) + ".  " : "\u2022  ";
            add(s, UiKit.wrap(prefix + item, W - 20, 14, Theme.TEXT));
            s.add(Box.createVerticalStrut(5));
        }
    }

    private void openApparatus(String name) {
        apparatusService.byName(name).ifPresent(a ->
                new ApparatusDetailDialog((JFrame) SwingUtilities.getWindowAncestor(this), a,
                        apparatusService.experimentsUsing(a)).setVisible(true));
    }
}
