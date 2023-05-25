package app.body.executeFlow;

import app.MVC_controller.MVC_controller;
import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.ArrayList;
import java.util.List;

public class executeFlowController implements bodyControllerDefinition {
   private FlowDefinitionImpl currentFlow;
    @FXML
    private Button startExecute;
   private bodyController body;
   private int sizeOfMandatoryList;
    @FXML
    private VBox mandatoryList;
    @FXML
    private VBox optionalList;
    private List<Pair<String,String>> freeInputsTemp;
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
         freeInputsTemp = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> mandatory: mandatoryInputs){
            Label label = new Label(mandatory.getKey());
            TextField textField = new TextField();

            textField.setOnAction(e->handleButtonAction(textField.getText(),mandatory.getKey()));
            textField.setPromptText(mandatory.getValue().getUserString());

            mandatoryList.getChildren().add(label);
            mandatoryList.getChildren().add(textField);
        }
        for (Pair<String, DataDefinitionDeclaration> optional: optionalInputs){
            Label label = new Label(optional.getKey());
            TextField textField = new TextField();

            textField.setOnAction(e->handleButtonAction(textField.getText(),optional.getKey()));
            textField.setPromptText(optional.getValue().getUserString());

            optionalList.getChildren().add(label);
            optionalList.getChildren().add(textField);
        }
        setSizeOfMandatoryList(mandatoryInputs.size());
       // currentFlow.setUserInputs(freeInputsTemp);
    }
    private void setSizeOfMandatoryList(int size){
        this.sizeOfMandatoryList = size;
    }
    public int getSizeOfMandatoryList(){
        return sizeOfMandatoryList;
    }
    @FXML
    void startExecuteAfterGetFreeInputs(ActionEvent event) {
        body.getMVC_controller().setFreeInputs(freeInputsTemp);
        body.getMVC_controller().executeFlow(currentFlow);
    }
    private void handleButtonAction(String data,String nameOfDD){
        if(!data.isEmpty()){
            freeInputsTemp.add(new Pair<>(nameOfDD,data));
        }
        if (freeInputsTemp.size() == getSizeOfMandatoryList()){
            startExecute.setDisable(false);
        }
    }
    @Override
    public void setBodyController(bodyController body) {
      this.body = body;
    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }
}
