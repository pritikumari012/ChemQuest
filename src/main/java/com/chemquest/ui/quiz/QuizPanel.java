package com.chemquest.ui.quiz;

import com.chemquest.model.ExperimentCategory;
import com.chemquest.model.QuizQuestion;
import com.chemquest.service.QuizService;
import com.chemquest.service.ProgressService;
import com.chemquest.ui.RoundedButton;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.ScrollPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class QuizPanel extends JPanel {
    private final QuizService quizService;
    private final ProgressService progressService;
    private final JPanel content = new JPanel();
    private ExperimentCategory category;
    private List<QuizQuestion> questions = List.of();
    private int questionIndex;
    private int score;
    private boolean answered;

    public QuizPanel(QuizService quizService, ProgressService progressService) {
        this.quizService = quizService;
        this.progressService = progressService;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        ScrollPanel scroll = new ScrollPanel(new BorderLayout(), 760);
        scroll.setBorder(new EmptyBorder(24, 28, 28, 28));
        scroll.add(content, BorderLayout.NORTH);
        add(UiKit.scroller(scroll), BorderLayout.CENTER);
        showCategories();
    }

    private void showCategories() {
        content.removeAll();
        heading("Chemistry Quiz", "Choose a level to start a 10-question multiple-choice quiz.");
        for (ExperimentCategory level : ExperimentCategory.values()) {
            RoundedPanel card = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
            card.setLayout(new BorderLayout(12, 8));
            card.setBorder(new EmptyBorder(18, 20, 18, 20));
            JPanel labels = new JPanel();
            labels.setOpaque(false);
            labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
            labels.add(UiKit.label(level.label() + " Chemistry", 20, Theme.MUSTARD, true));
            labels.add(Box.createVerticalStrut(5));
            labels.add(UiKit.label(quizService.count(level) + " questions  •  " + (quizService.count(level) * 10)
                    + " XP available", 13, Theme.TEXT_MUTED, false));
            card.add(labels, BorderLayout.CENTER);
            RoundedButton start = new RoundedButton("Start Quiz", true);
            start.addActionListener(event -> start(level));
            card.add(start, BorderLayout.EAST);
            content.add(card);
            content.add(Box.createVerticalStrut(14));
        }
        content.add(UiKit.label(quizService.totalCount() + " questions total  •  +10 XP per correct answer  •  No negative marking",
                13, Theme.TEAL, true));
        content.add(Box.createVerticalStrut(8));
        content.add(UiKit.label("Completed quizzes: " + quizService.completedQuizzes()
                + "  •  Total XP earned: " + quizService.earnedXp(), 13, Theme.TEXT_MUTED, false));
        refreshContent();
    }

    private void start(ExperimentCategory level) {
        category = level;
        questions = quizService.questions(level);
        questionIndex = 0;
        score = 0;
        answered = false;
        showQuestion();
    }

    private void showQuestion() {
        content.removeAll();
        QuizQuestion question = questions.get(questionIndex);
        heading(category.label() + " Chemistry Quiz",
                "Question " + (questionIndex + 1) + " of " + questions.size() + "  •  Score: " + score + " XP");

        RoundedPanel card = new RoundedPanel(20, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(22, 24, 22, 24));
        card.add(UiKit.wrap(question.prompt(), 650, 20, Theme.TEXT));
        card.add(Box.createVerticalStrut(18));

        JPanel answerPanel = new JPanel();
        answerPanel.setOpaque(false);
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        card.add(answerPanel);

        JPanel feedback = new JPanel();
        feedback.setOpaque(false);
        feedback.setLayout(new BoxLayout(feedback, BoxLayout.Y_AXIS));
        card.add(Box.createVerticalStrut(14));
        card.add(feedback);
        content.add(card);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        RoundedButton next = new RoundedButton("Next Question", true);
        next.setVisible(false);
        next.addActionListener(event -> {
            if (questionIndex + 1 < questions.size()) {
                questionIndex++;
                answered = false;
                showQuestion();
            } else {
                showResults();
            }
        });
        RoundedButton quit = new RoundedButton("Quit Quiz", false);
        quit.addActionListener(event -> showCategories());
        actions.add(next);
        actions.add(quit);
        content.add(Box.createVerticalStrut(14));
        content.add(actions);

        for (int i = 0; i < question.options().size(); i++) {
            int selected = i;
            RoundedButton option = new RoundedButton((char) ('A' + i) + ".  " + question.options().get(i), false);
            option.setFont(Theme.font(14));
            option.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            option.setAlignmentX(LEFT_ALIGNMENT);
            option.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 42));
            option.addActionListener(event -> {
                if (answered) {
                    return;
                }
                answered = true;
                boolean correct = selected == question.correctIndex();
                if (correct) {
                    score += 10;
                    option.setPrimary(true);
                } else {
                    option.setForeground(Theme.ERROR);
                }
                for (java.awt.Component component : answerPanel.getComponents()) {
                    if (component instanceof RoundedButton button) {
                        button.setEnabled(false);
                    }
                }
                feedback.add(UiKit.label(correct ? "Correct! +10 XP" : "Not quite. The correct answer is: "
                        + question.correctAnswer(), 14, correct ? Theme.SUCCESS : Theme.WARNING, true));
                feedback.add(Box.createVerticalStrut(5));
                feedback.add(UiKit.wrap(question.explanation(), 620, 13, Theme.TEXT_MUTED));
                next.setText(questionIndex + 1 == questions.size() ? "See Results" : "Next Question");
                next.setVisible(true);
                content.revalidate();
                content.repaint();
            });
            answerPanel.add(option);
            answerPanel.add(Box.createVerticalStrut(8));
        }
        refreshContent();
    }

    private void showResults() {
        quizService.recordCompletedQuiz(score);
        content.removeAll();
        int total = questions.size() * 10;
        int correctAnswers = score / 10;
        progressService.recordQuiz(category, questions.size(), correctAnswers);
        heading("Quiz Complete", category.label() + " Chemistry");

        RoundedPanel result = new RoundedPanel(22, Theme.SURFACE, Theme.SURFACE_ALT);
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setBorder(new EmptyBorder(26, 30, 26, 30));
        result.add(UiKit.label(score + " / " + total + " XP", 34, Theme.MUSTARD, true));
        result.add(Box.createVerticalStrut(8));
        result.add(UiKit.label(correctAnswers + " correct out of " + questions.size() + " questions",
                17, Theme.TEXT, true));
        result.add(Box.createVerticalStrut(8));
        result.add(UiKit.label("Accuracy: " + Math.round(100.0 * correctAnswers / questions.size()) + "%",
                14, Theme.TEAL, true));
        content.add(result);
        content.add(Box.createVerticalStrut(16));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        RoundedButton retry = new RoundedButton("Try Again", true);
        retry.addActionListener(event -> start(category));
        RoundedButton levels = new RoundedButton("Choose Another Level", false);
        levels.addActionListener(event -> showCategories());
        actions.add(retry);
        actions.add(levels);
        content.add(actions);
        refreshContent();
    }

    private void heading(String title, String subtitle) {
        JLabel titleLabel = UiKit.label(title, 28, Theme.TEXT, true);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        JLabel subtitleLabel = UiKit.label(subtitle, 13, Theme.TEXT_MUTED, false);
        subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(4));
        content.add(subtitleLabel);
        content.add(Box.createVerticalStrut(18));
    }

    private void refreshContent() {
        content.revalidate();
        content.repaint();
    }
}
