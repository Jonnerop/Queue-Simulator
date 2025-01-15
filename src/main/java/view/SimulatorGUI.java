package view;

import java.util.Objects;

import simu.framework.Trace;
import simu.framework.Trace.Level;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.*;

/*
 * The SimulatorGUI class represents the graphical user interface (GUI) for the Pizza Rex simulation.
 * It initializes the JavaFX application, loads the main scene, and sets up the stage for the simulation.
 */
public class SimulatorGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pizza_rex_visualization.fxml"));
            Parent root = fxmlLoader.load();
            Trace.setTraceLevel(Level.INFO);

            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.setResizable(false);

            primaryStage.setTitle("Pizza Rex Simulator");
            Scene scene = new Scene(root);

            Image pizzaImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cursor.png")));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pizza-shop.png")));
            ImageCursor pizzaCursor = new ImageCursor(pizzaImage);
            scene.setCursor(pizzaCursor);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}