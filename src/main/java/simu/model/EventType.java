package simu.model;

import simu.framework.IEventType;

public enum EventType implements IEventType {
	ARR1, // customer arrives
	ARR2, // pickup order arrives
	DOOR, // Customer waits to be seated
	SEAT, // customer moves to the table
	ORDER, // customer being serviced (food and drinks)
	READ, // pickup order is forwarded to the kitchen
	PREP, // chef prepares pizza for the oven
	OVEN, // pizza bakes in the oven
	SERVE, // waiter serves the pizza to the customer
	PACK, // waiter packs the pizza for the pickup order
	EAT, // customer eats the pizza
	PAY, // customer pays and leaves
	READY // pickup order is ready (necessary for calculating service time)
}
