package com.chemquest.ui;

import com.chemquest.app.AppState;
import com.chemquest.model.User;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

/**
 * Welcome screen with login, sign-up, and the post-login "Enter the Lab" screen.
 */
public class WelcomeFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String ENTER = "enter";

    private final AppState state;
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHolder = new JPanel(cards);
    private final LoginPanel loginPanel;
    private final SignupPanel signupPanel;
    private final JLabel enterGreeting = UiKit.label(" ", 22, Theme.TEXT, true);

    public WelcomeFrame(AppState state) {
        super("ChemQuest - A Futuristic Virtual Chemistry Lab");
        this.state = state;

        loginPanel = new LoginPanel(state.auth(), this::onLoginSuccess, () -> showSignup());
        signupPanel = new SignupPanel(state.auth(),
                id -> showLogin(id, "Account created. Enter your password to log in."),
                () -> showLogin("", null));

        cardHolder.setOpaque(false);
        cardHolder.add(loginPanel, LOGIN);
        cardHolder.add(signupPanel, SIGNUP);
        cardHolder.add(buildEnterPanel(), ENTER);

        RoundedPanel authCard = new RoundedPanel(28, new java.awt.Color(18, 24, 20, 225), Theme.TEAL_DARK);
        authCard.setLayout(new java.awt.BorderLayout());
        authCard.setBorder(new EmptyBorder(26, 32, 26, 32));
        authCard.add(cardHolder);
        authCard.setPreferredSize(new Dimension(420, 560));
        authCard.setMaximumSize(new Dimension(420, 560));
        authCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.add(centeredLabel("ChemQuest", 64, Theme.MUSTARD, true));
        column.add(centeredLabel("A Futuristic Virtual Chemistry Lab", 22, Theme.TEAL, false));
        column.add(centeredLabel("Learn & Explore the Lab", 16, Theme.TEXT, false));
        column.add(javax.swing.Box.createVerticalStrut(22));
        column.add(authCard);

        LabBackgroundPanel background = new LabBackgroundPanel();
        background.setLayout(new GridBagLayout());
        background.add(column);
        setContentPane(background);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 760));
        setSize(1180, 800);
        setLocationRelativeTo(null);
        showLogin("", null);
    }

    private JLabel centeredLabel(String text, int size, java.awt.Color color, boolean bold) {
        JLabel l = UiKit.label(text, size, color, bold);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JPanel buildEnterPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        JButton enter = new RoundedButton("Enter the Lab \u2192", true);
        enter.setFont(Theme.bold(17));
        enter.addActionListener(e -> {
            dispose();
            DashboardFrame.openFor(state);
        });
        JButton switchAccount = UiKit.link("Not you? Log in with a different account", () -> showLogin("", null));
        UiKit.addRow(p, javax.swing.Box.createVerticalStrut(60), 0);
        UiKit.addRow(p, UiKit.label("Login successful", 13, Theme.SUCCESS, true), 0);
        UiKit.addRow(p, enterGreeting, 6);
        UiKit.addRow(p, UiKit.label("Your lab is ready.", 14, Theme.TEXT_MUTED, false), 6);
        UiKit.addRow(p, enter, 30);
        UiKit.addRow(p, UiKit.centered(switchAccount), 12);
        return p;
    }

    private void onLoginSuccess(User user) {
        state.login(user);
        UiKit.setMessage(enterGreeting, "Welcome, " + user.displayName() + "!", Theme.TEXT);
        enterGreeting.setFont(Theme.bold(22));
        cards.show(cardHolder, ENTER);
    }

    private void showLogin(String identifier, String info) {
        state.logout(); // make sure no stale session survives when returning to the login state
        loginPanel.prefill(identifier, info);
        cards.show(cardHolder, LOGIN);
    }

    private void showSignup() {
        signupPanel.reset();
        cards.show(cardHolder, SIGNUP);
    }
}
