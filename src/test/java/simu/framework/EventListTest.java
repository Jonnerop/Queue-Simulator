package simu.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventListTest {
    private EventList eventList;
    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        event1 = new Event(new IEventType() {}, 10.0, 1);
        event2 = new Event(new IEventType() {}, 5.0, 2);
        event3 = new Event(new IEventType() {}, 15.0, 3);
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @Test
    void add() {
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        assertEquals(5.0, eventList.getNextTime());
    }

    @Test
    void remove() {
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        Event removedEvent = eventList.remove();
        assertEquals(event2, removedEvent);
        assertEquals(10.0, eventList.getNextTime());
    }

    @Test
    void getNextTime() {
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        assertEquals(5.0, eventList.getNextTime());
    }
}