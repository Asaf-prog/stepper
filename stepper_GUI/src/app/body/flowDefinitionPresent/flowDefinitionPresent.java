package app.body.flowDefinitionPresent;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class flowDefinitionPresent implements bodyControllerDefinition {
    private List<FlowDefinitionImpl> flows;
    @FXML
    private VBox flowDetailsBox;
    @FXML
    private VBox flowListOfButtons;
    @FXML
    private TreeView<String> flowDetailsTreeView;
    @FXML
    private Label FlowNameTL;
    @FXML
    private Label flowDescriptionTL;
    @FXML
    private Label numOfSteps;
    @FXML
    private TextArea descreptionOfFlow;
    @FXML
    private Label freeInputNumber;
    @FXML
    private Button executeFlow;
    private bodyController body;
    private FlowDefinitionImpl currentFlow;
    @Override
    public void show() {

        for (FlowDefinitionImpl flow : flows){
            Button button = new Button(flow.getName());
            button.setOnAction(e -> handleButtonAction(flow));
            flowListOfButtons.getChildren().add(button);
        }
    }
    private void handleButtonAction(FlowDefinitionImpl flow){
        flowDescriptionTL.setVisible(true);
        FlowNameTL.setText(flow.getName());//name
        FlowNameTL.setVisible(true);
        descreptionOfFlow.setVisible(true);
        descreptionOfFlow.setText(flow.getDescription());
        numOfSteps.setVisible(true);
        numOfSteps.setText("The number of steps is: "+ flow.getFlowSteps().size());
        freeInputNumber.setVisible(true);
        freeInputNumber.setText("The number of free inputs is: "+ flow.getFlowFreeInputs().size());
        //todo add the number of continuation
        body.setCurrentFlow(flow);
        executeFlow.setDisable(false);
    }
   @FXML
    void executeFlowFunc(ActionEvent event) {
        body.executeExistFlowScreen(body.getCurrentFlow());
    }
    @Override
    public void setBodyController(bodyController body) {
        this.body = body;
    }
    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {
        this.flows = list;
    }
    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow){
        currentFlow = flow;
    }
}
