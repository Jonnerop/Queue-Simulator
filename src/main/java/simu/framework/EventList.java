package simu.framework;

import java.util.PriorityQueue;

/**
 * The EventList class manages a list of events in a simulation, using a priority queue to ensure events are processed in order of occurrence.
 */
public class EventList {
	private final PriorityQueue<Event> list = new PriorityQueue<Event>();
	
	public EventList(){
	 
	}

	/**
	 * Removes and returns the next event from the event list, which is the event with the earliest time.
	 *
	 * @return The next event in the queue.
	 */
	public Event remove(){
		Trace.out(Trace.Level.INFO,"Removing from event list " + list.peek().getType() + " " + list.peek().getTime() );
		return list.remove();
	}
	/**
	 * Adds a new event to the event list, automatically placing it in the correct order based on the event time.
	 *
	 * @param e The event to add to the list.
	 */
	public void add(Event e){
		Trace.out(Trace.Level.INFO,"Adding new event to list " + e.getType() + " " + e.getTime());
		list.add(e);
	}

	/**
	 * Returns the time of the next event in the list without removing it.
	 *
	 * @return The time of the next event.
	 */
	public double getNextTime(){
		return list.peek().getTime();
	}

	public Event getNextEvent() {
		return list.peek();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}
