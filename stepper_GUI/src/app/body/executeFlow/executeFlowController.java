package app.body.executeFlow;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.bodyControllerForContinuation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.mappings.Continuation;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class executeFlowController implements bodyControllerDefinition, bodyControllerForContinuation {
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

    private List<Pair<String, DataDefinitionDeclaration>> currentMandatoryFreeInput;

    private List<Pair<String, DataDefinitionDeclaration>> currentOptionalFreeInput;
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
    public void SetCurrentFlow(FlowDefinitionImpl flow){
        currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow(){
        return currentFlow;
    }
    @FXML
    private Button continuation;
    @Override
    public void showForContinuation() {
        //first, create a list of mandatory and optional that the user need to supply
        //secondly, create the component
        List<Pair<String, DataDefinitionDeclaration>> needToSupply = createListThatTheUserNeedToSupply();
        List<Pair<String, DataDefinitionDeclaration>> thisDataExist = dataThatSupplyByUser(needToSupply);
        
    }
    private List<Pair<String, DataDefinitionDeclaration>> dataThatSupplyByUser(List<Pair<String, DataDefinitionDeclaration>> needToSupply){
        List<Pair<String, DataDefinitionDeclaration>> thisDataExist = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration>run:getCurrentFlow().getFlowFreeInputs()){
         for (Pair<String, DataDefinitionDeclaration>runOfSupply: needToSupply){
             if (run.getKey().equals(runOfSupply.getKey()))
                 thisDataExist.add(run);
         }
     }
        return thisDataExist;
    }
    private List<Pair<String, DataDefinitionDeclaration>> createListThatTheUserNeedToSupply(){
        List<Pair<String, DataDefinitionDeclaration>> needToSupply= new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> inputThatTheFlowNeeded = getCurrentFlow().getFlowFreeInputs();
        for (Pair<String, DataDefinitionDeclaration> run: inputThatTheFlowNeeded){
            if (!existInData(run.getKey()))
                needToSupply.add(run);
        }
        return needToSupply;
    }
    private boolean existInData(String nameOfDD){
        for (Pair<String, DataDefinitionDeclaration> run: currentOptionalFreeInput){
            if (run.getKey().equals(nameOfDD))
                return true;
        }
        for (Pair<String, DataDefinitionDeclaration> run: currentMandatoryFreeInput){
            if (run.getKey().equals(nameOfDD))
                return true;
        }
        return false;
    }
    @Override
    public void setCurrentFlowForContinuation(FlowDefinitionImpl flow) {
        currentFlow = flow;
    }
    @Override
    public void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclaration>> mandatory, List<Pair<String, DataDefinitionDeclaration>> optional) {
        currentMandatoryFreeInput = mandatory;
        currentOptionalFreeInput = optional;
    }
    private List<Pair<String, DataDefinitionDeclaration>> getCurrentMandatoryFreeInput(){
        return currentMandatoryFreeInput;
    }
    private List<Pair<String, DataDefinitionDeclaration>> getCurrentOptionalFreeInput(){
        return currentOptionalFreeInput;
    }

    @Override
    public void show() {
        //first of all, create a two list : mandatoryInputs and optionalInputs:
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
        mandatoryHandler(mandatoryInputs);
        optionalHandler(optionalInputs);
        setSizeOfMandatoryList(mandatoryInputs.size());
        currentMandatoryFreeInput = mandatoryInputs;
        currentOptionalFreeInput = optionalInputs;

    }
    private void optionalHandler(List<Pair<String, DataDefinitionDeclaration>> optionalInputs) {
        for (Pair<String, DataDefinitionDeclaration> optional: optionalInputs){
            Label label = new Label(optional.getKey());
            label.setStyle("-fx-text-fill: white");
            TextField textField = new TextField();

            Button addButton = new Button("Save");
            HBox nameAndAddOrEdit = new HBox();

            textField.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    event.consume();
                }
            });
            nameAndAddOrEdit.getChildren().add(textField);
            nameAndAddOrEdit.getChildren().add(addButton);
            nameAndAddOrEdit.setSpacing(10);

            addButton.setOnAction(e->handleButtonAction(addButton,textField,
                    textField.getText(),optional.getKey(),optional.getValue().dataDefinition().getType()));
            textField.setPromptText(optional.getValue().getUserString());
            optionalList.getChildren().add(label);
            optionalList.getChildren().add(nameAndAddOrEdit);
            optionalList.setSpacing(10);
        }
    }
    private void mandatoryHandler(List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs) {
        for (Pair<String, DataDefinitionDeclaration> mandatory: mandatoryInputs){
            Label label = new Label(mandatory.getKey());
            label.setStyle("-fx-text-fill: #ffffff");
            TextField textField = new TextField();
            Button addButton = new Button("Save");

            HBox nameAndAddOrEdit = new HBox();
            textField.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    event.consume();
                }
            });
            if (mandatory.getKey().equals("FOLDER_NAME"))
            {
                textField.setEditable(false);
            }
            nameAndAddOrEdit.getChildren().add(textField);
            nameAndAddOrEdit.getChildren().add(addButton);
            nameAndAddOrEdit.setSpacing(10);

            addButton.setOnAction(e->handleButtonAction(addButton,textField,textField.getText(),mandatory.getKey(),
                    mandatory.getValue().dataDefinition().getType()));
            textField.setPromptText(mandatory.getValue().getUserString());
            mandatoryList.getChildren().add(label);
            mandatoryList.getChildren().add(nameAndAddOrEdit);
            mandatoryList.setSpacing(10);
        }
    }
    @FXML
    void startContinuationAfterGetFreeInputs(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        for (Continuation continuation : currentFlow.getContinuations()){
            RadioButton button = new RadioButton(continuation.getTargetFlow());
            button.setStyle("-fx-text-fill: white");
            button.setOnAction(e-> {
                try {
                    handleButtonActionForContinuation(continuation.getTargetFlow());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            button.setToggleGroup(group);
            continuationVbox.getChildren().add(button);
        }
        continuationVbox.setSpacing(10);
        continuationVbox.setVisible(true);
        continuationLabel.setText("Continuation for "+ currentFlow.getName());
    }
    private void handleButtonActionForContinuation(String nameOfTargetFlow) throws Exception {
        FlowDefinitionImpl targetFlow = getFlowByName(nameOfTargetFlow);
        if (targetFlow != null) {
            body.handlerContinuation(targetFlow,currentMandatoryFreeInput,currentOptionalFreeInput);
        }
        else
            throw new Exception("Target flow is null");
    }
    private FlowDefinitionImpl getFlowByName(String nameOfTargetFlow){
        Stepper stepperData = DataManager.getData();
        List<FlowDefinitionImpl> flows = stepperData.getFlows();
        for (FlowDefinitionImpl targetFlow: flows ){
            if (targetFlow.getName().equals(nameOfTargetFlow))
                return targetFlow;
        }
        return null;
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
    private void handleButtonAction(Button addButton,TextField textField,String data,String nameOfDD,Class<?> type) {
        if (addButton.getText().equals("Edit")) {
            //addButton.setText("Save");

            for (Pair<String, String> pair : freeInputsTemp) {
                if (pair.getKey().equals(nameOfDD)) {
                    freeInputsTemp.remove(pair);
                    if (freeInputsTemp.isEmpty()){
                      //  handleButtonActionForEdit(addButton,textField,data,nameOfDD);
                        break;
                }}
            }
            textField.clear();
            addButton.setText("Save");
            if (freeInputsTemp.size() != getSizeOfMandatoryList()){
                startExecute.setDisable(true);
            }
        }
        else {
            if (!data.isEmpty()) {
                freeInputsTemp.add(new Pair<>(nameOfDD, data));
                addButton.setText("Edit");
            } else if (nameOfDD.equals("FOLDER_NAME")) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose File");
                File selectedFile = directoryChooser.showDialog(null);
                freeInputsTemp.add(new Pair<>(nameOfDD, selectedFile.toString()));
                addButton.setText("Edit");
                textField.setText(selectedFile.toString());
            }
            if (freeInputsTemp.size() == getSizeOfMandatoryList()) {
                startExecute.setDisable(false);
                startExecute.setOnMouseEntered(event ->
                        startExecute.setStyle("-fx-background-color: #36e6f3;"));
                startExecute.setOnMouseExited(event ->
                        startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
            }
        }
    }
    private void handleButtonActionForEdit(Button addButton,TextField textField,String data,String nameOfDD){
        if (!data.isEmpty()) {
            freeInputsTemp.add(new Pair<>(nameOfDD, data));
            addButton.setText("Edit");
        } else if (nameOfDD.equals("FOLDER_NAME")) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose File");
            File selectedFile = directoryChooser.showDialog(null);
            freeInputsTemp.add(new Pair<>(nameOfDD, selectedFile.toString()));
            addButton.setText("Edit");
            textField.setText(selectedFile.toString());
        }
        if (freeInputsTemp.size() == getSizeOfMandatoryList()) {
            startExecute.setDisable(false);
            startExecute.setOnMouseEntered(event ->
                    startExecute.setStyle("-fx-background-color: #36e6f3;"));
            startExecute.setOnMouseExited(event ->
                    startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
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
