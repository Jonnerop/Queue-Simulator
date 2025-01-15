package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Visualization4 extends Canvas implements IVisualization {

    private final GraphicsContext gc;
    private double i = 0;
    private double j = 10;
    private final double imageWidth = 30;
    private final double imageHeight = 30;
    private final List<OrderImage> orders = new ArrayList<>();

    public Visualization4(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        emptyScreen();
    }

    @Override
    public void emptyScreen() {
        gc.setFill(Color.web("#73A580")); //background color
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public void newOrderImage() {
        //check if the pane has space for new images, if not, shift orders up
        if (j >= this.getHeight() - imageHeight) {
            shiftOrdersUp();
            j -= imageHeight;  //adjust vertical position to stay at the last row
        }

        Image image = new Image("pizza.png");
        gc.drawImage(image, i, j, imageWidth, imageHeight);
        orders.add(new OrderImage(i, j, image));

        //update position for the next image
        i += imageWidth;
        if (i >= (this.getWidth() - imageWidth)) {
            i = 0;
            j += imageHeight;  //move to the next row when reaching the end of the current row
        }
    }

    @Override
    public void deleteOrderImage() {
        //ensure that there is at least one image to remove
        if (!orders.isEmpty()) {
            orders.remove(orders.size() - 1);
            emptyScreen();

            //redraw the remaining images
            for (OrderImage order : orders) {
                gc.drawImage(order.image, order.x, order.y, imageWidth, imageHeight);
            }

            //adjust position for the next image addition
            i = 0;
            j = 10;
            for (OrderImage order : orders) {
                gc.drawImage(order.image, order.x, order.y, imageWidth, imageHeight);
                i += imageWidth;
                if (i >= (this.getWidth() - imageWidth)) {
                    i = 0;
                    j += imageHeight;  //move to the next row when reaching the end of the current row
                }
            }
        }
    }

    @Override
    public void shiftOrdersUp() {
        emptyScreen();
        //remove all images on the first row (row 0)
        orders.removeIf(order -> order.y == 10);

        //shift all remaining images up by one row
        for (OrderImage order : orders) {
            order.y -= imageHeight;
            gc.drawImage(order.image, order.x, order.y, imageWidth, imageHeight);
        }
    }

    @Override
    public void resetCustomerCount() {
        i = 0;
        j = 10;
        orders.clear();
    }

    // methods not used in this visualization
    @Override
    public void newCustomerImage() {}
    @Override
    public void newCustomer() {}
    @Override
    public void shiftCustomersUp() {}

    //helper class to track the positions of pizza images
    private static class OrderImage {
        double x, y;
        Image image;

        OrderImage(double x, double y, Image image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }
    }
}
