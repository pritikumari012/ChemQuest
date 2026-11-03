package com.chemquest.ui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class ScrollPanel extends JPanel {
    public ScrollPanel(LayoutManager layout, int preferredWidth) {
        super(layout);
        setOpaque(false);
        setPreferredSize(new Dimension(preferredWidth, 600));
    }
}
