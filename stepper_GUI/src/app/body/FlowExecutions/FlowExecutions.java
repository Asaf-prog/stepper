package app.body.FlowExecutions;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class FlowExecutions implements bodyControllerDefinition {
    @FXML
    private VBox boxWithList;
    private bodyController body;
    private List<FlowDefinitionImpl> flows;
    @Override
    public void show() {
        for (FlowDefinitionImpl flow : flows){
            Button button = new Button(flow.getName());
            button.setOnAction(e -> handleButtonAction(flow));
            boxWithList.getChildren().add(button);
        }
    }
    private void handleButtonAction(FlowDefinitionImpl flow){
        body.executeExistFlowScreen(flow);
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

    }
}
