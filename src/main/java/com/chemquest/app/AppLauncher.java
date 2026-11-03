package com.chemquest.app;

import com.chemquest.ui.WelcomeFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

/** Starts the Swing application on the Event Dispatch Thread. */
public final class AppLauncher {
    private AppLauncher() { }

    public static void launch() {
        System.setProperty("sun.java2d.opengl", "false"); // safest rendering default on laptops
        SwingUtilities.invokeLater(() -> {
            themeDialogs();
            AppState state = new AppState();
            new WelcomeFrame(state).setVisible(true);
        });
    }

    /** Makes JOptionPane dialogs (e.g. the logout confirmation) match the dark theme. */
    private static void themeDialogs() {
        Color bg = new Color(0x27, 0x2F, 0x25);
        Color fg = new Color(0xF4, 0xEF, 0xE3);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("Panel.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);
        UIManager.put("Label.foreground", fg);
    }
}
