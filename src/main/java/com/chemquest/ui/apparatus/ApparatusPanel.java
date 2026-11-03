package com.chemquest.ui.apparatus;

import com.chemquest.model.Apparatus;
import com.chemquest.service.ApparatusService;
import com.chemquest.ui.RoundedButton;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

public class ApparatusPanel extends JPanel {
    private final ApparatusService apparatusService;
    private final ApparatusList list = new ApparatusList();
    private final JTextField search = UiKit.textField();
    private final JLabel count = UiKit.label(" ", 12, Theme.TEXT_MUTED, false);

    public ApparatusPanel(ApparatusService apparatusService) {
        this.apparatusService = apparatusService;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 28, 16, 28));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(UiKit.label("Apparatus", 28, Theme.TEXT, true));
        header.add(Box.createVerticalStrut(4));
        header.add(UiKit.label("Browse laboratory tools. Scroll to explore the full collection; drag a 3D model to rotate it.",
                13, Theme.TEXT_MUTED, false));
        header.add(Box.createVerticalStrut(12));

        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(count, BorderLayout.EAST);
        header.add(searchRow);
        add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getVerticalScrollBar().setBlockIncrement(180);
        scroll.getVerticalScrollBar().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setForeground(Theme.TEAL);
        add(scroll, BorderLayout.CENTER);

        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
        });
        refresh();
    }

    private void refresh() {
        list.removeAll();
        List<Apparatus> apparatusItems = apparatusService.search(search.getText());
        for (Apparatus apparatus : apparatusItems) {
            list.add(card(apparatus));
            list.add(Box.createVerticalStrut(14));
        }
        if (apparatusItems.isEmpty()) {
            JLabel empty = UiKit.label("No apparatus matched your search.", 14, Theme.TEXT_MUTED, false);
            empty.setBorder(new EmptyBorder(20, 28, 20, 28));
            list.add(empty);
        }
        count.setText(apparatusItems.size() + " items");
        list.revalidate();
        list.repaint();
    }

    private JPanel card(Apparatus apparatus) {
        RoundedPanel card = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(new EmptyBorder(12, 14, 12, 18));
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 194));
        card.setPreferredSize(new Dimension(760, 194));

        card.add(new Apparatus3DView(apparatus.name(), 230, 160), BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel category = UiKit.label(apparatus.category(), 11, Theme.TEAL, true);
        JLabel name = UiKit.label(apparatus.name(), 20, Theme.MUSTARD, true);
        JLabel details = UiKit.wrap(apparatus.description(), 420, 13, Theme.TEXT);
        category.setAlignmentX(LEFT_ALIGNMENT);
        name.setAlignmentX(LEFT_ALIGNMENT);
        details.setAlignmentX(LEFT_ALIGNMENT);
        text.add(category);
        text.add(Box.createVerticalStrut(3));
        text.add(name);
        text.add(Box.createVerticalStrut(7));
        text.add(details);

        RoundedButton detail = new RoundedButton("See details", true);
        detail.setFont(Theme.bold(12));
        detail.addActionListener(event -> openDetails(apparatus));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        actions.add(detail);
        text.add(Box.createVerticalGlue());
        text.add(actions);
        card.add(text, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(760, 190));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        UiKit.onClick(card, () -> openDetails(apparatus));
        return card;
    }

    private void openDetails(Apparatus apparatus) {
        new ApparatusDetailDialog(null, apparatus, apparatusService.experimentsUsing(apparatus)).setVisible(true);
    }

    private static final class ApparatusList extends JPanel implements javax.swing.Scrollable {
        private ApparatusList() {
            setOpaque(false);
            setBorder(new EmptyBorder(8, 28, 24, 28));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        @Override public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(900, 600);
        }

        @Override public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return 24;
        }

        @Override public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return Math.max(100, visibleRect.height - 48);
        }

        @Override public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
