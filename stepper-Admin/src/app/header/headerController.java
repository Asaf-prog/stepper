package app.header;

import app.MVC_controller.MVC_controller;
import app.management.mainController;
import app.management.style.StyleManager;
import com.google.gson.Gson;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class headerController {

    @FXML
    private Button loadXMLbutton;
    @FXML
    private Button userManagement;
    @FXML
    private Label flow1ProgressLabel;
    @FXML
    private Label flow2ProgressLabel;
    @FXML
    private Label flow3ProgressLabel;
    @FXML
    private Label flow4ProgressLabel;
    @FXML
    private Button roleManagement;
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
    @FXML
    private Button ExecutionsHistory;
    private mainController main;
    @FXML
    private Label loaded;
    @FXML
    private Button buypremiumBtn;
    @FXML
    private ImageView close;
    @FXML
    private HBox topBar;
    @FXML
    private ImageView barLogo;
    @FXML
    private Button closeButton;
    @FXML
    private HBox menuHbox;
    public String lastPressed = "none";
    private FlowDefinitionImpl currentFlow;
    private MVC_controller controller;
    double x, y;
    boolean subscription = false;
    int nextFreeProgressor = 1;
    String buttonStyle;
    private RotateTransition rotateTransition;
    @FXML
    private ToggleButton loaderScreen;
    public static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static Gson gson = new Gson();


    ////////////////////////////  functions  ////////////////////////////
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
        assert userManagement != null : "fx:id=\"userManagement\" was not injected: check your FXML file 'header.fxml'.";
        assert ExecutionsHistory != null : "fx:id=\"ExecutionsHistory\" was not injected: check your FXML file 'header.fxml'.";
        assert progressGrid != null : "fx:id=\"progressGrid\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressBar != null : "fx:id=\"flow1ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert saveData != null : "fx:id=\"saveData\" was not injected: check your FXML file 'header.fxml'.";
        assert loadData != null : "fx:id=\"loadData\" was not injected: check your FXML file 'header.fxml'.";
        assert roleManagement != null : "fx:id=\"roleManagement\" was not injected: check your FXML file 'header.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'header.fxml'.";
        assert topBar != null : "fx:id=\"topBar\" was not injected: check your FXML file 'header.fxml'.";
        assert barLogo != null : "fx:id=\"barLogo\" was not injected: check your FXML file 'header.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'header.fxml'.";
        assert menuHbox != null : "fx:id=\"menuHbox\" was not injected: check your FXML file 'header.fxml'.";
        assert loaderScreen != null : "fx:id=\"loaderScreen\" was not injected: check your FXML file 'header.fxml'.";
    }

    public void setLastPressed(String lastPressed) {
        if (lastPressed.equals("none")) {
            return;
        } else if (lastPressed.equals("flowExecution")) {
            setAsPressed(roleManagement);
        } else if (lastPressed.equals("flowDefinition")) {
            setAsPressed(userManagement);
        } else if (lastPressed.equals("statistics")) {
            setAsPressed(Statistics);
        } else if (lastPressed.equals("executionsHistory")) {
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
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        currentStage.close();
        Thread.currentThread().interrupt();
        Request request = new Request.Builder()
                .url(Constants.ADMIN_LOGOUT)
                .get()
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.exit();
                System.exit(0);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.exit();
                    System.exit(0);
                }
            }
        });


    }

    @FXML
    void UserManagementPresent(ActionEvent event) {
        removeLastPressed();
        setLastPressed("flowDefinition");
        main.showUserManagement();
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

    public void hideInformation() {
        loaded.setVisible(false);
        loadXMLbutton.setVisible(false);
    }

    public void setVisibleInformation() {
        loaded.setVisible(true);
        loadXMLbutton.setVisible(true);
    }

    private void removeLastPressed() {
        switch (lastPressed) {
            case "flowExecution":
                removePressed(roleManagement);
                break;
            case "flowDefinition":
                removePressed(userManagement);
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

    public void addProgress(ProgressBar progressBar, Label label, int free) {
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
        String buttonStyle = closeButton.getStyle();
        closeButton.setOnMouseEntered(event -> {
            closeButton.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 40px;-fx-border-radius: 26");
        });
        closeButton.setOnMouseExited(event -> {
            closeButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 40px;-fx-border-radius: 26");
        });

        setTopBar();

        asserts();
        Events();
        setCssScreenButtons();
        setVGrow();
        ifAlreadyLoaded();
        checkOnlyOneAdmin();
    }

    private void checkOnlyOneAdmin() {

        Request request = new Request.Builder().url(Constants.GET_IS_ONLY_ONE_ADMIN).build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String isOnlyOneAdmin = response.body().string();
                response.body().close();
                if (isOnlyOneAdmin.equals("false")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("You are the only admin in the system, you can't delete yourself");
                        alert.showAndWait();
                        Platform.exit();
                    });
                }//else do nothing all good with you my man :)

            }
        });

    }

    private void ifAlreadyLoaded() {
        Request request = new Request.Builder().url(Constants.GET_IS_ALREADY_LOADED).build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Error connecting to server");
                    alert.showAndWait();
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                boolean isAlreadyLoaded = Boolean.parseBoolean(responseBody.string());
                responseBody.close();
                if (isAlreadyLoaded) {
                    Platform.runLater(() -> {
                        setVisibleInformation();
                        setAllButtonsAnable();
                    });
                }

            }
        });
    }


    private void setAllButtonsAnable() {
        roleManagement.setDisable(false);
        userManagement.setDisable(false);
        Statistics.setDisable(false);
        ExecutionsHistory.setDisable(false);
    }

    private void setMenuButtonGroup() {
        ToggleGroup group = new ToggleGroup();
    }

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }

    private void setTopBar() {

        Tooltip tooltip = new Tooltip("Asaf=Efes\nSaar=Gever");
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

    private void setCssScreenButtons() {
        roleManagement.getStyleClass().add("screenButton");
        ExecutionsHistory.getStyleClass().add("screenButton");
        Statistics.getStyleClass().add("screenButton");
        userManagement.getStyleClass().add("screenButton");

        userManagement.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), userManagement);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = userManagement.getStyle();
                userManagement.setStyle("-fx-border-color: black;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(0,33,255);");
            } else {
                buttonStyle = userManagement.getStyle();
                userManagement.setStyle("-fx-border-color: white;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(139,0,201);");
            }
        });

        userManagement.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), userManagement);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            userManagement.setStyle(buttonStyle);
        });

        roleManagement.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), roleManagement);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = roleManagement.getStyle();
                roleManagement.setStyle("-fx-border-color: black;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(0,33,255);");
            } else {
                buttonStyle = roleManagement.getStyle();
                roleManagement.setStyle("-fx-border-color: white;-fx-background-radius: 20; -fx-border-radius: 20;-fx-background-color: rgb(139,0,201);");
            }
        });

        roleManagement.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), roleManagement);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            roleManagement.setStyle(buttonStyle);
        });
        ExecutionsHistory.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), ExecutionsHistory);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = ExecutionsHistory.getStyle();
                ExecutionsHistory.setStyle("-fx-background-color: rgb(32,33,255);-fx-background-radius: 20;-fx-border-color: #020101; -fx-border-radius: 20;");
            } else {
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
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = Statistics.getStyle();
                Statistics.setStyle("-fx-background-color: rgb(62,31,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");

            } else {
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
                themeToggle.setText("Dark Theme");
                scene.getStylesheets().clear();
                scene.getStylesheets().add("app/management/style/lightTheme.css");
                themeToggle.setStyle("-fx-background-color: transparent;-fx-text-fill: black;-fx-border-color: white;-fx-border-width: 1;-fx-border-radius: 20");
                themeToggle.getStyleClass().remove("toggle-switch-dark");
                themeToggle.getStyleClass().add("toggle-switch-light");

            }
        } else {
            Scene scene = themeToggle.getScene();
            if (scene != null) {
                StyleManager.setTheme("dark");
                themeToggle.setText("Light Theme");
                scene.getStylesheets().clear();
                scene.getStylesheets().add("app/management/style/darkTheme.css");
                themeToggle.setStyle("-fx-background-color: transparent;-fx-text-fill: yellow;");
                themeToggle.getStyleClass().remove("toggle-switch-light");
                themeToggle.getStyleClass().add("toggle-switch-dark");
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
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error2");
                alert.setContentText("Error saving data");
                alert.show();

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
                String res = "false";
                if (res.equals("true")) {
                    DataManager.loadDataGui(file.getPath());
                    DataManager.getData().setXmlPath(DataManager.getData().getXmlPath());
                    ActivateMenuButtons();
                    loadXMLbutton.setText("Loaded:");
                    loaded.setText(DataManager.getData().getXmlPath());
                    initializedData();
                    main.getBodyController().setBodyScreen();
                }//else ->->-> to exception
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error1");
                    alert.setContentText("Error loading data");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error4");
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
            } else {
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

        loadData.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), loadData);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            if (StyleManager.getCurrentTheme().equals("light")) {
                buttonStyle = loadData.getStyle();
                loadData.setStyle("-fx-background-color: rgb(0,30,255);-fx-background-radius: 20;-fx-border-color: #000000; -fx-border-radius: 20;");
            } else {
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
            } else {
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
    void RoleManagementPresent(ActionEvent event) {
        removeLastPressed();
        setLastPressed("flowExecution");
        main.showRoleManagement();
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
                RequestBody body =
                        new MultipartBody.Builder()
                                .addFormDataPart("file", selectedFile.getName(), RequestBody.create(selectedFile, MediaType
                                        .parse("text/xml")))
                                .build();

                Request request = new Request.Builder()
                        .url(Constants.XML_UPLOAD)
                        .post(body)
                        .build();

                System.out.println(request.toString());
                HttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() -> System.out.println("Something went wrong: " + e.getMessage()));
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {//because of redirect
                            String error = response.header("errorMsg");
                            Platform.runLater(() -> {

                                //present error message
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error loading XML file");
                                alert.setContentText(error);
                                alert.showAndWait();
                            });
                        } else {
                            Platform.runLater(() -> {
                                try {
                                    ActivateMenuButtons();
                                    String urlHeader = response.header("url");
                                    roleManagement.setDisable(false);
                                    ExecutionsHistory.setDisable(false);
                                    loadXMLbutton.setText("Loaded:");
                                    loaded.setText(urlHeader);
                                    initializedData();
                                    stopRotate();
                                    loadXMLbutton.setRotate(0);
                                    main.getBodyController().setBodyScreen();
                                    updateAdminApp(main);

                                } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                            });
                        }
                    }
                });
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("problem loading XML file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private static void updateAdminApp(mainController main) throws IOException {
        //update using main and body controllers
        main.getBodyController().initAdminApp();
    }


    private void stopRotate() {
        rotateTransition.stop();
    }

    private void ActivateMenuButtons() {
        roleManagement.setDisable(false);
        userManagement.setDisable(false);
        Statistics.setDisable(false);
        if (DataManager.getData() != null) {
            if (DataManager.getData().getFlowExecutions().size() > 0)
                ExecutionsHistory.setDisable(false);
            else
                ExecutionsHistory.setDisable(true);
        }

    }
    public void setDisableOnExecutionsHistory() {
        ExecutionsHistory.setDisable(false);
    }
    private void initializedData() {
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
