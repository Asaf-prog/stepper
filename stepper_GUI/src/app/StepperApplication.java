package app;


import app.management.resizeHelper.ResizeHelper;
import app.management.style.StyleManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class StepperApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StyleManager onlyOne = new StyleManager();
        primaryStage.setTitle("Stepper Application");
        setLoading(primaryStage);

    }
    private void setLoading(Stage primaryStage) {
        Image icon = new Image("app/management/content/loader.png");
        ImageView imageView = new ImageView(icon);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        imageView.setFitHeight(250);
        StackPane preloadLayout = new StackPane( imageView);
        preloadLayout.setPrefWidth(600);
        preloadLayout.setPrefHeight(300);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(450);
        preloadLayout.getChildren().add(progressBar);
        StackPane.setAlignment(progressBar, Pos.BOTTOM_CENTER);

        Scene preloadScene = new Scene(preloadLayout, 485, 245);
        primaryStage.setScene(preloadScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        setBounds(primaryStage);
        primaryStage.show();
        simulatePreloadTime(primaryStage, progressBar);
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

    private void simulatePreloadTime(Stage primaryStage, ProgressBar progressBar) {
        //Properties config = loadConfigProperties();
        int preloadTime = 1800;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        // Load the main app UI
                        Parent root = FXMLLoader.load(getClass().getResource("management/app.fxml"));
                        Scene scene = new Scene(root, 1090, 734);

                        primaryStage.setScene(scene);
                        ResizeHelper.addResizeListener(primaryStage);
                        primaryStage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, preloadTime);
    }
    private Properties loadConfigProperties() {
        Properties config = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            //config.load(inputStream);
        } catch (IOException e) {
           Properties  p = new Properties();
           p.setProperty("preload.time", "2000");
           return p ;
        }
        return config;
    }
}
