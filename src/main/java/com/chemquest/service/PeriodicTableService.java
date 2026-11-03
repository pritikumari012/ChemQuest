package com.chemquest.service;

import com.chemquest.model.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PeriodicTableService {
    private static final List<Element> ELEMENTS = build();

    public int count() {
        return ELEMENTS.size();
    }

    public List<Element> all() {
        return List.copyOf(ELEMENTS);
    }

    public Element byAtomicNumber(int atomicNumber) {
        return ELEMENTS.stream()
                .filter(e -> e.atomicNumber() == atomicNumber)
                .findFirst()
                .orElse(null);
    }

    public Element bySymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return null;
        }
        return ELEMENTS.stream()
                .filter(e -> e.symbol().equalsIgnoreCase(symbol.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Element> search(String query) {
        String text = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (text.isEmpty()) {
            return all();
        }
        return ELEMENTS.stream()
                .filter(e -> e.name().toLowerCase(Locale.ROOT).contains(text)
                        || e.symbol().toLowerCase(Locale.ROOT).contains(text)
                        || Integer.toString(e.atomicNumber()).contains(text))
                .sorted(Comparator.comparingInt(Element::atomicNumber))
                .toList();
    }

    private static List<Element> build() {
        List<Element> list = new ArrayList<>();
        list.add(new Element(1, "H", "Hydrogen", 1, 1, "Non-metal", "The lightest element and the most abundant element in the universe."));
        list.add(new Element(2, "He", "Helium", 1, 18, "Noble gas", "A very stable, inert noble gas used in balloons and cryogenics."));
        list.add(new Element(3, "Li", "Lithium", 2, 1, "Alkali metal", "A reactive metal used in rechargeable batteries and lightweight alloys."));
        list.add(new Element(4, "Be", "Beryllium", 2, 2, "Alkaline earth metal", "A strong, light metal used in aerospace and X-ray windows."));
        list.add(new Element(5, "B", "Boron", 2, 13, "Metalloid", "A metalloid important in glassmaking and semiconductors."));
        list.add(new Element(6, "C", "Carbon", 2, 14, "Non-metal", "The backbone of organic chemistry and life on Earth."));
        list.add(new Element(7, "N", "Nitrogen", 2, 15, "Non-metal", "A major component of air and essential for proteins and DNA."));
        list.add(new Element(8, "O", "Oxygen", 2, 16, "Non-metal", "Essential for respiration and the oxidation of fuels."));
        list.add(new Element(9, "F", "Fluorine", 2, 17, "Halogen", "The most reactive halogen and a component in fluorides and fluoropolymers."));
        list.add(new Element(10, "Ne", "Neon", 2, 18, "Noble gas", "Glows brightly in lighting and signage."));
        list.add(new Element(11, "Na", "Sodium", 3, 1, "Alkali metal", "A highly reactive metal commonly found as the sodium ion in salts."));
        list.add(new Element(12, "Mg", "Magnesium", 3, 2, "Alkaline earth metal", "A light metal that burns with a brilliant white flame."));
        list.add(new Element(13, "Al", "Aluminium", 3, 13, "Post-transition metal", "A corrosion-resistant, lightweight metal used in manufacturing and transport."));
        list.add(new Element(14, "Si", "Silicon", 3, 14, "Metalloid", "The basis of modern semiconductors and computer chips."));
        list.add(new Element(15, "P", "Phosphorus", 3, 15, "Non-metal", "Essential to DNA, ATP, and many biomolecules."));
        list.add(new Element(16, "S", "Sulfur", 3, 16, "Non-metal", "Known for its yellow crystals and role in sulfuric acid production."));
        list.add(new Element(17, "Cl", "Chlorine", 3, 17, "Halogen", "A reactive halogen used in disinfectants and PVC production."));
        list.add(new Element(18, "Ar", "Argon", 3, 18, "Noble gas", "An inert gas used in light bulbs and welding atmospheres."));
        list.add(new Element(19, "K", "Potassium", 4, 1, "Alkali metal", "A very reactive metal essential for plant and animal cells."));
        list.add(new Element(20, "Ca", "Calcium", 4, 2, "Alkaline earth metal", "Important in bones, shells, and cement chemistry."));
        list.add(new Element(21, "Sc", "Scandium", 4, 3, "Transition metal", "A light transition metal used in high-intensity lamps."));
        list.add(new Element(22, "Ti", "Titanium", 4, 4, "Transition metal", "Strong, light, corrosion-resistant and used in aerospace."));
        list.add(new Element(23, "V", "Vanadium", 4, 5, "Transition metal", "Used to strengthen steel and in catalysts."));
        list.add(new Element(24, "Cr", "Chromium", 4, 6, "Transition metal", "Resistant to corrosion and valued for chrome plating."));
        list.add(new Element(25, "Mn", "Manganese", 4, 7, "Transition metal", "Used in steelmaking and battery chemistry."));
        list.add(new Element(26, "Fe", "Iron", 4, 8, "Transition metal", "The most used structural metal and a major component of steel."));
        list.add(new Element(27, "Co", "Cobalt", 4, 9, "Transition metal", "Used in strong magnets, batteries, and pigments."));
        list.add(new Element(28, "Ni", "Nickel", 4, 10, "Transition metal", "Resistant to corrosion and used in electroplating and alloys."));
        list.add(new Element(29, "Cu", "Copper", 4, 11, "Transition metal", "An excellent electrical conductor and corrosion-resistant metal."));
        list.add(new Element(30, "Zn", "Zinc", 4, 12, "Transition metal", "Used to galvanize steel and in batteries."));
        list.add(new Element(31, "Ga", "Gallium", 4, 13, "Post-transition metal", "A metal that melts near room temperature."));
        list.add(new Element(32, "Ge", "Germanium", 4, 14, "Metalloid", "Used in semiconductors and fiber optics."));
        list.add(new Element(33, "As", "Arsenic", 4, 15, "Metalloid", "A toxic element used in semiconductor materials and insecticides historically."));
        list.add(new Element(34, "Se", "Selenium", 4, 16, "Non-metal", "Required in trace amounts in biology and used in electronics."));
        list.add(new Element(35, "Br", "Bromine", 4, 17, "Halogen", "The only liquid halogen at room temperature."));
        list.add(new Element(36, "Kr", "Krypton", 4, 18, "Noble gas", "Used in lighting and as a tracer gas."));
        list.add(new Element(37, "Rb", "Rubidium", 5, 1, "Alkali metal", "A soft, reactive metal used in specialized research and atomic clocks."));
        list.add(new Element(38, "Sr", "Strontium", 5, 2, "Alkaline earth metal", "Used in fireworks and ceramic glazes."));
        list.add(new Element(39, "Y", "Yttrium", 5, 3, "Transition metal", "Used in phosphors for LEDs and displays."));
        list.add(new Element(40, "Zr", "Zirconium", 5, 4, "Transition metal", "Highly resistant to corrosion and used in nuclear reactors."));
        list.add(new Element(41, "Nb", "Niobium", 5, 5, "Transition metal", "Used in steel alloys and superconducting magnets."));
        list.add(new Element(42, "Mo", "Molybdenum", 5, 6, "Transition metal", "Important in high-strength alloys and catalysts."));
        list.add(new Element(43, "Tc", "Technetium", 5, 7, "Transition metal", "A radioactive metal with no stable isotopes."));
        list.add(new Element(44, "Ru", "Ruthenium", 5, 8, "Transition metal", "Used in electronics and corrosion-resistant alloys."));
        list.add(new Element(45, "Rh", "Rhodium", 5, 9, "Transition metal", "Highly reflective and used in catalytic converters."));
        list.add(new Element(46, "Pd", "Palladium", 5, 10, "Transition metal", "Used in catalytic converters and jewelry."));
        list.add(new Element(47, "Ag", "Silver", 5, 11, "Transition metal", "A precious metal with excellent electrical conductivity."));
        list.add(new Element(48, "Cd", "Cadmium", 5, 12, "Transition metal", "Used in batteries and coatings but is toxic."));
        list.add(new Element(49, "In", "Indium", 5, 13, "Post-transition metal", "Used in touch screens and LCDs."));
        list.add(new Element(50, "Sn", "Tin", 5, 14, "Post-transition metal", "A malleable metal used in solder and plating."));
        list.add(new Element(51, "Sb", "Antimony", 5, 15, "Metalloid", "Used in flame retardants and alloys."));
        list.add(new Element(52, "Te", "Tellurium", 5, 16, "Metalloid", "Used in semiconductor and solar materials."));
        list.add(new Element(53, "I", "Iodine", 5, 17, "Halogen", "Required for thyroid hormones and used in antiseptics."));
        list.add(new Element(54, "Xe", "Xenon", 5, 18, "Noble gas", "Used in flash lamps and MRI imaging."));
        list.add(new Element(55, "Cs", "Cesium", 6, 1, "Alkali metal", "A soft, highly reactive metal used in atomic clocks."));
        list.add(new Element(56, "Ba", "Barium", 6, 2, "Alkaline earth metal", "Used in medical imaging and fireworks."));
        list.add(new Element(57, "La", "Lanthanum", 6, 3, "Lanthanide", "Used in camera lenses and catalytic converters."));
        list.add(new Element(58, "Ce", "Cerium", 6, 3, "Lanthanide", "Used in polishing powders and catalytic converters."));
        list.add(new Element(59, "Pr", "Praseodymium", 6, 3, "Lanthanide", "Used in strong magnets and glass coloring."));
        list.add(new Element(60, "Nd", "Neodymium", 6, 3, "Lanthanide", "Key ingredient in powerful permanent magnets."));
        list.add(new Element(61, "Pm", "Promethium", 6, 3, "Lanthanide", "A radioactive lanthanide used in luminous paint."));
        list.add(new Element(62, "Sm", "Samarium", 6, 3, "Lanthanide", "Used in magnets and nuclear controls."));
        list.add(new Element(63, "Eu", "Europium", 6, 3, "Lanthanide", "Used in red phosphors for display screens."));
        list.add(new Element(64, "Gd", "Gadolinium", 6, 3, "Lanthanide", "Used in MRI contrast agents and magnets."));
        list.add(new Element(65, "Tb", "Terbium", 6, 3, "Lanthanide", "Used in phosphors and magnets."));
        list.add(new Element(66, "Dy", "Dysprosium", 6, 3, "Lanthanide", "Used in high-performance magnets."));
        list.add(new Element(67, "Ho", "Holmium", 6, 3, "Lanthanide", "Used in magnetic materials and lasers."));
        list.add(new Element(68, "Er", "Erbium", 6, 3, "Lanthanide", "Used in fiber-optic amplifiers and lasers."));
        list.add(new Element(69, "Tm", "Thulium", 6, 3, "Lanthanide", "Used in portable X-ray devices."));
        list.add(new Element(70, "Yb", "Ytterbium", 6, 3, "Lanthanide", "Used in lasers and atomic clocks."));
        list.add(new Element(71, "Lu", "Lutetium", 6, 3, "Lanthanide", "Used in catalysts and PET scanner detectors."));
        list.add(new Element(72, "Hf", "Hafnium", 6, 4, "Transition metal", "Used in high-temperature applications and nuclear control rods."));
        list.add(new Element(73, "Ta", "Tantalum", 6, 5, "Transition metal", "Used in capacitors and corrosion-resistant equipment."));
        list.add(new Element(74, "W", "Tungsten", 6, 6, "Transition metal", "Has the highest melting point of any metal and is used in filaments."));
        list.add(new Element(75, "Re", "Rhenium", 6, 7, "Transition metal", "Used in jet engines and high-temperature alloys."));
        list.add(new Element(76, "Os", "Osmium", 6, 8, "Transition metal", "One of the densest elements and very resistant to corrosion."));
        list.add(new Element(77, "Ir", "Iridium", 6, 9, "Transition metal", "Used in high-temperature crucibles and spark plugs."));
        list.add(new Element(78, "Pt", "Platinum", 6, 10, "Transition metal", "A precious metal used in catalysts and electronics."));
        list.add(new Element(79, "Au", "Gold", 6, 11, "Transition metal", "A valuable metal valued for conductivity and chemical stability."));
        list.add(new Element(80, "Hg", "Mercury", 6, 12, "Transition metal", "A liquid metal at room temperature with historical uses in thermometers."));
        list.add(new Element(81, "Tl", "Thallium", 6, 13, "Post-transition metal", "A toxic metal used in specialized electronics."));
        list.add(new Element(82, "Pb", "Lead", 6, 14, "Post-transition metal", "A dense metal historically used in pipes, batteries, and shielding."));
        list.add(new Element(83, "Bi", "Bismuth", 6, 15, "Post-transition metal", "Used in low-melting alloys and medicines."));
        list.add(new Element(84, "Po", "Polonium", 6, 16, "Metalloid", "A radioactive element with very limited uses."));
        list.add(new Element(85, "At", "Astatine", 6, 17, "Halogen", "A highly radioactive halogen with no stable isotopes."));
        list.add(new Element(86, "Rn", "Radon", 6, 18, "Noble gas", "A radioactive noble gas produced by uranium decay."));
        list.add(new Element(87, "Fr", "Francium", 7, 1, "Alkali metal", "An extremely rare radioactive alkali metal."));
        list.add(new Element(88, "Ra", "Radium", 7, 2, "Alkaline earth metal", "A radioactive element once used in luminous paints."));
        list.add(new Element(89, "Ac", "Actinium", 7, 3, "Actinide", "A radioactive actinide used in research and radiation sources."));
        list.add(new Element(90, "Th", "Thorium", 7, 3, "Actinide", "A radioactive metal with potential in nuclear fuel cycles."));
        list.add(new Element(91, "Pa", "Protactinium", 7, 3, "Actinide", "A rare radioactive actinide."));
        list.add(new Element(92, "U", "Uranium", 7, 3, "Actinide", "A heavy radioactive metal used as fuel for nuclear power."));
        list.add(new Element(93, "Np", "Neptunium", 7, 3, "Actinide", "A radioactive actinide produced in nuclear reactors."));
        list.add(new Element(94, "Pu", "Plutonium", 7, 3, "Actinide", "A dense radioactive metal used in nuclear fuel and weapons."));
        list.add(new Element(95, "Am", "Americium", 7, 3, "Actinide", "Used in smoke detectors and neutron sources."));
        list.add(new Element(96, "Cm", "Curium", 7, 3, "Actinide", "A radioactive actinide used in research and thermoelectric generators."));
        list.add(new Element(97, "Bk", "Berkelium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(98, "Cf", "Californium", 7, 3, "Actinide", "Used in neutron sources and scientific research."));
        list.add(new Element(99, "Es", "Einsteinium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(100, "Fm", "Fermium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(101, "Md", "Mendelevium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(102, "No", "Nobelium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(103, "Lr", "Lawrencium", 7, 3, "Actinide", "A synthetic radioactive actinide."));
        list.add(new Element(104, "Rf", "Rutherfordium", 7, 4, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(105, "Db", "Dubnium", 7, 5, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(106, "Sg", "Seaborgium", 7, 6, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(107, "Bh", "Bohrium", 7, 7, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(108, "Hs", "Hassium", 7, 8, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(109, "Mt", "Meitnerium", 7, 9, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(110, "Ds", "Darmstadtium", 7, 10, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(111, "Rg", "Roentgenium", 7, 11, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(112, "Cn", "Copernicium", 7, 12, "Transition metal", "A synthetic radioactive transition metal."));
        list.add(new Element(113, "Nh", "Nihonium", 7, 13, "Post-transition metal", "A synthetic radioactive post-transition metal."));
        list.add(new Element(114, "Fl", "Flerovium", 7, 14, "Post-transition metal", "A synthetic radioactive post-transition metal."));
        list.add(new Element(115, "Mc", "Moscovium", 7, 15, "Post-transition metal", "A synthetic radioactive post-transition metal."));
        list.add(new Element(116, "Lv", "Livermorium", 7, 16, "Post-transition metal", "A synthetic radioactive post-transition metal."));
        list.add(new Element(117, "Ts", "Tennessine", 7, 17, "Halogen", "A synthetic radioactive halogen."));
        list.add(new Element(118, "Og", "Oganesson", 7, 18, "Noble gas", "A synthetic noble gas with a very short half-life."));
        return List.copyOf(list);
    }
}
