package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static jdk.jfr.internal.SecuritySupport.getResourceAsStream;

public class StepperApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stepper Application");
        Parent load = FXMLLoader.load(getClass().getResource("management/app.fxml"));
        Scene scene = new Scene(load,1100,720);
        primaryStage.initStyle(StageStyle.UNIFIED);
        scene.setOnMouseEntered(e -> showWindow(primaryStage));
        scene.setOnMouseExited(e -> hideWindow(primaryStage));
        primaryStage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
        primaryStage.setScene(scene);
        setPrimaryStage(primaryStage);
        centerWindowOnScreen(primaryStage);
        primaryStage.show();
    }

    private void showWindow(Stage primaryStage) {
        primaryStage.setOpacity(1.0); // Make the window fully opaque
    }

    private void hideWindow(Stage primaryStage) {
        primaryStage.setOpacity(0.99); // Make the window fully transparent
    }


    private static void setPrimaryStage(Stage primaryStage) {
        primaryStage.setResizable(true);
        primaryStage.setMaxWidth(1440);
        primaryStage.setMaxHeight(960);

    }
    private void centerWindowOnScreen(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
        double windowWidth = stage.getWidth();
        double windowHeight = stage.getHeight();

        double windowX = (screenWidth - windowWidth) / 2;
        double windowY = (screenHeight - windowHeight) / 2;

        stage.setX(windowX);
        stage.setY(windowY);



    }
    public static void main(String[] args) {
        launch(args);
    }
}
