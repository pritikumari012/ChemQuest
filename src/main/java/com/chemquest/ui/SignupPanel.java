package com.chemquest.ui;

import com.chemquest.service.AuthException;
import com.chemquest.service.AuthenticationService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.util.function.Consumer;

/** Create-account form. Calls AuthenticationService; shows validation or success messages. */
public class SignupPanel extends JPanel {
    private final AuthenticationService auth;
    private final Consumer<String> onGoToLogin;
    private final JTextField idField = UiKit.textField();
    private final JPasswordField pwField = UiKit.passwordField();
    private final JPasswordField confirmField = UiKit.passwordField();
    private final JLabel message = UiKit.label(" ", 13, Theme.TEXT_MUTED, false);
    private final JButton createButton = new RoundedButton("Create Account", true);
    private final JButton continueButton = new RoundedButton("Continue to Login", false);

    public SignupPanel(AuthenticationService auth, Consumer<String> onGoToLogin, Runnable onBackToLogin) {
        this.auth = auth;
        this.onGoToLogin = onGoToLogin;
        setOpaque(false);
        setLayout(new GridBagLayout());

        UiKit.addRow(this, UiKit.label("Create your account", 22, Theme.TEXT, true), 0);
        UiKit.addRow(this, UiKit.label("Email address or phone number", 13, Theme.TEXT_MUTED, false), 14);
        UiKit.addRow(this, idField, 4);
        UiKit.addRow(this, UiKit.label("Password (min 8 chars, letters + numbers)", 13, Theme.TEXT_MUTED, false), 10);
        UiKit.addRow(this, pwField, 4);
        UiKit.addRow(this, UiKit.label("Confirm password", 13, Theme.TEXT_MUTED, false), 10);
        UiKit.addRow(this, confirmField, 4);
        UiKit.addRow(this, message, 10);
        UiKit.addRow(this, createButton, 6);
        UiKit.addRow(this, continueButton, 6);
        UiKit.addRow(this, UiKit.centered(UiKit.link("Already have an account? Log in", onBackToLogin)), 6);
        UiKit.addRow(this, UiKit.label("<html><div style='width:320px'>Local demo accounts: stored on this computer only "
                + "(not in SQL). Passwords are saved as salted hashes.</div></html>", 11, Theme.TEXT_MUTED, false), 6);

        continueButton.setVisible(false);
        createButton.addActionListener(e -> submit());
        idField.addActionListener(e -> pwField.requestFocusInWindow());
        pwField.addActionListener(e -> confirmField.requestFocusInWindow());
        confirmField.addActionListener(e -> submit());
        continueButton.addActionListener(e -> onGoToLogin.accept(idField.getText().trim()));
    }

    private void submit() {
        try {
            auth.register(idField.getText(), new String(pwField.getPassword()), new String(confirmField.getPassword()));
            UiKit.setMessage(message, "Account created successfully! You can now log in.", Theme.SUCCESS);
            pwField.setText("");
            confirmField.setText("");
            createButton.setVisible(false);
            continueButton.setVisible(true);
        } catch (AuthException ex) {
            UiKit.setMessage(message, ex.getMessage(), Theme.ERROR);
        }
    }

    /** Called each time this form is shown so it starts fresh. */
    public void reset() {
        idField.setText("");
        pwField.setText("");
        confirmField.setText("");
        message.setText(" ");
        createButton.setVisible(true);
        continueButton.setVisible(false);
        idField.requestFocusInWindow();
    }
}
