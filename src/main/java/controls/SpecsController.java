package controls;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import simu.model.DefaultSpecs;
import simu.model.NegativeValueException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

/**
 * The SpecsController class manages the user input for setting and saving
 * simulation specifications. It handles loading default values, validating inputs,
 * and saving changes to a HashMap for use in the simulation.
 */
public class SpecsController {

    final double DIVISOR = 100.0; // divisor for probability fields

    // --------- static initializations ---------
    private static HashMap<String, DefaultSpecs.Spec> fieldValues = new HashMap<>();
    private static final HashSet<String> PROBABILITY_FIELDS = new HashSet<>();

    // adds the probability fields to the hashset
    static {
        // initialize the set of probability fields
        PROBABILITY_FIELDS.add("margProb");
        PROBABILITY_FIELDS.add("meatProb");
        PROBABILITY_FIELDS.add("vegProb");
        PROBABILITY_FIELDS.add("smallProb");
        PROBABILITY_FIELDS.add("medProb");
        PROBABILITY_FIELDS.add("largeProb");
        PROBABILITY_FIELDS.add("waterProb");
        PROBABILITY_FIELDS.add("sodaProb");
        PROBABILITY_FIELDS.add("beerProb");
    }

    // --------- FXML variables ---------

    @FXML
    private TextField margRev, meatRev, vegRev,
            margExp, meatExp, vegExp,
            margProb, meatProb, vegProb,
            smallProb, medProb, largeProb,
            waterRev, sodaRev, beerRev,
            waterExp, sodaExp, beerExp,
            waterProb, sodaProb, beerProb,
            meanAmount, varianceAmount;
    @FXML
    private Label errorLabel;

    // --------- initialization ---------

    // called when the fxml file is loaded, sets the field values based on the hashmap
    public void initialize() {
        setFieldValues();
    }

    // --------- event handler  ---------

    /**
     * Handles the save button press event. Validates and saves the field values
     * to the specifications HashMap and closes the window if successful.
     *
     * @return The updated HashMap of specifications or null if an error occurs.
     */
    @FXML
    public HashMap<String, DefaultSpecs.Spec> handleSave() {
        try {
            errorLabel.setText(""); // clear previous error message

            if (getFieldValues() && validateProbabilities() && validateVariance()) {
                System.out.println("Validation Passed! Closing window...");
                // close the window
                Stage stage = (Stage) errorLabel.getScene().getWindow();
                stage.close();

                return fieldValues; // return the hashmap (called specs in the model)
            }

        } catch (NegativeValueException | NumberFormatException e) {
            errorLabel.setText(e.getMessage());
        }
        return null;
    }

        // --------- text field value getters and value adding to hashmap ---------

    /**
     * Retrieves the values from the input fields, validates them, and stores them in the HashMap.
     *
     * @return True if all values are successfully retrieved and validated.
     * @throws NegativeValueException If a value is negative.
     */
        private boolean getFieldValues () throws NegativeValueException {

            String[] fieldNames = getFieldNames();

            try {
                // loop through the field names and adds their values to the hashmap
                for (String fieldName : fieldNames) {
                    TextField field = getTextFieldByName(fieldName);
                    if (field != null) {
                        String value = field.getText(); // get the value from the text field
                        double doubleValue = Double.parseDouble(value); // convert to double

                        validatePositiveValue(doubleValue);

                        addSpec(fieldName, doubleValue); // add the spec to the hashmap
                    }
                }
                return true;
            } catch (NumberFormatException e) { // handle non-numeric input
                throw new NumberFormatException("Input must be a number.");
            } catch (NegativeValueException e) { // handle negative values
               throw new NegativeValueException(e.getMessage());
            }
        }

    /**
     * Adds a specification to the HashMap, converting probability fields to fractions.
     *
     * @param key The key (field name) to add.
     * @param value The value to store.
     * @throws NegativeValueException If the value is negative.
     */
        private void addSpec (String key, double value) throws NegativeValueException {
            if (PROBABILITY_FIELDS.contains(key)) {
                fieldValues.put(key, new DefaultSpecs.Spec(key, value/DIVISOR));
            } else {
                fieldValues.put(key, new DefaultSpecs.Spec(key, value));
            }
        }

        // --------- validation methods ---------

        // checks that the probabilities sum up to 100%
        private boolean validateProbabilities (){
            final double TOLERANCE = 0.001; // allowed tolerance for rounding errors
            // sum of probabilities for each category
            double totalPizzaProb = sumProbabilities("margProb", "meatProb", "vegProb");
            double totalSizeProb = sumProbabilities("smallProb", "medProb", "largeProb");
            double totalDrinkProb = sumProbabilities("waterProb", "sodaProb", "beerProb");

            // check that the probabilities add up to approximately 100%
            if (Math.abs(totalPizzaProb - 1.0) > TOLERANCE ||
                    Math.abs(totalSizeProb - 1.0) > TOLERANCE ||
                    Math.abs(totalDrinkProb - 1.0) > TOLERANCE) {
                errorLabel.setText("Probabilities must add up to 100%.");
                return false;
            } else {
                return true;
            }
        }

        // sum the probabilities of the fields
        private double sumProbabilities (String...fields){
            double sum = 0;
            for (String field : fields) {
                sum += fieldValues.get(field).getValue();
            }
            return sum;
        }

        // validate that the variance is greater than 0
        private boolean validateVariance () throws NegativeValueException {
            if (fieldValues.get("varianceAmount").getValue() <= 0) {
                throw new NegativeValueException("Variance must be greater than 0.");
            }
            return true;
        }

        // validate that the value is positive
        private void validatePositiveValue (double value) throws NegativeValueException {
            if (value < 0) {
                throw new NegativeValueException("Input must be positive.");
            }
        }

        // --------- text field setup methods ---------

        // sets the text field values to the values in the hashmap
        private void setFieldValues () {
            // use default values when the program is run for the first time
            if (fieldValues == null || fieldValues.isEmpty()) {
                fieldValues = DefaultSpecs.setDefaultSpecs();
            }

            // loop through all the text field names and set the corresponding text field values
            for (String fieldName : getFieldNames()) {
                DefaultSpecs.Spec spec = fieldValues.get(fieldName);

                if (spec != null) {
                    double value = spec.getValue();
                    // multiply by 100 if it's a probability field (to convert back to percentage)
                    if (PROBABILITY_FIELDS.contains(fieldName)) {
                        value *= 100;
                    }
                    setField(getTextFieldByName(fieldName), value);
                }
            }
        }

        // sets the text field value
        private void setField (TextField field,double value){
            field.setText(String.format(Locale.US, "%.2f", value));
        }

        // resets the values to the default values
        @FXML
        public void resetValues() {
            fieldValues = DefaultSpecs.setDefaultSpecs();
            setFieldValues();
        }

        // --------- helper methods ---------

        // returns the field names for looping
        private String[] getFieldNames () {
            return new String[]{
                    "margRev", "margExp", "meatRev", "meatExp", "vegRev", "vegExp",
                    "waterRev", "sodaRev", "beerRev", "waterExp", "sodaExp", "beerExp",
                    "margProb", "meatProb", "vegProb", "smallProb", "medProb", "largeProb", "waterProb", "sodaProb", "beerProb",
                    "meanAmount", "varianceAmount"
            };
        }

        // returns the textField object based on the field name
        private TextField getTextFieldByName (String fieldName){
            return switch (fieldName) {
                case "margRev" -> margRev;
                case "margExp" -> margExp;
                case "meatRev" -> meatRev;
                case "meatExp" -> meatExp;
                case "vegRev" -> vegRev;
                case "vegExp" -> vegExp;
                case "waterRev" -> waterRev;
                case "sodaRev" -> sodaRev;
                case "beerRev" -> beerRev;
                case "waterExp" -> waterExp;
                case "sodaExp" -> sodaExp;
                case "beerExp" -> beerExp;
                case "margProb" -> margProb;
                case "meatProb" -> meatProb;
                case "vegProb" -> vegProb;
                case "smallProb" -> smallProb;
                case "medProb" -> medProb;
                case "largeProb" -> largeProb;
                case "waterProb" -> waterProb;
                case "sodaProb" -> sodaProb;
                case "beerProb" -> beerProb;
                case "meanAmount" -> meanAmount;
                case "varianceAmount" -> varianceAmount;
                default -> throw new IllegalStateException("Unexpected value: " + fieldName);
            };
        }
}
