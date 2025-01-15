package controls;

import simu.model.Order;

public interface IControllerForModel {
    // interface for the model
    void initializeEngine();
    void startSimu();
    void resetSimu();
    void handleOrderCompletion(Order order);
    void showEndResults();
    void visualizeCustomer();
    void visualizeOrder();
    void updateFinanceUI();
    void updatePizzaCharger(int quarter);
    void deleteOrderImage();
    void updateUI();
}
