package simu.model;

import java.util.HashMap;

/**
 * The DefaultSpecs class provides default specification values for various products
 * and probabilities in the simulation, such as revenue, expenses, and probabilities
 * for different types of items. These specifications are stored in a HashMap.
 */
public class DefaultSpecs {

    private static final HashMap<String, Spec> specs = new HashMap<>();

    public static HashMap<String, Spec> setDefaultSpecs() {
        specs.put("margRev", new Spec("margRev", 9.0));
        specs.put("margExp", new Spec("margExp", 4.0));
        specs.put("meatRev", new Spec("meatRev", 12.0));
        specs.put("meatExp", new Spec("meatExp", 5.0));
        specs.put("vegRev", new Spec("vegRev", 10.0));
        specs.put("vegExp", new Spec("vegExp", 4.5));
        specs.put("waterRev", new Spec("waterRev", 0.0));
        specs.put("sodaRev", new Spec("sodaRev", 4.0));
        specs.put("beerRev", new Spec("beerRev", 6.0));
        specs.put("waterExp", new Spec("waterExp", 0.1));
        specs.put("sodaExp", new Spec("sodaExp", 1.5));
        specs.put("beerExp", new Spec("beerExp", 3.0));
        specs.put("margProb", new Spec("margProb", 0.6));
        specs.put("meatProb", new Spec("meatProb", 0.3));
        specs.put("vegProb", new Spec("vegProb", 0.1));
        specs.put("smallProb", new Spec("smallProb", 0.25));
        specs.put("medProb", new Spec("medProb", 0.55));
        specs.put("largeProb", new Spec("largeProb", 0.2));
        specs.put("waterProb", new Spec("waterProb", 0.3));
        specs.put("sodaProb", new Spec("sodaProb", 0.5));
        specs.put("beerProb", new Spec("beerProb", 0.2));
        specs.put("meanAmount", new Spec("meanAmount", 30.0));
        specs.put("varianceAmount", new Spec("varianceAmount", 0.5));
        return specs;
    }

    public static class Spec {
        String name;
        double value;

        public Spec(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }
    }
}
