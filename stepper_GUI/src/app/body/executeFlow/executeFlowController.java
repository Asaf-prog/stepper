package app.body.executeFlow;

import app.MVC_controller.MVC_controller;
import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.mappings.Continuation;
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
    private Label continuationLabel;
    @FXML
    private VBox optionalList;
    @FXML
    private VBox continuationVbox;
    private List<Pair<String,String>> freeInputsTemp;
    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow){
        currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow(){
        return currentFlow;
    }
    @FXML
    private Button continuation;
    @FXML
    void initialize() {
        assert startExecute != null : "fx:id=\"startExecute\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert mandatoryList != null : "fx:id=\"mandatoryList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert optionalList != null : "fx:id=\"optionalList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuation != null : "fx:id=\"continuation\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationVbox != null : "fx:id=\"continuationVbox\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationLabel != null : "fx:id=\"continuationLabel\" was not injected: check your FXML file 'executeFlowController.fxml'.";

        continuation.setVisible(false);
        continuationVbox.setVisible(false);

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
    }
    @FXML
    void startContinuationAfterGetFreeInputs(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        for (Continuation continuation : currentFlow.getContinuations()){
            RadioButton button = new RadioButton(continuation.getTargetFlow());
            button.setStyle("-fx-text-fill: white");
            button.setOnAction(e-> handleButtonActionForContinuation(continuation.getTargetFlow()));
            button.setToggleGroup(group);
            continuationVbox.getChildren().add(button);
        }
        continuationVbox.setSpacing(10);
        continuationVbox.setVisible(true);
        continuationLabel.setText("Continuation for "+ currentFlow.getName());
    }
    private void handleButtonActionForContinuation(String nameOfTargetFlow){

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
        continuation.setVisible(true);
        body.getMVC_controller().executeFlow(currentFlow);
        if (currentFlow.getContinuations().size() != 0){
            continuation.setDisable(false);
        }
    }
    private void handleButtonAction(String data,String nameOfDD){
        if(!data.isEmpty()){
            freeInputsTemp.add(new Pair<>(nameOfDD,data));
        }
        if (freeInputsTemp.size() == getSizeOfMandatoryList()){
            startExecute.setDisable(false);
            startExecute.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 20; -fx-text-fill: black;) ");
            startExecute.setOnMouseEntered(event -> startExecute.setStyle("-fx-background-color: #36e6f3;"));
            startExecute.setOnMouseExited(event -> startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
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
