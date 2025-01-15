package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;
import simu.model.customerTypes.AbstractCustomer;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The CustomerServicePoint class represents a service point where customer groups are served in a queue.
 * It handles adding customer groups to a queue, starting service for them, and scheduling new events
 * when service is completed.
 */
public class CustomerServicePoint extends ServicePoint{

    private final LinkedList<AbstractCustomer[]> queue = new LinkedList<>();
    private final ContinuousGenerator generator;
    private final EventList eventList;
    private final EventType typeToBeScheduled;


    /**
     * Constructs a CustomerServicePoint with the specified service time generator, event list,
     * and event type to be scheduled after service completion.
     *
     * @param generator The generator to determine the service time.
     * @param eventList The event list to add new events.
     * @param type The type of event to schedule after service.
     */
    public CustomerServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type);
        this.generator = generator;
        this.eventList = eventList;
        this.typeToBeScheduled = type;
    }

    /**
     * Adds a group of customers to the queue and records their entry time.
     *
     * @param group The group of customers to add to the queue.
     * @param start The time when the group enters the queue.
     */
    @Override
    public void addGroupToQueue(AbstractCustomer[] group, double start){   // Jonon 1. asiakas aina palvelussa
        queue.add(group);
        for (AbstractCustomer c : group) {
            c.enterQueue(start);
        }
    }

    /**
     * Removes and returns the group of customers currently being served.
     *
     * @return The group of customers that has been served.
     */

    @Override
    public AbstractCustomer[] pullGroupFromQueue(){  // Poistetaan palvelussa ollut
        occupied = false;
        releaseWaiter();
        return queue.poll();
    }

    /**
     * Starts the service for the next group in the queue and schedules a new event
     * for when the service is completed.
     *
     * @param i    The identifier for the service.
     * @param exit The time when the group starts service.
     */
    @Override
    public void startService(int i, double exit) { //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        serviceStartTime = exit;
        assert queue.peek() != null;
        AbstractCustomer[] group = queue.peek();
        for (AbstractCustomer c : group) {
            c.exitQueue(exit);
            Trace.out(Trace.Level.INFO, "Starting service for customer " + c.getId());
        }
        assignWaiter();
        occupied = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(typeToBeScheduled,Clock.getInstance().getTime()+serviceTime, i));
    }

    @Override
    public boolean isInQueue() {
        return !queue.isEmpty();
    }
}
