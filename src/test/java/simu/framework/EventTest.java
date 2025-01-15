package simu.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private IEventType eventType;

    @BeforeEach
    void setUp() {
        eventType = new IEventType() {};
        event = new Event(eventType, 10.0, 1);
    }

    @Test
    void getId() {
        assertEquals(1, event.getId());
    }

    @Test
    void setType() {
        IEventType newType = new IEventType() {};
        event.setType(newType);
        assertEquals(newType, event.getType());
    }

    @Test
    void getType() {
        assertEquals(eventType, event.getType());
    }

    @Test
    void setTime() {
        event.setTime(20.0);
        assertEquals(20.0, event.getTime());
    }

    @Test
    void getTime() {
        assertEquals(10.0, event.getTime());
    }

    @Test
    void compareTo() {
        Event earlierEvent = new Event(eventType, 5.0, 2);
        Event laterEvent = new Event(eventType, 15.0, 3);

        assertTrue(event.compareTo(earlierEvent) > 0);
        assertTrue(event.compareTo(laterEvent) < 0);
        assertEquals(0, event.compareTo(new Event(eventType, 10.0, 4)));
    }
}