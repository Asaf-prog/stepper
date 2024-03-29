package ClientsApp.app.header;

import ClientsApp.app.Client.Client;
import ClientsApp.app.MVC_controller.MVC_controller;
import ClientsApp.app.management.mainController;
import ClientsApp.app.management.style.StyleManager;
import com.google.gson.Gson;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class headerController {
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
    private Pane mainPane;
    @FXML
    private VBox headerVbox;
    @FXML
    private ToggleButton themeToggle;
    @FXML
    private Button Statistics;
    @FXML
    private TextField myRoles;
    @FXML
    private Button ExecutionsHistory;
    private mainController main;
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
    @FXML
    private HBox HBoxData;
    private Gson gson = new Gson();


    public String lastPressed = "none";
    private FlowDefinitionDTO currentFlow;
    private MVC_controller controller;
    double x, y;
    boolean subscription = false;
    int nextFreeProgressor = 1;
    String buttonStyle;
    private RotateTransition rotateTransition;
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    @FXML
    private ToggleButton loaderScreen;
    @FXML
    private Label IsManager;
    @FXML
    private Label NameInHeader;

    protected Client client;


    ////////////////////////////  functions  ////////////////////////////
    void asserts() {
        assert headerVbox != null : "fx:id=\"headerVbox\" was not injected: check your FXML file 'header.fxml'.";
        assert topBar != null : "fx:id=\"topBar\" was not injected: check your FXML file 'header.fxml'.";
        assert barLogo != null : "fx:id=\"barLogo\" was not injected: check your FXML file 'header.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'header.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'header.fxml'.";
        assert buypremiumBtn != null : "fx:id=\"buypremiumBtn\" was not injected: check your FXML file 'header.fxml'.";
        assert themeToggle != null : "fx:id=\"themeToggle\" was not injected: check your FXML file 'header.fxml'.";
        assert progressGrid != null : "fx:id=\"progressGrid\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressBar != null : "fx:id=\"flow1ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow3ProgressBar != null : "fx:id=\"flow3ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressBar != null : "fx:id=\"flow2ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressBar != null : "fx:id=\"flow4ProgressBar\" was not injected: check your FXML file 'header.fxml'.";
        assert flow1ProgressLabel != null : "fx:id=\"flow1ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert flow2ProgressLabel != null : "fx:id=\"flow2ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert flow3ProgressLabel != null : "fx:id=\"flow3ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert flow4ProgressLabel != null : "fx:id=\"flow4ProgressLabel\" was not injected: check your FXML file 'header.fxml'.";
        assert NameInHeader != null : "fx:id=\"NameInHeader\" was not injected: check your FXML file 'header.fxml'.";
        assert IsManager != null : "fx:id=\"IsManager\" was not injected: check your FXML file 'header.fxml'.";
        assert myRoles != null : "fx:id=\"myRoles\" was not injected: check your FXML file 'header.fxml'.";
        assert menuHbox != null : "fx:id=\"menuHbox\" was not injected: check your FXML file 'header.fxml'.";
        assert FlowsDefinition != null : "fx:id=\"FlowsDefinition\" was not injected: check your FXML file 'header.fxml'.";
        assert ExecutionsHistory != null : "fx:id=\"ExecutionsHistory\" was not injected: check your FXML file 'header.fxml'.";
        assert flowExecution != null : "fx:id=\"flowExecution\" was not injected: check your FXML file 'header.fxml'.";
        mainPane.setVisible(false);
        menuHbox.setVisible(false);
        setRoleBox();

        setTimerRefresher();

    }

    private void setTimerRefresher() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client != null) {
                    getUpdatedClient();
                }
            }
        }, 0, 1500);
    }

    private void getUpdatedClient() {
        Request request = new Request.Builder()
                .url(ClientConstants.GET_CLIENT_UPDATES)
                .addHeader("isManager", client.isManager())
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.isSuccessful()) {//client needs to update
                    String json = response.body().string();
                    StepperUser updatedUser =gson.fromJson(json, StepperUser.class);
                    response.close();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Client updated=updatedClient(updatedUser);
                            client = updated;
                            updateRoles(updatedUser.getRoles());
                            updateClient(client.isManagerBoolean());

                        }
                    });
                }

            }
        });
    }

    private Client updatedClient(StepperUser updatedUser) {

        Client updatedClient = new Client(updatedUser);
        return updatedClient;
    }

    private void setRoleBox() {
        myRoles.setStyle("-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 16px; -fx-font-weight: bold;-fx-wrap-text: true;");
    }

    public void setVisibleInformation(){
        mainPane.setVisible(true);
        menuHbox.setVisible(true);
    }
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
        Stage currentStage = (Stage) closeButton.getScene().getWindow();

        currentStage.close();
        Thread.currentThread().interrupt();
        Request logMeOut = new Request.Builder().url(ClientConstants.LOGOUT).build();
        ClientHttpClientUtil.runAsync(logMeOut, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to log out");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //bye bye
                Platform.exit();
            }
        });

    }
    @FXML
    void setMyRoles(ActionEvent event) {

    }
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
    public void updateRoles(List<String> roles){
        myRoles.clear();

        String rolesString = "";
        for (String role : roles) {
            rolesString += role + ", ";
        }
        //replace last , with .
        if (rolesString.length() > 2)
            rolesString = rolesString.substring(0, rolesString.length() - 2);



        myRoles.setText(rolesString);

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
    public void addProgress(ProgressBar progressBar, String  id,int free) {
        String style4Bar, style4Label;
        switch (free) {
            case 1:
                style4Bar = flow1ProgressBar.getStyle();
                style4Label = flow1ProgressLabel.getStyle();
                flow1ProgressBar.setProgress(progressBar.getProgress());
                flow1ProgressLabel.setText(id);
                flow1ProgressBar.setStyle(style4Bar);
                flow1ProgressLabel.setStyle(style4Label);
                break;
            case 2:
                style4Bar = flow2ProgressBar.getStyle();
                style4Label = flow2ProgressLabel.getStyle();
                flow2ProgressBar = progressBar;
                flow2ProgressLabel.setText( id);
                flow2ProgressBar.setStyle(style4Bar);
                flow2ProgressLabel.setStyle(style4Label);
                break;
            case 3:
                style4Bar = flow3ProgressBar.getStyle();
                style4Label = flow3ProgressLabel.getStyle();
                flow3ProgressBar = progressBar;
                flow3ProgressLabel.setText( id);
                flow3ProgressBar.setStyle(style4Bar);
                flow3ProgressLabel.setStyle(style4Label);
                break;
            case 4:
                style4Bar = flow4ProgressBar.getStyle();
                style4Label = flow4ProgressLabel.getStyle();
                flow4ProgressBar = progressBar;
                flow4ProgressLabel.setText(id);
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
            closeButton.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 40px;-fx-border-radius: 26");
        });
        closeButton.setOnMouseExited(event -> {
            closeButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 40px;-fx-border-radius: 26");
        });

        setTopBar();

        asserts();
       // setCssScreenButtons();
    }

    private void setMenuButtonGroup() {
        ToggleGroup group = new ToggleGroup();
    }

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    private void setTopBar() {

        Tooltip tooltip = new Tooltip("Asaf=Gever\nsaar=Efes");
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

        flowExecution.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), flowExecution);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            flowExecution.setStyle(buttonStyle);
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

    @FXML
    void flowExecutionPresents(ActionEvent event) {
        removeLastPressed();
        setLastPressed("flowExecution");
        main.setCurrentFlow(currentFlow);
        main.showExecution();

    }
    @FXML
    void StatisticsFunc(ActionEvent event) {
        main.showStats();
    }

    private void stopRotate() {
        rotateTransition.stop();
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


    private List<FlowDefinitionDTO> getFlowsDTO(List<FlowDefinitionImpl> flows  ) {
        List<FlowDefinitionDTO> flowsDTO=new ArrayList<>();
        for (FlowDefinitionImpl flow : flows) {
            FlowDefinitionDTO toAdd = Mapper.convertToFlowDefinitionDTO(flow);
            flowsDTO.add(toAdd);
        }
        return flowsDTO;
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
    public void SetExecutionButtonVisible(FlowDefinitionDTO flowDefinition){

        if (!menuHbox.getChildren().contains(flowExecution)) {
            flowExecution.setVisible(true);
            flowExecution.setDisable(false);
            menuHbox.getChildren().add(flowExecution);

        }
        flowExecution.setDisable(false);
        this.currentFlow = flowDefinition;
    }
    public void setNameOnScreen(String userName){
        NameInHeader.setText(userName);
        FlowsDefinition.setDisable(false);
        ExecutionsHistory.setDisable(false);
        HBoxData.setVisible(true);
    }
    public void setClient(String name){
        this.client = new Client(name,true);
    }
    public Client getClient(){
        return client;
    }

    public void updateClient(boolean isManager) {
        client.setManager(isManager);


        Platform.runLater( () -> {
            IsManager.setText(client.isManager());
             }
        );
    }


    public List<String> getCurrentRoles() {
        String roles = myRoles.getText();
        if (roles.length() > 1) {
            return new ArrayList<>();
        }
        String[] rolesArray = roles.split(",");
        List<String> rolesList = new ArrayList<>();
        for (String role : rolesArray) {
            rolesList.add(role);
        }
        return rolesList;
    }

    public void setCurrentRoles(List<String> roles) {
        String rolesString = "";
        for (String role : roles) {
            rolesString += role + " ,";
        }
        rolesString = rolesString.substring(0, rolesString.length() - 2);
        myRoles.setText(rolesString);

    }

    public int getProgressBarByID(String id) {
        String label1 = flow1ProgressLabel.getText();
        String label2 = flow2ProgressLabel.getText();
        String label3 = flow3ProgressLabel.getText();
        String label4 = flow4ProgressLabel.getText();
        String last4=id.substring(id.length()-4);
         if (label1.equals(last4)) {
                return 1;
         }else if (label2.equals(last4)) {
                return 2;
         }else if (label3.equals(last4)) {
                return 3;
         }else if (label4.equals(last4)) {
                return 4;
         }
         return 1;
    }

    public ProgressBar getProgressBar(int index) {
        switch (index) {
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

    public boolean inLast4Execution(String id) {
        String last4=id.substring(id.length()-4);
        String label1 = flow1ProgressLabel.getText();
        String label2 = flow2ProgressLabel.getText();
        String label3 = flow3ProgressLabel.getText();
        String label4 = flow4ProgressLabel.getText();
        if (label1.equals(last4) || label2.equals(last4) || label3.equals(last4) || label4.equals(last4)) {
            return true;
        }
        return false;
    }
}
