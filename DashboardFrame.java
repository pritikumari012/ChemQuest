package com.chemquest.ui;

import com.chemquest.app.AppState;
import com.chemquest.ui.NavigationPanel.Page;
import com.chemquest.ui.apparatus.ApparatusPanel;
import com.chemquest.ui.experiments.ExperimentsPanel;
import com.chemquest.ui.periodic.PeriodicTablePanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/** Main window after login: sidebar + a page area that switches without restarting anything. */
public class DashboardFrame extends JFrame {
    private final AppState state;
    private final CardLayout pages = new CardLayout();
    private final JPanel content = new JPanel(pages);
    private final NavigationPanel nav;

    /** Protected entry point: without a logged-in user the Welcome screen opens instead. */
    public static void openFor(AppState state) {
        if (!state.isLoggedIn()) {
            new WelcomeFrame(state).setVisible(true);
            return;
        }
        new DashboardFrame(state).setVisible(true);
    }

    private DashboardFrame(AppState state) {
        super("ChemQuest - Virtual Chemistry Lab");
        this.state = state;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        nav = new NavigationPanel(state.currentUser().displayName(), this::onNavigate);
        content.setBackground(Theme.BG);

        content.add(new DashboardHomePanel(state, this::onNavigate), Page.DASHBOARD.name());
        content.add(new PeriodicTablePanel(state.periodicTable()), Page.PERIODIC_TABLE.name());
        content.add(new ApparatusPanel(state.apparatus()), Page.APPARATUS.name());
        content.add(new ExperimentsPanel(state.experiments(), state.apparatus(), this::onNavigate), Page.EXPERIMENTS.name());
        content.add(placeholder(Page.VIRTUAL_LAB, "Phase 4",
                "Five interactive simulations (neutralisation, oxygen preparation, indicators and pH, metal reactivity, precipitation) with a rotatable beaker and a documented pH model."), Page.VIRTUAL_LAB.name());
        content.add(placeholder(Page.QUIZ, "Phase 5",
                "A 60-question bank (20 per category) with instant feedback, XP rewards and a results summary."), Page.QUIZ.name());
        content.add(placeholder(Page.PROGRESS, "Phase 5",
                "Progress derived from your real activity: accuracy, XP, category progress bars, recent activity and badges."), Page.PROGRESS.name());

        setLayout(new BorderLayout());
        add(nav, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        setMinimumSize(new Dimension(1000, 680));
        setSize(1280, 800);
        setLocationRelativeTo(null);
        show(Page.DASHBOARD);
    }

    private JPanel placeholder(Page page, String phase, String description) {
        return new ModulePlaceholderPanel(page.title(), description, phase, () -> show(Page.DASHBOARD));
    }

    private void onNavigate(Page page) {
        if (!state.isLoggedIn()) { // guard: protected pages need an active session
            returnToWelcome();
            return;
        }
        if (page == Page.LOGOUT) {
            confirmLogout();
        } else {
            show(page);
        }
    }

    private void show(Page page) {
        pages.show(content, page.name());
        nav.setSelected(page);
    }

    private void confirmLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to log out of ChemQuest?",
                "Confirm logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            state.logout();
            returnToWelcome();
        }
    }

    private void returnToWelcome() {
        dispose();
        SwingUtilities.invokeLater(() -> new WelcomeFrame(state).setVisible(true));
    }
}
