package com.chemquest.service;

import com.chemquest.model.Apparatus;
import com.chemquest.model.Experiment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ApparatusService {
    private static final List<Apparatus> APPARATUS = build();
    private final Map<String, List<Experiment>> experimentsByApparatus = new LinkedHashMap<>();

    public ApparatusService(ExperimentService experimentService) {
        for (Apparatus apparatus : APPARATUS) {
            List<Experiment> using = new ArrayList<>();
            for (Experiment experiment : experimentService.all()) {
                if (experiment.apparatus().contains(apparatus.name())) {
                    using.add(experiment);
                }
            }
            experimentsByApparatus.put(apparatus.name(), using);
        }
    }

    public List<Apparatus> all() {
        return List.copyOf(APPARATUS);
    }

    public Optional<Apparatus> byName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return APPARATUS.stream()
                .filter(a -> a.name().equalsIgnoreCase(name.trim()))
                .findFirst();
    }

    public List<Experiment> experimentsUsing(Apparatus apparatus) {
        if (apparatus == null) {
            return List.of();
        }
        return experimentsByApparatus.getOrDefault(apparatus.name(), List.of());
    }

    public List<Apparatus> search(String query) {
        String text = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (text.isEmpty()) {
            return all();
        }
        return APPARATUS.stream()
                .filter(a -> a.name().toLowerCase(Locale.ROOT).contains(text)
                        || a.category().toLowerCase(Locale.ROOT).contains(text)
                        || a.description().toLowerCase(Locale.ROOT).contains(text))
                .sorted(Comparator.comparing(Apparatus::name))
                .toList();
    }

    private static List<Apparatus> build() {
        return List.of(
                new Apparatus("Beaker", "Glassware", "A cylindrical container with a flat base used for mixing, heating and observing solutions.", "Holding liquids, rough measuring, heating with a water bath.", "Wear goggles; keep away from open flames when heated."),
                new Apparatus("Conical Flask", "Glassware", "A flask with a narrow neck to minimise splashing during swirling.", "Mixing and titration, reaction vessels for controlled swirling.", "Do not stopper while heating; use a tripod or clamp securely."),
                new Apparatus("Measuring Cylinder", "Glassware", "A tall graduated container for relatively accurate liquid measurement.", "Measuring liquid volumes in experiments and solutions.", "Read at eye level to avoid parallax errors."),
                new Apparatus("Pipette", "Glassware", "A slender tube used to transfer precise liquid volumes.", "Delivering fixed volumes in titrations and dilution assays.", "Use a pipette filler; never suck directly by mouth."),
                new Apparatus("Burette", "Glassware", "A long calibrated tube with a tap used for controlled delivery of liquid.", "Titration and precise drop-wise addition of a reagent.", "Check the tap for leaks before starting; clamp securely."),
                new Apparatus("Test Tube", "Glassware", "A small open tube used for simple reactions and heating small samples.", "Trial reactions, heating a few drops, observing gas formation.", "Point away from people and do not heat closed tubes."),
                new Apparatus("Watch Glass", "Glassware", "A shallow circular glass dish used to cover beakers or hold solids.", "Evaporating few drops, covering liquids, drying crystals.", "Handle carefully; glass can crack if heated suddenly."),
                new Apparatus("Evaporating Dish", "Glassware", "A shallow dish used to evaporate a liquid to leave a solute behind.", "Evaporating solutions to concentrate or crystallize solids.", "Avoid overheating or thermal shock."),
                new Apparatus("Tripod Stand", "Support", "A metal stand used to support apparatus above a flame or heating source.", "Holding wire gauze and glassware over a burner.", "Use heat-resistant gloves when adjusting hot equipment."),
                new Apparatus("Wire Gauze", "Support", "A woven mesh placed on a tripod to spread heat evenly.", "Supporting glassware while heating to avoid hotspots.", "Do not touch while hot."),
                new Apparatus("Clamp Stand", "Support", "A support stand fitted with clamps to hold vessels securely.", "Holding burettes, flasks, and tubes in place.", "Tighten clamps sufficiently to prevent slips."),
                new Apparatus("Bunsen Burner", "Heating", "A gas burner used to supply heat in practical chemistry work.", "Heating solutions, decomposing solids, ignition tests.", "Keep flammable substances away; use the flame with care."),
                new Apparatus("Spirit Lamp", "Heating", "A simple burner powered by alcohol.", "Small-scale heating in demonstrations and simple bench work.", "Keep away from paper, cloth, and solvent vapours."),
                new Apparatus("Thermometer", "Measurement", "A calibrated instrument to measure temperature.", "Monitoring reaction temperature and heating stages.", "Handle gently and never use as a stirring rod."),
                new Apparatus("pH Paper", "Measurement", "A paper strip that changes colour with acidity or alkalinity.", "Checking pH of solutions and neutralisation endpoints.", "Do not contaminate the paper with other solutions."),
                new Apparatus("Filter Funnel", "Separation", "A cone-shaped funnel used with filter paper to separate solids from liquids.", "Filtration and washing precipitates.", "Use a suitable support to prevent spills."),
                new Apparatus("Filter Paper", "Separation", "A porous paper used to trap solids during filtration.", "Separating precipitates from solution.", "Use fresh paper for each filtration."),
                new Apparatus("Delivery Tube", "Gas Handling", "A tube used to transfer gases from a reaction vessel to another apparatus.", "Collecting gases and transferring them safely.", "Ensure connections are secure and gas is vented appropriately."),
                new Apparatus("Gas Jar", "Gas Handling", "A glass jar used to collect and store gases over water or air.", "Gas collection and observing volume or properties.", "Do not use a closed jar for hot gases without venting."),
                new Apparatus("Syringe", "Measurement", "A calibrated plunger device used to measure or inject small volumes.", "Collecting solutions, transferring gases, or adding measured drops.", "Avoid sharp needles if used in biology; clean after use."),
                new Apparatus("Spatula", "Handling", "A narrow scoop for handling solids and powders.", "Transferring solids, weighing, making mixtures.", "Keep dry and clean to avoid contamination."),
                new Apparatus("Mortar and Pestle", "Handling", "A bowl and grinder used to crush and powder solids.", "Grinding crystals or dry solids before mixing.", "Use carefully to avoid inhaling dust."),
                new Apparatus("Droppers", "Handling", "Small glass or plastic tubes with bulbs used for adding droplets.", "Adding exact drops during indicator or reagent tests.", "Do not dip used dropper back into clean reagent bottles."),
                new Apparatus("Balance", "Measurement", "A precision instrument that measures mass accurately.", "Weighing reagents and products.", "Zero before use and keep the pan clean."),
                new Apparatus("Tongs", "Safety", "A gripping tool for holding hot objects safely.", "Handling hot glassware and heated materials.", "Use heat-resistant gloves alongside tongs when necessary."),
                new Apparatus("Safety Goggles", "Safety", "Protective eyewear worn to guard against splashes and particles.", "Protecting eyes throughout practical work.", "Always wear during lab work and store after use.")
        );
    }
}
