package simu.framework;

import controls.IControllerForModel;
import dao.ACustomerDao;
import dao.OrderDao;
import jakarta.persistence.EntityManager;
import simu.model.customerTypes.AbstractCustomer;

import java.util.HashMap;

/**
 * The Engine class is an abstract base class that represents the core simulation engine.
 * It extends the Thread class to allow the simulation to run concurrently.
 * Subclasses must provide implementations for handling events, simulation presets,
 * and results processing.
 */
public abstract class Engine extends Thread implements IEngine{
	private static final int MINUTES_IN_DAY = 720; // for converting the days (restaurant opening hours) to minutes
	// simulation parameters
	private double simuTime = 0; // simulation time in minutes
	private long delay = 0;
	protected int tableAmount = 0, waiterAmount = 0, chefAmount = 0;

	// pizza charger variables
	private double quarterSimuTime, halfSimuTime, threeQuarterSimuTime, fullPizzaSimuTime;
	private boolean quarterUpdated = false, halfUpdated = false, threeQuarterUpdated = false;

	private boolean running = false; // simulation state
	private final Clock clock;
	protected EventList eventList;
	protected IControllerForModel controller;

	/**
	 * Constructs the simulation engine with a given controller.
	 *
	 * @param controller The controller interface used to interact with the simulation model.
	 */
	public Engine(IControllerForModel controller) {
		this.controller = controller;
		clock = Clock.getInstance();
		eventList = new EventList();
	}

	// setters and getters for the simulation parameters

	@Override
	public void setSimuTime(double time) { simuTime = convertTime(time); }

	@Override
	public void setDelay(long delay) { this.delay = delay; }

	@Override
	public long getDelay() { return delay; }

	@Override
	public void setTableAmount(int amount) { this.tableAmount = amount; }

	@Override
	public int getTableAmount() { return tableAmount; }

	@Override
	public void setWaiterAmount(int amount) { this.waiterAmount = amount; }

	@Override
	public int getWaiterAmount() { return waiterAmount; }

	@Override
	public void setChefAmount(int amount) { this.chefAmount = amount; }

	@Override
	public int getChefAmount() { return chefAmount; }

	private double currentTime(){
		return eventList.getNextTime();
	}

	// Converts the time from days to minutes (restaurant opening hours)
	@Override
	public double convertTime(double time) { return time * MINUTES_IN_DAY; }

	public void run(){
		running = true;
		presets(); // initialize the first event

		setUpPizzaChargerPresets();

		while (simulating() && running) {
			delay();

			if (eventList != null) {
				Trace.out(Trace.Level.INFO, "\nA-phase: current time " + currentTime());
				clock.setTime(currentTime());

				Trace.out(Trace.Level.INFO, "\nB-phase:");
				executeBEvents();

				Trace.out(Trace.Level.INFO, "\nC-phase:");
				tryCEvents();

				updatePizzaCharger();
			}
		}
		results();
	}

	// sets up the pizza charger presets for the updates
	private void setUpPizzaChargerPresets() {
		quarterSimuTime = simuTime / 4;
		halfSimuTime = simuTime / 2;
		threeQuarterSimuTime = 3 * simuTime / 4;
		fullPizzaSimuTime = simuTime-10; // to update the pizza charger to full right before the simulation ends
	}

	// executes the events in the event list
	private void executeBEvents(){
		while (eventList.getNextTime() == clock.getTime()){
			executeEvent(eventList.remove());
		}
	}

	// returns true if the simulation will continue
	private boolean simulating(){
		Trace.out(Trace.Level.INFO, "Clock time: " + clock.getTime());
		return clock.getTime() < simuTime;
	}

	// delays the simulation
	private void delay() {
		Trace.out(Trace.Level.INFO, "Delay " + delay);
		try {
			sleep(delay);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted");
		}
	}

	// stops the simulation when reset
	@Override
	public void resetEngine() {
		running = false;
		clock.setTime(0);
		AbstractCustomer.setI(1);
		eventList = null;
		resetValues();
		ACustomerDao.truncateTables();
	};

	// updates the pizza charger based on the simulation time
	private void updatePizzaCharger() {
		double currentTime = clock.getTime();
		if (!quarterUpdated && currentTime >= quarterSimuTime) {
			controller.updatePizzaCharger(1);
			quarterUpdated = true;
		}

		if (!halfUpdated && currentTime >= halfSimuTime) {
			controller.updatePizzaCharger(2);
			halfUpdated = true;
		}

		if (!threeQuarterUpdated && currentTime >= threeQuarterSimuTime) {
			controller.updatePizzaCharger(3);
			threeQuarterUpdated = true;
		}

		if (threeQuarterUpdated && currentTime >= fullPizzaSimuTime) {
			controller.updatePizzaCharger(4);
		}
	}

	// abstract methods implemented in the subclasses
	protected abstract void executeEvent(Event t);
	protected abstract void tryCEvents();
	protected abstract void presets();
	protected abstract void results();
	protected abstract void resetValues();
	protected abstract void setupServicePoints();
}