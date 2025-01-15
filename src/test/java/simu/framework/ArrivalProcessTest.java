package simu.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.model.EventType;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalProcessTest {
    private ArrivalProcess arrivalProcess;
    private EventList eventList;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        arrivalProcess = new ArrivalProcess(10.0, 2.0, eventList, false);
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @Test
    void setMean() {
        arrivalProcess.setMean(20);
        assertEquals(20, arrivalProcess.getMean());
    }

    @Test
    void setVariance() {
        arrivalProcess.setVariance(5.0);
        assertEquals(5.0, arrivalProcess.getVariance());
    }

    @Test
    void testGetAdjustedMean() {
        assertEquals(10.0, arrivalProcess.getAdjustedMean());

        arrivalProcess = new ArrivalProcess(10.0, 2.0, new EventList(), true);
        assertEquals(5.0, arrivalProcess.getAdjustedMean());
    }

    @Test
    void testRushHour() {
        Clock.getInstance().setTime(70); // 11:10 AM rush hour
        assertEquals(0.33, arrivalProcess.rushHour());

        Clock.getInstance().setTime(500); // 6:20 PM rush hour
        assertEquals(0.33, arrivalProcess.rushHour());

        Clock.getInstance().setTime(300); // 3:00 PM regular hour
        assertEquals(1.0, arrivalProcess.rushHour());
    }

    @Test
    void isDiscountDay() {
        assertFalse(arrivalProcess.isDiscountDay());
        arrivalProcess = new ArrivalProcess(10.0, 2.0, eventList, true);
        assertTrue(arrivalProcess.isDiscountDay());
    }

    @Test
    void generateNext() {
        arrivalProcess.generateNext();
        assertFalse(eventList.isEmpty());
        Event event = eventList.getNextEvent();
        assertNotNull(event);
        assertTrue(event.getType() == EventType.ARR1 || event.getType() == EventType.ARR2);
    }

    @Test
    void getMean() {
        assertEquals(10.0, arrivalProcess.getMean());
    }

    @Test
    void getVariance() {
        assertEquals(2.0, arrivalProcess.getVariance());
    }
}