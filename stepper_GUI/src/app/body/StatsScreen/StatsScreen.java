package app.body.StatsScreen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

public class StatsScreen implements bodyControllerDefinition {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> flowStatsList;

    @FXML
    private ListView<RadioButton> flowsList;

    @FXML
    private Pane flowPane;

    @FXML
    private Label flowDefinitionsSize;

    @FXML
    private ListView<String> stepsList;

    @FXML
    private ListView<String> stepStatsList;

    @FXML
    private Label flowExecutionsSize;

    @FXML
    private Pane stepperPane;

    private Stepper stepperData;


    @FXML
    void initialize() {
        assert flowPane != null : "fx:id=\"flowPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowsList != null : "fx:id=\"flowsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepsList != null : "fx:id=\"stepsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowDefinitionsSize != null : "fx:id=\"flowDefinitionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepStatsList != null : "fx:id=\"stepStatsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowStatsList != null : "fx:id=\"flowStatsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowExecutionsSize != null : "fx:id=\"flowExecutionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepperPane != null : "fx:id=\"stepperPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        setListsToVisible();
        stepperData= DataManager.getData();
        setListsView();
        Binds();
        updateLists(); //set Labels and check if needed to put on tables
        //set listeners
        setListeners();
    }

    private void setListsView() {
        flowsList.setStyle("-fx-control-inner-background:#36393e ;");
        flowStatsList.setStyle("-fx-control-inner-background: #36393e;");
        stepsList.setStyle("-fx-control-inner-background: #36393e;");
        stepStatsList.setStyle("-fx-control-inner-background: #36393e;");


    }

    private void Binds() {
        ListChangeListener<RadioButton> itemsChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    flowsList.setPrefHeight((flowsList.getItems().size() * flowsList.getFixedCellSize())+24);
                }
            }
        };
        flowsList.setFixedCellSize(24);
        flowsList.getItems().addListener((ListChangeListener<RadioButton>) itemsChangeListener);
        flowsList.setPrefHeight(flowsList.getItems().size() * flowsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener2 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    stepsList.setPrefHeight((stepsList.getItems().size() * stepsList.getFixedCellSize())+24);
                }
            }
        };
        stepsList.setFixedCellSize(24);
        stepsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener2);
        stepsList.setPrefHeight(stepsList.getItems().size() * stepsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener3 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    stepStatsList.setPrefHeight((stepStatsList.getItems().size() * stepStatsList.getFixedCellSize())+24);
                }
            }
        };
        stepStatsList.setFixedCellSize(24);
        stepStatsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener3);
        stepStatsList.setPrefHeight(stepStatsList.getItems().size() * stepStatsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener4 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    flowStatsList.setPrefHeight((flowStatsList.getItems().size() * flowStatsList.getFixedCellSize())+24);
                }
            }
        };
        flowStatsList.setFixedCellSize(24);
        flowStatsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener4);
        flowStatsList.setPrefHeight(flowStatsList.getItems().size() * flowStatsList.getFixedCellSize());

    }

    private void setListeners() {
        //add listener to the flow list
//        flowsList.getItems().get(0).getToggleGroup().getProperties().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                // Get the selected flow from the newValue (RadioButton)
//                FlowDefinitionImpl selectedFlow = getFlowFromRadioButton(newValue);
//                // Update the stepsList based on the selected flow
//                updateStepsList(selectedFlow);
//            }
//        });



    }

    private void updateStepsList(FlowDefinitionImpl selectedFlow) {
        stepsList.getItems().clear();
        stepStatsList.getItems().clear();
        if (selectedFlow != null) {
            for (StepUsageDeclaration step : selectedFlow.getSteps()) {
                stepsList.getItems().add(step.getFinalStepName());
                stepStatsList.getItems().add("Used "+step.getTimeUsed()+" times  ||" + "  Average time: "+step.getAvgTime()+" MS");
            }
            flowExecutionsSize.setText("There are "+selectedFlow.getFlowSteps().size()+" Steps in this flow");
        } else {
            flowExecutionsSize.setText("Nothing Selected yet");
        }
    }

    private FlowDefinitionImpl getFlowFromRadioButton(RadioButton pick) {
        String flowName = pick.getText();
        for (FlowDefinitionImpl flow : stepperData.getFlows()) {
            if (flow.getName().equals(flowName)) {
                return flow;
            }

        }
        return null;
    }
    private void updateLists() {
        if (stepperData.getFlows().size()>0){
            ToggleGroup flowToggle = new ToggleGroup();
            for (FlowDefinitionImpl flow:stepperData.getFlows()){
                RadioButton flowSelection = new RadioButton(flow.getName());
                flowSelection.setToggleGroup(flowToggle);
                flowSelection.setOnAction(event -> {
                    RadioButton selectedFlowRadioButton = (RadioButton) flowToggle.getSelectedToggle();
                    if (selectedFlowRadioButton != null) {
                        FlowDefinitionImpl selectedFlow = getFlowFromRadioButton(selectedFlowRadioButton);
                        updateStepsList(selectedFlow);
                    }
                });
                flowsList.getItems().add(flowSelection);//check what happened her
            }
            flowDefinitionsSize.setText("There are "+stepperData.getFlows().size()+" Flow Definitions");
            //set stats table here
            //maybe add listener to the second list
            for (FlowDefinitionImpl flow:stepperData.getFlows()){
                String singleFlowStats= "Used "+flow.getTimesUsed()+" times  ||" + "  Average time: "+flow.getAvgTime()+" MS";
                flowStatsList.getItems().add(singleFlowStats);
            }
            flowExecutionsSize.setText("");

        }else {
            flowDefinitionsSize.setText("There are no Flow Definitions");
        }
        //set stats table here
        //todo
        if (stepperData.getFlows().size() == 0) {//both are empty
            popAlert();//show appropriate message
        }
    }
    private void setListsToVisible() {
        flowsList.setVisible(true);
        flowsList.setDisable(false);
        flowStatsList.setVisible(true);
        flowStatsList.setDisable(false);
        stepsList.setVisible(true);
        stepsList.setDisable(false);
        stepStatsList.setVisible(true);
        stepStatsList.setDisable(false);
    }

    private static void popAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Content");
        alert.setHeaderText(null);
        alert.setContentText("Nothing to show, consider adding a Data");
        alert.showAndWait();
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
