package app;


import app.management.resizeHelper.ResizeHelper;
import app.management.style.StyleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class StepperApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        //setLoadingScreen(primaryStage);

        StyleManager onlyOne = new StyleManager();
        primaryStage.setTitle("Stepper Application");
        Parent load = FXMLLoader.load(getClass().getResource("management/app.fxml"));
        Scene scene = new Scene(load,1090,734);
       // primaryStage.initStyle(StageStyle.UNIFIED);
        scene.setOnMouseEntered(e -> showWindow(primaryStage));
        scene.setOnMouseExited(e -> hideWindow(primaryStage));
        primaryStage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
        primaryStage.setScene(scene);
        setPrimaryStage(primaryStage);
        centerWindowOnScreen(primaryStage);
        setBounds(primaryStage);
        primaryStage.show();
    }

    private void setLoadingScreen(Stage primaryStage) throws InterruptedException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("management/Loader/PreLoader.fxml"));
        Scene preloadScene = new Scene(root, 350, 200);
        primaryStage.setScene(preloadScene);
        primaryStage.show();
        TimerTask task = new TimerTask() {
            public void run() {
                // Load the actual application scene
                // Replace "MainScene.fxml" with the actual name of your main application FXML file
                Platform.runLater(() -> {
                    try {
                        Parent mainRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                        Scene mainScene = new Scene(mainRoot);

                        // Set the main application scene after the preload screen delay
                        primaryStage.setScene(mainScene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1500);

        primaryStage.close();
    }


    private static void setBounds(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        ResizeHelper.addResizeListener(primaryStage);
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
