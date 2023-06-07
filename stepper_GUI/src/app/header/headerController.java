package app.header;

import app.MVC_controller.MVC_controller;
import app.management.mainController;
import app.management.style.StyleManager;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

import java.io.File;

public class headerController {

    @FXML
    private Button loadXMLbutton;
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
    private Button flowExecution;
    @FXML
    private Button saveData;
    @FXML
    private Button loadData;
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
    @FXML
    private Button Statistics;
    private MVC_controller controller;
    @FXML
    private Button ExecutionsHistory;
    private mainController main;
    @FXML
    private Label loaded;
    @FXML
    private Button buypremiumBtn;
    String buttonStyle;
    @FXML
    private ImageView close;
    @FXML
    private HBox topBar;

    @FXML
    private ImageView barLogo;
    double x, y;

    @FXML
    private Button closeButton;
    @FXML
    private HBox menuHbox;

    public String lastPressed = "none";

    public void setLastPressed(String lastPressed) {
    	if (lastPressed.equals("none")) {
    		return;
    	} else if(lastPressed.equals("flowExecution")) {
    		setAsPressed(flowExecution);
    	} else if (lastPressed.equals("flowDefinition")) {
            setAsPressed(FlowsDefinition);
        }else if (lastPressed.equals("statistics")) {
            setAsPressed(Statistics);
        }else if (lastPressed.equals("executionsHistory")) {
            setAsPressed(ExecutionsHistory);
        }
    }

    private void setAsPressed(Button pressed) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), pressed);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }


    @FXML
    void closeApp(ActionEvent event) {
        Platform.exit();
    }



    boolean subscription = false;
    int nextFreeProgressor = 1;
    private RotateTransition rotateTransition;


    ////////////////////////////  functions  ////////////////////////////
    @FXML
    void FlowsDefinitionPresent(ActionEvent event) {
        removeLastPressed();
        setLastPressed("flowDefinition");
        main.showFlowDefinition();
    }

    void StatsScreenPresent() {
        removeLastPressed();
        setLastPressed("statistics");
        main.showStats();
    }

    public void setMVCController(MVC_controller controller) {
        this.controller = controller;
    }

    public void setMainController(mainController main) {
        this.main = main;
    }

    @FXML
    void ExecutionsHistoryFunc(ActionEvent event) {
        removeLastPressed();
        setLastPressed("executionsHistory");
        main.showHistoryExe();
    }

    private void removeLastPressed() {
        switch(lastPressed) {
            case "flowExecution":
                removePressed(flowExecution);
                break;
            case "flowDefinition":
                removePressed(FlowsDefinition);
                break;
            case "statistics":
                removePressed(Statistics);
                break;
            case "executionsHistory":
                removePressed(ExecutionsHistory);
                break;
        }
    }

    private void removePressed(Button flowExecution) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), flowExecution);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();

    }

    public void addProgress(ProgressBar progressBar, Label label,int free) {
        String style4Bar, style4Label;
        switch (free) {
            case 1:
                style4Bar = flow1ProgressBar.getStyle();
                style4Label = flow1ProgressLabel.getStyle();
                flow1ProgressBar.setProgress(progressBar.getProgress());
                flow1ProgressLabel = label;
                flow1ProgressBar.setStyle(style4Bar);
                flow1ProgressLabel.setStyle(style4Label);
                break;
            case 2:
                style4Bar = flow2ProgressBar.getStyle();
                style4Label = flow2ProgressLabel.getStyle();
                flow2ProgressBar = progressBar;
                flow2ProgressLabel = label;
                flow2ProgressBar.setStyle(style4Bar);
                flow2ProgressLabel.setStyle(style4Label);
                break;
            case 3:
                style4Bar = flow3ProgressBar.getStyle();
                style4Label = flow3ProgressLabel.getStyle();
                flow3ProgressBar = progressBar;
                flow3ProgressLabel = label;
                flow3ProgressBar.setStyle(style4Bar);
                flow3ProgressLabel.setStyle(style4Label);
                break;
            case 4:
                style4Bar = flow4ProgressBar.getStyle();
                style4Label = flow4ProgressLabel.getStyle();
                flow4ProgressBar = progressBar;
                flow4ProgressLabel = label;
                flow4ProgressBar.setStyle(style4Bar);
                flow4ProgressLabel.setStyle(style4Label);
                break;
        }
    }
    public int getNextFreeProgress() {
        if (nextFreeProgressor == 4) {
            nextFreeProgressor = 1;
            return 4;
        }
        return nextFreeProgressor++;
    }
    @FXML
    void initialize() {
        setTheme();
        setMenuButtonGroup();
        makeExecutionButtonInvisible();
        String buttonStyle=closeButton.getStyle();
        closeButton.setOnMouseEntered(event -> {
            closeButton.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 15px;");
        });
        closeButton.setOnMouseExited(event -> {
            closeButton.setStyle(buttonStyle);
        });

        setTopBar();

        asserts();
        Events();
        setCssScreenButtons();
        setVGrow();
    }

    private void setMenuButtonGroup() {
        ToggleGroup group = new ToggleGroup();


    }

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    private void setTopBar() {

        Tooltip tooltip = new Tooltip("Saar Gever \nAsaf Homo");
        tooltip.setStyle("-fx-font-size: 22px; -fx-font-family: 'Arial'; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 10, 0, 0, 1);");
        Tooltip.install(barLogo, tooltip);
        topBar.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        topBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }

    private void setVGrow() {
        //GridPane.setVgrow(loaded, Priority.ALWAYS);
        //GridPane.setVgrow(progressGrid, Priority.ALWAYS);
    }

    private void asserts() {
        assert flow3ProgressBar != null : "fx:id=\"flow3ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressBar != null : "fx:id=\"flow2ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert Statistics != null : "fx:id=\"Statistics\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressLabel != null : "fx:id=\"flow4ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressLabel != null : "fx:id=\"flow2ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert loaded != null : "fx:id=\"loaded\" was not injected: check your FXML file 'header.fxml'.";
        assert buypremiumBtn != null : "fx:id=\"buypremiumBtn\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressLabel != null : "fx:id=\"flow1ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert loadXMLbutton != null : "fx:id=\"path\" was not injected: check your FXML file 'header.fxml'.";
        assert themeToggle != null : "fx:id=\"themeToggle\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressBar != null : "fx:id=\"flow4ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow3ProgressLabel != null : "fx:id=\"flow3ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert FlowsDefinition != null : "fx:id=\"FlowsDefinition\" was not injected: check your FXML file 'header.fxml'.";
        assert ExecutionsHistory != null : "fx:id=\"ExecutionsHistory\" was not injected: check your FXML file 'header.fxml'.";
        assert progressGrid != null : "fx:id=\"progressGrid\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressBar != null : "fx:id=\"flow1ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert saveData != null : "fx:id=\"saveData\" was not injected: check your FXML file 'header.fxml'.";
        assert loadData != null : "fx:id=\"loadData\" was not injected: check your FXML file 'header.fxml'.";
    }

    private void setCssScreenButtons() {
        FlowsDefinition.getStyleClass().add("screenButton");
        ExecutionsHistory.getStyleClass().add("screenButton");
        Statistics.getStyleClass().add("screenButton");
        flowExecution.getStyleClass().add("screenButton");

        flowExecution.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), flowExecution);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")){
                buttonStyle = flowExecution.getStyle();
                flowExecution.setStyle("-fx-border-color: black;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(0,33,255);");
            } else {
                buttonStyle = flowExecution.getStyle();
                flowExecution.setStyle("-fx-border-color: white;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(139,0,201);");
            }
        });

        FlowsDefinition.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), FlowsDefinition);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            FlowsDefinition.setStyle(buttonStyle);
        });



        FlowsDefinition.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), FlowsDefinition);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")){
                buttonStyle = FlowsDefinition.getStyle();
                FlowsDefinition.setStyle("-fx-border-color: black;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(0,33,255);");
            } else {
                buttonStyle = FlowsDefinition.getStyle();
                FlowsDefinition.setStyle("-fx-border-color: white;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(139,0,201);");
            }
        });

        FlowsDefinition.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), FlowsDefinition);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            FlowsDefinition.setStyle(buttonStyle);
        });
        ExecutionsHistory.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), ExecutionsHistory);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")){
                buttonStyle = ExecutionsHistory.getStyle();
                ExecutionsHistory.setStyle("-fx-background-color: rgb(32,33,255);-fx-background-radius: 20;-fx-border-color: #020101; -fx-border-radius: 20;");
            } else{
                buttonStyle = ExecutionsHistory.getStyle();
                ExecutionsHistory.setStyle("-fx-background-color: rgb(139,0,201);-fx-background-radius: 20;-fx-border-color: white; -fx-border-radius: 20;");
            }

        });

        ExecutionsHistory.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), ExecutionsHistory);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            ExecutionsHistory.setStyle(buttonStyle);
        });
        Statistics.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), Statistics);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")){
                buttonStyle = Statistics.getStyle();
                Statistics.setStyle("-fx-background-color: rgb(62,31,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");

            }else {
                buttonStyle = Statistics.getStyle();
                Statistics.setStyle("-fx-background-color: rgb(139,0,201);-fx-background-radius: 20;-fx-border-color: white; -fx-border-radius: 20;");
            }
        });
        Statistics.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), Statistics);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
            Statistics.setStyle(buttonStyle);
        });

    }
    @FXML
    void changeTheme(ActionEvent event) {
        if (themeToggle.isSelected()) {
            Scene scene = themeToggle.getScene();
            if (scene != null) {
                StyleManager.setTheme("light");
                themeToggle.setText("Light Theme");
                scene.getStylesheets().clear();
                scene.getStylesheets().add("app/management/style/lightTheme.css");
            }
        } else {
            Scene scene = themeToggle.getScene();
            if (scene != null) {
                StyleManager.setTheme("dark");
                themeToggle.setText("Dark Theme");
                scene.getStylesheets().clear();
                scene.getStylesheets().add("app/management/style/darkTheme.css");
            }
        }
    }
    @FXML
    void SaveData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("StepperData");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                DataManager.saveDataGui(file.getPath());
            } catch (Exception e) {//todo need to include all the exceptions from ex1
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error saving data");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void loadData(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Data");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                DataManager.loadDataGui(file.getPath());
                DataManager.getData().setXmlPath(DataManager.getData().getXmlPath());
                ActivateMenuButtons();
                loadXMLbutton.setText("Loaded:");
                loaded.setText(DataManager.getData().getXmlPath());
                initializedData();
            } catch (Exception e) {//todo need to include all the exceptions from ex1
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error loading data");
                alert.showAndWait();
            }
        }

    }

    @FXML
    void BuyPremium(ActionEvent event) {
        buypremiumBtn.setOnMouseEntered(event1 -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), buypremiumBtn);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

        });
        buypremiumBtn.setOnMouseExited(event1 -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), buypremiumBtn);
            scaleTransition.setToX(0.9);
            scaleTransition.setToY(0.9);
            scaleTransition.play();

        });
        if (!subscription) {
            subscription = true;
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
        } else {
            Stage stage = (Stage) buypremiumBtn.getScene().getWindow();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("I LOVE YOU <3");
            alert.setHeaderText(null);
            alert.getDialogPane().setPrefSize(1000, 600);
            alert.getDialogPane().setStyle("-fx-background-color: blue;");

            alert.setContentText("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#BBGGGGPGGBB#&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#BGPYJ?77!~~~~~!!7J5B&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#GGPY7!~~~~~~~~~^^^^^~!?P#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BGGG5?!!~~~~~~~~~~~~^^~~~~~?P&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#GPGG5J!!!!~~~~~~~~~~~~~~~~~~~!Y#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#GGGGPJ7!!!!!!!!!!~~~~~^^^~~~~~~!5B@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#BGGGGGP?7?Y5PPGGGG5?!!~~!!!!~~~~~!YPB&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BGPGGBBBBY?J5PPPPPGGGPYJ?77JPGP5Y7!~!YPPB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#BGBGGBBBBGYY5YYY5PPGPPPGPYY5PPPGGGG5775GPPB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BBBBGGGBBBGJ?77!777777?Y55?~!5PPGGP5PGY?GGGPG#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@GPGBBGGBBBGJ7!~~~~~~~~~~~!!~^^!!7??JJ?JYPBBGGPG#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@GGBBBGBBBBG?7777!!!~~~~~!!~~^^~~~~~~~~!7JGBBGGPB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@BGBBBBBBBBGY????77!!!!7?7!!!~~~~~~~~~~~~!YBBBGGG&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@BBBBBBBBBBPY???777777?JYJYYJ??77!!~~~~!~75GGBBGPB&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#BBBBBBBBBGGP?77777?J5GBBBGPPGPY?7!~!!!75BBGGGPB&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BGBBBBBBBB#BY?7??5PGGGGGPPPPGGGPY7!!!?5BGBBGGB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@BGGGBPGBBBBBPJ7J5BBBG55YYJ?JJ5PGGY77YPBBGBBGPG#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BGGGGBBBBBBGPPBBPYJ??????7?JPBBP?5GBBBGBGPG#&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BBBGBBBBBBBBGY77?5P55Y?!!?PBGPGBBBBGPPPPPG#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&BGGBBBBBBBBBBBGP5PBBBBB577JPBGGBBBBGPG&#BGPB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#&&#GG&&&##BPBBBBBBBBBBBGGBGGGBBBGG#&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&#&&&##GJ5GBBBBBBBBBBBPJ?5GBBGB@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&&&&&&&&&&@@@@@@@@&###&&&&&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            alert.getDialogPane().setStyle("-fx-font-family: monospace");
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.setWidth(1080);
            dialogStage.setHeight(720);

            // Set the alert window siz
            //alert.initOwner(window instanceof Stage ? (Stage) window : null);
            alert.showAndWait();
        }

    }

    private void Events() {
        loadXMLbutton.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), loadXMLbutton);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = loadXMLbutton.getStyle();
                loadXMLbutton.setStyle("-fx-background-color: rgb(0,30,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");
            }
            else {
                buttonStyle = loadXMLbutton.getStyle();
                loadXMLbutton.setStyle("-fx-background-color: rgb(139,0,201);-fx-background-radius: 20;-fx-border-color: white; -fx-border-radius: 20;");
            }

        });
        loadXMLbutton.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), loadXMLbutton);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
            loadXMLbutton.setStyle(buttonStyle);

        });
        loadXMLbutton.setOnMousePressed(event -> {
            rotateTransition = new RotateTransition(Duration.seconds(1), loadXMLbutton);
            rotateTransition.setByAngle(360); // Rotate by 360 degrees
            rotateTransition.setCycleCount(800); // Perform the rotation once
            rotateTransition.setAutoReverse(false); // Disable auto-reverse
            rotateTransition.play(); // Start the rotation animation
        });
//        buypremiumBtn.setStyle("fx-border-color: #ffffff");
//        buypremiumBtn.setStyle("-fx-border-width: 2px");
//        buypremiumBtn.setStyle("-fx-text-fill: #ffff00");
//        buypremiumBtn.setStyle("-fx-background-color:  #24292e");

        loadData.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), loadData);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")){
                buttonStyle = loadData.getStyle();
                loadData.setStyle("-fx-background-color: rgb(0,30,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");
            } else{
                buttonStyle = loadData.getStyle();
                loadData.setStyle("-fx-background-color: rgb(139,0,201);-fx-background-radius: 20;-fx-border-color: white; -fx-border-radius: 20;");
            }

        });
        loadData.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), loadData);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
            loadData.setStyle(buttonStyle);

        });
        saveData.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), saveData);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = saveData.getStyle();
                saveData.setStyle("-fx-background-color: rgb(0,30,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");
            }else {
                buttonStyle = saveData.getStyle();
                saveData.setStyle("-fx-background-color: rgb(139,0,201);-fx-background-radius: 20;-fx-border-color: white; -fx-border-radius: 20;");
            }
        });
        saveData.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), saveData);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
            saveData.setStyle(buttonStyle);
        });
    }

    @FXML
    void flowExecutionPresents(ActionEvent event) {
        removeLastPressed();
        setLastPressed("flowExecution");
        main.showExecution();

    }
    @FXML
    void StatisticsFunc(ActionEvent event) {
        main.showStats();
    }

    @FXML
    void loadDataXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        stopRotate();
        loadXMLbutton.setRotate(0);
        main.initialize();
        if (selectedFile != null) {
            try {
                GetDataFromXML.fromXmlFileToObject(selectedFile.getAbsolutePath());
                ActivateMenuButtons();
                DataManager.getData().setXmlPath(selectedFile.getPath());
                FlowsDefinition.setDisable(false);
                ExecutionsHistory.setDisable(false);//***
                loadXMLbutton.setText("Loaded:");
                loaded.setText(DataManager.getData().getXmlPath());
                initializedData();
                stopRotate();
                loadXMLbutton.setRotate(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void stopRotate() {
        rotateTransition.stop();
    }

    private void ActivateMenuButtons() {
        FlowsDefinition.setDisable(false);
        Statistics.setDisable(false);
        if (DataManager.getData() != null) {
            if (DataManager.getData().getFlowExecutions().size() > 0)
                ExecutionsHistory.setDisable(false);
            else
                ExecutionsHistory.setDisable(true);
        }

    }


    public void makeExecutionButtonVisible() {
        menuHbox.getChildren().add(flowExecution);
        flowExecution.setVisible(true);
    }
    public void makeExecutionButtonInvisible() {
        menuHbox.getChildren().remove(flowExecution);
        flowExecution.setVisible(false);
    }
    public void setDisableOnExecutionsHistory() {
        ExecutionsHistory.setDisable(false);
    }

    private void initializedData() {
        Stepper stepperData = DataManager.getData();
        main.setFlows(stepperData.getFlows());
    }

    public ProgressBar getNextProgressBar(int free) {
        switch (free) {
            case 1:
                return flow1ProgressBar;
            case 2:
                return flow2ProgressBar;
            case 3:
                return flow3ProgressBar;
            case 4:
                return flow4ProgressBar;

        }
        return null;
    }

    public Label getNextLabel(int free) {
        switch (free) {
            case 1:
                return flow1ProgressLabel;
            case 2:
                return flow2ProgressLabel;
            case 3:
                return flow3ProgressLabel;
            case 4:
                return flow4ProgressLabel;

        }
        return null;
    }
}


