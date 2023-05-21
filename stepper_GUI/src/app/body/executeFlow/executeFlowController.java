package app.body.executeFlow;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.ArrayList;
import java.util.List;

public class executeFlowController implements bodyControllerDefinition {
   private FlowDefinitionImpl currentFlow;
   private bodyController body;
    @FXML
    private VBox mandatoryList;
    @FXML
    private VBox optionalList;
    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow){
        currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow(){
        return currentFlow;
    }
    @Override
    public void show() {
        //first of all create a two list : mandatoryInputs and optionalInputs:
        List<Pair<String, DataDefinitionDeclaration>> freeInputs= getCurrentFlow().getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> optionalInputs = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> pair: freeInputs){
            if (pair.getValue().isMandatory()){
                mandatoryInputs.add(pair);
            }
            else {
                optionalInputs.add(pair);
            }
        }
        for (Pair<String, DataDefinitionDeclaration> mandatory: mandatoryInputs){
            Button button = new Button(mandatory.getKey());
            button.setOnAction(e -> handleButtonAction(mandatory.getValue()));
            mandatoryList.getChildren().add(button);
        }
        for (Pair<String, DataDefinitionDeclaration> optional: optionalInputs){
            Button button = new Button(optional.getKey());
            button.setOnAction(e -> handleButtonAction(optional.getValue()));
            optionalList.getChildren().add(button);
        }
    }
    private void handleButtonAction(DataDefinitionDeclaration dd){

        TextField textField = new TextField();
       textField.setVisible(true);
        String userInput = textField.getText(); // Get the user's input from the text field
        System.out.println("User Input: " + userInput);
        textField.setVisible(true);

        System.out.println(textField.getText());
    }

//    private void handleButtonAction(FlowDefinitionImpl flow){
//        flowDescriptionTL.setVisible(true);
//        FlowNameTL.setText(flow.getName());//name
//        FlowNameTL.setVisible(true);
//        descreptionOfFlow.setVisible(true);
//        descreptionOfFlow.setText(flow.getDescription());
//        numOfSteps.setVisible(true);
//        numOfSteps.setText("The number of steps is: "+ flow.getFlowSteps().size());
//        freeInputNumber.setVisible(true);
//        freeInputNumber.setText("The number of free inputs is: "+ flow.getFlowFreeInputs().size());
//        body.setCurrentFlow(flow);
//        executeFlow.setDisable(false);
//    }
    @Override
    public void setBodyController(bodyController body) {
      this.body = body;
    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }
}
