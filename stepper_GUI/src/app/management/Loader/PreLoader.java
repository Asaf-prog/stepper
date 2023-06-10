package app.management.Loader;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modules.flow.execution.executionManager.tasks.ExecutionTask;

public class PreLoader extends javafx.application.Preloader {
    @FXML
    private ProgressBar progressBar;
    private Stage stage;
    @FXML
    private AnchorPane anchorPane;

        @FXML
        void initialize() {
            assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'PreLoader.fxml'.";


        }
 // stage.getIcons().add(new Image("app/management/content/aviadsIcon.png"));

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        progressBar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            // Run the progress bar for 5 seconds and then close the preloader screen
            long showDuration = 2000; // Duration to show the preloader in milliseconds
            long startTime = System.currentTimeMillis();

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
                double progress = (System.currentTimeMillis() - startTime) / (double) showDuration;
                progressBar.setProgress(progress);
            }));
            timeline.setCycleCount(100);
            timeline.play();

            timeline.setOnFinished(event -> {
                Platform.runLater(() -> {
                    stage.hide(); // Close the preloader screen
                });
            });
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        stage.getIcons().add(new Image("app/management/content/aviadsIcon.png"));
        stage.setTitle("Welcome to Stepper");
        stage.initStyle(StageStyle.UNDECORATED); // Hide window decorations (title bar, close button, etc.)

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PreLoader.fxml"));
        loader.setController(this);
        Scene preloaderScene = new Scene(loader.load());
        stage.setScene(preloaderScene);

        stage.show();

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
