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
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class AdminStepperApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StyleManager onlyOne = new StyleManager();
        primaryStage.setTitle("Admin Application");
        setLoading(primaryStage);

    }

    private void setLoading(Stage primaryStage) {
        Image icon = new Image("app/management/content/loader.png");
        ImageView imageView = new ImageView(icon);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        imageView.setFitHeight(250);
        StackPane preloadLayout = new StackPane(imageView);
        preloadLayout.setPrefWidth(600);
        preloadLayout.setPrefHeight(300);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(450);
        preloadLayout.getChildren().add(progressBar);
        StackPane.setAlignment(progressBar, Pos.BOTTOM_CENTER);
        //check if another instance of the app is running
        boolean alreadyRunning = isAppActive();
        if (!alreadyRunning) {
            setAdminON();
            Scene preloadScene = new Scene(preloadLayout, 485, 245);
            primaryStage.setScene(preloadScene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            // Set stage bounds and show the stage
            //setPrimaryStage(primaryStage);
            //centerWindowOnScreen(primaryStage);
            setBounds(primaryStage);
            primaryStage.show();

            // Simulate loading time
            simulatePreloadTime(primaryStage, progressBar);
        } else {
            // Display the appropriate message in an alert dialog
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("---Admin app in Use!!---");
                alert.setContentText("Only one instance of the Admin App is allowed.");
                alert.setOnCloseRequest(event -> {
                    Platform.exit();
                    System.exit(0);
                });
                alert.showAndWait();

                Platform.exit();
            });
            return;
        }
    }

    private void setAdminON() {

        //create req to post method
    }

    private boolean isAppActive() {
        //check with the server if admin active
        Request request = new Request.Builder()
                .url(ClientConstants.VALIDATE_ADMIN)
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())

                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //get boolean from header
                boolean active = Boolean.parseBoolean(response.header("active"));
                if (active) {
                    //need to close app
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("---Admin app in Use!!---");
                        alert.setContentText("Only one instance of the Admin App is allowed.");
                        alert.setOnCloseRequest(event -> {
                            Platform.exit();
                            System.exit(0);
                        });
                        alert.showAndWait();

                        Platform.exit();
                    });


                } else {
                    //continue
                }
            }
        });
        return false;
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
                }, preloadTime);//problems with scrifts
            }

            private Properties loadConfigProperties() {
                Properties config = new Properties();
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                    //config.load(inputStream);
                } catch (IOException e) {
                    Properties p = new Properties();
                    p.setProperty("preload.time", "2000");
                    return p;
                }
                return config;
            }
        }