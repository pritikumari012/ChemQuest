package com.chemquest.ui;

import com.chemquest.model.User;
import com.chemquest.service.AuthException;
import com.chemquest.service.AuthenticationService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.util.function.Consumer;

/** Login form. On success it hands the User to the callback; it never stores the password. */
public class LoginPanel extends JPanel {
    private final AuthenticationService auth;
    private final Consumer<User> onSuccess;
    private final JTextField idField = UiKit.textField();
    private final JPasswordField pwField = UiKit.passwordField();
    private final JLabel message = UiKit.label(" ", 13, Theme.TEXT_MUTED, false);

    public LoginPanel(AuthenticationService auth, Consumer<User> onSuccess, Runnable onGoToSignup) {
        this.auth = auth;
        this.onSuccess = onSuccess;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JButton loginButton = new RoundedButton("Login", true);
        UiKit.addRow(this, UiKit.label("Welcome back", 22, Theme.TEXT, true), 0);
        UiKit.addRow(this, UiKit.label("Email address or phone number", 13, Theme.TEXT_MUTED, false), 14);
        UiKit.addRow(this, idField, 4);
        UiKit.addRow(this, UiKit.label("Password", 13, Theme.TEXT_MUTED, false), 10);
        UiKit.addRow(this, pwField, 4);
        UiKit.addRow(this, message, 10);
        UiKit.addRow(this, loginButton, 6);
        UiKit.addRow(this, UiKit.centered(UiKit.link("New to ChemQuest? Create an account", onGoToSignup)), 8);
        UiKit.addRow(this, UiKit.label("<html><div style='width:320px'>Local demo authentication: accounts live on this "
                + "computer only (not in SQL).</div></html>", 11, Theme.TEXT_MUTED, false), 6);

        loginButton.addActionListener(e -> submit());
        idField.addActionListener(e -> pwField.requestFocusInWindow());
        pwField.addActionListener(e -> submit());
    }

    private void submit() {
        try {
            User user = auth.login(idField.getText(), new String(pwField.getPassword()));
            pwField.setText("");
            message.setText(" ");
            onSuccess.accept(user);
        } catch (AuthException ex) {
            UiKit.setMessage(message, ex.getMessage(), Theme.ERROR);
        }
    }

    public void prefill(String identifier, String info) {
        idField.setText(identifier);
        pwField.setText("");
        if (info == null) {
            message.setText(" ");
        } else {
            UiKit.setMessage(message, info, Theme.SUCCESS);
        }
        (identifier.isEmpty() ? idField : pwField).requestFocusInWindow();
    }
}
