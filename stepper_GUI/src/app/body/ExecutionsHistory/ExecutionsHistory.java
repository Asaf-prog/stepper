package app.body.ExecutionsHistory;
import java.net.URL;
import java.util.*;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

public class ExecutionsHistory implements bodyControllerDefinition {
    @FXML
    private TableColumn<FlowExecutionTableItem, RadioButton> idCol;
    @FXML
    private Pane logsPane;
    @FXML
    private VBox inputsVbox4Value;
    @FXML
    private VBox inputsVbox;
    @FXML
    private VBox outputsVbox4Value;
    @FXML
    private Label flowOutputsLabel;
    private FlowExecution pickedExecution;
    @FXML
    private Button execute;
    @FXML
    private VBox outputsVbox;
    @FXML
    private VBox logsVbox;
    @FXML
    private Label logsLabel;
    @FXML
    private ImageView bisli;

    @FXML
    private TableColumn<FlowExecutionTableItem, String> nameCol;

    @FXML
    private TableView<FlowExecutionTableItem> tableData;

    @FXML
    private AnchorPane mainPane;
    private bodyController body;
    @FXML
    private Label executionCounterLabel;

    @FXML
    private TableColumn<FlowExecutionTableItem,  String> timeCol;

    @FXML
    private TableColumn<FlowExecutionTableItem,  String> resCol;
    private static final String LOG_LINE_STYLE = "-fx-text-fill: #24ff21;";
    private static final String ERROR_LINE_STYLE = "-fx-text-fill: red;";
    KeyFrame keyFrame;
    KeyValue keyValueX;
    KeyValue keyValueY;

    @FXML
    void initialize() {
        Stepper stepperData = DataManager.getData();
        asserts();
        setBisli();
        setupTable(stepperData);
        setAviadCursor();
        logsLabel.setText(". . .");
        ScrollPane scrollPane = new ScrollPane(logsVbox);
        scrollPane.setFitToWidth(true);
        // Set selection listener for table

    }

    private void setBisli() {


        bisli.setOnMouseEntered(e -> {
            double bisliX = bisli.getLayoutX();
            double bisliY = bisli.getLayoutY();
            // Adjust the position if it is too close to the cursor
            double sceneWidth = mainPane.getWidth();
            double sceneHeight = mainPane.getHeight();
            // Update the position of bisli
             double randomX = Math.random() * (sceneWidth - 50)%sceneWidth;
             double randomY = Math.random() * (sceneHeight - 50)%sceneHeight;
            bisli.setLayoutX(randomX);
            bisli.setLayoutY(randomY);
        });
    }


    private void setAviadCursor() {
        Image cursorImage = new Image(getClass().getResourceAsStream("aviadcursor2.png"));
        ImageView cursorImageView = new ImageView(cursorImage);
        double scaleFactor = 1.5; // Change this value to adjust the size
        mainPane.setCursor(new ImageCursor(cursorImageView.getImage(),
                cursorImageView.getImage().getWidth() *55 ,
                cursorImageView.getImage().getHeight() * 55));
    }

    private void asserts() {
        assert logsPane != null : "fx:id=\"logsPane\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert inputsVbox4Value != null : "fx:id=\"inputsVbox4Value\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert tableData != null : "fx:id=\"tableData\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert resCol != null : "fx:id=\"resCol\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert inputsVbox != null : "fx:id=\"inputsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert execute != null : "fx:id=\"execute\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert outputsVbox != null : "fx:id=\"outputsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert outputsVbox4Value != null : "fx:id=\"outputsVbox4Value\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert flowOutputsLabel != null : "fx:id=\"flowOutputsLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert logsLabel != null : "fx:id=\"logsLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert executionCounterLabel != null : "fx:id=\"executionCounterLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert timeCol != null : "fx:id=\"timeCol\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert logsVbox != null : "fx:id=\"logsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";


    }
    @FXML
    void executeFlow(ActionEvent event) {
        if (pickedExecution != null) {
            FlowDefinitionImpl flowDefinition =(FlowDefinitionImpl) pickedExecution.getFlowDefinition();
           // body.getMVC_controller().executeFlow(flowDefinition);
            //todo need to use the body in order to execute FLOW from here

        }
    }
    private void updateLogs(FlowExecution flowExecution,Stepper stepperData) {//todo add scroll pane to logs
        logsVbox.getChildren().clear();
        Label logsLabel = new Label();
        logsLabel.setText("logs for flow with id : "+flowExecution.getUniqueId());
        logsLabel.setStyle("-fx-font-size: 14;"+LOG_LINE_STYLE);

        logsVbox.getChildren().add(logsLabel);
        Map<String, List<Pair<String, String>>> logs = flowExecution.getLogs();
        for (StepUsageDeclaration step :flowExecution.getFlowDefinition().getFlowSteps()) {
            List<Pair<String, String>> logsPerStep=logs.get(step.getFinalStepName());
            for (Pair<String, String> log : logsPerStep) {
                addLog(log);
            }
        }
    }

    private void addLog(Pair<String, String> log) {
        Label newlog = new Label(log.getValue() + " : " + log.getKey());
        if(log.getValue().contains("ERROR"))
            newlog.setStyle(ERROR_LINE_STYLE+";-fx-font-size: 12;");
        else
            newlog.setStyle(LOG_LINE_STYLE+";-fx-font-size: 12;");
        logsVbox.getChildren().add(newlog);
    }

    private void setupTable(Stepper stepperData) {
        idCol.getStyleClass().add("titleOfColumn");
        nameCol.getStyleClass().add("titleOfColumn");
        timeCol.getStyleClass().add("titleOfColumn");
        resCol.getStyleClass().add("titleOfColumn");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        resCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        executionCounterLabel.setText("There are " + stepperData.getFlowExecutions().size() + " Flow Executions");


        final ObservableList<FlowExecutionTableItem> data = FXCollections.observableArrayList();
        ToggleGroup group = new ToggleGroup();

        for (FlowExecution flowExecution : stepperData.getFlowExecutions()) {
            FlowExecutionTableItem item =new FlowExecutionTableItem(flowExecution.getUniqueId(), flowExecution.getFlowDefinition().getName(),
                    flowExecution.getTotalTime().toMillis(), flowExecution.getFlowExecutionResult());
            item.addToToggleGroup(group);
            data.add(item);
            item.id.setOnAction(event -> {
                RadioButton selectedFlowRadioButton = (RadioButton) group.getSelectedToggle();
                if (selectedFlowRadioButton != null) {
                    UUID uuid= UUID.fromString(selectedFlowRadioButton.getText());
                    FlowExecution selectedFlow = stepperData.getFlowExecutionById(uuid);
                    //todo add logic when pick flow
                    pickedExecution=selectedFlow;
                    execute.setDisable(false);
                    updateLogs(selectedFlow,stepperData);

                }
            });
        }
        tableData.setItems(data);
        tableData.getColumns().addAll(idCol, nameCol, timeCol, resCol);
        resCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        resCol.setCellFactory(column -> new TableCell<FlowExecutionTableItem, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // Clear the existing text and styles
                setText(null);
                setStyle(" -fx-background-color: #36393e; -fx-border-color: black;" +
                        "-fx-alignment: CENTER; -fx-font-size: 14px; -fx-font-weight: bold;");

                if (item != null && !empty) {
                    setText(item);

                    // Set color based on the status value
                    if (item.toUpperCase().equals("FAIL")) {
                        setTextFill(Color.RED);
                    } else if (item.toUpperCase().equals("WARNING")) {
                        setTextFill(Color.YELLOW);
                    } else {//success
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

// Add the status column to the TableView
        tableData.getColumns().add(resCol);
        //tableData.setStyle("-fx-background-color: transparent;");

        tableBinds();

    }

    private void tableBinds() {
        //set table background color to transparent
       // tableData.setStyle("-fx-background-color: transparent;");


    }

    @Override
    public void show() {

    }

    @Override
    public void setBodyController(bodyController body) {

    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }

    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow) {

    }
}

