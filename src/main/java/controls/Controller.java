package controls;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import simu.framework.*;
import simu.model.*;
import simu.model.customerTypes.Customer;
import simu.model.customerTypes.TakeAway;
import view.*;

import java.util.HashMap;
import java.util.Objects;


/**
 * The Controller class handles the main interaction between the simulation view and model.
 * It manages user inputs, controls the simulation engine, and updates the UI elements such as
 * customer visualization, financial data, and simulation results.
 */
public class Controller implements IControllerForModel, IControllerForView {

	private IEngine engine;
	private Finance finance;
    private final Clock clock = Clock.getInstance();

	//visualization mid operation
	private IVisualization screen;
	private IVisualization screen2;
	private ImageView pizzaImageView;

	//fxml elements
	@FXML
	private AnchorPane base;
	@FXML
	private Button startButton, addSpeedButton, slowDownButton, resetButton, specButton;
	@FXML
	private TextField simuTime, delay, tableAmount, waiterAmount, chefAmount;
	@FXML
	private Label revenueAmount, expensesAmount, netProfitAmount, totalTimeAmount, arrivedCustomersAmount,
			arrivedTakeAwaysAmount, readyCustomersAmount, readyTakeAwaysAmount, customerAvgThroughput,
			takeAwayAvgThroughput, activeTimeAmount, serviceThroughput, utilization, customerWaitTime, takeAwayWaitTime,
			responseTime, queueLength, errorLabel;
	@FXML
	private Pane customerArrival, pizzaQueue, pizzaCharger;
	@FXML
	private GridPane resultPane;
	@FXML
	private ChoiceBox<String> topPizzaBox;

	private boolean discountDay = false;
	private boolean specsGiven = false;
	private HashMap<String, DefaultSpecs.Spec> specs;

	// --------- initialization ------------

	@FXML
	private void initialize() {
		screen = new Visualization3(500,100);
		screen2 = new Visualization4(500,100);
		customerArrival.getChildren().add((Canvas) screen);
		pizzaQueue.getChildren().add((Canvas) screen2);

		// pizza charger
		pizzaImageView = new ImageView();
		pizzaCharger.getChildren().add(pizzaImageView);
		initLayout();
		initChoiceBox();
	}

	// method to initialize the layout elements
	private void initLayout() {
		setVisibility(false, true); // hide the result pane and show the pizza charger
		setButtons(false, true, true, true, false); // set the button states
        screen.emptyScreen(); // clear the customer visualization
        screen2.emptyScreen(); // clear the order visualization
		errorLabel.setText(""); // clear the error label
		setDinoImage(); // set the dino image
	}

	// method to initialize the choice box
	private void initChoiceBox() {
		topPizzaBox.getItems().addAll("None", "Margherita", "Meatlover", "Vegan");
	}

	// --------- engine control methods ------------

	// starts the simulation
	@Override
	public void startSimu() {
		((Thread) engine).start();
	}

	// initializes the engine and sets the parameters for the simulation
	@Override
	public void initializeEngine() {
		try {
			if (specsGiven) {
				engine = new MyEngine(this, specs, discountDay);
				this.finance = new Finance(specs, discountDay);
			} else {
				engine = new MyEngine(this, discountDay);
				this.finance = new Finance();
			}
			engine.setDelay(getLongFromField(delay));
			engine.setSimuTime(getDoubleFromField(simuTime));
			engine.setTableAmount(getIntFromField(tableAmount));
			engine.setWaiterAmount(getIntFromField(waiterAmount));
			engine.setChefAmount(getIntFromField(chefAmount));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Input must be a number.");
		} catch (NegativeValueException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	// handle the start button press
	@FXML
	public void handleStartSimu() {
		try {
			errorLabel.setText("");
			initializeEngine();
			setVisibility(false, true);
			setButtons(true, false, false, false, true);
			startSimu();
		} catch (Exception e) {
			errorLabel.setText(e.getMessage());
		}
	}

	// handle the reset button press
	@FXML
	@Override
	public void resetSimu() {
		if (engine != null && engine instanceof Thread) {
			((Thread) engine).interrupt();
			engine.resetEngine();
		}
		finance.reset();
		resetLayout();
	}

	// --------- speed control methods ------------

	/**
	 * Slows down the simulation by increasing the delay between steps.
	 */
	@FXML
	public void slowDown() { // hidastetaan moottorisäiettä
		adjustDelay(1.1);
	}

	/**
	 * Speeds up the simulation by reducing the delay between steps.
	 */
	@FXML
	public void addSpeed() { // nopeutetaan moottorisäiettä
		adjustDelay(0.9);
	}

	// method to adjust the delay by a factor
	private void adjustDelay(double factor) {
		engine.setDelay((long) (engine.getDelay() * factor));
	}

	// --------- UI update methods ------------

	// updates the finance values in the UI
	@Override
	public void updateFinanceUI() {
		Platform.runLater(() -> {
			setDouble(revenueAmount, finance.getTotalRevenue());
			setDouble(expensesAmount, finance.getTotalExpenses());
			setDouble(netProfitAmount, finance.getNetProfit());
			});
	}

	@FXML
	public void toggleDiscountDay() {
		discountDay = !discountDay;
	}

	// calculate revenue and expenses for the order
	@Override
	public void handleOrderCompletion(Order order) {
		finance.addOrderRev(order);
		finance.addOrderExp(order);
		updateFinanceUI(); // update the UI with the finance data
	}

	// updates the finance display in the UI while the simulation is running
	@Override
	public void updateUI() {
		Platform.runLater(() -> {
            setInt(arrivedCustomersAmount, Customer.getCustomerAmount());
            setInt(readyCustomersAmount, Customer.getCustomersServed());
            setInt(arrivedTakeAwaysAmount, TakeAway.getTakeAwayAmount());
            setInt(readyTakeAwaysAmount, TakeAway.getTakeAwaysServed());
			setDouble(totalTimeAmount, clock.getTime());
            setDouble(customerAvgThroughput, Customer.getAverageThroughputTime());
            setDouble(takeAwayAvgThroughput, TakeAway.getAverageThroughputTime());
            setDouble(activeTimeAmount, ServicePoint.getTotalBusyTime());
		});
	}

	@Override
	public void visualizeCustomer() {
		Platform.runLater(() -> getVisu().newCustomerImage());}

    @Override
    public void visualizeOrder() {
        Platform.runLater(() -> getVisu2().newOrderImage());
    }

	@Override
    public void deleteOrderImage() {
        Platform.runLater(() -> getVisu2().deleteOrderImage());
    }

	@Override
	public void updatePizzaCharger(int quarter) {
		//load and display the appropriate pizza image based on how long the simu has been running
		String imagePath = "";
		switch (quarter) {
			case 0:
				imagePath = "";
				break;
			case 1:
				imagePath = "/1of4.png";
				break;
			case 2:
				imagePath = "/1of2.png";
				break;
			case 3:
				imagePath = "/3of4.png";
				break;
			case 4:
				imagePath = "/fullPizzaCharger.png";
				break;
		}

		//set the new image for the pizza charger
		int imageWidth = 100;
		int imageHeight = 100;

		Image pizzaImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), imageWidth, imageHeight, true, true);
		pizzaImageView.setImage(pizzaImage);
	}

	// --------- simulation end methods ------------

	// display the results of the simulation when it ends
	@Override
	public void showEndResults() {
		Platform.runLater(() -> {
			setVisibility(true, false);
			setButtons(true, true, true, false, true);
			setDouble(serviceThroughput, engine.getAverageThroughput());
			setDouble(utilization, engine.getUtilization());
			setDouble(customerWaitTime, engine.getCustomerWaitTime());
			setDouble(takeAwayWaitTime, engine.getTakeAwayWaitTime());
			setDouble(responseTime, engine.getResponseTime());
			setDouble(queueLength, engine.getAvgQueueLength());
		});
	}

	// --------- setters ------------

	private void setDouble(Label label, double value) {
		label.setText(String.format("%.2f", value));
	}

	private void setInt(Label label, int value) {
		label.setText(String.valueOf(value));
	}

	// --------- helpers ------------

	private void setButtons(boolean start, boolean addSpeed, boolean slowDown, boolean reset, boolean spec) {
		startButton.setDisable(start);
		addSpeedButton.setDisable(addSpeed);
		slowDownButton.setDisable(slowDown);
		resetButton.setDisable(reset);
		specButton.setDisable(spec);
	}

	private void setVisibility(boolean resultVisibility, boolean chargerVisibility) {
		resultPane.setVisible(resultVisibility);
		pizzaCharger.setVisible(chargerVisibility);
	}

	private long getLongFromField(TextField field) throws NegativeValueException {
		long value = Long.parseLong(field.getText());
		if (value < 0) {
			throw new NegativeValueException("Input cannot be negative.");
		}
		return value;
	}

	private double getDoubleFromField(TextField field) throws NegativeValueException {
		double value = Double.parseDouble(field.getText());
		if (value <= 0) {
			throw new NegativeValueException("Input cannot be negative or zero.");
		}
		return value;
	}

	private int getIntFromField(TextField field) throws NegativeValueException {
		int value = Integer.parseInt(field.getText());
		if (value <= 0) {
			throw new NegativeValueException("Input cannot be negative or zero.");
		}
		return value;
	}

	public IVisualization getVisu() {
		return screen;
	}

	public IVisualization getVisu2() {
		return screen2;
	}

	private void resetLayout() {
		getVisu().emptyScreen(); // clear the customer visualization
		getVisu2().emptyScreen(); // clear the order visualization
		getVisu().resetCustomerCount(); // reset the customer count
		getVisu2().resetCustomerCount(); // reset the order count
		setVisibility(false, true);
		updatePizzaCharger(0);
		resetResults();
		setButtons(false, true, true, true, false);
	}

	private void resetResults() {
		setInt(arrivedCustomersAmount, 0);
		setInt(readyCustomersAmount, 0);
		setInt(arrivedTakeAwaysAmount, 0);
		setInt(readyTakeAwaysAmount, 0);
		setDouble(totalTimeAmount, 0);
		setDouble(customerAvgThroughput, 0);
		setDouble(takeAwayAvgThroughput, 0);
		setDouble(activeTimeAmount, 0);
		setDouble(revenueAmount, 0);
		setDouble(expensesAmount, 0);
		setDouble(netProfitAmount, 0);
		setDouble(serviceThroughput, 0);
		setDouble(utilization, 0);
		setDouble(customerWaitTime, 0);
		setDouble(takeAwayWaitTime, 0);
		setDouble(responseTime, 0);
		setDouble(queueLength, 0);
	}

	public void setDinoImage() {
		double imageWidth = 265;
		double imageHeight = 236;
		Image dinoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/dino_eitaustaa.png")), imageWidth, imageHeight, true, true);
		ImageView dinoImageView = new ImageView(dinoImage);
		base.getChildren().add(dinoImageView);
		AnchorPane.setBottomAnchor(dinoImageView, 0.0);
		AnchorPane.setLeftAnchor(dinoImageView, 0.0);
	}

	// --------- order specifications ------------

	// opens the order specifications window when the specification button is pressed
	@FXML
	public void openSpecWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/order_specifications.fxml"));
			Parent root = loader.load();

			Stage newStage = new Stage();
			newStage.setResizable(false);
			newStage.setTitle("Order Specifications");
			newStage.setScene(new Scene(root));

			Image pizzaImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cursor.png")));
			ImageCursor pizzaCursor = new ImageCursor(pizzaImage);
			newStage.getScene().setCursor(pizzaCursor);

			newStage.showAndWait();

			SpecsController specsController = loader.getController();
			specs = specsController.handleSave();
			specsGiven = true;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			errorLabel.setText("Error opening order specifications");
		}
	}
}
