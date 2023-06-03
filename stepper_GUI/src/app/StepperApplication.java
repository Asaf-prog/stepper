package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static jdk.jfr.internal.SecuritySupport.getResourceAsStream;

public class StepperApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stepper Application");
        Parent load = FXMLLoader.load(getClass().getResource("management/app.fxml"));
        Scene scene = new Scene(load,1100,750);
        primaryStage.getIcons().add(new Image(("app/management/content/aviadsIcon.png")));
        primaryStage.setScene(scene);
        setPrimaryStage(primaryStage);
        centerWindowOnScreen(primaryStage);
        primaryStage.show();
    }

    private static void setPrimaryStage(Stage primaryStage) {
        primaryStage.setResizable(true);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMaxHeight(725);

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
