package com.chemquest.ui;

import com.chemquest.ui.apparatus.VirtualReactionView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Virtual chemistry workbench with illustrative, non-laboratory reaction models. */
public class VirtualLabPanel extends JPanel {
    private static final List<Chemical> CHEMICALS = List.of(
            new Chemical("CaCO3", "Calcium carbonate", "Limestone / chalk", "solid", 10.0, "g", null),
            new Chemical("CaO", "Calcium oxide", "Quicklime", "solid", 5.60, "g", null),
            new Chemical("CO2", "Carbon dioxide", "Carbon dioxide gas", "gas", 4.40, "g", null),
            new Chemical("CaCl2", "Calcium chloride", "Calcium chloride", "aqueous", 25, "mL", 0.10),
            new Chemical("HCl", "Hydrochloric acid", "Muriatic acid", "aqueous", 25, "mL", 0.10),
            new Chemical("NaOH", "Sodium hydroxide", "Caustic soda / lye", "aqueous", 25, "mL", 0.10),
            new Chemical("NaCl", "Sodium chloride", "Table salt", "aqueous", 25, "mL", 0.10),
            new Chemical("H2O", "Water", "Water", "liquid", 25, "mL", null),
            new Chemical("Mg", "Magnesium", "Magnesium metal", "solid", 1.0, "g", null),
            new Chemical("MgCl2", "Magnesium chloride", "Magnesium chloride", "aqueous", 25, "mL", 0.10),
            new Chemical("H2", "Hydrogen", "Hydrogen gas", "gas", 25, "mL", null),
            new Chemical("O2", "Oxygen", "Oxygen gas", "gas", 25, "mL", null),
            new Chemical("CuSO4", "Copper(II) sulfate", "Blue vitriol", "aqueous", 25, "mL", 0.10),
            new Chemical("Fe", "Iron", "Iron metal", "solid", 1.0, "g", null),
            new Chemical("FeSO4", "Iron(II) sulfate", "Ferrous sulfate", "aqueous", 25, "mL", 0.10),
            new Chemical("AgNO3", "Silver nitrate", "Lunar caustic", "aqueous", 25, "mL", 0.10),
            new Chemical("AgCl", "Silver chloride", "Silver chloride precipitate", "solid", 1.0, "g", null),
            new Chemical("NaNO3", "Sodium nitrate", "Soda niter", "aqueous", 25, "mL", 0.10),
            new Chemical("H2O2", "Hydrogen peroxide", "Peroxide solution", "aqueous", 25, "mL", 0.10)
    );

    private static final List<Reaction> REACTIONS = List.of(
            new Reaction("Thermal decomposition of calcium carbonate",
                    "CaCO3(s)  →  CaO(s) + CO2(g)",
                    List.of("CaCO3"), List.of("CaO", "CO2"),
                    "For the library's 10.0 g CaCO3 sample, theoretical products are about 5.60 g CaO and 4.40 g CO2. Total mass is 10.0 g in a closed system; CO2 escaping an open vessel lowers the measured vessel contents.",
                    "Model condition: heat is required (shown only; no real heating).", "Not applicable (dry thermal reaction)", 35),
            new Reaction("Acid–base neutralisation",
                    "HCl(aq) + NaOH(aq)  →  NaCl(aq) + H2O(l)",
                    List.of("HCl", "NaOH"), List.of("NaCl", "H2O"),
                    "Hydrochloric acid and sodium hydroxide react in a 1:1 mole ratio. Near the stoichiometric endpoint, the ideal strong-acid/strong-base model is approximately neutral.",
                    "Model condition: aqueous solutions; no real dispensing or mixing.", "Approximately pH 7 at stoichiometric equivalence", 70),
            new Reaction("Acid + carbonate",
                    "CaCO3(s) + 2HCl(aq)  →  CaCl2(aq) + H2O(l) + CO2(g)",
                    List.of("CaCO3", "HCl"), List.of("CaCl2", "H2O", "CO2"),
                    "Acids react with carbonates to produce a salt, water and carbon dioxide. Excess acid leaves an acidic solution; the displayed pH is a qualitative model estimate.",
                    "Model condition: aqueous acid and carbonate; no real gas is produced.", "Approximate acidic result; pH depends on quantities", 65),
            new Reaction("Metal + acid",
                    "Mg(s) + 2HCl(aq)  →  MgCl2(aq) + H2(g)",
                    List.of("Mg", "HCl"), List.of("MgCl2", "H2"),
                    "Magnesium displaces hydrogen from hydrochloric acid. Acid may remain after the metal is consumed; pH depends on starting quantities.",
                    "Model condition: gas formation shown visually only. Hydrogen is flammable.", "Approximate acidic result; pH depends on quantities", 60),
            new Reaction("Precipitation",
                    "AgNO3(aq) + NaCl(aq)  →  AgCl(s) + NaNO3(aq)",
                    List.of("AgNO3", "NaCl"), List.of("AgCl", "NaNO3"),
                    "Silver and chloride ions form insoluble silver chloride. The model displays precipitate formation; it does not mix chemicals.",
                    "Model condition: aqueous solutions; no real precipitate is formed.", "pH is not predicted by this simple precipitation model", 55)
    );

    private final DefaultListModel<Chemical> chemicalModel = new DefaultListModel<>();
    private final DefaultListModel<DosedChemical> selectedModel = new DefaultListModel<>();
    private final Map<String, DosedChemical> selectedChemicals = new LinkedHashMap<>();
    private final JList<Chemical> chemicalList = new JList<>(chemicalModel);
    private final JList<DosedChemical> selectedList = new JList<>(selectedModel);
    private final JComboBox<Reaction> reactionPicker = new JComboBox<>();
    private final JSpinner quantity = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 1000.0, 0.5));
    private final JLabel quantityUnit = UiKit.label("g", 12, Theme.TEXT_MUTED, false);
    private final JLabel equation = UiKit.label("Choose an experiment to see its equation and reactant controls.", 15, Theme.MUSTARD_LIGHT, true);
    private final JLabel status = UiKit.label("Choose an experiment from the selector below to begin.", 12, Theme.TEXT_MUTED, false);
    private final JLabel explanation = UiKit.label(" ", 12, Theme.TEXT, false);
    private final JLabel heatRequirement = UiKit.label("Condition: choose an experiment", 12, Theme.WARNING, true);
    private final JLabel pHReading = UiKit.label("Result pH: —", 12, Theme.TEXT, true);
    private final JSlider pHBar = new JSlider(0, 14, 7);
    private final JProgressBar liquidLevel = new JProgressBar(0, 100);
    private final JPanel reactionDetails = new JPanel();
    private final JPanel reactantRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
    private final VirtualReactionView reactionView = new VirtualReactionView(520, 390);
    private boolean reactionHasRun;
    private boolean preserveReactantsOnReactionChange;
    private int currentLiquidLevel;

    public VirtualLabPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(12, 16, 12, 16));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildWorkbench(), BorderLayout.CENTER);
        add(buildExperimentBar(), BorderLayout.SOUTH);
        CHEMICALS.forEach(chemicalModel::addElement);
        chemicalList.setSelectedIndex(0);
        reactionPicker.addItem(null);
        REACTIONS.forEach(reactionPicker::addItem);
        reactionPicker.addActionListener(event -> showSelectedReaction());
        chemicalList.addListSelectionListener(event -> updateQuantityDefaults());
        pHBar.addChangeListener(event -> updatePHReading());
        updateQuantityDefaults();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(UiKit.label("Virtual Chemistry Lab", 25, Theme.TEXT, true));
        header.add(Box.createVerticalStrut(2));
        header.add(UiKit.label("Choose an experiment, add its reagents, then run the visual model.",
                12, Theme.TEXT_MUTED, false));
        return header;
    }

    private JPanel buildWorkbench() {
        JPanel workbench = new JPanel(new BorderLayout(12, 0));
        workbench.setOpaque(false);
        RoundedPanel vesselCard = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
        vesselCard.setLayout(new BorderLayout(0, 8));
        vesselCard.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel vesselHeader = new JPanel(new BorderLayout());
        vesselHeader.setOpaque(false);
        vesselHeader.add(UiKit.label("3D Reaction Beaker", 15, Theme.MUSTARD, true), BorderLayout.WEST);
        vesselHeader.add(UiKit.label("Drag the beaker to rotate 360°", 11, Theme.TEXT_MUTED, false), BorderLayout.EAST);
        vesselCard.add(vesselHeader, BorderLayout.NORTH);
        reactionView.setPreferredSize(new Dimension(520, 390));
        vesselCard.add(reactionView, BorderLayout.CENTER);

        JPanel readouts = new JPanel(new BorderLayout(14, 4));
        readouts.setOpaque(false);
        JPanel phPanel = new JPanel(new BorderLayout(6, 0));
        phPanel.setOpaque(false);
        phPanel.add(UiKit.label("pH", 12, Theme.TEAL, true), BorderLayout.WEST);
        pHBar.setMajorTickSpacing(2);
        pHBar.setPaintTicks(true);
        pHBar.setOpaque(false);
        pHBar.setEnabled(false);
        phPanel.add(pHBar, BorderLayout.CENTER);
        phPanel.add(pHReading, BorderLayout.EAST);
        readouts.add(phPanel, BorderLayout.NORTH);
        liquidLevel.setStringPainted(true);
        liquidLevel.setString("Liquid level —");
        liquidLevel.setForeground(Theme.TEAL);
        liquidLevel.setBackground(Theme.BG_DEEP);
        readouts.add(liquidLevel, BorderLayout.CENTER);
        readouts.add(heatRequirement, BorderLayout.SOUTH);
        vesselCard.add(readouts, BorderLayout.SOUTH);
        workbench.add(vesselCard, BorderLayout.CENTER);
        workbench.add(buildChemicalSidebar(), BorderLayout.EAST);
        return workbench;
    }

    private JPanel buildChemicalSidebar() {
        RoundedPanel sidebar = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
        sidebar.setLayout(new BorderLayout(6, 6));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(270, 0));
        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        title.add(UiKit.label("Chemical library", 16, Theme.MUSTARD, true));
        title.add(UiKit.label("Chemical/common name · standard quantity", 10, Theme.TEXT_MUTED, false));
        sidebar.add(title, BorderLayout.NORTH);

        chemicalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chemicalList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel cell = new JPanel();
            cell.setOpaque(true);
            cell.setBackground(isSelected ? Theme.TEAL_DARK : Theme.SURFACE);
            cell.setBorder(new EmptyBorder(5, 6, 5, 6));
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.add(UiKit.label(value.formula() + " · " + value.chemicalName(), 11, Theme.TEXT, true));
            cell.add(UiKit.label(value.commonName() + " · " + value.standardDisplay(), 10, Theme.TEXT_MUTED, false));
            return cell;
        });
        JScrollPane chemicals = new JScrollPane(chemicalList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScroll(chemicals);
        sidebar.add(chemicals, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        JPanel dose = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        dose.setOpaque(false);
        dose.add(UiKit.label("Quantity:", 11, Theme.TEXT, false));
        quantity.setPreferredSize(new Dimension(82, 28));
        dose.add(quantity);
        dose.add(quantityUnit);
        controls.add(dose);
        RoundedButton addChemical = new RoundedButton("Add selected chemical", true);
        addChemical.setFont(Theme.bold(11));
        addChemical.addActionListener(event -> addSelectedChemical());
        controls.add(addChemical);
        controls.add(Box.createVerticalStrut(5));
        controls.add(UiKit.label("Selected reactants", 12, Theme.TEAL, true));
        selectedList.setVisibleRowCount(3);
        selectedList.setCellRenderer((list, value, index, isSelected, cellHasFocus) ->
                UiKit.label(value.chemical().formula() + " · " + value.amount() + " " + value.unit(),
                        10, isSelected ? Theme.MUSTARD : Theme.TEXT, false));
        JScrollPane selected = new JScrollPane(selectedList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        selected.setPreferredSize(new Dimension(240, 66));
        selected.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        styleScroll(selected);
        controls.add(selected);
        sidebar.add(controls, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel buildExperimentBar() {
        RoundedPanel bar = new RoundedPanel(18, Theme.SURFACE, Theme.SURFACE_ALT);
        bar.setLayout(new BorderLayout(8, 5));
        bar.setBorder(new EmptyBorder(9, 12, 9, 12));
        JPanel selector = new JPanel(new BorderLayout(8, 0));
        selector.setOpaque(false);
        selector.add(UiKit.label("Experiment / auto-match", 12, Theme.TEAL, true), BorderLayout.WEST);
        reactionPicker.setFont(Theme.font(12));
        reactionPicker.setBackground(Theme.FIELD_BG);
        reactionPicker.setForeground(Theme.TEXT);
        reactionPicker.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list,
                        value == null ? "Choose an experiment…" : value.toString(),
                        index, isSelected, cellHasFocus);
                setBackground(isSelected ? Theme.TEAL_DARK : Theme.FIELD_BG);
                setForeground(Theme.TEXT);
                return this;
            }
        });
        selector.add(reactionPicker, BorderLayout.CENTER);
        RoundedButton load = new RoundedButton("Load standard reactants", false);
        load.setFont(Theme.bold(10));
        load.addActionListener(event -> loadReactants());
        selector.add(load, BorderLayout.EAST);
        bar.add(selector, BorderLayout.NORTH);

        reactionDetails.setOpaque(false);
        reactionDetails.setLayout(new BoxLayout(reactionDetails, BoxLayout.Y_AXIS));
        equation.setFont(Theme.bold(16));
        reactionDetails.add(equation);
        reactantRow.setOpaque(false);
        reactantRow.setAlignmentX(LEFT_ALIGNMENT);
        reactionDetails.add(reactantRow);
        reactionDetails.add(explanation);
        reactionDetails.add(status);
        bar.add(reactionDetails, BorderLayout.CENTER);
        reactionDetails.setVisible(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);
        RoundedButton run = new RoundedButton("Run visual experiment", true);
        run.setFont(Theme.bold(11));
        run.addActionListener(event -> runExperiment());
        RoundedButton clear = new RoundedButton("Clear reactants", false);
        clear.setFont(Theme.bold(10));
        clear.addActionListener(event -> clearReactants());
        actions.add(run);
        actions.add(clear);
        bar.add(actions, BorderLayout.SOUTH);
        return bar;
    }

    private void showSelectedReaction() {
        Reaction reaction = selectedReaction();
        if (!preserveReactantsOnReactionChange) {
            invalidateResult();
        }
        if (reaction == null) {
            reactionDetails.setVisible(false);
            equation.setText("Choose an experiment to see its equation and reactant controls.");
            explanation.setText(" ");
            status.setText("Choose an experiment from the selector below to begin.");
            heatRequirement.setText("Condition: choose an experiment");
            pHBar.setEnabled(false);
            pHReading.setText("Result pH: —");
            liquidLevel.setValue(0);
            liquidLevel.setString("Liquid level —");
            refreshSelectedChemicals();
            revalidate();
            repaint();
            return;
        }
        reactionDetails.setVisible(true);
        equation.setText(reaction.equation());
        explanation.setText("<html><div style='width:960px'>" + UiKit.esc(reaction.explanation()) + "</div></html>");
        status.setText("Add the required reactants from the library, then run the visual model.");
        heatRequirement.setText(reaction.condition());
        heatRequirement.setForeground(reaction.condition().toLowerCase().contains("heat") ? Theme.WARNING : Theme.TEXT_MUTED);
        pHBar.setEnabled(false);
        pHReading.setText("Result pH: pending");
        refreshSelectedChemicals();
        liquidLevel.setValue(0);
        liquidLevel.setString("Liquid level —");
        reactionView.showReaction(List.of(), List.of(), 0, 7, 0, false);
        revalidate();
        repaint();
    }

    private Reaction selectedReaction() {
        return (Reaction) reactionPicker.getSelectedItem();
    }

    private void updateQuantityDefaults() {
        Chemical selected = chemicalList.getSelectedValue();
        if (selected == null) {
            return;
        }
        quantity.setValue(selected.standardQuantity());
        quantityUnit.setText(selected.unit());
    }

    private void addSelectedChemical() {
        Chemical chemical = chemicalList.getSelectedValue();
        if (chemical == null) {
            status.setText("Select a chemical from the library first.");
            return;
        }
        double amount = ((Number) quantity.getValue()).doubleValue();
        DosedChemical dose = new DosedChemical(chemical, amount, chemical.unit());
        selectedChemicals.put(chemical.formula(), dose);
        invalidateResult();
        refreshSelectedChemicals();
        status.setText(chemical.chemicalName() + " added at " + amount + " " + chemical.unit()
                + (chemical.standardConcentration() == null ? "" : " (" + chemical.standardConcentration() + " mol/L stock)"));
        Reaction match = matchingReaction();
        if (match != null) {
            selectDetectedReaction(match);
            status.setText("Matched experiment: " + match.name() + ". Run the visual experiment to view its modeled result.");
        }
    }

    private void clearReactants() {
        selectedChemicals.clear();
        invalidateResult();
        refreshSelectedChemicals();
    }

    private void invalidateResult() {
        reactionHasRun = false;
        currentLiquidLevel = 0;
        reactionView.showReaction(List.of(), List.of(), 0, 7, 0, false);
        liquidLevel.setValue(0);
        liquidLevel.setString("Liquid level —");
        pHBar.setEnabled(false);
        pHBar.setValue(7);
        pHReading.setText(selectedReaction() == null ? "Result pH: —" : "Result pH: pending");
    }

    private void refreshSelectedChemicals() {
        selectedModel.clear();
        selectedChemicals.values().forEach(selectedModel::addElement);
        reactantRow.removeAll();
        Reaction reaction = selectedReaction();
        if (reaction != null) {
            reactantRow.add(UiKit.label("Reactants:", 11, Theme.TEAL, true));
            for (String formula : reaction.reactants()) {
                DosedChemical selected = selectedChemicals.get(formula);
                String text = selected == null ? formula + " (choose from library)"
                        : formula + " · " + selected.amount() + " " + selected.unit();
                reactantRow.add(UiKit.label(text, 11,
                        selected == null ? Theme.TEXT_MUTED : Theme.TEXT, selected != null));
            }
        }
        reactantRow.revalidate();
        reactantRow.repaint();
        selectedList.revalidate();
        selectedList.repaint();
    }

    private void loadReactants() {
        Reaction reaction = selectedReaction();
        if (reaction == null) {
            status.setText("Choose an experiment before loading its reactants.");
            return;
        }
        clearReactants();
        for (String formula : reaction.reactants()) {
            CHEMICALS.stream().filter(chemical -> chemical.formula().equals(formula))
                    .findFirst().ifPresent(chemical -> selectedChemicals.put(formula,
                            new DosedChemical(chemical, chemical.standardQuantity(), chemical.unit())));
        }
        refreshSelectedChemicals();
        status.setText("Standard library quantities loaded. These are illustrative model inputs, not laboratory instructions.");
    }

    private void runExperiment() {
        Reaction reaction = selectedReaction();
        if (reaction == null) {
            reaction = matchingReaction();
            if (reaction == null) {
                status.setText(matchingSuggestion());
                return;
            }
            selectDetectedReaction(reaction);
        }
        List<String> requiredReactants = reaction.reactants();
        List<String> missing = requiredReactants.stream()
                .filter(formula -> !selectedChemicals.containsKey(formula))
                .toList();
        if (!missing.isEmpty()) {
            status.setText("Missing reactants: " + String.join(", ", missing)
                    + ". Select them in the library or load the standard quantities.");
            return;
        }
        List<String> unexpected = selectedChemicals.keySet().stream()
                .filter(formula -> !requiredReactants.contains(formula))
                .toList();
        if (!unexpected.isEmpty()) {
            status.setText("These selected chemicals are not reactants in " + reaction.name() + ": "
                    + String.join(", ", unexpected) + ". Clear reactants and select the intended chemicals.");
            return;
        }
        int level = selectedChemicals.values().stream()
                .mapToInt(dose -> dose.chemical().unit().equals("mL") ? (int) Math.round(dose.amount()) : 20)
                .sum();
        level = Math.max(20, Math.min(90, level));
        currentLiquidLevel = level;
        int pH = reaction.name().startsWith("Thermal") || reaction.name().startsWith("Acid–base") ? 7 : 4;
        boolean hasPHResult = !reaction.pHResult().startsWith("Not applicable")
                && !reaction.pHResult().startsWith("pH is not");
        pHBar.setEnabled(hasPHResult);
        pHBar.setValue(pH);
        reactionHasRun = true;
        if (hasPHResult) {
            updatePHReading(reaction);
        } else {
            pHReading.setText(reaction.pHResult());
        }
        liquidLevel.setValue(level);
        liquidLevel.setString("Illustrative fill level: " + level + "%");
        heatRequirement.setText(reaction.condition());
        reactionView.showReaction(reaction.reactants(), reaction.products(), 1, pH, level,
                reaction.condition().toLowerCase().contains("heat"));
        status.setText("Model result: " + reaction.pHResult() + ". Atom counts balance; total mass is conserved in a closed system.");
    }

    private Reaction matchingReaction() {
        List<String> formulas = selectedChemicals.keySet().stream().sorted().toList();
        if (formulas.isEmpty()) {
            return null;
        }
        List<Reaction> exactMatches = REACTIONS.stream()
                .filter(reaction -> reaction.reactants().stream().sorted().toList().equals(formulas))
                .toList();
        if (exactMatches.size() != 1) {
            return null;
        }
        Reaction match = exactMatches.get(0);
        boolean ambiguousSubset = REACTIONS.stream().anyMatch(reaction ->
                reaction.reactants().size() > formulas.size()
                        && reaction.reactants().containsAll(formulas));
        return ambiguousSubset ? null : match;
    }

    private String matchingSuggestion() {
        List<String> formulas = selectedChemicals.keySet().stream().sorted().toList();
        if (formulas.isEmpty()) {
            return "Add reactants from the chemical library or choose an experiment.";
        }
        List<String> candidates = REACTIONS.stream()
                .filter(reaction -> reaction.reactants().containsAll(formulas))
                .map(Reaction::name)
                .toList();
        if (!candidates.isEmpty()) {
            return "Selected reactants may match: " + String.join("; ", candidates)
                    + ". Add the remaining reactants or choose an experiment.";
        }
        return "No preset experiment matches the selected reactants. Choose a matching set or experiment.";
    }

    private void selectDetectedReaction(Reaction reaction) {
        preserveReactantsOnReactionChange = true;
        try {
            reactionPicker.setSelectedItem(reaction);
        } finally {
            preserveReactantsOnReactionChange = false;
        }
    }

    private void updatePHReading() {
        Reaction reaction = selectedReaction();
        if (reaction != null && pHBar.isEnabled()) {
            updatePHReading(reaction);
        }
    }

    private void updatePHReading(Reaction reaction) {
        int value = pHBar.getValue();
        pHReading.setText("Model pH: " + value + " (approx.)");
        pHBar.setForeground(indicatorColor(value));
        if (reactionHasRun) {
            reactionView.showReaction(reaction.reactants(), reaction.products(), 1, value,
                    currentLiquidLevel, reaction.condition().toLowerCase().contains("heat"));
        }
    }

    private Color indicatorColor(int pH) {
        float hue = (float) Math.max(0, Math.min(1, 0.98 - pH * 0.065));
        return Color.getHSBColor(hue, 0.76f, 0.94f);
    }

    private void styleScroll(JScrollPane scroll) {
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(true);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getVerticalScrollBar().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setForeground(Theme.TEAL);
    }

    private record Chemical(String formula, String chemicalName, String commonName, String phase,
                            double standardQuantity, String unit, Double standardConcentration) {
        private String standardDisplay() {
            return standardQuantity + " " + unit
                    + (standardConcentration == null ? "" : " · " + standardConcentration + " M");
        }
    }

    private record DosedChemical(Chemical chemical, double amount, String unit) {
        @Override public String toString() {
            return chemical.formula() + " · " + amount + " " + unit;
        }
    }

    private record Reaction(String name, String equation, List<String> reactants, List<String> products,
                            String explanation, String condition, String pHResult, int liquidLevel) {
        @Override public String toString() {
            return name;
        }
    }
}
