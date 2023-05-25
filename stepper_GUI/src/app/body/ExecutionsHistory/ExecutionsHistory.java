package app.body.ExecutionsHistory;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

import java.util.List;

public class ExecutionsHistory implements bodyControllerDefinition {
    @FXML
    private VBox listOfFlowToShow;
    private bodyController body;
    private List<FlowDefinitionImpl> flows;

    @Override
    public void show() {
        Stepper stepperData= DataManager.getData();
        List<FlowExecution> flowsExe = stepperData.getFlowExecutions();
        for (FlowExecution flow : flowsExe){
            Button button = new Button(flow.getFlowDefinition().getName());
            button.setOnAction(e -> handleButtonAction(flow));
            listOfFlowToShow.getChildren().add(button);
        }
    }
    private void handleButtonAction(FlowExecution flowExecution){
        //create a new body that present the data of all this flow

    }
    public void setFlows( List<FlowDefinitionImpl> flows){
        this.flows = flows;
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
    public void SetCurrentFlow(FlowDefinitionImpl flow) {
       // this.flows = flows;
    }
}
