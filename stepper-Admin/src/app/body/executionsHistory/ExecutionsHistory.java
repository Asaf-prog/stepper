
package app.body.executionsHistory;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import app.body.executionsHistory.DataViewer.DataViewerController;
import app.body.executionsHistory.tableStuff.FlowExecutionTableItem;
import app.management.style.StyleManager;
import Constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;
import modules.mappings.Continuation;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import util.ClientConstants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static modules.DataManeger.DataManager.stepperData;

public class ExecutionsHistory implements bodyControllerDefinition {
    @FXML
    private VBox stepTree;
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
    private String styleOfChoiceBox;
    @FXML
    private TableColumn<FlowExecutionTableItem, String> nameCol;
    @FXML
    private TableView<FlowExecutionTableItem> tableData;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label executionCounterLabel;
    @FXML
    private Label exeTime;
    @FXML
    private TableColumn<FlowExecutionTableItem,  String> timeCol;
    @FXML
    private TableColumn<FlowExecutionTableItem,  String> resCol;
    @FXML
    private TableColumn<FlowExecutionTableItem,  String> executedByCol;

    @FXML
    private  Button continuation;
    @FXML
    private ChoiceBox<String> filterChoiceBox;
    private List<Pair<String, String>> freeInputsMandatory ;
    private List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD ;
    private List<Pair<String, String>> freeInputsOptional;
    private List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD;
    private static final String LOG_LINE_STYLE = "-fx-text-fill: #24ff21;";
    private static final String ERROR_LINE_STYLE = "-fx-text-fill: #ff0000;";
    private bodyController body;
    private FlowExecution pickedExecution;

    ObservableList<FlowExecutionTableItem> allExecutions = FXCollections.observableArrayList();
    private String style;

    private Gson gson = new Gson();

    private final List<Stage> stages = new ArrayList<>();

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    @FXML
    void initialize() {
        style=execute.getStyle();
        setTheme();
        Stepper stepperData = DataManager.getData();
        asserts();
        setBisli();
        setupTable();
        setAviadCursor();
        logsLabel.setText(". . .");
        ScrollPane scrollPane = new ScrollPane(logsVbox);
        scrollPane.setFitToWidth(true);
        setFilter2Table();
        setBodyButtonStyle(execute);
        setBodyButtonStyle(continuation);
    }
    @Override
    public void onLeave() {
        for (Stage stage : stages) {
            stage.close();
        }
    }

    private void setFilter2Table() {

        //filterChoiceBox = new ChoiceBox<>();
        filterChoiceBox.getItems().addAll("All", "Success", "Warning", "Failure");
        filterChoiceBox.setValue("All");
        filterChoiceBox.setOnAction(event -> applyFilter(filterChoiceBox.getValue()));
        filterChoiceBox.setStyle(filterChoiceBox.getStyle()+ " -fx-border-color: black; " +
                "-fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 10px; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Comic Sans MS';");
        styleOfChoiceBox= filterChoiceBox.getStyle();
        filterChoiceBox.setStyle(styleOfChoiceBox + "-fx-background-color: #ffffff;");
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("All")) {
                filterChoiceBox.setStyle(styleOfChoiceBox + "-fx-background-color: #ffffff;");
            } else if (newValue.equals("Success")) {
                filterChoiceBox.setStyle(styleOfChoiceBox+"-fx-background-color: #00ff00;");
            } else if (newValue.equals("Warning")) {
                filterChoiceBox.setStyle(styleOfChoiceBox+"-fx-background-color: #fff400;");
            }
            else{// if (newValue.equals("Failure")) {
                filterChoiceBox.setStyle(styleOfChoiceBox+"-fx-background-color: #ff0000;");
            }
        });
    }

    private void applyFilter(String value) {
        ObservableList<FlowExecutionTableItem> items = allExecutions;
        ObservableList<FlowExecutionTableItem> data = FXCollections.observableArrayList();
        ToggleGroup group = new ToggleGroup();

        for (FlowExecutionTableItem item : items) {
            if (item.getResult().equals(value) || value.equals("All")) {
                item.addToToggleGroup(group);
                data.add(item);
                tableItemEvents(stepperData, group, item);
            }
        }

        tableData.setItems(data);
        setResultColumn();
        for (TableColumn<?, ?> column : tableData.getColumns()) {
            if (column.getText().equals("Status")) { // Replace "ColumnName" with the actual column name
                column = (TableColumn<FlowExecutionTableItem,  String>) resCol;
                break;
            }
        }
    }
    private void setBisli() {
        bisli.setOnMouseEntered(e -> {
            // Adjust the position if it is too close to the cursor
            double sceneWidth = mainPane.getWidth()-50;
            double sceneHeight = mainPane.getHeight()-50;
            // Update the position of bisli
            double randomX = Math.random() * (sceneWidth - 50)%sceneWidth;
            double randomY = Math.random() * (sceneHeight - 50)%sceneHeight;
            bisli.setLayoutX(randomX);
            bisli.setLayoutY(randomY);
        });
    }
    private void setAviadCursor() {
        Image cursorImage = new Image(getClass().getResourceAsStream("cursor4.png"));
        ImageView cursorImageView = new ImageView(cursorImage);
        mainPane.setCursor(new ImageCursor(cursorImageView.getImage()));
    }
    private void asserts() {
        assert stepTree != null : "fx:id=\"stepTree\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
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
        assert bisli != null : "fx:id=\"bisli\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert executedByCol != null : "fx:id=\"executedByCol\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
    }
    @FXML
    void executeFlow(ActionEvent event) {
        if (pickedExecution != null) {
            FlowDefinitionImpl flowDefinition =(FlowDefinitionImpl) pickedExecution.getFlowDefinition();
            addValueOfFreeInputsByTypes(flowDefinition);
            setFreeInputsByTypesToMandatoryAndOptionalWithDD(flowDefinition);
//            body.handlerForExecuteFromStatisticScreen(freeInputsMandatory,freeInputsOptional,flowDefinition,freeInputsMandatoryWithDD
//            ,freeInputsOptionalWithDD);
// todo: moveto server
        }
    }
    private void setFreeInputsByTypesToMandatoryAndOptionalWithDD(FlowDefinitionImpl flowDefinition){
        freeInputsMandatoryWithDD = new ArrayList<>();
        freeInputsOptionalWithDD = new ArrayList<>();
        for (Pair<String,DataDefinitionDeclaration>pair: flowDefinition.getFlowFreeInputs()){
            if (pair.getValue().isMandatory())
                freeInputsMandatoryWithDD.add(pair);
            else
                freeInputsOptionalWithDD.add(pair);
        }
    }
    private void addValueOfFreeInputsByTypes(FlowDefinitionImpl flowDefinition){
        freeInputsMandatory = new ArrayList<>();
        freeInputsOptional = new ArrayList<>();
        for (Pair<String, String> pair: pickedExecution.getUserInputs()){
            if (existInMandatoryList(flowDefinition,pair.getKey()))
                freeInputsMandatory.add(pair);

            else freeInputsOptional.add(pair);
        }
    }
    private boolean existInMandatoryList(FlowDefinitionImpl flowDefinition,String nameToSearch){
        for (Pair<String, DataDefinitionDeclaration> pair: flowDefinition.getFlowFreeInputs()){
            if (pair.getKey().equals(nameToSearch))
                return pair.getValue().isMandatory();
        }
        return true;
    }
    @FXML
    void onActionContinuation(ActionEvent event) {
        if (pickedExecution != null) {
            FlowDefinitionImpl flowDefinition = (FlowDefinitionImpl) pickedExecution.getFlowDefinition();
            if (flowDefinition.getContinuations() == null)
                return;
            //build list from target flows in continuations

            List<Continuation> continuations = flowDefinition.getContinuations();
            List<String> targetFlows = new ArrayList<>();
            for (Continuation continuation : continuations) {
                targetFlows.add(continuation.getTargetFlow());
            }
            if (targetFlows.size() != 0) {
                //popup scene with list of target flows to choose one from
                Stage stage = new Stage();
                stages.add(stage);
                stage.setTitle("Choose target flow");
                //ContinuationPopUp controller = new ContinuationPopUp(pickedExecution, targetFlows, stage, body);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("continuation/ContinuationPopUp.fxml"));
               // loader.setController(controller);
                Parent root;
                try {
                    root = loader.load();

                    Scene scene = new Scene(root, 500, 300);
                    stage.setScene(scene);
                    stage.show();

                }  catch (IllegalStateException | IOException ex) {
                    // Handle the exception gracefully
                }
            } else{
                Tooltip tooltip = new Tooltip("No continuations for this flow");
                double tooltipX = continuation.localToScreen(continuation.getBoundsInLocal()).getMinX() + continuation.getWidth() / 2;
                double tooltipY = continuation.localToScreen(continuation.getBoundsInLocal()).getMinY() - 30; // Adjust the offset as needed
                tooltip.show(continuation, tooltipX, tooltipY);
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(e -> tooltip.hide());
                delay.play();
                tooltip.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: white;");
                continuation.setTooltip(tooltip);
            }
        }

    }

    private void setBodyButtonStyle(Button button) {
        button.setOnMouseEntered(event ->
                button.setStyle(style+ "-fx-background-color: rgb(255,0,96); -fx-background-radius: 20;-fx-border-color: #566dff"));
        button.setOnMouseExited(event ->
                button.setStyle(style));


    }

    private FlowDefinitionImpl getFlowFromName(String targetFlow) {
        for (FlowDefinitionImpl flowDefinition : stepperData.getFlows()) {
            if (flowDefinition.getName().equals(targetFlow))
                return flowDefinition;
        }
        return null;
    }

    private void updateLogs(FlowExecution flowExecution,Stepper stepperData) {
        logsVbox.getChildren().clear();
        Label logsLabel = new Label();
        logsLabel.setText("   logs for flow with id : " + flowExecution.getUniqueId());
        logsLabel.setStyle("-fx-font-size: 14;" + LOG_LINE_STYLE);
        logsVbox.getChildren().add(logsLabel);
        logsVbox.getChildren().add(stepTree);


    }
    private void addLog(Pair<String, String> log) {
        Label newLog = new Label(log.getValue() + " : " + log.getKey());
        if(log.getValue().contains("ERROR"))
            newLog.setStyle(ERROR_LINE_STYLE+";-fx-font-size: 12;");
        else
            newLog.setStyle(LOG_LINE_STYLE+";-fx-font-size: 12;");
        logsVbox.getChildren().add(newLog);
    }
    private void setupTable() {
        Request request = new Request.Builder()
                .url(util.Constants.GET_TABLE_DATA)
                .get()
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get table data");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                ResponseBody responseBody = response.body();
                String body = responseBody.string();
                responseBody.close();

                if (body == "" || body == null)
                    return;
                try {
                    List<FlowExecutionDTO> flowExecutionDTOS = gson.fromJson(body, new TypeToken<List<FlowExecutionDTO>>() {
                    }.getType());
                    if (flowExecutionDTOS == null)
                        return;

                    Platform.runLater(() -> {
                                tableData.setStyle("-fx-control-inner-background: transparent;");
                                initTable();
                                executionCounterLabel.setText("There are " + stepperData.getFlowExecutions().size() + " Flow Executions");

                                final ObservableList<FlowExecutionTableItem> data = FXCollections.observableArrayList();
                                ToggleGroup group = new ToggleGroup();
                                FlowExecutionResult flowExecutionResult;
                                for (FlowExecutionDTO flowExecution : flowExecutionDTOS) {
                                    if (flowExecution.getFlowExecutionResult().equals(FlowExecutionResult.FAILURE))
                                        flowExecutionResult = FlowExecutionResult.FAILURE;
                                    else if (flowExecution.getFlowExecutionResult().equals(FlowExecutionResult.SUCCESS))
                                        flowExecutionResult = FlowExecutionResult.SUCCESS;
                                    else
                                        flowExecutionResult = FlowExecutionResult.WARNING;


                                    FlowExecutionTableItem item = new FlowExecutionTableItem(flowExecution.getUniqueId(), flowExecution.getExecutedBy(), flowExecution.getFlowDefinition().getName(),
                                            flowExecution.getStartTime(), flowExecutionResult);
                                    flowExecution.isDone.addListener((observable, oldValue, newValue) -> {
                                        if (newValue) {
                                            item.setResultFromExecutionResult(flowExecution.getFlowExecutionResult());
                                            setResultColumn();
                                        }
                                    });

                                    item.addToToggleGroup(group);
                                    data.add(item);
                                    tableItemEvents(stepperData, group, item);
                                }
                                tableData.setItems(data);
                                setResultColumn();
                                allExecutions = data;


                                // Add the status column to the TableView
                                for (TableColumn<?, ?> column : tableData.getColumns()) {
                                    if (column.getText().equals("Status")) { // Replace "ColumnName" with the actual column name
                                        column = (TableColumn<FlowExecutionTableItem, String>) resCol;
                                        break;
                                    }
                                }
                            }
                    );

                } catch (JsonSyntaxException e) {
                    return;
                }
            }
        });



    }
    private void initTable() {
        idCol.getStyleClass().add("titleOfColumn");
        nameCol.getStyleClass().add("titleOfColumn");
        timeCol.getStyleClass().add("titleOfColumn");
        resCol.getStyleClass().add("titleOfColumn");
        executedByCol.getStyleClass().add("titleOfColumn");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        resCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        executedByCol.setCellValueFactory(new PropertyValueFactory<>("executedBy"));
    }

    private void tableItemEvents(Stepper stepperData, ToggleGroup group, FlowExecutionTableItem item) {
        item.getId().setOnAction(event -> {
            RadioButton selectedFlowRadioButton = (RadioButton) group.getSelectedToggle();
            if (selectedFlowRadioButton != null) {
                UUID uuid= UUID.fromString(selectedFlowRadioButton.getText());
                FlowExecution selectedFlow = stepperData.getFlowExecutionById(uuid);
                pickedExecution=selectedFlow;
                updateLogs(selectedFlow, stepperData);
                updateInputs(selectedFlow);
                updateOutputs(selectedFlow);
                updateTime(selectedFlow);
                updateLogsTree(selectedFlow);
                continuation.setDisable(false);
                execute.setDisable(false);
            }
        });
    }
    private void setResultColumn() {
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
                    if (item.toUpperCase().equals("FAILURE")) {
                        setTextFill(Color.RED);
                    } else if (item.toUpperCase().equals("WARNING")) {
                        setTextFill(Color.YELLOW);
                    } else {//success
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });
    }

    private void updateLogsTree(FlowExecution selectedFlow) {

        stepTree.getChildren().clear();

        TreeView<String> stepTreeView = new TreeView<>();
        TreeItem<String> root = new TreeItem<>("Steps");

        stepTreeView.setStyle("-fx-background-color: transparent; -fx-blend-mode: MULTIPLY;");
        stepTreeView.getStylesheets().add("app/management/style/darkTheme.css");
        stepTreeView.getStyleClass().add("tree");

        stepTreeView.setRoot(root);
        for (StepUsageDeclaration step : selectedFlow.getFlowDefinition().getFlowSteps()) {
            TreeItem<String> stepRoot = new TreeItem<>(step.getFinalStepName());
            List<Pair<String, String>> logsPerStep = selectedFlow.getLogs().get(step.getFinalStepName());
            if (logsPerStep != null) {
                for (Pair<String, String> log : logsPerStep) {
                    if (log != null) {
                        TreeItem<String> logItem = new TreeItem<>(log.getValue() + " : " + log.getKey());
                        stepRoot.getChildren().add(logItem);
                    }
                }
            }
            stepTreeView.getRoot().getChildren().add(stepRoot);
            stepTreeView.setCellFactory(treeView -> {
                TreeCell<String> cell = new TreeCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setStyle("-fx-background-color: transparent; -fx-text-fill: #24ff21;"); // Set the style of each tree component
                        }
                    }
                };
                cell.setOnMouseClicked(event -> {
                    if (!cell.isEmpty()) {
                        TreeItem<String> treeItem = cell.getTreeItem();
                        if (treeItem != null) {
                            if (treeItem.isExpanded()) {
                                treeItem.setExpanded(false);
                            } else {
                                treeItem.setExpanded(true);
                            }
                        }
                    }
                });
                return cell;
            });
            stepTreeView.setShowRoot(false);
            stepTreeView.getStyleClass().add("logsTree");

        }
        stepTree.getChildren().add(stepTreeView);
        //logScrollPane.setStyle("-fx-background-color: transparent;");

    }
    private void addLogToTree(TreeView<String> stepItem, Pair<String, String> log) {
        TreeItem<String> logItem = new TreeItem<>(log.getValue() + " : " + log.getKey());
        stepItem.getRoot().getChildren().add(logItem);
    }

    private void updateTime(FlowExecution selectedFlow) {
        exeTime.setDisable(false);
        exeTime.setVisible(true);
        exeTime.setText("Total-Time: "+selectedFlow.getTotalTime().toMillis()+" ms ");
    }

    private void updateOutputs(FlowExecution selectedFlow) {
        Label title= (Label) this.outputsVbox.getChildren().get(0);
        Label title2= (Label) this.outputsVbox4Value.getChildren().get(0);
        this.outputsVbox4Value.getChildren().clear();
        this.outputsVbox.getChildren().clear();
        this.outputsVbox.getChildren().add(title);
        this.outputsVbox4Value.getChildren().add(title2);
        Map<String, Object> outputs=selectedFlow.getAllExecutionOutputs();
        if(outputs!=null) {
            for (Map.Entry<String, Object> entry : outputs.entrySet()) {
                Label newOutput = new Label(entry.getKey());
                Label outputValue = setLabelForOutput(entry.getValue(),entry.getKey());
                newOutput.getStyleClass().add("DDLabel");
                outputValue.getStyleClass().add("DDLabel");
                newOutput.setPrefWidth(outputsVbox.getPrefWidth());
                outputValue.setPrefWidth(outputsVbox4Value.getPrefWidth());
                newOutput.setPrefHeight(28);
                outputValue.setPrefHeight(28);
                this.outputsVbox4Value.getChildren().add(outputValue);
                this.outputsVbox.getChildren().add(newOutput);
            }
        }

    }

    private Label setLabelForOutput(Object value, String name) {
        Label result = new Label();
        if (value instanceof RelationData) {
            result.setText("Relation");
        }else if (value instanceof ArrayList) {//print an arraylist
            result.setText("List");
        }else if (value instanceof List) {
            result.setText("List");
        }else {
            result.setText(value.toString());
        }
        result.setOnMouseEntered(event -> {
            result.setStyle("-fx-border-color: blue; -fx-border-width: 3px;");
        });
        result.setOnMouseExited(event -> {
            result.setStyle("-fx-border-color: #ffff00; -fx-border-width: 1px;");
        });
        result.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataViewer/DataViewer.fxml"));
                        Parent root = (Parent) loader.load();
                        DataViewerController controller = loader.getController();

                        if (controller!=null ) {
                            controller.setData(value, name);
                            Stage stage = new Stage();
                            stages.add(stage);
                            stage.setTitle("Data Viewer");
                            stage.setScene(new Scene(root, 600, 400));
                            stage.show();
                        }
                    }  catch (IllegalStateException | IOException ex) {
                        // Handle the exception gracefully
                    }
                }
        );
        return result;
    }
    private void updateInputs(FlowExecution selectedFlow) {
        Label title= (Label) this.inputsVbox.getChildren().get(0);
        Label title2= (Label) this.inputsVbox4Value.getChildren().get(0);
        this.inputsVbox.getChildren().clear();
        this.inputsVbox4Value.getChildren().clear();
        this.inputsVbox.getChildren().add(title);
        this.inputsVbox4Value.getChildren().add(title2);
        List<Pair<String, String>> inputs=selectedFlow.getUserInputs();
        if(inputs!=null) {
            for (Pair<String, String> entry : inputs) {
                Label newInput = new Label(entry.getKey());
                Label inputValue = new Label(entry.getValue());
                inputValue.setOnMouseEntered(event -> {
                    inputValue.setStyle("-fx-border-color: blue; -fx-border-width: 3px;");
                });
                inputValue.setOnMouseExited(event -> {
                    inputValue.setStyle("-fx-border-color: #ffff00; -fx-border-width: 1px;");
                });
                inputValue.setOnMouseClicked(event -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataViewer/DataViewer.fxml"));
                            try {
                                Parent root = loader.load();
                                DataViewerController controller = loader.getController();
                                controller.setData(entry.getValue(), entry.getKey());
                                Stage stage = new Stage();
                                stages.add(stage);
                                stage.setTitle("Data Viewer");
                                stage.setScene(new Scene(root, 600, 400));
                                stage.show();
                            } catch (IllegalStateException | IOException ex) {
                                // Handle the exception gracefully
                            }
                        }
                );
                newInput.getStyleClass().add("DDLabel");
                inputValue.getStyleClass().add("DDLabel");
                newInput.setPrefWidth(inputsVbox.getPrefWidth());
                inputValue.setPrefWidth(inputsVbox4Value.getPrefWidth());
                newInput.setPrefHeight(28);
                inputValue.setPrefHeight(28);
                this.inputsVbox4Value.getChildren().add(inputValue);
                this.inputsVbox.getChildren().add(newInput);
            }
        }
    }
    @Override
    public void show() {
    }
    @Override
    public void setBodyController(bodyController body) {
        this.body=body;
    }
    @Override
    public void setFlowsDetails(List<FlowDefinitionDTO> list) {

    }
    @Override
    public void SetCurrentFlow(FlowDefinitionDTO flow) {

    }
}

