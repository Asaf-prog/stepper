package app.body.StatsScreen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

public class StatsScreen implements bodyControllerDefinition {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> infoListForStepper;

    @FXML
    private ListView<Button> flowDefinitionsList;

    @FXML
    private Pane flowPane;

    @FXML
    private Label flowDefinitionsSize;

    @FXML
    private ListView<RadioButton> flowExecutionsList;

    @FXML
    private ListView<String> infoListForFlow;

    @FXML
    private Label flowExecutionsSize;

    @FXML
    private Pane stepperPane;

    private Stepper stepperData;


    @FXML
    void initialize() {
        assert infoListForFlow != null : "fx:id=\"infoListForFlow\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowDefinitionsList != null : "fx:id=\"flowDefinitionsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowPane != null : "fx:id=\"flowPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowDefinitionsSize != null : "fx:id=\"flowDefinitionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowExecutionsList != null : "fx:id=\"flowExecutionsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert infoListForStepper != null : "fx:id=\"infoListForStepper\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowExecutionsSize != null : "fx:id=\"flowExecutionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepperPane != null : "fx:id=\"stepperPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        setListsToInvisible();
        stepperData= DataManager.getData();
        updateLists(); //set Labels and check if needed to put on tables
        //set listeners
        setListeners();
    }

    private void setListeners() {
        flowDefinitionsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                for (FlowDefinitionImpl flow:stepperData.getFlows()){
                    if (flow.getName().equals(newValue.getText())){
                        //show flow info make sure to make it visible
                    }
                }
            }
        });
        flowExecutionsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                for (FlowExecution flow:stepperData.getFlowExecutions()){
                    if (flow.getFlowDefinition().getName().equals(newValue.getText())){
                        //show flow info make sure to make it visible
                    }
                }
            }
        });
    }
    private void updateLists() {
        if (stepperData.getFlows().size()>0){
            for (FlowDefinitionImpl flow:stepperData.getFlows()){
                flowDefinitionsList.getItems().add(new Button(flow.getName()));//check what happened here
            }
            flowDefinitionsList.setVisible(true);
            flowDefinitionsSize.setText("There are "+stepperData.getFlows().size()+" Flow Definitions");
        }else {
            flowDefinitionsSize.setText("There are no Flow Definitions");
        }
        if (stepperData.getFlowExecutions().size()>0){
            for (FlowExecution flow:stepperData.getFlowExecutions()){
                String listItem = flow.getFlowDefinition().getName();
                RadioButton rb2Add=new RadioButton(listItem);
                rb2Add.setVisible(true);
                flowExecutionsList.getItems().add(rb2Add);//todo maybe decorate the list
            }
            flowExecutionsList.setVisible(true);
            flowExecutionsSize.setText("There are "+stepperData.getFlowExecutions().size()+" Flow Executions");
        } else {
            flowExecutionsSize.setText("There are no Flow Executions");
            if (stepperData.getFlows().size() == 0) {//both are empty
                popAlert();//show appropriate message
            }
        }
    }
    private void setListsToInvisible() {
        flowExecutionsList.setVisible(false);
        infoListForStepper.setVisible(false);
        infoListForFlow.setVisible(false);
        flowDefinitionsList.setVisible(false);
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
