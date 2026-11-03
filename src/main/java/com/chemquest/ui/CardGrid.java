package com.chemquest.ui;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class CardGrid extends JPanel {
    public CardGrid(int cardWidth) {
        setOpaque(false);
        setLayout(new GridLayout(0, 1, 12, 12));
        setAlignmentX(LEFT_ALIGNMENT);
    }
}
