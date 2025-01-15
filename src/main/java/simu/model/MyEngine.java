package simu.model;

import controls.IControllerForModel;
import dao.ACustomerDao;
import dao.IDao;
import dao.OrderDao;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Uniform;
import simu.framework.*;
import eduni.distributions.Normal;
import eduni.distributions.RandomGenerator;
import simu.model.customerTypes.AbstractCustomer;
import simu.model.customerTypes.Customer;
import simu.model.customerTypes.TakeAway;

import java.util.HashMap;
import java.util.List;

/**
 * The MyEngine class is a specific implementation of the Engine class,
 * managing the simulation of a pizzeria. It sets up service points, handles
 * events like customer arrivals and service, and tracks financial and performance metrics.
 */
public class MyEngine extends Engine {

    private final ArrivalProcess arrivalProcess;
    private final OrderRandomizer orderRandomizer;
    private final IDao<AbstractCustomer> aCustomerDao;
    private final IDao<Order> orderDao;
    private final Finance finance;
    private final Clock clock = Clock.getInstance();
    private ServicePoint[][] servicePoints;
    private ServicePoint doorService;
    private ServicePoint eating;
    private AbstractCustomer[][] tableList;
    private double totalTimeInQueue;
    private double averageThroughput;
    private double utilization;
    private double customerWaitTime;
    private double takeAwayWaitTime;
    private double responseTime;
    private double avgQueueLength;



    /**
     * Constructs a MyEngine object with a controller, specifications, and a discount day flag.
     *
     * @param controller The controller interface to communicate with the simulation.
     * @param specs The default specifications for the simulation.
     * @param discountDay Flag indicating whether it is a discount day.
     */
    public MyEngine(IControllerForModel controller, HashMap<String, DefaultSpecs.Spec> specs, boolean discountDay) {
        super(controller);
        finance = new Finance(specs, discountDay);
        orderRandomizer = new OrderRandomizer(new Uniform(0, 1), specs);
        arrivalProcess = new ArrivalProcess(specs.get("meanAmount").getValue(), specs.get("varianceAmount").getValue(), eventList, discountDay);
        aCustomerDao = new ACustomerDao();
        orderDao = new OrderDao();
    }

    /**
     * Constructs a MyEngine object with a controller and a discount day flag,
     * using default specifications.
     *
     * @param controller The controller interface to communicate with the simulation.
     * @param discountDay Flag indicating whether it is a discount day.
     */
    public MyEngine(IControllerForModel controller, boolean discountDay) {
        this(controller, DefaultSpecs.setDefaultSpecs(), discountDay);
    }

    /**
     * Sets up the service points (tables, kitchen, takeaway points) for the simulation.
     * These points are where customers are processed through the restaurant.
     */
    @Override
    public void setupServicePoints() {
        // gets the amount of tables, waiters and chefs from the super class
        int tables = getTableAmount();
        int waiters = getWaiterAmount();
        int chefs = getChefAmount();
        tableList = new AbstractCustomer[tables][];

        servicePoints = new ServicePoint[tables + 2][];
        ServicePoint[] kitchenServicePoints = new ServicePoint[chefs + 1];
        ServicePoint[] takeAwayServicePoints = new ServicePoint[3];
        servicePoints[0] = kitchenServicePoints;
        servicePoints[1] = takeAwayServicePoints;
        for (int i = 0; i < tables; i++) {
            ServicePoint[] tableServicePoints = new ServicePoint[4];
            tableServicePoints[0] = new CustomerServicePoint(new Normal(1, 2), eventList, EventType.SEAT); //customer sits down
            tableServicePoints[1] = new CustomerServicePoint(new Normal(4, 0.5), eventList, EventType.ORDER); //waiter takes order
            tableServicePoints[2] = new PizzaServicePoint(new Normal(1, 2), eventList, EventType.SERVE); //pizza served to customer
            tableServicePoints[3] = new CustomerServicePoint(new Normal(2, 0.2), eventList, EventType.PAY); //customer pays and leaves
            servicePoints[i+2] = tableServicePoints;
        }

        takeAwayServicePoints[0] = new ServicePoint(new Normal(1, 2), eventList, EventType.READ); //takeaway order read
        takeAwayServicePoints[1] = new PizzaServicePoint(new Normal(2, 2), eventList, EventType.PACK); //pizza packed for takeaway
        takeAwayServicePoints[2] = new ServicePoint(new Normal(1, 1), eventList, EventType.READY); //takeaway order ready

        kitchenServicePoints[0] = new PizzaServicePoint(new Normal(3, 2), eventList, EventType.OVEN); //pizza in oven
        for (int i = 0; i < chefs; i++) {
            kitchenServicePoints[i+1] = new PizzaServicePoint(new Normal(5, 3), eventList, EventType.PREP); //pizza preparation
        }

        for (int i = 0; i < waiters; i++) {
            Waiter waiter = new Waiter();
            Waiter.addToWaiterList(waiter);
        }

        doorService = new CustomerServicePoint(new Normal(1, 0.5), eventList, EventType.DOOR); //checks for available tables
        eating = new ServicePoint(new Normal(20, 2), eventList, EventType.EAT); //customer eats
        // error handling needed for the amount of tables, waiters and chefs if they are set to 0
    }


	@Override
	protected void presets() {
		setupServicePoints();
        arrivalProcess.generateNext(); //first customer arrives
	}

    private void setToKitchenQueue(Order o) {
        int shortestQueue = 0;
        int currentQueue;
        int shortest = 0;
        currentQueue = servicePoints[0][1].getQueueSize();
        for (int i = 1; i < servicePoints[0].length; i++) {
            if (servicePoints[0][i].getQueueSize() <= shortestQueue) {
                servicePoints[0][i].addOrderToQueue(o);
                break;
            } else if (servicePoints[0][i].getQueueSize() < currentQueue) {
                currentQueue = servicePoints[0][i].getQueueSize();
                shortest = i;
            }
        }
        if (currentQueue > shortestQueue) {
            servicePoints[0][shortest].addOrderToQueue(o);
        }
    }

	@Override
	protected void executeEvent(Event e){  //B-type events
		AbstractCustomer c;
        AbstractCustomer[] customerGroup;
        Order o;
        ServicePoint s;
        int tableEventId;
        int chefEventId;
        ContinuousGenerator groupSizeGen;
        int groupSize;
		switch ((EventType)e.getType()){
            // Customer arrivals
            case ARR1:
                groupSizeGen = new Normal(2, 1);
                groupSize = (int) Math.round(groupSizeGen.sample());
                if (groupSize < 1) {
                    groupSize = 1;
                } else if (groupSize > 4) {
                    groupSize = 4;
                }
                customerGroup = new AbstractCustomer[groupSize];
                for (int i = 0; i < groupSize; i++) {
                    AbstractCustomer customer = new Customer();
                    customerGroup[i] = customer;
                    aCustomerDao.persist(customer);
                    controller.visualizeCustomer();
                }
                doorService.addGroupToQueue(customerGroup, clock.getTime());
                arrivalProcess.generateNext();
                break;
            case ARR2:
                AbstractCustomer takeAway = new TakeAway();
                servicePoints[1][0].addToQueue(takeAway, clock.getTime());
                aCustomerDao.persist(takeAway);
                arrivalProcess.generateNext();
                controller.visualizeCustomer();
                break;
            case DOOR:
                customerGroup = doorService.pullGroupFromQueue();
                doorService.updateBusyTime(clock.getTime());
                tableList[e.getId()] = customerGroup;
                for (AbstractCustomer customer : customerGroup) {
                    customer.setTable(e.getId());
                }
                servicePoints[e.getId() + 2][0].addGroupToQueue(customerGroup, clock.getTime());
                break;
            // Customer sits down
            case SEAT:
                tableEventId = e.getId();
                s = servicePoints[tableEventId][0];
                customerGroup = s.pullGroupFromQueue();
                s.updateBusyTime(clock.getTime());
                servicePoints[tableEventId][1].addGroupToQueue(customerGroup, clock.getTime());
                break;
            // Waiter takes order from customer
            case ORDER:
                tableEventId = e.getId();
                s = servicePoints[tableEventId][1];
                customerGroup = s.pullGroupFromQueue();
                s.updateBusyTime(clock.getTime());
                for (AbstractCustomer customer: customerGroup) {
                    servicePoints[tableEventId][2].addToQueue(customer, clock.getTime());
                    o = orderRandomizer.createOrder(customer);
                    controller.visualizeOrder();
                    customer.setOrder(o);
                    setToKitchenQueue(o);
                    orderDao.persist(o);
                    aCustomerDao.update(aCustomerDao.find(customer.getId()));
                }
                break;
            // Waiter reads takeaway order
            case READ:
                s = servicePoints[1][0];
                c = s.pullFromQueue();
                s.updateBusyTime(clock.getTime());
                o = orderRandomizer.createOrder(c);
                c.setOrder(o);
                servicePoints[1][1].addToQueue(c, clock.getTime());
                setToKitchenQueue(o);
                orderDao.persist(o);
                aCustomerDao.update(aCustomerDao.find(c.getId()));
                controller.visualizeOrder();
                break;
            // Pizza preparation servicepoints
            case PREP:
                chefEventId = e.getId();
                s = servicePoints[0][chefEventId];
                o = s.pullOrderFromQueue();
                s.updateBusyTime(clock.getTime());
                finance.addOrderExp(o);
                servicePoints[0][0].addOrderToQueue(o);
                break;
            case OVEN:
                s = servicePoints[0][0];
                o = s.pullOrderFromQueue();
                PizzaServicePoint.removePizzaFromOven();
                s.updateBusyTime(clock.getTime());
                if (o.getCustomer().getType().equals("Customer")) {
                    servicePoints[o.getCustomer().getTable() + 2][2].addOrderToQueue(o);
                } else {
                    servicePoints[1][1].addOrderToQueue(o);
                }
                break;
            // Waiter serves pizza to customer
            case SERVE:
                tableEventId = e.getId();
                s = servicePoints[tableEventId][2];
                s.pullOrderFromQueue();
                c = s.pullFromQueue();
                controller.deleteOrderImage();
                s.updateBusyTime(clock.getTime());
                eating.startEating(c);
                break;
            // Customer eats followed by payment
            case EAT:
                c = eating.finishEating();
                c.setHasEaten(true);
                customerGroup = tableList[c.getTable()];
                int readyToPay = 0;
                for (AbstractCustomer customer : customerGroup) {
                    if (customer.isHasEaten()) {
                        readyToPay++;
                    }
                }
                if (readyToPay == customerGroup.length) {
                    servicePoints[c.getTable() + 2][3].addGroupToQueue(customerGroup, clock.getTime());
                }
                break;
            case PAY:
                tableEventId = e.getId();
                s = servicePoints[tableEventId][3];
                customerGroup = s.pullGroupFromQueue();
                s.updateBusyTime(clock.getTime());
                for (AbstractCustomer customer : customerGroup) {
                    o = customer.getOrder();
                    finance.addOrderRev(o);
                    customer.setExitTime(Clock.getInstance().getTime());
                    customer.report();
                    Customer.updateCustomersServed();
                    AbstractCustomer.updateBothServed();
                }
                tableList[customerGroup[0].getTable()] = null;
                break;
            // Takeaway order packing and readying
            case PACK:
                s = servicePoints[1][1];
                s.pullOrderFromQueue();
                c = s.pullFromQueue();
                s.updateBusyTime(clock.getTime());
                servicePoints[1][2].addToQueue(c, clock.getTime());
                controller.deleteOrderImage();
                break;
            case READY:
                s = servicePoints[1][2];
                c = s.pullFromQueue();
                s.updateBusyTime(clock.getTime());
                o = c.getOrder();
                finance.addOrderRev(o);
                c.setExitTime(Clock.getInstance().getTime());
                c.report();
                TakeAway.updateTakeAwaysServed();
                AbstractCustomer.updateBothServed();
                break;
        }
        finance.addOtherExpense(getWaiterAmount(), getChefAmount(), clock.getTime());
        controller.updateFinanceUI();
        controller.updateUI();
    }

	protected void tryCEvents(){
        for (int i = 1; i < servicePoints[0].length; i++) { // Only goes through chefs
            if (!servicePoints[0][i].isOccupied() && servicePoints[0][i].isInQueue()) {
                servicePoints[0][i].startService(i, clock.getTime());
            }
        }
        for (boolean o : PizzaServicePoint.getOvenList()) { // Pizza oven
            if (!servicePoints[0][0].isOccupied() && servicePoints[0][0].isInQueue() && !o) {
                servicePoints[0][0].startService(0, clock.getTime());
                PizzaServicePoint.putPizzaInOven();
            }
        }
        for (int i = 0; i < servicePoints[1].length; i++) { // Takeaway service points
            if (!servicePoints[1][i].isOccupied() && servicePoints[1][i].isInQueue()) {
                servicePoints[1][i].startService(i, clock.getTime());
            }
        }
        for (int i = 2; i < servicePoints.length; i++) {
            for (int j = 0; j < servicePoints[i].length; j++) { // Customer group service points
                if (servicePoints[i][j].isInQueue() && !servicePoints[i][j].isOccupied()) {
                    servicePoints[i][j].startService(i, clock.getTime());
                }
            }
        }
        for (int i = 0; i < tableList.length; i++) { // Door service only
            if (tableList[i] == null) {
                if (!doorService.isOccupied() && doorService.isInQueue()) {
                    doorService.startService(i, clock.getTime());
                }
                break;
            }
        }
	}

    /**
     * Calculates the results of the simulation, including performance metrics such as
     * average throughput, queue lengths, wait times, and utilization.
     */
	public void results() {
        for (AbstractCustomer abstractCustomer: aCustomerDao.findAll()) {
           totalTimeInQueue += abstractCustomer.getTotalTimeInQueue();
            if (abstractCustomer.getType().equals("Customer")) {
                customerWaitTime += abstractCustomer.getTotalTimeInQueue();
            } else if (abstractCustomer.getType().equals("Takeaway")) {
                takeAwayWaitTime += abstractCustomer.getTotalTimeInQueue();
            }
        }
        averageThroughput = AbstractCustomer.getBothServed() / clock.getTime();
        utilization = ServicePoint.getTotalBusyTime() / clock.getTime();
        responseTime = totalTimeInQueue / AbstractCustomer.getBothServed();
        avgQueueLength = totalTimeInQueue / clock.getTime();
        controller.showEndResults();
        System.out.println("Customer wait time: " + customerWaitTime + " Take away wait time: " + takeAwayWaitTime);
        System.out.println("Throughput: " + averageThroughput + "\n utilization: " + utilization + "\n response time: " + responseTime + "\n avg queue length: " + avgQueueLength);
	}

    public void resetValues() {
        Customer.setCustomersServed(0);
        Customer.setCustomerAmount(0);
        Customer.setTotalThroughputTime(0);
        TakeAway.setTakeAwaysServed(0);
        TakeAway.setTakeAwayAmount(0);
        TakeAway.setTotalThroughputTime(0);
        ServicePoint.setTotalBusyTime(0);
        finance.setPreviousTime(0);
    }

    public double getAverageThroughput() {
        return averageThroughput;
    }

    public double getAvgQueueLength() {
        return avgQueueLength;
    }

    public double getCustomerWaitTime() {
        return customerWaitTime;
    }

    public double getTakeAwayWaitTime() {
        return takeAwayWaitTime;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public double getUtilization() {
        return utilization;
    }


}
