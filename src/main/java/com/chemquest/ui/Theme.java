package com.chemquest.ui;

import java.awt.Color;
import java.awt.Font;

/** Central colour and font palette so every screen looks consistent. */
public final class Theme {
    private Theme() { }

    public static final Color BG_DEEP = new Color(0x12, 0x16, 0x13);
    public static final Color BG = new Color(0x1C, 0x21, 0x1B);
    public static final Color SURFACE = new Color(0x27, 0x2F, 0x25);
    public static final Color SURFACE_ALT = new Color(0x32, 0x3C, 0x30);
    public static final Color FIELD_BG = new Color(0x1A, 0x20, 0x19);

    public static final Color MUSTARD = new Color(0xE3, 0xB5, 0x2E);
    public static final Color MUSTARD_LIGHT = new Color(0xF2, 0xCB, 0x5A);
    public static final Color TEAL = new Color(0x2F, 0xB8, 0xA8);
    public static final Color TEAL_DARK = new Color(0x1F, 0x7F, 0x76);

    public static final Color TEXT = new Color(0xF4, 0xEF, 0xE3);
    public static final Color TEXT_MUTED = new Color(0xB5, 0xB8, 0xA8);
    public static final Color SUCCESS = new Color(0x5F, 0xC2, 0x6B);
    public static final Color WARNING = new Color(0xF0, 0xA2, 0x2B);
    public static final Color ERROR = new Color(0xE5, 0x5B, 0x4D);

    public static Font font(int size) {
        return new Font("Segoe UI", Font.PLAIN, size);
    }

    public static Font bold(int size) {
        return new Font("Segoe UI", Font.BOLD, size);
    }
}
