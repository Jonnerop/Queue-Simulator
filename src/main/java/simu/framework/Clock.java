package simu.framework;

/**
 * The Clock class represents a singleton system clock to keep track of the simulation time.
 * It provides methods to get and set the current time and ensures that only one instance of the clock exists.
 */
public class Clock {
	private double time;
	private static Clock instance;

	/**
	 * Private constructor to prevent direct instantiation (Singleton pattern).
	 * Initializes the time to zero.
	 */
	private Clock(){
		time = 0;
	}

	/**
	 * Returns the singleton instance of the Clock. If the instance does not exist,
	 * it is created. This ensures only one instance of the Clock is available.
	 *
	 * @return The singleton instance of the Clock.
	 */
	public static Clock getInstance(){
		if (instance == null){
			instance = new Clock();
		}
		return instance;
	}
	
	public void setTime(double time){
		this.time = time;
	}

	public double getTime(){
		return time;
	}
}
