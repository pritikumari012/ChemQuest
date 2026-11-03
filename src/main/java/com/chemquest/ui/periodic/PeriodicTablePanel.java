package com.chemquest.ui.periodic;

import com.chemquest.model.Element;
import com.chemquest.service.PeriodicTableService;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.ScrollPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.util.List;

public class PeriodicTablePanel extends JPanel {
    private static final int COLUMNS = 18;
    private final PeriodicTableService service;
    private final JTextField search = UiKit.textField();
    private final JLabel count = UiKit.label(" ", 12, Theme.TEXT_MUTED, false);
    private final JPanel detailPanel = new JPanel();
    private final JPanel table = new JPanel(new GridBagLayout());

    public PeriodicTablePanel(PeriodicTableService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        ScrollPanel content = new ScrollPanel(new BorderLayout(), 1000);
        content.setBorder(new EmptyBorder(20, 20, 24, 20));
        JPanel page = new JPanel();
        page.setOpaque(false);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(UiKit.label("Periodic Table", 28, Theme.TEXT, true));
        page.add(Box.createVerticalStrut(4));
        page.add(UiKit.label("Explore all " + service.count() + " elements by period and group. Select an element for details.",
                13, Theme.TEXT_MUTED, false));
        page.add(Box.createVerticalStrut(12));

        JPanel searchRow = new JPanel(new BorderLayout(12, 0));
        searchRow.setOpaque(false);
        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(count, BorderLayout.EAST);
        page.add(searchRow);
        page.add(Box.createVerticalStrut(14));
        page.add(buildLegend());
        page.add(Box.createVerticalStrut(12));

        table.setOpaque(false);
        JScrollPane tableScroll = new JScrollPane(table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBorder(null);
        tableScroll.setOpaque(false);
        tableScroll.getViewport().setOpaque(false);
        tableScroll.getHorizontalScrollBar().setUnitIncrement(24);
        tableScroll.setPreferredSize(new Dimension(980, 450));
        page.add(tableScroll);
        page.add(Box.createVerticalStrut(12));

        detailPanel.setOpaque(false);
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(new EmptyBorder(14, 16, 14, 16));
        detailPanel.add(UiKit.label("Element details", 20, Theme.MUSTARD, true));
        detailPanel.add(Box.createVerticalStrut(6));
        detailPanel.add(UiKit.label("Select an element in the table to inspect it.", 13, Theme.TEXT_MUTED, false));
        page.add(detailPanel);
        content.add(page, BorderLayout.NORTH);
        add(UiKit.scroller(content), BorderLayout.CENTER);

        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
        });
        refresh();
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel();
        legend.setOpaque(false);
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        JPanel blocks = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        blocks.setOpaque(false);
        blocks.add(UiKit.label("Blocks:", 12, Theme.TEXT, true));
        for (String block : List.of("s block", "p block", "d block", "f block")) {
            blocks.add(legendChip(block, new Color(0x43, 0x68, 0x8B)));
        }
        JPanel families = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        families.setOpaque(false);
        families.add(UiKit.label("Families:", 12, Theme.TEXT, true));
        for (String category : List.of("Alkali metals", "Alkaline earth", "Transition metals",
                "Other metals", "Metalloid", "Non-metals", "Halogen", "Noble gas",
                "Lanthanoids", "Actinoids")) {
            families.add(legendChip(category, categoryColor(category)));
        }
        legend.add(blocks);
        legend.add(families);
        return legend;
    }

    private JLabel legendChip(String text, Color color) {
        JLabel chip = UiKit.label("  " + text + "  ", 10, Theme.TEXT, true);
        chip.setOpaque(true);
        chip.setBackground(color);
        chip.setBorder(new EmptyBorder(4, 5, 4, 5));
        return chip;
    }

    private void refresh() {
        List<Element> elements = service.search(search.getText());
        table.removeAll();
        addPlaceholder("57–71\nLanthanides", 5, 2);
        addPlaceholder("89–103\nActinides", 6, 2);
        for (Element element : elements) {
            int row = element.period() - 1;
            int column = element.group() - 1;
            if (element.category().equals("Lanthanide")) {
                row = 7;
                column = element.atomicNumber() - 57 + 2;
            } else if (element.category().equals("Actinide")) {
                row = 8;
                column = element.atomicNumber() - 89 + 2;
            }
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = column;
            gbc.gridy = row;
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.fill = GridBagConstraints.BOTH;
            table.add(elementTile(element), gbc);
        }
        table.setPreferredSize(new Dimension(COLUMNS * 53, 9 * 68));
        count.setText(elements.size() + " / " + service.count());
        table.revalidate();
        table.repaint();
    }

    private void addPlaceholder(String text, int row, int column) {
        JLabel placeholder = new JLabel("<html><center>" + text.replace("\n", "<br>")
                + "</center></html>", JLabel.CENTER);
        placeholder.setOpaque(true);
        placeholder.setBackground(Theme.SURFACE_ALT);
        placeholder.setForeground(Theme.TEXT_MUTED);
        placeholder.setFont(Theme.font(9));
        placeholder.setPreferredSize(new Dimension(49, 62));
        placeholder.setToolTipText("Lanthanide and actinide series are shown below the main table.");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.BOTH;
        table.add(placeholder, gbc);
    }

    private JButton elementTile(Element element) {
        JButton tile = new JButton();
        tile.setToolTipText(element.name() + " · " + element.category() + " · " + blockFor(element) + " block");
        tile.setHorizontalAlignment(JButton.LEFT);
        tile.setVerticalAlignment(JButton.CENTER);
        tile.setText("<html><div style='text-align:left'>"
                + "<small>" + element.atomicNumber() + " · " + blockFor(element) + "</small><br>"
                + "<b style='font-size:16px'>" + element.symbol() + "</b><br>"
                + "<small>" + element.name() + "</small></div></html>");
        tile.setPreferredSize(new Dimension(49, 62));
        tile.setBackground(categoryColor(element.category()));
        tile.setForeground(Theme.TEXT);
        tile.setFont(Theme.font(9));
        tile.setFocusPainted(false);
        tile.setBorder(new EmptyBorder(4, 5, 4, 3));
        tile.setOpaque(true);
        tile.addActionListener(event -> showDetail(element));
        return tile;
    }

    private Color categoryColor(String category) {
        return switch (category) {
            case "Alkali metal", "Alkali metals" -> new Color(0x8B, 0x4E, 0x3E);
            case "Alkaline earth metal", "Alkaline earth" -> new Color(0x9B, 0x72, 0x3B);
            case "Transition metal", "Transition metals" -> new Color(0x5C, 0x6C, 0x78);
            case "Post-transition metal", "Other metals" -> new Color(0x4A, 0x78, 0x78);
            case "Metalloid" -> new Color(0x5B, 0x7B, 0x51);
            case "Non-metal", "Non-metals" -> new Color(0x3F, 0x78, 0x61);
            case "Halogen" -> new Color(0x7A, 0x65, 0x9A);
            case "Noble gas" -> new Color(0x4B, 0x68, 0x9A);
            case "Lanthanide", "Lanthanoids" -> new Color(0x9A, 0x5B, 0x7D);
            case "Actinide", "Actinoids" -> new Color(0x8B, 0x55, 0x61);
            default -> Theme.SURFACE;
        };
    }

    private String blockFor(Element element) {
        if (element.category().equals("Lanthanide") || element.category().equals("Actinide")) {
            return "f";
        }
        if (element.group() == 1 || element.group() == 2 || element.symbol().equals("He")) {
            return "s";
        }
        return element.group() <= 12 ? "d" : "p";
    }

    private void showDetail(Element element) {
        detailPanel.removeAll();
        RoundedPanel card = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.add(UiKit.label(element.name() + "  (" + element.symbol() + ")", 24, Theme.MUSTARD, true));
        card.add(Box.createVerticalStrut(5));
        card.add(UiKit.label("Atomic number " + element.atomicNumber() + "  •  Period " + element.period()
                + "  •  Group " + element.group() + "  •  " + blockFor(element) + " block", 13, Theme.TEAL, true));
        card.add(Box.createVerticalStrut(8));
        card.add(UiKit.wrap("Category: " + element.category() + ". " + element.summary(), 900, 13, Theme.TEXT));
        detailPanel.add(card);
        detailPanel.revalidate();
        detailPanel.repaint();
    }
}
