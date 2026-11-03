package com.chemquest.service;

import com.chemquest.model.ExperimentCategory;
import com.chemquest.model.QuizQuestion;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class QuizService {
    private static final Map<ExperimentCategory, List<QuizQuestion>> QUESTIONS = build();
    private int earnedXp;
    private int completedQuizzes;

    public List<QuizQuestion> questions(ExperimentCategory category) {
        return QUESTIONS.getOrDefault(category, List.of());
    }

    public int count(ExperimentCategory category) {
        return questions(category).size();
    }

    public int totalCount() {
        return QUESTIONS.values().stream().mapToInt(List::size).sum();
    }

    public void recordCompletedQuiz(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Quiz score cannot be negative.");
        }
        earnedXp += score;
        completedQuizzes++;
    }

    public int earnedXp() {
        return earnedXp;
    }

    public int completedQuizzes() {
        return completedQuizzes;
    }

    private static Map<ExperimentCategory, List<QuizQuestion>> build() {
        Map<ExperimentCategory, List<QuizQuestion>> questions = new EnumMap<>(ExperimentCategory.class);
        questions.put(ExperimentCategory.BASIC, List.of(
                q("What happens to blue litmus paper in an acidic solution?", "It turns red", "It turns green", "It stays blue", "It turns black", 0, "Acids turn blue litmus red."),
                q("What pH value is neutral at room temperature?", "0", "7", "10", "14", 1, "A neutral aqueous solution has a pH close to 7 at room temperature."),
                q("Which gas is formed when magnesium reacts with dilute hydrochloric acid?", "Oxygen", "Carbon dioxide", "Hydrogen", "Nitrogen", 2, "A reactive metal displaces hydrogen from dilute acid."),
                q("Which gas is produced when an acid reacts with a carbonate?", "Hydrogen", "Carbon dioxide", "Chlorine", "Ammonia", 1, "Acid + carbonate produces salt, water and carbon dioxide."),
                q("Which reaction type produces one product from two or more reactants?", "Combination", "Decomposition", "Displacement", "Neutralisation", 0, "A combination reaction joins reactants into a single product."),
                q("What is the main purpose of crystallisation?", "To separate an insoluble solid by size", "To obtain crystals of a dissolved solid", "To measure pH", "To identify a gas", 1, "Crystallisation recovers a dissolved solid as crystals from a concentrated solution."),
                q("In filtration, what is the solid retained on the filter paper called?", "Filtrate", "Solvent", "Residue", "Distillate", 2, "The retained solid is the residue; the liquid passing through is the filtrate."),
                q("In paper chromatography, where should the sample spot start?", "Below the solvent level", "Above the solvent level", "At the top edge", "Anywhere on the paper", 1, "The spot starts above the solvent so it is carried up the paper rather than dissolving into the solvent pool."),
                q("Which tool is used to measure pH approximately with a colour comparison?", "Universal indicator", "Burette", "Balance", "Thermometer", 0, "Universal indicator gives a colour corresponding to an approximate pH."),
                q("What is the product combination for acid plus metal carbonate?", "Salt and hydrogen", "Salt, water and carbon dioxide", "Base and oxygen", "Only water", 1, "An acid and carbonate form a salt, water and carbon dioxide.")
        ));
        questions.put(ExperimentCategory.INTERMEDIATE, List.of(
                q("What is the mole ratio of oxalic acid to sodium hydroxide in neutralisation?", "1:1", "1:2", "2:1", "2:3", 1, "One mole of diprotic oxalic acid neutralises two moles of sodium hydroxide."),
                q("What is the usual endpoint colour in an acidified permanganate titration of Fe2+?", "Persistent faint pink", "Bright green", "Milky white", "Colourless to deep blue", 0, "A slight excess of permanganate gives a persistent faint-pink endpoint."),
                q("What is the purpose of adding dilute acid when preparing Mohr's salt solution?", "To increase its colour", "To help limit oxidation of Fe2+", "To precipitate sulfate", "To make it alkaline", 1, "Acidic conditions help stabilise Fe2+ against oxidation by air."),
                q("Which observation can indicate a precipitate has formed in qualitative analysis?", "A solid appears in the solution", "The liquid evaporates instantly", "The solution becomes heavier", "The pH paper dries", 0, "An insoluble product can appear as a solid precipitate."),
                q("Why are controls useful in functional-group tests?", "They replace the unknown sample", "They help compare and interpret the test response", "They increase the sample mass", "They set the boiling point", 1, "Positive and negative controls help show whether an observed response is meaningful."),
                q("Which conditions generally help produce larger alum crystals?", "Rapid boiling to dryness", "Slow, undisturbed cooling", "Constant stirring while cooling", "Adding insoluble sand", 1, "Slow cooling gives particles more opportunity to form larger ordered crystals."),
                q("When should starch indicator usually be added in an iodine-thiosulfate titration?", "At the start to make the solution blue", "Near the pale-yellow endpoint", "After the endpoint", "Before adding iodine", 1, "Adding starch near the endpoint helps make the colour change sharper."),
                q("For a fixed endpoint, which simple quantity is often used to compare reaction rates?", "Time", "1/time", "Temperature multiplied by time", "Mass divided by colour", 1, "For the same measured endpoint, relative rate is often compared using 1/time."),
                q("What should be marked immediately after removing a developed chromatogram?", "The solvent front", "The solvent bottle", "The centre of the beaker", "The paper's mass", 0, "The solvent front can evaporate, so mark it promptly to calculate Rf."),
                q("What is the Rf value?", "Distance solvent moved divided by sample mass", "Distance solute moved divided by distance solvent front moved", "Sample mass divided by solvent volume", "Time divided by temperature", 1, "Rf compares the distance travelled by a substance with the solvent-front distance.")
        ));
        questions.put(ExperimentCategory.ADVANCED, List.of(
                q("In an EDTA hardness titration, what is the mole ratio of EDTA to a metal ion?", "1:1", "1:2", "2:1", "5:1", 0, "EDTA forms a 1:1 complex with each Ca2+ or Mg2+ ion."),
                q("What colour change indicates the usual Eriochrome Black T endpoint?", "Blue to red", "Wine-red to blue", "Yellow to colourless", "Green to purple", 1, "The indicator changes from wine-red to blue when EDTA complexes the hardness ions."),
                q("What happens to conductivity near the equivalence point in a strong acid–strong base titration?", "It often reaches a minimum", "It becomes permanently zero", "It always doubles", "It cannot change", 0, "Highly mobile H+ ions are neutralised; conductivity often reaches a minimum before rising with excess OH-."),
                q("How is the equivalence point commonly identified on a potentiometric titration curve?", "At the curve's inflection or sharpest change", "At the first data point", "At the highest burette reading", "Where the solution becomes colourless", 0, "The equivalence point corresponds to the steep region's inflection, often found via a derivative plot."),
                q("What does the Beer-Lambert law relate?", "Absorbance and concentration", "Pressure and volume only", "Mass and density only", "pH and temperature only", 0, "For a suitable solution and wavelength, absorbance is proportional to concentration: A = εlc."),
                q("Why is a reagent blank used in spectrophotometry?", "To zero the instrument for background absorbance", "To increase the sample concentration", "To clean the cuvette", "To determine sample density", 0, "A blank accounts for absorbance from the solvent and reagents."),
                q("What does percentage yield compare?", "Actual product amount with theoretical product amount", "Reactant pH with product pH", "Time with temperature", "Volume with density", 0, "Percentage yield = actual yield divided by theoretical yield, multiplied by 100%."),
                q("In an Ostwald viscometer comparison, which factor besides flow time appears in the relative viscosity relation?", "Liquid density", "Colour", "pH", "Electrical resistance", 0, "The comparison uses both density and flow time: η1/η2 = ρ1t1/(ρ2t2)."),
                q("Why must temperature be controlled during a viscosity measurement?", "Viscosity depends on temperature", "Temperature changes the glass colour", "It changes the liquid's name", "It eliminates the need for repeats", 0, "A liquid's viscosity can change significantly with temperature."),
                q("In Mohr's method, what indicates the chloride endpoint?", "Persistent brick-red silver chromate", "A blue starch complex", "A faint pink permanganate colour", "A green nickel precipitate", 0, "After chloride is consumed, excess Ag+ forms brick-red Ag2CrO4 with chromate.")
        ));
        return Map.copyOf(questions);
    }

    private static QuizQuestion q(String prompt, String a, String b, String c, String d,
                                  int correct, String explanation) {
        return new QuizQuestion(prompt, List.of(a, b, c, d), correct, explanation);
    }
}
