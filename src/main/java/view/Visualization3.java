package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Visualization3 extends Canvas implements IVisualization {

    private final GraphicsContext gc;
    private double i = 0;
    private double j = 10;
    private final double imageWidth = 10;
    private final double imageHeight = 15;
    private final List<CustomerImage> customers = new ArrayList<>();

    public Visualization3(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        emptyScreen();
    }

    @Override
    public void emptyScreen() {
        gc.setFill(Color.web("#73A580"));
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public void newCustomerImage() {
        //if the pane is full (the next row would go out of bounds), clear the first row and shift others up
        if (j >= this.getHeight() - imageHeight) {
            shiftCustomersUp();
            j -= imageHeight;  //adjust vertical position to stay at the last row
        }

        Image image = new Image("human.png");
        gc.drawImage(image, i, j, imageWidth, imageHeight);

        customers.add(new CustomerImage(i, j, image));

        i += imageWidth;
        if (i >= (this.getWidth() - imageWidth)) {
            i = 0;
            j += imageHeight;  //move to the next row when reaching the end of the row
        }
    }

    @Override
    public void newCustomer(){}

    @Override
    public void shiftCustomersUp() {
        emptyScreen();

        //remove all customer images that are on the first row
        customers.removeIf(customer -> customer.y == 10);

        //shift remaining customer images up by one row height
        for (CustomerImage customer : customers) {
            customer.y -= imageHeight;
            //redraw the shifted customer image
            gc.drawImage(customer.image, customer.x, customer.y, imageWidth, imageHeight);
        }
    }

    @Override
    public void resetCustomerCount() {
        i = 0;
        j = 10;
        customers.clear();
    }

    // methods not used in this visualization
    @Override
    public void newOrderImage() {}
    @Override
    public void deleteOrderImage() {}
    @Override
    public void shiftOrdersUp() {}

    //helper class to track the positions of customer images
    private static class CustomerImage {
        double x, y;
        Image image;

        CustomerImage(double x, double y, Image image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }
    }
}

