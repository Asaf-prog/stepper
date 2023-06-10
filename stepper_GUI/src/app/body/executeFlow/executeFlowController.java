package app.body.executeFlow;
import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.bodyControllerExecuteFromHistory;
import app.body.bodyControllerForContinuation;
import app.body.executeFlow.executionDetails.ExecutionsDetails;
import app.management.style.StyleManager;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
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

public class executeFlowController implements bodyControllerDefinition,bodyControllerForContinuation, bodyControllerExecuteFromHistory {
    @FXML
    private Button showDetails;
    @FXML
    private Button continuation;
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
    private  VBox optionalList;
    @FXML
    private VBox continuationVbox;
    private List<Pair<String, String>> freeInputsTemp;
    private List<Pair<String, String>>  freeInputsTempForContinuation = new ArrayList<>();
    private List<Pair<String, String>> freeInputsMandatory ;
    private List<Pair<String, String>> freeInputsOptional;
    private List<Pair<String, DataDefinitionDeclaration>> currentMandatoryFreeInput;
    private List<Pair<String, DataDefinitionDeclaration>> currentOptionalFreeInput;
    private List<Pair<String, String>> freeInputsMandatoryFromHistory;
    private List<Pair<String, String>> freeInputsOptionalFromHistory;
    @FXML
    private Label flowNameLabel;
    private boolean isComeFromHistory =false;
    private String style;
    List<Stage> stages = new ArrayList<>();

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    @FXML
    void initialize() {
        style=startExecute.getStyle();
        asserts();
        setTheme();
        continuation.setVisible(false);
        continuationVbox.setVisible(false);
        continuationExe.setVisible(false);
        setBodyButtonStyle(continuation);
        setBodyButtonStyle(continuationExe);
        setBodyButtonStyle(showDetails);
        setBodyButtonStyle(startExecute);

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
        assert flowNameLabel != null : "fx:id=\"flowNameLabel\" was not injected: check your FXML file 'executeFlowController.fxml'.";
    }

    @Override
    public void onLeave() {
        for (Stage stage : stages) {
            stage.close();
        }
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
        flowNameLabel.setText("Collect Input For Flow : "+getCurrentFlow().getName());

    }
    @Override
    public void showForContinuation() {
        continuationExe.setVisible(true);
        continuationExe.setDisable(true);
        startExecute.setVisible(false);
        flowNameLabel.setText("Collect Input For Flow : "+getCurrentFlow().getName());
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
            label.getStylesheets().add("app/management/style/darkTheme.css");
            label.getStyleClass().add("inputLabel");
            label.setStyle("-fx-text-fill: white");
            HBox nameAndAddOrEdit = new HBox();
            nameAndAddOrEdit.getChildren().add(label);
            if (thisDataSupplyByRecentFlow(mandatory.getKey(),continuation)){//this data exist
                String dataLabel = getDataThatSupplyAndUpdateTheListOfFreeInputs(continuation,mandatory.getKey());
                if (dataLabel !=null) {
                    Label data = new Label(dataLabel);
                    data.getStylesheets().add("app/management/style/darkTheme.css");
                    data.getStyleClass().add("inputLabel");
                    data.setStyle("-fx-text-fill: white");
                    nameAndAddOrEdit.getChildren().add(data);
                }
                else
                    throw new RuntimeException();
            }
            else {//the user need to give us this data
                TextField textField = new TextField();
                textField.setStyle("-fx-alignment: center;-fx-border-radius: 30; -fx-font-size: 16");
                textField.setPromptText("Enter here");
                Button addButton = new Button("Save");
                addButton.getStylesheets().add("app/management/style/darkTheme.css");
                addButton.getStyleClass().add("inputButton");
                setButtonStyle(addButton);
                addButton.setOnAction(event -> handleButtonAction(addButton, textField, textField.getText(),
                        mandatory.getKey(), mandatory.getValue().dataDefinition().getType(), nameAndAddOrEdit,false));
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
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
        for (Pair<String, DataDefinitionDeclaration> lastMandatory: currentMandatoryFreeInput){
            if (lastMandatory.getKey().equals(nameToSearch))
                return true;
        }
        return false;
    }
    private void optionalHandlerForContinuation(Continuation continuation,List<Pair<String, DataDefinitionDeclaration>>optionalInputs ){
        for (Pair<String, DataDefinitionDeclaration> optional:optionalInputs){
            Label label = new Label(optional.getKey());
            label.getStylesheets().add("app/management/style/darkTheme.css");
            label.getStyleClass().add("inputLabel");
            setLabelStyle(label);
            HBox nameAndAddOrEdit = new HBox();
            nameAndAddOrEdit.getChildren().add(label);
            if (thisDataSupplyByRecentFlowInOptional(optional.getKey(),continuation)){//this data exist

                String dataLabel = getDataThatSupplyAndUpdateTheListOfFreeInputsForOptional(continuation,optional.getKey());
                if (dataLabel !=null) {
                    Label data = new Label(dataLabel);
                    setLabelStyle(data);

                    data.getStylesheets().add("app/management/style/darkTheme.css");
                    data.getStyleClass().add("inputLabel");
                    nameAndAddOrEdit.getChildren().add(data);
                }
                else
                    throw new RuntimeException();
            }
            else {//the user need to give us this data
                TextField textField = new TextField();
                textField.setStyle("-fx-alignment: center;-fx-border-radius: 10; -fx-font-size: 16");
                textField.setPromptText("Enter here");
                Button addButton = new Button("Save");
                addButton.getStylesheets().add("app/management/style/darkTheme.css");
                addButton.getStyleClass().add("inputButton");
                setButtonStyle(addButton);
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

    private void setLabelStyle(Label data) {
        if (StyleManager.getCurrentTheme().equals("dark"))
            data.setStyle("-fx-text-fill: #ffff00; -fx-font-size: 16px;-fx-alignment: center;-fx-font-family: 'Arial Rounded MT Bold';");
        else
            data.setStyle("-fx-text-fill: black;-fx-font-size: 16px;-fx-alignment: center;-fx-font-family: 'Arial Rounded MT Bold';");
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
            label.getStylesheets().add("app/management/style/darkTheme.css");
            label.getStyleClass().add("inputLabel");
            setLabelStyle(label);
            TextField textField = new TextField();

            textField.setStyle("-fx-alignment: center;-fx-border-radius: 10; -fx-font-size: 16");
            textField.setPromptText("Enter here");

            Button addButton = new Button("Save");
            addButton.getStylesheets().add("app/management/style/darkTheme.css");
            addButton.getStyleClass().add("inputButton");
            setButtonStyle(addButton);
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
            label.getStylesheets().add("app/management/style/darkTheme.css");
            label.getStyleClass().add("inputLabel");

            setLabelStyle(label);
            TextField textField = new TextField();
            textField.setStyle("-fx-alignment: center;-fx-border-radius: 10; -fx-font-size: 16");
            textField.setPromptText("Enter here");
            Button addButton = new Button("Save");
            addButton.getStylesheets().add("app/management/style/darkTheme.css");
            addButton.getStyleClass().add("inputButton");
            setButtonStyle(addButton);

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
                    textField.setStyle("-fx-text-fill: #fffa3b; -fx-background-color: transparent");
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
        if (mandatoryInputs.size() == checkHowMandatoryInputsINFreeInputsTemp())
            startExecute.setDisable(false);
    }

    private void setButtonStyle(Button addButton) {
        if (StyleManager.getCurrentTheme().equals("dark")) {
            addButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffff00; -fx-font-size: 15; -fx-border-radius: 9; -fx-border-width: 2; -fx-border-color: #3760ff;-fx-font-family: 'Roboto';-fx-alignment: center-right;");
        } else {
            addButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;-fx-border-width: 2;-fx-border-radius: 9; -fx-font-size: 15; -fx-border-color: #3760ff;-fx-font-family: 'Roboto';-fx-alignment: center-right;");
        }
        String style1 = addButton.getStyle();
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle(style1+"-fx-background-color: #e600ff; -fx-text-fill: #000000; -fx-background-radius: 10; -fx-border-width: 2;");

        });
        addButton.setOnMouseExited(e -> {
            addButton.setStyle(style1);
        });
    }

    private String getExistInInitialValue(String nameToSearchInKey){
        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValuesData()){
            if (initialInputValues.getInputName().equals(nameToSearchInKey))
                return initialInputValues.getInitialValue();
        }
        return null;
    }
    private boolean existInInitialValue(String nameToSearchInKey){
        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValuesData()){
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
                if (currentMandatoryFreeInput == null){
                    System.out.println("null");
                }
                if (currentOptionalFreeInput == null){

                }
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
        if (!isComeFromHistory)
            setTheNewInputsThatTheUserSupply();
        body.getMVC_controller().setFreeInputs(freeInputsTemp);
        showDetails.setDisable(true);
        continuation.setVisible(true);
        body.getMVC_controller().executeFlow(currentFlow);
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }
        isComeFromHistory = false;

        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setVisible(true);
        showDetails.setDisable(false);
        enablesDetails(lastFlowExecution);
        showDetails.setDisable(false);

        lastFlowExecution.isDoneProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(() -> {
                    popupDetails();
                });
            }
        });

    }
    private void enablesDetails(FlowExecution lastFlowExecution) {
        if (lastFlowExecution != null) {
            if (lastFlowExecution.isDone.get()) {
                showDetails.setDisable(false);
                showDetails.setVisible(true);
            }

        }


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

            Parent root = loader.load();
            Stage stage = new Stage();
            stages.add(stage);
            stage.setTitle("Flow Details");
            //set icon as previous stage
            stage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
            stage.setScene(new Scene(root, 1060, 365));
            stage.show();
            //disable app until the user close the window
        }catch (IllegalStateException | IOException ex) {
            VerySecretCode();
        }
    }

    private void VerySecretCode() {
        // :)
    }

    private FlowExecution getLastFlowExecution() {
        return stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size() - 1);
    }
    private void handleButtonAction(Button addButton, TextField textField, String data, String nameOfDD, Class<?> type, HBox Hbox,boolean isOptional) {
        if (addButton.getText().equals("Edit")) {
            //if
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
                    }
                }
                textField.clear();
                addButton.setText("Save");
                if (freeInputsTemp.size() != getSizeOfMandatoryList()) {
                    startExecute.setDisable(true);
                }
            }
        }
        else {
            if (!data.isEmpty() && !nameOfDD.equals("FOLDER_NAME")) {
                Boolean valid=validateInput(data, type,textField,addButton);
                if (valid) {
                    freeInputsTemp.add(new Pair<>(nameOfDD, data));
                    addButton.setText("Edit");
                }else {
                    addButton.setText("Save");
                }

            } else if (nameOfDD.equals("FOLDER_NAME")) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");
                File selectedFile = directoryChooser.showDialog(null);
                if (selectedFile != null) {
                    freeInputsTemp.add(new Pair<>(nameOfDD, selectedFile.toString()));
                    addButton.setText("Edit");
                    int index = Hbox.getChildren().indexOf(textField);
                    Label label = new Label(selectedFile.toString());
                    label.getStylesheets().add("app/management/style/darkTheme.css");
                    label.getStyleClass().add("inputLabel");
                    label.setStyle("-fx-text-fill: yellow ; -fx-font-size: 12px");
                    setLabelStyle(label);
                    Hbox.getChildren().set(index, label);
                    textField.setText(selectedFile.toString());
                }
            }
            if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()&& !isOptional) {
                startExecute.setDisable(false);
            }
        }
    }

    private void setBodyButtonStyle(Button button) {
        button.setOnMouseEntered(event ->
                button.setStyle(style+ "-fx-background-color: rgb(255,0,96); -fx-background-radius: 20;-fx-border-color: #566dff"));
        button.setOnMouseExited(event ->
                button.setStyle(style));


    }

    private boolean validateInput(String data, Class<?> type, TextField textField,Button btn) {
        if (type.equals(Integer.class)) {
            try {
                Integer.parseInt(data);
            } catch (NumberFormatException e) {
                String msg = "Please enter a number";
                setApropTooltip(textField, msg);
                return false;
            }
        } else if (type.equals(Double.class)) {
            try {
                Double.parseDouble(data);
            } catch (NumberFormatException e) {
                String msg = "Please enter a double";
                setApropTooltip(textField, msg);
                return false;
            }
        }else if (type.equals(String.class)) {
                if (data.isEmpty()) {
                    String msg = "Please enter a String";
                    setApropTooltip(textField, msg);
                    return false;
                }if (data.contains("?")) {
                    String msg = "Please enter a String without '?'";
                    setApropTooltip(textField, msg);
                    return false;
                }
            }
        return true;
    }

    private static void setApropTooltip(TextField textField, String msg) {
        Tooltip tooltip = new Tooltip(msg);
        tooltip.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #000000;");
        double tooltipX = textField.localToScreen(textField.getBoundsInLocal()).getMinX() + textField.getWidth() / 2;
        double tooltipY = textField.localToScreen(textField.getBoundsInLocal()).getMinY() - 30; // Adjust the offset as needed
        tooltip.show(textField, tooltipX, tooltipY);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> tooltip.hide());
        delay.play();
        textField.clear();
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
        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setVisible(true);
        enablesDetails(lastFlowExecution);
        showDetails.setDisable(false);

//        lastFlowExecution.isDoneProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                Platform.runLater(() -> {
//                    popupDetails();
//                });
//            }
//        });

    }
    @Override
    public void setBodyControllerContinuation(bodyController body){
        this.body = body;
    }
    @Override
    public void showFromHistory(){
        freeInputsTemp = new ArrayList<>();
        createComponentForMandatoryFromHistory();
        createComponentForOptionalFromHistory();
        startExecute.setDisable(false);
        isComeFromHistory = true;
    }
    private void createComponentForOptionalFromHistory() {
        for (Pair<String,String> optinal: freeInputsOptionalFromHistory) {
            Label label = new Label(optinal.getKey());
            label.setStyle("-fx-text-fill: white");
            HBox nameAndAddOrEdit = new HBox();
            Label data = new Label(optinal.getValue());
            data.setStyle("-fx-text-fill: white");
            nameAndAddOrEdit.getChildren().add(label);
            nameAndAddOrEdit.getChildren().add(data);
            nameAndAddOrEdit.setSpacing(5);
            optionalList.getChildren().add(nameAndAddOrEdit);
            optionalList.setSpacing(10);
            freeInputsTemp.add(optinal);
            setLabelStyle(data);
        }
    }

    private void createComponentForMandatoryFromHistory() {
        for (Pair<String,String> mandatory: freeInputsMandatoryFromHistory) {
            Label label = new Label(mandatory.getKey());
            label.setStyle("-fx-text-fill: white");
            HBox nameAndAddOrEdit = new HBox();
            Label data = new Label(mandatory.getValue());
            data.setStyle("-fx-text-fill: white");
            nameAndAddOrEdit.getChildren().add(label);
            nameAndAddOrEdit.getChildren().add(data);
            nameAndAddOrEdit.setSpacing(5);
            mandatoryList.getChildren().add(nameAndAddOrEdit);
            mandatoryList.setSpacing(10);
            freeInputsTemp.add(mandatory);
        }
    }
    @Override
    public void setBodyControllerFromHistory(bodyController body){
        this.body = body;
    }
    @Override
    public void SetCurrentFlowFromHistory(FlowDefinitionImpl flow){
        this.currentFlow = flow;
    }
    @Override
    public void setFreeInputsMandatoryAndOptional(List<Pair<String, String>> freeInputMandatory,
                                                  List<Pair<String, String>> freeInputOptional,List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD ){

        this.freeInputsMandatoryFromHistory = freeInputMandatory;
        this.freeInputsOptionalFromHistory = freeInputOptional;
        this.freeInputsMandatory = freeInputMandatory;
        this.freeInputsOptional = freeInputOptional;

        this.currentMandatoryFreeInput = freeInputsMandatoryWithDD;
        this.currentOptionalFreeInput = freeInputsOptionalWithDD;

    }
}
