package ClientsApp.app.body.executeFlow;

import ClientsApp.app.Client.Client;
import ClientsApp.app.body.bodyInterfaces.bodyControllerDefinition;
import ClientsApp.app.body.bodyController;
import ClientsApp.app.body.bodyInterfaces.bodyControllerExecuteFromHistory;
import ClientsApp.app.body.bodyInterfaces.bodyControllerForContinuation;
import ClientsApp.app.body.executeFlow.executionDetails.ExecutionsDetails;
import ClientsApp.app.management.style.StyleManager;
import Servlets.ClientServlets.login.FileName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import modules.mappings.InitialInputValues;
import modules.stepper.Stepper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.flow.DataDefinitionDeclarationDTO;
import services.stepper.other.ContinuationDTO;
import services.stepper.other.ContinuationMappingDTO;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static modules.DataManeger.DataManager.stepperData;

public class executeFlowController implements bodyControllerDefinition,bodyControllerForContinuation, bodyControllerExecuteFromHistory {
    @FXML
    private Button showDetails;
    @FXML
    private Button continuation;
    @FXML
    private Button continuationExe;
    @FXML
    private Button startExecute;
    private Map<String,Object> outputsOfLastFlow;
    @FXML
    private VBox mandatoryList;
    @FXML
    private Label continuationLabel;
    @FXML
    private  VBox optionalList;
    @FXML
    private VBox continuationVbox;
    @FXML
    private Label flowNameLabel;

    private FlowDefinitionDTO currentFlow;

    private FlowDefinitionDTO lastFlow;
    private bodyController body;
    private int sizeOfMandatoryList;
    private List<Pair<String, String>> freeInputsTemp;
    private List<Pair<String, String>>  freeInputsTempForContinuation = new ArrayList<>();
    private List<Pair<String, String>> freeInputsMandatory ;
    private List<Pair<String, String>> freeInputsOptional;
    private List<Pair<String, DataDefinitionDeclarationDTO>> currentMandatoryFreeInput;
    private List<Pair<String, DataDefinitionDeclarationDTO>> currentOptionalFreeInput;
    private List<Pair<String, String>> freeInputsMandatoryFromHistory;
    private List<Pair<String, String>> freeInputsOptionalFromHistory;
    private boolean isComeFromHistory =false;
    private String style;
    List<Stage> stages = new ArrayList<>();
    private Client client;
    private List<String> fileName;// list of all the data definition that type of File
    private Gson gson = new Gson();
    private int indexOfLabel;
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

        //get free inputs from server with init input
        //first of all, create a two list : mandatoryInputs and optionalInputs:

        currentFlow=body.getCurrentFlow();
        List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs = currentFlow.getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclarationDTO>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclarationDTO>> optionalInputs = new ArrayList<>();

        for (Pair<String, DataDefinitionDeclarationDTO> pair : freeInputs) {
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
        //todo 3 need to move into servlet

    }
    @Override
    public void showForContinuation() {
        //todo 3 need to move into servlet
        continuationExe.setVisible(true);
        continuationExe.setDisable(true);
        startExecute.setVisible(false);
        flowNameLabel.setText("Collect Input For Flow : "+getCurrentFlow().getName());
        //first, create a list of mandatory and optional that the user need to supply
        //secondly, create the component
        ContinuationDTO continuation1 = null;//=> fulfil the data of the mapping to the flow
        for (ContinuationDTO continuationToThisFlow: lastFlow.getContinuations()){
            if (continuationToThisFlow.getTargetFlow().equals(currentFlow.getName()))
                continuation1 = continuationToThisFlow;
        }
        createVboxMandatoryAndOptionalWithInitValue(continuation1);
    }
    private void createVboxMandatoryAndOptionalWithInitValue(ContinuationDTO continuation){
        List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs = getCurrentFlow().getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclarationDTO>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclarationDTO>> optionalInputs = new ArrayList<>();

        for (Pair<String, DataDefinitionDeclarationDTO> pair : freeInputs) {
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
    private void mandatoryHandlerForContinuation(ContinuationDTO continuation,List<Pair<String, DataDefinitionDeclarationDTO>> mandatoryInputs){
        for (Pair<String, DataDefinitionDeclarationDTO> mandatory:mandatoryInputs){
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
                        mandatory.getKey(), mandatory.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit,false));
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
            }
            nameAndAddOrEdit.setSpacing(5);
            mandatoryList.getChildren().add(nameAndAddOrEdit);
        }

        if (checkHowMandatoryInputsINFreeInputsTemp() == mandatoryInputs.size() || mandatoryInputs.size() == freeInputsTemp.size())
            continuationExe.setDisable(false);
        mandatoryList.setSpacing(10);
    }
    private String getDataThatSupplyAndUpdateTheListOfFreeInputsForOptional(ContinuationDTO continuation,String nameToSearch){
        if (existInInitialValue(nameToSearch)){
            return getExistInInitialValue(nameToSearch);
        }
        else if (customMappingWithContinuation(nameToSearch,continuation)){//let search this data in the outputs of last flow
            for(ContinuationMappingDTO mapping: continuation.getMappingList()){
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
    private String getDataThatSupplyAndUpdateTheListOfFreeInputs(ContinuationDTO continuation,String nameToSearch){

        if (existInInitialValue(nameToSearch)) {
            String data = getData(nameToSearch);
            freeInputsTemp.add(new Pair<>(nameToSearch,data));
            return getExistInInitialValue(nameToSearch);
        }
        else if (customMappingWithContinuation(nameToSearch,continuation)){//let search this data in the outputs of last flow
            for(ContinuationMappingDTO mapping: continuation.getMappingList()){
                if (mapping.getTargetData().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,outputsOfLastFlow.get(mapping.getSourceData()).toString()));//add data to list of data
                    return outputsOfLastFlow.get(mapping.getSourceData()).toString();
                }
            }
        } else if (thisDataSupplyByFreeInputsOfLastFlow(nameToSearch,continuation)) {

            for (Pair<String, String> lastMandatory:freeInputsMandatory) {
                if (lastMandatory.getKey().equals(nameToSearch)) {
                    freeInputsTemp.add(new Pair<>(nameToSearch,lastMandatory.getValue()));//add data to list of data
                    return lastMandatory.getValue();
                }
            }
        }
        return null;
    }
    private String getData(String nameToSearch){
//        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValuesData()){
//            if (initialInputValues.getInputName().equals(nameToSearch))
//                return initialInputValues.getInitialValue();
//        }
        //also need to move to the server
        return null;
    }
    private boolean thisDataSupplyByRecentFlow(String nameToSearch,ContinuationDTO continuation){
        if(outputsOfLastFlow.containsKey(nameToSearch) || thisDataSupplyByFreeInputsOfLastFlow(nameToSearch,continuation)||
                customMappingWithContinuation(nameToSearch,continuation)||existInInitialValue(nameToSearch))
            return true;
        else
            return false;
    }
    private boolean thisDataSupplyByRecentFlowInOptional(String nameToSearch,ContinuationDTO continuation){
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
    private boolean customMappingWithContinuation(String nameToSearch,ContinuationDTO continuation){

        for(ContinuationMappingDTO mapping: continuation.getMappingList()){
            if (mapping.getTargetData().equals(nameToSearch))
                return true;
        }
        return false;
    }
    private boolean thisDataSupplyByFreeInputsOfLastFlow(String nameToSearch,ContinuationDTO continuation){
        for (Pair<String, DataDefinitionDeclarationDTO> lastMandatory: currentMandatoryFreeInput){
            if (lastMandatory.getKey().equals(nameToSearch) ) {
                return true;
            }

        }
        return false;
    }
    private void optionalHandlerForContinuation(ContinuationDTO continuation,List<Pair<String, DataDefinitionDeclarationDTO>>optionalInputs ){
        for (Pair<String, DataDefinitionDeclarationDTO> optional:optionalInputs){
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
                        optional.getKey(), optional.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit,true));
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
    public void SetCurrentFlow(FlowDefinitionDTO flow) {
        currentFlow = flow;
    }
    public FlowDefinitionDTO getCurrentFlow() {
        return currentFlow;
    }

    private boolean existInData(String nameOfDD) {
        for (Pair<String, DataDefinitionDeclarationDTO> run : currentOptionalFreeInput) {
            if (run.getKey().equals(nameOfDD))
                return true;
        }
        for (Pair<String, DataDefinitionDeclarationDTO> run : currentMandatoryFreeInput) {
            if (run.getKey().equals(nameOfDD))
                return true;
        }
        return false;
    }
    @Override
    public void setCurrentFlowForContinuation(FlowDefinitionDTO flow) {
        currentFlow = flow;
    }

    @Override
    public void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclarationDTO>> mandatory, List<Pair<String, DataDefinitionDeclarationDTO>> optional
            ,List<Pair<String, String>>mandatoryIn,
                                               List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl lastFlow) {
        currentMandatoryFreeInput = mandatory;
        currentOptionalFreeInput = optional;
        freeInputsMandatory = mandatoryIn;
        freeInputsOptional = optionalIn;
        outputsOfLastFlow = outputs;
//        this.lastFlow = lastFlow;
        //todo remove when moving to server
    }
    private List<Pair<String, DataDefinitionDeclarationDTO>> getCurrentMandatoryFreeInput() {
        return currentMandatoryFreeInput;
    }

    private List<Pair<String, DataDefinitionDeclarationDTO>> getCurrentOptionalFreeInput() {
        return currentOptionalFreeInput;
    }
    private void optionalHandler(List<Pair<String, DataDefinitionDeclarationDTO>> optionalInputs) {
        for (Pair<String, DataDefinitionDeclarationDTO> optional : optionalInputs) {
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
                        optional.getKey(), optional.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit, true));

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
    private void createListOfFreeInputFromTypeFile(){
        String finalUrl = HttpUrl
                .parse(ClientConstants.FILE_NAME)
                .newBuilder()
                .addQueryParameter("flowName", currentFlow.getName())
                .build()
                .toString();
        //dummy body
        // RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() -> {//general error
                            String msg = "Please enter a valid input";
                        });
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() == 200) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String bodyRes = responseBody.string();
                                List<String> roles = gson.fromJson(bodyRes, new TypeToken<List<String>>() {
                                }.getType());
                            } else {//code 422

                            }
                        }
                    }
                }
        );
    }
    private void createListOfFreeInputFromFileName(){
        fileName = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclarationDTO>>  freeInput = getCurrentFlow().getFlowFreeInputs();
        for (Pair<String, DataDefinitionDeclarationDTO> free : freeInput){
            if (existInFileName(free.getKey())){
                fileName.add(free.getKey());
            }
        }
    }
    private boolean existInFileName(String name2Search){
        for (FileName n : FileName.values()){
            if (n.name().equalsIgnoreCase(name2Search))
                return true;
        }
        return false;
    }
    private void mandatoryHandler(List<Pair<String, DataDefinitionDeclarationDTO>> mandatoryInputs) {
        //createListOfFreeInputFromTypeFile();
        createListOfFreeInputFromFileName();
        for (Pair<String, DataDefinitionDeclarationDTO> mandatory : mandatoryInputs) {
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
                if (mandatory.getKey().equals("FOLDER_NAME") || existInFileName(mandatory.getKey())) {
                    textField.setEditable(false);
                    addButton.setText("Browse");
                    textField.setText("FOLDER_PATH");
                    textField.setStyle("-fx-text-fill: #fffa3b; -fx-background-color: transparent");
                    nameAndAddOrEdit.getChildren().add(textField);
                    nameAndAddOrEdit.getChildren().add(addButton);
                    nameAndAddOrEdit.setSpacing(10);
                } else if (mandatory.getKey().equals("OPERATION")) {
                    ToggleGroup toggleGroup = new ToggleGroup();
                    HBox hBoxToToggel = new HBox();
                    textField.setVisible(false);
                    RadioButton radioButtonZip = new RadioButton("ZIP");
                    radioButtonZip.setToggleGroup(toggleGroup);
                    radioButtonZip.setOnAction(event -> handleButtonAction(addButton, textField,"ZIP",
                            mandatory.getKey(), mandatory.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit,false));
                    radioButtonZip.setStyle("-fx-text-fill: #fffa3b; -fx-background-color: transparent");
                    RadioButton radioButtonUnZip = new RadioButton("UNZIP");
                    radioButtonUnZip.setToggleGroup(toggleGroup);
                    radioButtonUnZip.setOnAction(event -> handleButtonAction(addButton, textField, "UNZIP",
                            mandatory.getKey(), mandatory.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit,false));

                    radioButtonUnZip.setStyle("-fx-text-fill: #fffa3b; -fx-background-color: transparent");
                    hBoxToToggel.getChildren().addAll(radioButtonZip,radioButtonUnZip);
                    hBoxToToggel.setSpacing(10);
                    nameAndAddOrEdit.getChildren().add(hBoxToToggel);

                } else {
                    nameAndAddOrEdit.getChildren().add(textField);
                    nameAndAddOrEdit.getChildren().add(addButton);
                    nameAndAddOrEdit.setSpacing(10);
                }
                addButton.setOnAction(e -> handleButtonAction(addButton, textField, textField.getText(),
                        mandatory.getKey(), mandatory.getValue().getDataDefinition().getTypeName(), nameAndAddOrEdit,false));
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
        for (InitialInputValues initialInputValues: currentFlow.getORIGINALInitialInputValuesData()){
            if (initialInputValues.getInputName().equals(nameToSearchInKey))
                return initialInputValues.getInitialValue();
        }
//        todo 4
        return null;
    }
    private boolean existInInitialValue(String nameToSearchInKey){
//        for (InitialInputValues initialInputValues: currentFlow.getInitialInputValuesData()){
//            if (initialInputValues.getInputName().equals(nameToSearchInKey))
//                return true;
//        }
        //todo 5 same as 4
        return false;
    }

    @FXML
    void startContinuationAfterGetFreeInputs(ActionEvent event) {
//        continuationVbox.getChildren().clear();
//        ToggleGroup group = new ToggleGroup();
//        for (Continuation continuation : currentFlow.getContinuations()) {
//            RadioButton button = new RadioButton(continuation.getTargetFlow());
//            button.setStyle("-fx-text-fill: white");
//            button.setOnAction(e -> {
//                try {
//                    handleButtonActionForContinuation(continuation.getTargetFlow());
//                } catch (Exception ex) {
//                    System.out.println("Basa10");
//                }
//            });
//            button.setToggleGroup(group);
//            continuationVbox.getChildren().add(button);
//        }
//        continuationVbox.setSpacing(10);
//        continuationVbox.setVisible(true);
//        continuationLabel.setText("Continuation for " + currentFlow.getName());
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
                setTheNewInputsThatTheUserSupply();
                //todo remove // before adding all this to server side
                //  body.handlerContinuation(targetFlow, currentMandatoryFreeInput, currentOptionalFreeInput,freeInputsMandatory,freeInputsOptional,outputs,targetFlow);
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
        for (int i = flowExecutions.size() - 1; i >= 0; i--) {
            FlowExecution flowExecution = flowExecutions.get(i);
            if (flowExecution.getFlowDefinition().getName().equals(nameOfCurrentFlow)) {
                return flowExecution;
            }
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
        currentFlow.setUserInputs(freeInputsTemp);
        showDetails.setDisable(true);
        continuation.setVisible(true);
        body.getMVC_controller().executeFlow(currentFlow);
        //todo 6 same as 4
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }
        isComeFromHistory = false;
        showDetails.setVisible(true);
        showDetails.setDisable(false);
        enablesDetails();
        showDetails.setDisable(false);

    }
    private void enablesDetails() {
        showDetails.setDisable(false);
        showDetails.setVisible(true);
    }
    private void setTheNewInputsThatTheUserSupply(){
        freeInputsMandatory = new ArrayList<>();
        freeInputsOptional = new ArrayList<>();
        for (Pair<String, String> data : freeInputsTemp){
            for (Pair<String, DataDefinitionDeclarationDTO> run: currentMandatoryFreeInput){
                if (run.getKey().equals(data.getKey()))
                    freeInputsMandatory.add(new Pair<>(data.getKey(),data.getValue()));
            }
            for (Pair<String, DataDefinitionDeclarationDTO> run: currentOptionalFreeInput){
                if (run.getKey().equals(data.getKey()))
                    freeInputsOptional.add(new Pair<>(data.getKey(),data.getValue()));
            }
        }
    }
    private void popupDetails() {

        showDetails.setDisable(false);
        showDetails.setVisible(true);
    }
    private void handleButtonActionForShowDetails() {
        String id =getLastExecutionId();
        FlowEnded(id);
    }

    private String getLastExecutionId() {
        return body.getMVC_controller().getLastExecutionId();
    }

    private boolean FlowEnded(String id) {
        //send to server the request
        Request request = new Request.Builder()
                .url(ClientConstants.FLOW_ENDED)
                .get()
                .addHeader("flowId", id)
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("fail");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        showExecutionDetails(id);
                    });
                } else {
                    System.out.println("did not finish yet");
                }
                response.close();

            }
        });
        return true;
    }
    private void showExecutionDetails(String id) {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/ClientsApp/app/body/executeFlow/executionDetails/ExecutionsDetails.fxml"));
        // loader.setController(new ExecutionsDetails(id));
        loader.setControllerFactory(controllerClass -> {
            return new ExecutionsDetails(id);

        });
        try {

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Flow Details");
            //set icon as previous stage
            stage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
            stage.setScene(new Scene(root, 1060, 365));
            stage.show();
            //disable app until the user close the window
        } catch (IllegalStateException | IOException ex) {
            VerySecretCode();
            //todo remove@!!!
            ex.printStackTrace();
        }
    }
    private void VerySecretCode() {
        // :)
    }

    private FlowExecution getLastFlowExecution() {
        if (stepperData == null)
            stepperData = DataManager.getData();
        if (stepperData.getFlowExecutions()!= null && stepperData.getFlowExecutions().size() != 0)
            return stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size() - 1);
        return null;
    }
    private boolean nameOfDDExistInListOfFreeInputChangeData(String nameOfDD,String data){
        for (Pair<String, String> pair:freeInputsTemp){
            if (pair.getKey().equals(nameOfDD)) {
                freeInputsTemp.remove(pair);
                freeInputsTemp.add(new Pair<>(nameOfDD,data));
                return true;
            }
        }
        return false;// the data definition is  not exist
    }
    private void handleButtonAction(Button addButton, TextField textField, String data, String nameOfDD, String type, HBox Hbox,boolean isOptional) {

        if (nameOfDD.equals("OPERATION"))
            handleOperation(addButton, data, nameOfDD);
        else {
            if (addButton.getText().equals("Edit")) {
                if (nameOfDD.equals("FOLDER_NAME") || existInFileName(nameOfDD)) {
                    if (nameOfDD.equals("FOLDER_NAME")){
                        handleEditFolderName(Hbox);
                    } else
                        editTheDataDefinitionThatWeWantToChange(addButton, textField, nameOfDD, Hbox);

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
            } else {//equals to save, so it's the first time that we save the data of the user
                if (!data.isEmpty() && !(nameOfDD.equals("FOLDER_NAME") || existInFileName(nameOfDD))) {
                    validateInput(data, type,textField,addButton,nameOfDD);

                } else if (nameOfDD.equals("FOLDER_NAME")|| existInFileName(nameOfDD)) {
                    if (nameOfDD.equals("FOLDER_NAME")){//popUp For The Name Of The File

                        firstTimeLoadFolderName(addButton, textField, nameOfDD, Hbox);
                    }else {
                        firstTimeLOadPathOfFileThatWeWillGoingToCreate(addButton, textField, nameOfDD, Hbox,isOptional);
                    }
                }
                if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()&& !isOptional)
                    startExecute.setDisable(false);
            }
        }
    }
    private void firstTimeLOadPathOfFileThatWeWillGoingToCreate(Button addButton, TextField textField, String nameOfDD, HBox Hbox,boolean isOptional) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selectedFile = directoryChooser.showDialog(null);

        if (selectedFile != null)
            updateUserInterfaceWithTheNewPath(addButton, textField, nameOfDD, Hbox, selectedFile,isOptional);
        if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()&& !isOptional)
            startExecute.setDisable(false);
    }
    private void updateUserInterfaceWithTheNewPath(Button addButton, TextField textField, String nameOfDD, HBox Hbox, File selectedFile,boolean isOptional) {
        Platform.runLater(() -> {
            // Alert alert = new Alert(Alert.AlertType.INFORMATION);
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Name Of File Dialog");
            dialog.setContentText("Name Of File:");

            // Show the input dialog and wait for user input
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                // Here, you can use the user's name in your application
                System.out.println("User's name: " + name);
                String fullPath = selectedFile.toString() +File.separator+ name;
                freeInputsTemp.add(new Pair<>(nameOfDD, fullPath));

                addButton.setText("Edit");
                int index = Hbox.getChildren().indexOf(textField);
                indexOfLabel=index;
                // Label label = new Label(selectedFile.toString());
                Label label = new Label(fullPath);
                label.getStylesheets().add("app/management/style/darkTheme.css");
                label.getStyleClass().add("inputLabel");
                label.setStyle("-fx-text-fill: yellow ; -fx-font-size: 12px");
                setLabelStyle(label);
                Hbox.getChildren().set(index, label);
                //textField.setText(selectedFile.toString());
                textField.setText(fullPath);
                checkUpdate(isOptional);
            });
        });
    }
    private void checkUpdate(boolean isOptional) {
        if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()&& !isOptional)
            startExecute.setDisable(false);
    }
    private void firstTimeLoadFolderName(Button addButton, TextField textField, String nameOfDD, HBox Hbox) {
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
    private void editTheDataDefinitionThatWeWantToChange(Button addButton, TextField textField, String nameOfDD, HBox Hbox) {
        Platform.runLater(() -> {
            addButton.setText("Save");
            for (Pair<String, String> pair : freeInputsTemp) {
                if (pair.getKey().equals(nameOfDD)) {
                    freeInputsTemp.remove(pair);
                    break;
                }
            }
            // textField.clear();
            addButton.setText("Save");
            if (freeInputsTemp.size() != getSizeOfMandatoryList()) {
                startExecute.setDisable(true);
            }
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Directory");
            File selectedFile = directoryChooser.showDialog(null);

            if (selectedFile != null) {

                // Alert alert = new Alert(Alert.AlertType.INFORMATION);
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Name Of File Dialog");
                dialog.setContentText("Name Of File:");

                // Show the input dialog and wait for user input
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    // Here, you can use the user's name in your application
                    System.out.println("User's name: " + name);
                    String fullPath = selectedFile.toString() + File.separator + name;
                    freeInputsTemp.add(new Pair<>(nameOfDD, fullPath));

                    addButton.setText("Edit");
                    //int index = Hbox.getChildren().indexOf(Label);//**
                    // Label label = new Label(selectedFile.toString());
                    Label label = new Label(fullPath);
                    label.getStylesheets().add("app/management/style/darkTheme.css");
                    label.getStyleClass().add("inputLabel");
                    label.setStyle("-fx-text-fill: yellow ; -fx-font-size: 12px");
                    setLabelStyle(label);
                    Hbox.getChildren().set( indexOfLabel,label);
                    //textField.setText(selectedFile.toString());
                    textField.setText(fullPath);
                });
            }
        });
    }
    private void handleEditFolderName(HBox Hbox) {
        String data;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            int index = getIndexOfLabel(Hbox);
            Label label = (Label) Hbox.getChildren().get(index);
            label.setText(selectedDirectory.getAbsolutePath());
            data = selectedDirectory.getAbsolutePath();//already valid
        }
    }
    private void handleOperation(Button addButton, String data, String nameOfDD) {
        if (nameOfDDExistInListOfFreeInputChangeData(nameOfDD, data)){
            //we change to exist data-definition from zip to unzip and vice versa
            if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()) {
                startExecute.setDisable(false);
            }
        } else {
            freeInputsTemp.add(new Pair<>(nameOfDD, data));
            addButton.setText("Edit");
            if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()) {
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
    private void validateInput(String data, String type, TextField textField, Button btn, String nameOfDD) {

        //build req for valid input servlet
        String finalUrl = HttpUrl
                .parse(ClientConstants.VALIDATE_INPUT)
                .newBuilder()
                .addQueryParameter("userInput", data)
                .addQueryParameter("dataType", type)
                .build()
                .toString();
        //dummy body
        RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() -> {//general error
                            String msg = "Please enter a valid input";
                            setApropTooltip(textField, msg);
                            btn.setText("Save");
                        });
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() == 200) {
                            Platform.runLater(() -> {
                                freeInputsTemp.add(new Pair<>(nameOfDD, data));
                                btn.setText("Edit");
                                if (checkHowMandatoryInputsINFreeInputsTemp() == getSizeOfMandatoryList()) {
                                    startExecute.setDisable(false);
                                }
                            });
                        } else {//code 422
                            Platform.runLater(() -> {
                                //get msg from response
                                String msg = response.header("message");
                                setApropTooltip(textField, msg);
                                btn.setText("Save");
                            });
                        }
                    }
                });

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
            for (Pair<String, DataDefinitionDeclarationDTO> mandatory: currentMandatoryFreeInput){
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
    public void setFlowsDetails(List<FlowDefinitionDTO> list) {

    }
    public void showDetails(ActionEvent actionEvent){
        showExecutionDetails(body.getMVC_controller().getLastExecutionId());
    }
    @FXML
    void ContinuationExecution(ActionEvent event) {
        //body.getMVC_controller().setFreeInputs(freeInputsTemp);
        //todo send to server the free inputs and run the flow
        
        // body.getMVC_controller().executeFlow(currentFlow);
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }
        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setVisible(true);
        // enablesDetails();
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
    public void SetCurrentFlowFromHistory(FlowDefinitionDTO flow){
        this.currentFlow = flow;
    }
    @Override
    public void setFreeInputsMandatoryAndOptional(List<Pair<String, String>> freeInputMandatory,
                                                  List<Pair<String, String>> freeInputOptional,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsMandatoryWithDD
            ,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsOptionalWithDD ){
        this.freeInputsMandatoryFromHistory = freeInputMandatory;
        this.freeInputsOptionalFromHistory = freeInputOptional;
        this.freeInputsMandatory = freeInputMandatory;
        this.freeInputsOptional = freeInputOptional;

        this.currentMandatoryFreeInput = freeInputsMandatoryWithDD;
        this.currentOptionalFreeInput = freeInputsOptionalWithDD;
    }
    @Override
    public void setClient(Client client){
        this.client = client;
    }
}
