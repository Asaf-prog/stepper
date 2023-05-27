package app.header;

import app.MVC_controller.MVC_controller;
import app.management.mainController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import java.io.File;

public class headerController {

    @FXML
    private Button path;
    @FXML
    private Button FlowsDefinition;
    @FXML
    private Label flow1ProgressLabel;
    @FXML
    private Label flow2ProgressLabel;
    @FXML
    private Label flow3ProgressLabel;
    @FXML
    private Label flow4ProgressLabel;

    @FXML
    private ProgressBar flow1ProgressBar;
    @FXML
    private ProgressBar flow2ProgressBar;
    @FXML
    private ProgressBar flow3ProgressBar;
    @FXML
    private ProgressBar flow4ProgressBar;
    @FXML
    private GridPane progressGrid;

    @FXML
    private ToggleButton themeToggle;
    private RotateTransition rotateTransition;
    @FXML
    private Button Statistics;
    private MVC_controller controller;
    @FXML
    private Button FlowsExecution;
    @FXML
    private Button ExecutionsHistory;
    private mainController main;
    @FXML
    private Label loaded;

    @FXML
    private Button buypremiumBtn;
    
    @FXML
    void FlowsDefinitionPresent(ActionEvent event) {
        main.showFlowDefinition();
    }

    void StatsScreenPresent(){
        main.showStats();
    }
    public void setMVCController(MVC_controller controller){
        this.controller = controller;
    }
    public void setMainController(mainController main){
        this.main =main;
    }
    @FXML
    void FlowsExecutionFunc(ActionEvent event) {
        main.FlowsExecutionInMenu();
    }
    @FXML
    void ExecutionsHistoryFunc(ActionEvent event) {
        main.showHistoryExe();
    }

    @FXML
    void initialize() {
        assert flow3ProgressBar != null : "fx:id=\"flow3ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressBar != null : "fx:id=\"flow2ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert Statistics != null : "fx:id=\"Statistics\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressLabel != null : "fx:id=\"flow4ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressLabel != null : "fx:id=\"flow2ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert loaded != null : "fx:id=\"loaded\" was not injected: check your FXML file 'header.fxml'.";
        assert buypremiumBtn != null : "fx:id=\"buypremiumBtn\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressLabel != null : "fx:id=\"flow1ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert path != null : "fx:id=\"path\" was not injected: check your FXML file 'header.fxml'.";
        assert themeToggle != null : "fx:id=\"themeToggle\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressBar != null : "fx:id=\"flow4ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow3ProgressLabel != null : "fx:id=\"flow3ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert FlowsDefinition != null : "fx:id=\"FlowsDefinition\" was not injected: check your FXML file 'header.fxml'.";
        assert ExecutionsHistory != null : "fx:id=\"ExecutionsHistory\" was not injected: check your FXML file 'header.fxml'.";
        assert progressGrid != null : "fx:id=\"ProgressGrid\" was not injected: check your FXML file 'header.fxml'.";
        assert FlowsExecution != null : "fx:id=\"FlowsExecution\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressBar != null : "fx:id=\"flow1ProgressBar\" was not injected: check your FXML file 'header.fxml'.";

        Events();
        screensToggleGrouping();


    }

    @FXML
    void changeTheme(ActionEvent event) {

    }
    private void screensToggleGrouping() {
       //todo change screens to be in toggle group and maybe change components to be in toggle group
        //update progress grid

    }

    @FXML
    void BuyPremium(ActionEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.1), buypremiumBtn);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.play();
        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(0.3), buypremiumBtn);
        rotateTransition2.setByAngle(360); // Rotate by 360 degrees
        rotateTransition2.setCycleCount(2); // Perform the rotation once
        rotateTransition2.setAutoReverse(false); // Disable auto-reverse
        rotateTransition2.play(); // Start the rotation animation
        buypremiumBtn.setRotate(0);
        scaleTransition.setToX(0.9);
        scaleTransition.setToY(0.9);
        scaleTransition.stop();
        scaleTransition.play();
        buypremiumBtn.setLayoutX(buypremiumBtn.getLayoutX() - 150);
        buypremiumBtn.setText("You are now Subscribed to Premium!");
        buypremiumBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);

    }
    private void Events() {
        path.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), path);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();
        });
        path.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), path);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
        path.setOnMousePressed( event -> {
            rotateTransition = new RotateTransition(Duration.seconds(1), path);
            rotateTransition.setByAngle(360); // Rotate by 360 degrees
            rotateTransition.setCycleCount(800); // Perform the rotation once
            rotateTransition.setAutoReverse(false); // Disable auto-reverse
            rotateTransition.play(); // Start the rotation animation
        });
        buypremiumBtn.setStyle("fx-border-color: #ffffff");
        buypremiumBtn.setStyle("-fx-border-width: 2px");
        buypremiumBtn.setStyle("-fx-text-fill: #ffff00");
        buypremiumBtn.setStyle("-fx-background-color:  #36393e");

    }

    @FXML
    void StatisticsFunc(ActionEvent event) {
        main.showStats();

    }
    @FXML
    void loadDataXML(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        stopRotate();
        path.setRotate(0);
        main.initialize();
        if (selectedFile != null) {
            try {
                GetDataFromXML.fromXmlFileToObject(selectedFile.getAbsolutePath());
                path.setText("Loaded:");
                ActivateMenuButtons();

                FlowsExecution.setDisable(false);
                FlowsDefinition.setDisable(false);
                ExecutionsHistory.setDisable(false);//***
               // ExecutionsHistory.setDisable(false);//***
                loaded.setText(selectedFile.getPath());
                initializedData();
                stopRotate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void stopRotate() {
        rotateTransition.stop();
    }

    private void ActivateMenuButtons() {
        FlowsExecution.setDisable(false);
        FlowsDefinition.setDisable(false);
        Statistics.setDisable(false);
        //ExecutionsHistory.setDisable(false);
    }


    public void setDisableOnExecutionsHistory(){
        ExecutionsHistory.setDisable(false);
    }
    private void initializedData(){
        Stepper stepperData = DataManager.getData();
        main.setFlows(stepperData.getFlows());

    }
}


