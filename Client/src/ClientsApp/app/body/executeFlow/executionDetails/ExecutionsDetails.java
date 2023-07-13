package ClientsApp.app.body.executeFlow.executionDetails;

import ClientsApp.app.body.bodyController;
import ClientsApp.app.body.executionsHistory.DataViewer.DataViewerController;
import ClientsApp.app.management.style.StyleManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static modules.DataManeger.DataManager.stepperData;

public class ExecutionsDetails {
    @FXML
    private VBox stepTree;
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
    private VBox outputsVbox;
    @FXML
    private VBox logsVbox;
    @FXML
    private Label logsLabel;
    @FXML
    private Pane logScrollPane;
    @FXML
    private AnchorPane mainPane;
    private bodyController body;
    @FXML
    private Label executionCounterLabel;
    @FXML
    private Label exeTime;
    @FXML
    private Label statusLabel;
    private static final String LOG_LINE_STYLE = "-fx-text-fill: #24ff21;";
    private static final String ERROR_LINE_STYLE = "-fx-text-fill: #ff0000;";
    private FlowExecution theFlow=null;

    public String currStyle="";

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    @FXML
    void initialize() {
        setTheme();
        asserts();
        logsLabel.setText(". . .");
        ScrollPane scrollPane = new ScrollPane(logsVbox);
        scrollPane.setFitToWidth(true);
        if (stepperData.getFlowExecutions().size()!=0)
            theFlow = getLastFlowExecution(stepperData);
        if (theFlow != null) {
        updateLogs(theFlow, stepperData);
        updateLogsTree(theFlow);
        updateInputs(theFlow);
        updateOutputs(theFlow);
        updateStatusAndTime(theFlow);
        }
    }

    private void updateStatusAndTime(FlowExecution theFlow) {
        switch (theFlow.getFlowExecutionResult()){
            case SUCCESS:
                statusLabel.setText("Result: Success");
                statusLabel.setStyle("-fx-text-fill: #24ff21;");
                break;
            case WARNING:
                statusLabel.setText("Result: Warning");
                statusLabel.setStyle("-fx-text-fill: #fffa00;");
                break;
            case FAILURE:
                statusLabel.setText("Result: Failure");
                statusLabel.setStyle("-fx-text-fill: #ff0000;");
                break;
        }

        exeTime.setText("Execution time: "+theFlow.getTotalTime().toMillis()+" ms");
        executionCounterLabel.setText("Execution number: "+theFlow.getUniqueId());
    }

        private FlowExecution getLastFlowExecution(Stepper stepperData){

                return stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size() - 1);

    }

    private void asserts() {
        assert stepTree != null : "fx:id=\"stepTree\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert logsPane != null : "fx:id=\"logsPane\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert inputsVbox4Value != null : "fx:id=\"inputsVbox4Value\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert inputsVbox != null : "fx:id=\"inputsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert outputsVbox != null : "fx:id=\"outputsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert outputsVbox4Value != null : "fx:id=\"outputsVbox4Value\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert flowOutputsLabel != null : "fx:id=\"flowOutputsLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert logsLabel != null : "fx:id=\"logsLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert executionCounterLabel != null : "fx:id=\"executionCounterLabel\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";
        assert logsVbox != null : "fx:id=\"logsVbox\" was not injected: check your FXML file 'ExecutionsHistory.fxml'.";

    }
    private void updateLogs(FlowExecution flowExecution,Stepper stepperData) {
        logsVbox.getChildren().clear();
        Label logsLabel = new Label();
        logsLabel.setText("logs for flow with id : "+flowExecution.getUniqueId());
        logsLabel.setStyle("-fx-font-size: 14;"+LOG_LINE_STYLE);
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
    private void updateLogsTree(FlowExecution selectedFlow) {//check
        stepTree.getChildren().clear();
        TreeView<String> stepTreeView = new TreeView<>();
        TreeItem<String> root = new TreeItem<>("Steps");
        stepTreeView.setRoot(root);
        for (StepUsageDeclaration step : selectedFlow.getFlowDefinition().getFlowSteps()) {
            TreeItem<String> stepRoot = new TreeItem<>(step.getFinalStepName());
            if (selectedFlow.getLogs().get(step.getFinalStepName()) != null) {
                for (Pair<String, String> log : selectedFlow.getLogs().get(step.getFinalStepName())) {
                    if (log != null) {
                        TreeItem<String> logItem = new TreeItem<>(log.getValue() + " : " + log.getKey());
                        stepRoot.getChildren().add(logItem);
                    }
                }

            }
            else
                stepRoot.getChildren().add(new TreeItem<>("No logs for this step"));

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
                            setStyle("-fx-background-color: transparent; -fx-text-fill: #00ff00;"); // Set the style of each tree component
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

    public void setFlowExecution(FlowExecution flow) {
        theFlow = flow;
    }
    private void updateTime(FlowExecution selectedFlow) {
        exeTime.setDisable(false);
        exeTime.setVisible(true);
        exeTime.setText("Total-Time: "+selectedFlow.getTotalTime().toMillis()+" MS ");
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
                newOutput.setPrefWidth(outputsVbox.getPrefWidth());
                outputValue.setPrefWidth(outputsVbox4Value.getPrefWidth());
                newOutput.setPrefHeight(28);
                outputValue.setPrefHeight(28);
                newOutput.setStyle("-fx-alignment: top-center; -fx-border-color: yellow; -fx-font-size: 14px;"
               +" -fx-text-fill: #ffffff; -fx-font-weight: bold;-fx-wrap-text: true; -fx-border-width: 1px;");
                outputValue.setStyle("-fx-alignment: top-center; -fx-border-color: yellow; -fx-font-size: 14px;"
                        +" -fx-text-fill: #ffffff; -fx-font-weight: bold;-fx-wrap-text: true; -fx-border-width: 1px;");
                this.outputsVbox4Value.getChildren().add(outputValue);
                this.outputsVbox.getChildren().add(newOutput);
            }
        }

    }
    private Label setLabelForOutput(Object value, String name) {
        Label result = new Label();
        if (value instanceof RelationData) {
            result.setText("relation");
        } else if (value instanceof ArrayList) {//print an arraylist
            result.setText("arraylist");
        }
        if (value instanceof List) {
            result.setText("list");
        } else {
            result.setText(value.toString());
        }
        result.setOnMouseEntered(event -> {
            currStyle = result.getStyle();
            result.setStyle("-fx-border-color: blue; -fx-border-width: 3px; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-alignment: top-center;");
        });
        result.setOnMouseExited(event -> {
            result.setStyle(currStyle);
        });
        result.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/body/executionsHistory/DataViewer/DataViewer.fxml"));
                        Parent root = (Parent) loader.load();
                        DataViewerController controller = loader.getController();

                        if (controller!=null ) {
                            controller.setData(value, name);
                            Stage stage = new Stage();
                            stage.setTitle("Data Viewer");
                            stage.setScene(new Scene(root, 600, 400));
                            stage.show();
                        }
                    } catch (IllegalStateException | IOException ex) {
                        return;
                        // Handle the exception gracefully
                    }
                }
        );
        return result;
    }

    private void updateInputs(FlowExecution selectedFlow) {
        Label title = (Label) this.inputsVbox.getChildren().get(0);
        Label title2 = (Label) this.inputsVbox4Value.getChildren().get(0);
        this.inputsVbox.getChildren().clear();
        this.inputsVbox4Value.getChildren().clear();
        this.inputsVbox.getChildren().add(title);
        this.inputsVbox4Value.getChildren().add(title2);
        List<Pair<String, String>> inputs = selectedFlow.getUserInputs();
        if (inputs != null) {
            for (Pair<String, String> entry : inputs) {
                Label newInput = new Label(entry.getKey());
                Label inputValue = new Label(entry.getValue());
                newInput.setStyle("-fx-alignment: top-center; -fx-border-color: yellow; -fx-font-size: 14px;"
                        +" -fx-text-fill: #ffffff; -fx-font-weight: bold;-fx-wrap-text: true; -fx-border-width: 1px;");
                inputValue.setStyle("-fx-alignment: top-center; -fx-border-color: yellow; -fx-font-size: 14px;"
                        +" -fx-text-fill: #ffffff; -fx-font-weight: bold;-fx-wrap-text: true; -fx-border-width: 1px;");
                inputValue.setOnMouseEntered(event -> {
                    currStyle = inputValue.getStyle();
                    inputValue.setStyle("-fx-border-color: blue; -fx-border-width: 3px; -fx-text-fill: #ffffff; -fx-font-weight: bold;-fx-alignment: top-center;");
                });
                inputValue.setOnMouseExited(event -> {
                    inputValue.setStyle(currStyle);
                });
                inputValue.setOnMouseClicked(event -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/body/executionsHistory/DataViewer/DataViewer.fxml"));
                            try {
                                Parent root = loader.load();
                                DataViewerController controller = loader.getController();
                                controller.setData(entry.getValue(), entry.getKey());
                                Stage stage = new Stage();
                                stage.setTitle("Data Viewer");
                                stage.setScene(new Scene(root, 600, 400));
                                stage.show();
                            }  catch (IllegalStateException | IOException ex) {
                                return;
                                // Handle the exception gracefully
                            }
                        }
                );
                newInput.setPrefWidth(inputsVbox.getPrefWidth());
                inputValue.setPrefWidth(inputsVbox4Value.getPrefWidth());
                newInput.setPrefHeight(28);
                inputValue.setPrefHeight(28);
                this.inputsVbox4Value.getChildren().add(inputValue);
                this.inputsVbox.getChildren().add(newInput);
            }
        }
    }
}

