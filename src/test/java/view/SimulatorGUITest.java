package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulatorGUITest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        new SimulatorGUI().start(stage);
    }

    @Test
    void testStageIsShown() {
        assertNotNull(primaryStage);
        assertTrue(primaryStage.isShowing());
    }

    @Test
    void testSceneIsLoaded() {
        Scene scene = primaryStage.getScene();
        assertNotNull(scene);
    }
}