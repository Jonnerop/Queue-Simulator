package simu.framework;

/**
 * The Event class represents an event in the simulation, containing the event type,
 * the time at which the event occurs, and a unique identifier for the event.
 * It implements the Comparable interface to allow sorting events based on time.
 */
public class Event implements Comparable<Event> {
	private IEventType type;
	private double time;
	private final int id;
	
	public Event(IEventType type, double time, int id){
		this.type = type;
		this.time = time;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setType(IEventType type) {
		this.type = type;
	}
	public IEventType getType() {
		return type;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getTime() {
		return time;
	}

	@Override
	public int compareTo(Event arg) {
		if (this.time < arg.time) return -1;
		else if (this.time > arg.time) return 1;
		return 0;
	}
}
