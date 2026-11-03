package com.chemquest.ui.experiments;

import com.chemquest.model.Experiment;
import com.chemquest.model.ExperimentCategory;
import com.chemquest.service.ApparatusService;
import com.chemquest.service.ExperimentService;
import com.chemquest.ui.CardGrid;
import com.chemquest.ui.NavigationPanel.Page;
import com.chemquest.ui.RoundedButton;
import com.chemquest.ui.RoundedPanel;
import com.chemquest.ui.ScrollPanel;
import com.chemquest.ui.Theme;
import com.chemquest.ui.UiKit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Filterable experiment cards with a full detail page for each experiment. */
public class ExperimentsPanel extends JPanel {
    private static final String LIST = "list";
    private static final String DETAIL = "detail";

    private final ExperimentService experiments;
    private final ApparatusService apparatus;
    private final Consumer<Page> navigate;
    private final CardLayout cards = new CardLayout();
    private final JPanel detailHolder = new JPanel(new BorderLayout());
    private final CardGrid grid = new CardGrid(330);
    private final JTextField search = UiKit.textField();
    private final JLabel count = UiKit.label(" ", 12, Theme.TEXT_MUTED, false);
    private final Map<RoundedButton, ExperimentCategory> chips = new LinkedHashMap<>();
    private ExperimentCategory filter;

    public ExperimentsPanel(ExperimentService experiments, ApparatusService apparatus, Consumer<Page> navigate) {
        this.experiments = experiments;
        this.apparatus = apparatus;
        this.navigate = navigate;
        setLayout(cards);
        setBackground(Theme.BG);
        detailHolder.setBackground(Theme.BG);
        add(buildListPage(), LIST);
        add(detailHolder, DETAIL);
        refresh();
    }

    private JPanel buildListPage() {
        ScrollPanel content = new ScrollPanel(new BorderLayout(), 700);
        content.setBorder(new EmptyBorder(24, 28, 28, 28));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        JLabel title = UiKit.label("Experiments", 28, Theme.TEXT, true);
        title.setAlignmentX(LEFT_ALIGNMENT);
        JLabel sub = UiKit.label("Explore " + experiments.count()
                + " important Basic, Intermediate and Advanced chemistry experiments. Open any card for the full write-up.",
                13, Theme.TEXT_MUTED, false);
        sub.setAlignmentX(LEFT_ALIGNMENT);
        head.add(title);
        head.add(sub);
        head.add(Box.createVerticalStrut(12));

        JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipRow.setOpaque(false);
        chipRow.setAlignmentX(LEFT_ALIGNMENT);
        addChip(chipRow, "All", null);
        for (ExperimentCategory c : ExperimentCategory.values()) {
            addChip(chipRow, c.label() + " (" + c.level() + ")", c);
        }
        head.add(chipRow);
        head.add(Box.createVerticalStrut(10));

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        row.add(search, BorderLayout.CENTER);
        row.add(count, BorderLayout.EAST);
        head.add(row);
        head.add(Box.createVerticalStrut(16));

        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                refresh();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                refresh();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                refresh();
            }
        });

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(head, BorderLayout.NORTH);
        north.add(grid, BorderLayout.CENTER);
        content.add(north, BorderLayout.NORTH);
        return wrap(UiKit.scroller(content));
    }

    private JPanel wrap(javax.swing.JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);
        p.add(c);
        return p;
    }

    private void addChip(JPanel row, String text, ExperimentCategory category) {
        RoundedButton chip = new RoundedButton(text, category == null);
        chip.setFont(Theme.bold(12));
        chip.addActionListener(e -> {
            filter = category;
            chips.forEach((b, c) -> b.setPrimary(c == filter));
            refresh();
        });
        chips.put(chip, category);
        row.add(chip);
    }

    private void refresh() {
        List<Experiment> list = experiments.search(filter, search.getText());
        grid.removeAll();

        if (list.isEmpty()) {
            grid.add(UiKit.label("No experiments match your search.", 14, Theme.TEXT_MUTED, false));
            count.setText("0 of " + experiments.count());
            grid.revalidate();
            grid.repaint();
            return;
        }

        for (ExperimentCategory category : ExperimentCategory.values()) {
            List<Experiment> group = list.stream()
                    .filter(e -> e.category() == category)
                    .toList();
            if (group.isEmpty()) {
                continue;
            }

            JPanel section = new JPanel();
            section.setOpaque(false);
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            JLabel heading = UiKit.label(category.label() + " experiments", 18, Theme.MUSTARD, true);
            heading.setAlignmentX(LEFT_ALIGNMENT);
            section.add(heading);
            section.add(Box.createVerticalStrut(10));
            for (Experiment e : group) {
                section.add(card(e));
                section.add(Box.createVerticalStrut(12));
            }
            grid.add(section);
        }

        count.setText(list.size() + " of " + experiments.count());
        grid.revalidate();
        grid.repaint();
    }

    private RoundedPanel card(Experiment e) {
        RoundedPanel card = new RoundedPanel(22, Theme.SURFACE, Theme.SURFACE_ALT);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel cat = UiKit.label(e.category().label() + "  \u2022  " + e.difficulty(), 11, Theme.TEAL, true);
        JLabel title = UiKit.wrap(e.title(), 270, 16, Theme.MUSTARD);
        title.setFont(Theme.bold(16));
        JLabel desc = UiKit.wrap(e.shortDesc(), 270, 12, Theme.TEXT);
        JLabel meta = UiKit.label("~" + e.minutes() + " min  \u2022  " + e.apparatus().size() + " apparatus  \u2022  " + e.category().level(),
                11, Theme.TEXT_MUTED, false);
        for (JLabel l : new JLabel[]{cat, title, desc, meta}) {
            l.setAlignmentX(LEFT_ALIGNMENT);
        }
        text.add(cat);
        text.add(Box.createVerticalStrut(4));
        text.add(title);
        text.add(Box.createVerticalStrut(6));
        text.add(desc);
        text.add(Box.createVerticalStrut(8));
        text.add(meta);
        card.add(text, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        RoundedButton explore = new RoundedButton("Explore Experiment", true);
        explore.setFont(Theme.bold(12));
        explore.addActionListener(ev -> open(e));
        RoundedButton quiz = new RoundedButton("Take Quiz", false);
        quiz.setFont(Theme.bold(12));
        quiz.addActionListener(ev -> navigate.accept(Page.QUIZ));
        buttons.add(explore);
        buttons.add(quiz);
        card.add(buttons, BorderLayout.SOUTH);
        UiKit.onClick(card, () -> open(e));
        return card;
    }

    private void open(Experiment e) {
        detailHolder.removeAll();
        detailHolder.add(new ExperimentDetailPanel(e, apparatus, this::backToList,
                () -> navigate.accept(Page.QUIZ), () -> navigate.accept(Page.VIRTUAL_LAB)));
        detailHolder.revalidate();
        cards.show(this, DETAIL);
    }

    private void backToList() {
        cards.show(this, LIST);
    }
}
