package simu.framework;

import javafx.scene.control.Label;

import java.util.HashMap;

public interface IEngine {

    void setSimuTime(double time);
    void setDelay(long time);
    void setTableAmount(int amount);
    void setWaiterAmount(int amount);
    void setChefAmount(int amount);
    void resetEngine();
    long getDelay();
    int getTableAmount();
    int getWaiterAmount();
    int getChefAmount();
    double convertTime(double time);
    double getAverageThroughput();
    double getUtilization();
    double getCustomerWaitTime();
    double getTakeAwayWaitTime();
    double getResponseTime();
    double getAvgQueueLength();
}
