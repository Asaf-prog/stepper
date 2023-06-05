package app.body.executeFlow;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.bodyControllerForContinuation;
import app.body.executeFlow.executionDetails.ExecutionsDetails;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.mappings.Continuation;
import modules.mappings.ContinuationMapping;
import modules.mappings.InitialInputValues;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static modules.DataManeger.DataManager.stepperData;

public class executeFlowController implements bodyControllerDefinition,bodyControllerForContinuation {
    @FXML
    private Button showDetails;
    private FlowDefinitionImpl currentFlow;
    private FlowDefinitionImpl lastFlow;
    @FXML
    private Button continuationExe;
    @FXML
    private Button startExecute;
    private bodyController body;
    private int sizeOfMandatoryList;
    private Map<String,Object> outputsOfLastFlow;
    @FXML
    private VBox mandatoryList;
    @FXML
    private Label continuationLabel;
    @FXML
    private VBox optionalList;
    @FXML
    private VBox continuationVbox;
    private List<Pair<String, String>> freeInputsTemp;
    private List<Pair<String, String>>  freeInputsTempForContinuation = new ArrayList<>();
    private List<Pair<String, String>> freeInputsMandatory ;
    private List<Pair<String, String>> freeInputsOptional;
    @FXML
    private Button continuation;
    private List<Pair<String, DataDefinitionDeclaration>> currentMandatoryFreeInput;
    private List<Pair<String, DataDefinitionDeclaration>> currentOptionalFreeInput;
    @FXML
    void initialize() {
        asserts();
        continuation.setVisible(false);
        continuationVbox.setVisible(false);
        continuationExe.setVisible(false);
    }
    private void asserts() {
        assert startExecute != null : "fx:id=\"startExecute\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert mandatoryList != null : "fx:id=\"mandatoryList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert optionalList != null : "fx:id=\"optionalList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuation != null : "fx:id=\"continuation\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationVbox != null : "fx:id=\"continuationVbox\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationLabel != null : "fx:id=\"continuationLabel\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert showDetails != null : "fx:id=\"showDetails\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationExe != null : "fx:id=\"continuationExe\" was not injected: check your FXML file 'executeFlowController.fxml'.";

    }
    @Override
    public void show() {
        //first of all, create a two list : mandatoryInputs and optionalInputs:
        List<Pair<String, DataDefinitionDeclaration>> freeInputs = getCurrentFlow().getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> optionalInputs = new ArrayList<>();

        for (Pair<String, DataDefinitionDeclaration> pair : freeInputs) {
            if (pair.getValue().isMandatory()) {
                mandatoryInputs.add(pair);
            } else {
                optionalInputs.add(pair);
            }
        }
        freeInputsTemp = new ArrayList<>();
        currentMandatoryFreeInput = mandatoryInputs;
        currentOptionalFreeInput = optionalInputs;
        mandatoryHandler(mandatoryInputs);
        optionalHandler(optionalInputs);
        setSizeOfMandatoryList(mandatoryInputs.size());
    }
    @Override
    public void showForContinuation() {
        continuationExe.setVisible(true);
        continuationExe.setDisable(true);
        startExecute.setVisible(false);
        //first, create a list of mandatory and optional that the user need to supply
        //secondly, create the component
        Continuation continuation1 = null;//=> fulfil the data of the mapping to the flow
        for (Continuation continuationToThisFlow: lastFlow.getContinuations()){
            if (continuationToThisFlow.getTargetFlow().equals(currentFlow.getName()))
                continuation1 = continuationToThisFlow;
        }
        createVboxMandatoryAndOptionalWithInitValue(continuation1);
    }
    private void createVboxMandatoryAndOptionalWithInitValue(Continuation continuation){
        List<Pair<String, DataDefinitionDeclaration>> freeInputs = getCurrentFlow().getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> optionalInputs = new ArrayList<>();

        for (Pair<String, DataDefinitionDeclaration> pair : freeInputs) {
            if (pair.getValue().isMandatory()) {
                mandatoryInputs.add(pair);
            } else {
                optionalInputs.add(pair);
            }
        }
        freeInputsTemp = new ArrayList<>();
        mandatoryHandlerForContinuation(continuation,mandatoryInputs);
        optionalHandlerForContinuation(continuation,optionalInputs);
    }
    private void mandatoryHandlerForContinuation(Continuation continuation,List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs){
        for (Pair<String, DataDefinitionDeclaration> mandatory:mandatoryInputs){
            Label label = new Label(mandatory.getKey());
            label.setStyle("-fx-text-fill: white");
            HBox nameAndAddOrEdit = new HBox();
            nameAndAddOrEdit.getChildren().add(label);
            if (thisDataSupplyByRecentFlow(mandatory.getKey(),continuation)){//this data exist
                String dataLabel = getDataThatSupplyAndUpdateTheListOfFreeInputs(continuation,mandatory.getKey());
                if (dataLabel !=null) {
                    Label data = new Label(dataLabel);
                    data.setStyle("-fx-text-fill: white");
                    nameAndAddOrEdit.getChildren().add(data);
                }
                else
                    throw new RuntimeException();
            }
            else {//the user need to give us this data
                TextField textField = new TextField();
                Button addButton = new Button("Save");
                addButton.setOnAction(event -> handleButtonAction(addButton, textField, textField.getText(),
                        mandatory.getKey(), mandatory.getValue().dataDefinition().getType(), nameAndAddOrEdit,false));
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);

//                nameAndAddOrEdit.setSpacing(10);
//                nameAndAddOrEdit.setPrefWidth(mandatoryList.getPrefWidth());
//
//                textField.setPromptText(data.getValue().getUserString());
//                //mandatoryList.getChildren().add(label);
//                mandatoryList.getChildren().add(nameAndAddOrEdit);
//                mandatoryList.setSpacing(10);
//            }
//            else{//is optional
            }
            nameAndAddOrEdit.setSpacing(5);
            mandatoryList.getChildren().add(nameAndAddOrEdit);
        }
        if (freeInputsTemp.size() == mandatoryInputs.size())
            continuationExe.setDisable(false);
        mandatoryList.setSpacing(10);
    }
    private String getDataThatSupplyAndUpdateTheListOfFreeInputsForOptional(Continuation continuation,String nameToSearch){
       if (existInInitialValue(nameToSearch)){
           return getExistInInitialValue(nameToSearch);
       }
        else if (customMappingWithContinuation(nameToSearch,continuation)){//let search this data in the outputs of last flow
            for(ContinuationMapping mapping: continuation.getMappingList()){
                if (mapping.getTargetData().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,outputsOfLastFlow.get(mapping.getSourceData()).toString()));//add data to list of data
                    return outputsOfLastFlow.get(mapping.getSourceData()).toString();
                }
            }
        } else if (thisDataSupplyByFreeInputsOfLastFlowForOptional(nameToSearch)) {
            for (Pair<String, String> lastOptional:freeInputsOptional) {
                if (lastOptional.getKey().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,lastOptional.getValue()));//add data to list of data
                    return lastOptional.getValue();
                }
            }
        }
        return null;
    }
    private boolean thisDataSupplyByFreeInputsOfLastFlowForOptional(String nameToSearch){
        for (Pair<String, String> lastMandatory:freeInputsOptional){
            if (lastMandatory.getKey().equals(nameToSearch))
                return true;
        }
        return false;
    }
    private String getDataThatSupplyAndUpdateTheListOfFreeInputs(Continuation continuation,String nameToSearch){
        if (existInInitialValue(nameToSearch)) {
            return getExistInInitialValue(nameToSearch);
        }
        else if (customMappingWithContinuation(nameToSearch,continuation)){//let search this data in the outputs of last flow
            for(ContinuationMapping mapping: continuation.getMappingList()){
                if (mapping.getTargetData().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,outputsOfLastFlow.get(mapping.getSourceData()).toString()));//add data to list of data
                    return outputsOfLastFlow.get(mapping.getSourceData()).toString();
                }
            }
        } else if (thisDataSupplyByFreeInputsOfLastFlow(nameToSearch)) {
            for (Pair<String, String> lastMandatory:freeInputsMandatory) {
                if (lastMandatory.getKey().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,lastMandatory.getValue()));//add data to list of data
                    return lastMandatory.getValue();
                }
            }
        }
            return null;
    }
    private boolean thisDataSupplyByRecentFlow(String nameToSearch,Continuation continuation){
        if(outputsOfLastFlow.containsKey(nameToSearch) || thisDataSupplyByFreeInputsOfLastFlow(nameToSearch)||
                customMappingWithContinuation(nameToSearch,continuation)||existInInitialValue(nameToSearch))
            return true;
        else
            return false;
    }
    private boolean thisDataSupplyByRecentFlowInOptional(String nameToSearch,Continuation continuation){
        if (outputsOfLastFlow.containsKey(nameToSearch) ||customMappingWithContinuation(nameToSearch,continuation) ||
                thisDataSupplyByFreeInputsOfLastFlowOptional(nameToSearch)|| existInInitialValue(nameToSearch))
            return true;
        else
            return false;
    }
    private boolean thisDataSupplyByFreeInputsOfLastFlowOptional(String nameToSearch){
        for (Pair<String, String> lastOptional:freeInputsOptional){
            if (lastOptional.getKey().equals(nameToSearch))
                return true;
        }
        return false;
    }
    private boolean customMappingWithContinuation(String nameToSearch,Continuation continuation){
       for(ContinuationMapping mapping: continuation.getMappingList()){
           if (mapping.getTargetData().equals(nameToSearch))
               return true;
       }
       return false;
    }
    private boolean thisDataSupplyByFreeInputsOfLastFlow(String nameToSearch){
        for (Pair<String, String> lastMandatory:freeInputsMandatory){
            if (lastMandatory.getKey().equals(nameToSearch))
                return true;
        }
        return false;
    }
    private void optionalHandlerForContinuation(Continuation continuation,List<Pair<String, DataDefinitionDeclaration>>optionalInputs ){
        for (Pair<String, DataDefinitionDeclaration> optional:optionalInputs){
            Label label = new Label(optional.getKey());
            label.setStyle("-fx-text-fill: white");
            HBox nameAndAddOrEdit = new HBox();
            nameAndAddOrEdit.getChildren().add(label);
            if (thisDataSupplyByRecentFlowInOptional(optional.getKey(),continuation)){//this data exist
                //todo => need to check if it's correct!
                String dataLabel = getDataThatSupplyAndUpdateTheListOfFreeInputsForOptional(continuation,optional.getKey());
                if (dataLabel !=null) {
                    Label data = new Label(dataLabel);
                    data.setStyle("-fx-text-fill: white");
                    nameAndAddOrEdit.getChildren().add(data);
                }
                else
                    throw new RuntimeException();
            }
            else {//the user need to give us this data
                TextField textField = new TextField();
                Button addButton = new Button("Save");
                addButton.setOnAction(event -> handleButtonAction(addButton, textField, textField.getText(),
                        optional.getKey(), optional.getValue().dataDefinition().getType(), nameAndAddOrEdit,true));
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
            }
            nameAndAddOrEdit.setSpacing(5);
            optionalList.getChildren().add(nameAndAddOrEdit);

        }
        optionalList.setSpacing(10);
    }
    private Map<String,Object> getOutputsOfLastFlow(){
        return outputsOfLastFlow;
    }
    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow) {
        currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow() {
        return currentFlow;
    }

    private boolean existInData(String nameOfDD) {
        for (Pair<String, DataDefinitionDeclaration> run : currentOptionalFreeInput) {
            if (run.getKey().equals(nameOfDD))
                return true;
        }
        for (Pair<String, DataDefinitionDeclaration> run : currentMandatoryFreeInput) {
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
    public void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclaration>> mandatory, List<Pair<String, DataDefinitionDeclaration>> optional
            ,List<Pair<String, String>>mandatoryIn,
                                               List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl lastFlow) {
        currentMandatoryFreeInput = mandatory;
        currentOptionalFreeInput = optional;
        freeInputsMandatory = mandatoryIn;
        freeInputsOptional = optionalIn;
        outputsOfLastFlow = outputs;
        this.lastFlow = lastFlow;
    }
    private List<Pair<String, DataDefinitionDeclaration>> getCurrentMandatoryFreeInput() {
        return currentMandatoryFreeInput;
    }

    private List<Pair<String, DataDefinitionDeclaration>> getCurrentOptionalFreeInput() {
        return currentOptionalFreeInput;
    }
    private void optionalHandler(List<Pair<String, DataDefinitionDeclaration>> optionalInputs) {
        for (Pair<String, DataDefinitionDeclaration> optional : optionalInputs) {
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
            if (!existInInitialValue(optional.getKey())) {
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
                nameAndAddOrEdit.setSpacing(10);

                addButton.setOnAction(e -> handleButtonAction(addButton, textField, textField.getText(),
                        optional.getKey(), optional.getValue().dataDefinition().getType(), nameAndAddOrEdit, true));

                textField.setPromptText(optional.getValue().getUserString());
                optionalList.getChildren().add(label);
                optionalList.getChildren().add(nameAndAddOrEdit);
                optionalList.setSpacing(10);
            }
            else {
                String initData = getExistInInitialValue(optional.getKey());
                freeInputsTemp.add(new Pair<>(optional.getKey(),initData));
            }
        }
    }
    private void mandatoryHandler(List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs) {
        for (Pair<String, DataDefinitionDeclaration> mandatory : mandatoryInputs) {
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
            if (!existInInitialValue(mandatory.getKey())){//to this value in this current step there is no initial value
                if (mandatory.getKey().equals("FOLDER_NAME")) {
                    textField.setEditable(false);
                    addButton.setText("Browse");
                    textField.setText("FOLDER_PATH");
                    textField.setStyle("-fx-text-fill: #ffffff; -fx-background-color: transparent");
                    nameAndAddOrEdit.getChildren().add(textField);
                    nameAndAddOrEdit.getChildren().add(addButton);
                    nameAndAddOrEdit.setSpacing(10);
                } else {
                    nameAndAddOrEdit.getChildren().add(textField);
                    nameAndAddOrEdit.getChildren().add(addButton);
                    nameAndAddOrEdit.setSpacing(10);
                }
                addButton.setOnAction(e -> handleButtonAction(addButton, textField, textField.getText(),
                        mandatory.getKey(), mandatory.getValue().dataDefinition().getType(), nameAndAddOrEdit,false));
                textField.setPromptText(mandatory.getValue().getUserString());
                mandatoryList.getChildren().add(label);
                mandatoryList.getChildren().add(nameAndAddOrEdit);
                mandatoryList.setSpacing(10);

            }
            else {//to this value in this current step there is initial value
                String initData = getExistInInitialValue(mandatory.getKey());
                freeInputsTemp.add(new Pair<>(mandatory.getKey(),initData));
            }
        }
    }
    private String getExistInInitialValue(String nameToSearchInKey){
        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValues()){
            if (initialInputValues.getInputName().equals(nameToSearchInKey))
                return initialInputValues.getInitialValue();
        }
        return null;
    }
    private boolean existInInitialValue(String nameToSearchInKey){
        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValues()){
            if (initialInputValues.getInputName().equals(nameToSearchInKey))
                return true;
        }
        return false;
    }
    @FXML
    void startContinuationAfterGetFreeInputs(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        for (Continuation continuation : currentFlow.getContinuations()) {
            RadioButton button = new RadioButton(continuation.getTargetFlow());
            button.setStyle("-fx-text-fill: white");
            button.setOnAction(e -> {
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
        continuationLabel.setText("Continuation for " + currentFlow.getName());
    }
    private void handleButtonActionForContinuation(String nameOfTargetFlow) throws Exception {
        FlowDefinitionImpl targetFlow = getFlowByName(nameOfTargetFlow);
        if (targetFlow != null) {
            ///need to add the list of the output of the current step
            FlowExecution flowThatCurrentFinish = getFlowExecutionByName(currentFlow.getName());
            if (flowThatCurrentFinish != null){
                Map<String,Object> outputs = flowThatCurrentFinish.getAllExecutionOutputs();
            body.handlerContinuation(targetFlow, currentMandatoryFreeInput, currentOptionalFreeInput,freeInputsMandatory,freeInputsOptional,outputs,currentFlow);
        }
            else
                throw new RuntimeException();
        }
        else
            throw new Exception("Target flow is null");
    }
    private FlowDefinitionImpl getFlowByName(String nameOfTargetFlow) {
        List<FlowDefinitionImpl> flows = stepperData.getFlows();
        for (FlowDefinitionImpl targetFlow : flows) {
            if (targetFlow.getName().equals(nameOfTargetFlow))
                return targetFlow;
        }
        return null;
    }
    private FlowExecution getFlowExecutionByName(String nameOfCurrentFlow){
        Stepper stepperData = DataManager.getData();
        List<FlowExecution> flowExecutions = stepperData.getFlowExecutions();
        for (FlowExecution flowExecution: flowExecutions){
            if (flowExecution.getFlowDefinition().getName().equals(nameOfCurrentFlow))
                return flowExecution;
        }
        return null;
    }
    private void setSizeOfMandatoryList(int size) {
        this.sizeOfMandatoryList = size;
    }

    public int getSizeOfMandatoryList() {
        return sizeOfMandatoryList;
    }

    @FXML
    void startExecuteAfterGetFreeInputs(ActionEvent event) {
        setTheNewInputsThatTheUserSupply();
        body.getMVC_controller().setFreeInputs(freeInputsTemp);
        showDetails.setDisable(true);
        continuation.setVisible(true);
        body.getMVC_controller().executeFlow(currentFlow);
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }

        //pops out flows Details...
        // todo here import stepper and get last execution then add listener to isDone prop and when it's true then show details button
        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setVisible(true);

        lastFlowExecution.isDoneProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                Platform.runLater(() -> {
                    popupDetails();
                });
            }
        });
    }
    private void setTheNewInputsThatTheUserSupply(){
        freeInputsMandatory = new ArrayList<>();
        freeInputsOptional = new ArrayList<>();
        for (Pair<String, String> data : freeInputsTemp){
            for (Pair<String, DataDefinitionDeclaration> run: currentMandatoryFreeInput){
                if (run.getKey().equals(data.getKey()))
                    freeInputsMandatory.add(new Pair<>(data.getKey(),data.getValue()));
            }
            for (Pair<String, DataDefinitionDeclaration> run: currentOptionalFreeInput){
                if (run.getKey().equals(data.getKey()))
                    freeInputsOptional.add(new Pair<>(data.getKey(),data.getValue()));
            }
        }
    }
    private void popupDetails() {
        showExecutionDetails();
        showDetails.setDisable(false);
        showDetails.setVisible(true);
    }
    private void handleButtonActionForShowDetails() {
        showExecutionDetails();
    }

    private void showExecutionDetails() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("executionDetails/ExecutionsDetails.fxml"));
        ExecutionsDetails executionsDetails = new ExecutionsDetails();
        try {
            //todo aviad- check with thread try doing together
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Flow Details");
            //set icon as previous stage
            stage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
            stage.setScene(new Scene(root, 1060, 365));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private FlowExecution getLastFlowExecution() {
        return stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size() - 1);
    }
    private void handleButtonAction(Button addButton, TextField textField, String data, String nameOfDD, Class<?> type, HBox Hbox,boolean isOptional) {
        if (addButton.getText().equals("Edit")) {
            if (nameOfDD.equals("FOLDER_NAME")) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");
                File selectedDirectory = directoryChooser.showDialog(null);
                if (selectedDirectory != null) {
                    int index = getIndexOfLabel(Hbox);
                    Label label = (Label) Hbox.getChildren().get(index);
                    label.setText(selectedDirectory.getAbsolutePath());
                    data = selectedDirectory.getAbsolutePath();
                }
            } else {
                addButton.setText("Save");
                for (Pair<String, String> pair : freeInputsTemp) {
                    if (pair.getKey().equals(nameOfDD)) {
                        freeInputsTemp.remove(pair);
                        break;
//                        if (freeInputsTemp.isEmpty()) {
//                            break;
//                        }
                    }
                }
                textField.clear();
                addButton.setText("Save");
                if (freeInputsTemp.size() != getSizeOfMandatoryList()) {
                    startExecute.setDisable(true);
                }
            }
        } else {
            if (!data.isEmpty() && !nameOfDD.equals("FOLDER_NAME")) {
                freeInputsTemp.add(new Pair<>(nameOfDD, data));
                addButton.setText("Edit");
            } else if (nameOfDD.equals("FOLDER_NAME")) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");
                File selectedFile = directoryChooser.showDialog(null);
                freeInputsTemp.add(new Pair<>(nameOfDD, selectedFile.toString()));
                addButton.setText("Edit");
                int index = Hbox.getChildren().indexOf(textField);
                Label label = new Label(selectedFile.toString());
                label.setStyle("-fx-text-fill: yellow ; -fx-font-size: 12px");
                Hbox.getChildren().set(index, label);
                textField.setText(selectedFile.toString());
            }
            if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()&& !isOptional) {
                startExecute.setDisable(false);
                startExecute.setOnMouseEntered(event ->
                        startExecute.setStyle("-fx-background-color: #36e6f3;"));
                startExecute.setOnMouseExited(event ->
                        startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
            }
        }
    }
    private int checkHowMandatoryInputsINFreeInputsTemp(){
        int counter = 0;
        for (Pair<String, String> data: freeInputsTemp){
            for (Pair<String, DataDefinitionDeclaration> mandatory: currentMandatoryFreeInput){
                if (mandatory.getKey().equals(data.getKey()))
                    counter++;
            }
        }
        return counter;
    }
    private int getIndexOfLabel(HBox hbox) {
        int index = 0;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                index = hbox.getChildren().indexOf(node);
            }
        }
        return index;
    }
    @Override
    public void setBodyController(bodyController body) {
        this.body = body;
    }
    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }
    public void showDetails(ActionEvent actionEvent){
        showExecutionDetails();
    }
    @FXML
    void ContinuationExecution(ActionEvent event) {
        body.getMVC_controller().setFreeInputs(freeInputsTemp);

        body.getMVC_controller().executeFlow(currentFlow);
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }
        showDetails.setDisable(true);
        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setVisible(true);
    }
    @Override
    public void setBodyControllerContinuation(bodyController body){
        this.body = body;
    }
}
