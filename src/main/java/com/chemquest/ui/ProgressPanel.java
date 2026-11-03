package com.chemquest.ui;

import com.chemquest.model.ExperimentCategory;
import com.chemquest.service.ProgressService;
import com.chemquest.service.ProgressService.CategorySnapshot;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ProgressPanel extends JPanel {
    private final ProgressService progress;
    private final JPanel content = new JPanel();
    private final JScrollPane scroll = new JScrollPane(content,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public ProgressPanel(ProgressService progress) {
        this.progress = progress;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(26, 30, 30, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        content.removeAll();
        content.add(UiKit.label("Your Learning Progress", 28, Theme.TEXT, true));
        content.add(Box.createVerticalStrut(5));
        content.add(UiKit.label("Review your quiz practice by chemistry level and focus on the next step.",
                13, Theme.TEXT_MUTED, false));
        content.add(Box.createVerticalStrut(18));

        RoundedPanel summary = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBorder(new EmptyBorder(18, 20, 18, 20));
        summary.add(UiKit.label("Questions answered: " + progress.totalAttempted(), 22, Theme.MUSTARD, true));
        summary.add(Box.createVerticalStrut(7));
        summary.add(UiKit.label("Correct answers: " + progress.totalCorrect()
                + "  •  Completed quizzes: " + progress.completedQuizzes(), 14, Theme.TEXT, false));
        summary.add(Box.createVerticalStrut(10));
        summary.add(UiKit.label("Badge: " + progress.badgeName(), 18, Theme.TEAL, true));
        summary.add(Box.createVerticalStrut(4));
        summary.add(UiKit.label(progress.badgeProgress(), 13, Theme.TEXT_MUTED, false));
        content.add(summary);
        content.add(Box.createVerticalStrut(18));
        content.add(UiKit.label("Progress by category", 19, Theme.MUSTARD, true));
        content.add(Box.createVerticalStrut(10));

        for (ExperimentCategory category : ExperimentCategory.values()) {
            CategorySnapshot snapshot = progress.category(category);
            RoundedPanel categoryCard = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
            categoryCard.setLayout(new BoxLayout(categoryCard, BoxLayout.Y_AXIS));
            categoryCard.setBorder(new EmptyBorder(16, 18, 16, 18));
            categoryCard.add(UiKit.label(category.label() + " Chemistry", 17, Theme.TEXT, true));
            categoryCard.add(Box.createVerticalStrut(6));
            categoryCard.add(UiKit.label(snapshot.attempted() + " questions answered  •  "
                    + snapshot.correct() + " correct  •  " + snapshot.accuracyPercent() + "% accuracy",
                    13, Theme.TEXT_MUTED, false));
            categoryCard.add(Box.createVerticalStrut(8));
            JProgressBar accuracy = new JProgressBar(0, 100);
            accuracy.setValue(snapshot.accuracyPercent());
            accuracy.setStringPainted(true);
            accuracy.setString(snapshot.accuracyPercent() + "% accuracy");
            accuracy.setForeground(snapshot.accuracyPercent() >= 80 ? Theme.SUCCESS : Theme.TEAL);
            accuracy.setBackground(Theme.BG_DEEP);
            categoryCard.add(accuracy);
            categoryCard.add(Box.createVerticalStrut(8));
            categoryCard.add(UiKit.wrap(recommendation(snapshot), 780, 13, Theme.TEXT));
            content.add(categoryCard);
            content.add(Box.createVerticalStrut(12));
        }

        content.add(UiKit.wrap("Badge milestones: 50 answered questions = Good; 100 = Very Good; "
                + "200 = Excellent (5 Stars). Progress is tracked during this app session.",
                780, 12, Theme.TEXT_MUTED));
        content.revalidate();
        content.repaint();
    }

    private String recommendation(CategorySnapshot snapshot) {
        if (snapshot.attempted() == 0) {
            return "Not attempted yet. Start this level's quiz to build a baseline and discover which topics to practise.";
        }
        if (snapshot.accuracyPercent() < 60) {
            return "Focus area: review the experiment theory, equations and indicator observations for this level, "
                    + "then retry a quiz. Your current accuracy suggests these topics need more practice.";
        }
        if (snapshot.accuracyPercent() < 80) {
            return "Developing well. Revisit the questions you missed and practise applying the reaction equations "
                    + "and lab observations.";
        }
        return "Strength: excellent quiz accuracy in this level. Keep it strong by practising another set of questions.";
    }
}
