package app.body.executeFlow;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.executeFlow.executionDetails.ExecutionsDetails;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import app.body.bodyControllerForContinuation;
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
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import static modules.DataManeger.DataManager.stepperData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class executeFlowController implements bodyControllerDefinition,bodyControllerForContinuation {
    @FXML
    private Button showDetails;
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
    }

    private void asserts() {
        assert showDetails != null : "fx:id=\"showDetails\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert startExecute != null : "fx:id=\"startExecute\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert mandatoryList != null : "fx:id=\"mandatoryList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert optionalList != null : "fx:id=\"optionalList\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuation != null : "fx:id=\"continuation\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationVbox != null : "fx:id=\"continuationVbox\" was not injected: check your FXML file 'executeFlowController.fxml'.";
        assert continuationLabel != null : "fx:id=\"continuationLabel\" was not injected: check your FXML file 'executeFlowController.fxml'.";
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
        mandatoryHandler(mandatoryInputs);
        optionalHandler(optionalInputs);
        setSizeOfMandatoryList(mandatoryInputs.size());
        currentMandatoryFreeInput = mandatoryInputs;
        currentOptionalFreeInput = optionalInputs;

    }
    @Override
    public void showForContinuation() {
        //first, create a list of mandatory and optional that the user need to supply
        //secondly, create the component
        List<Pair<String, DataDefinitionDeclaration>> needToSupply = createListThatTheUserNeedToSupply();
        handelMandatoryForContinuationCreateLabelForInput(needToSupply);
        fillDataForExecuteFlow(needToSupply);

    }
    private void fillDataForExecuteFlow(List<Pair<String, DataDefinitionDeclaration>> needToSupply){
        for (Pair<String, DataDefinitionDeclaration> data: needToSupply){
            if (data.getValue().isMandatory()){
                TextField textField = new TextField();

                Button addButton = new Button("Save");
                HBox nameAndAddOrEdit = new HBox();

                textField.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        event.consume();
                    }
                });
                addButton.setOnAction(e-> handelButtonForNewDataForContinuation());
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
                nameAndAddOrEdit.setSpacing(10);
                nameAndAddOrEdit.setPrefWidth(mandatoryList.getPrefWidth());

                textField.setPromptText(data.getValue().getUserString());
                //mandatoryList.getChildren().add(label);
                mandatoryList.getChildren().add(nameAndAddOrEdit);
                mandatoryList.setSpacing(10);
            }
            else{//is optional

            }
        }

    }
    private void handelButtonForNewDataForContinuation(){

    }

    private void handelMandatoryForContinuationCreateLabelForInput(List<Pair<String, DataDefinitionDeclaration>> needToSupply) {

        List<Pair<String, DataDefinitionDeclaration>> thisDataExist = dataThatSupplyByUser(needToSupply);

        for (Pair<String, DataDefinitionDeclaration> mandatory: thisDataExist){
            if (mandatory.getValue().isMandatory()){
                Label labelDetails = new Label(mandatory.getKey());
                labelDetails.setStyle("-fx-text-fill: #ffffff");
                String dataThatCollect =getDataBYName(mandatory.getKey(),true);
                Label label = new Label(dataThatCollect);
               mandatoryList.getChildren().add(labelDetails);
                mandatoryList.getChildren().add(label);
            }
            else {
                Label labelDetails = new Label(mandatory.getKey());
                labelDetails.setStyle("-fx-text-fill: #ffffff");
                Label label = new Label(getDataBYName(mandatory.getKey(),true));
                optionalList.getChildren().add(labelDetails);
                optionalList.getChildren().add(label);
            }
        }
    }
    private String getDataBYName(String dataToSearch, boolean isMandatory){
        // this function search if the target exist or if this continuation is with same name
        if (isMandatory){
            for (Pair<String,String>mandatory:freeInputsMandatory){
                if (mandatory.getKey().equals(dataToSearch) || thisNameIsACustomMappingContinuation(dataToSearch,mandatory.getKey())) {
                    freeInputsTempForContinuation.add(mandatory);
                    return mandatory.getValue();
                }
            }
            return null;
        }
        else {
            for (Pair<String,String>optional:freeInputsOptional){
                if (optional.getKey().equals(dataToSearch)) {
                    freeInputsTempForContinuation.add(optional);
                    return optional.getValue();
                }
            }
            return null;
        }
    }
    private boolean thisNameIsACustomMappingContinuation(String dataToSearch,String dataThatExist){
        List<Continuation> continuations = currentFlow.getContinuations();
        for (Continuation continuation1: continuations){
//           if (continuation1.getMappingList())
                return true;
        }


        return false;
    }
    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow) {
        currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow() {
        return currentFlow;
    }

    private List<Pair<String, DataDefinitionDeclaration>> dataThatSupplyByUser(List<Pair<String, DataDefinitionDeclaration>> needToSupply) {
        List<Pair<String, DataDefinitionDeclaration>> thisDataExist = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> run : getCurrentFlow().getFlowFreeInputs()) {
            for (Pair<String, DataDefinitionDeclaration> runOfSupply : needToSupply) {
                if (run.getKey().equals(runOfSupply.getKey()))
                    thisDataExist.add(run);
            }
        }
        return thisDataExist;
    }

    private List<Pair<String, DataDefinitionDeclaration>> createListThatTheUserNeedToSupply() {
        List<Pair<String, DataDefinitionDeclaration>> needToSupply = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> inputThatTheFlowNeeded = getCurrentFlow().getFlowFreeInputs();
        for (Pair<String, DataDefinitionDeclaration> run : inputThatTheFlowNeeded) {
            if (!existInData(run.getKey()))
                needToSupply.add(run);
        }
        return needToSupply;
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
                                               List<Pair<String, String>>optionalIn) {
        currentMandatoryFreeInput = mandatory;
        currentOptionalFreeInput = optional;
        freeInputsMandatory = mandatoryIn;
        freeInputsOptional = optionalIn;
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
            nameAndAddOrEdit.getChildren().add(textField);
            nameAndAddOrEdit.getChildren().add(addButton);
            nameAndAddOrEdit.setSpacing(10);
            nameAndAddOrEdit.setPrefWidth(optionalList.getPrefWidth());
            addButton.setOnAction(e -> handleButtonAction(addButton, textField, textField.getText(), optional.getKey(), optional.getValue().dataDefinition().getType(), nameAndAddOrEdit));

            textField.setPromptText(optional.getValue().getUserString());
            optionalList.getChildren().add(label);
            optionalList.getChildren().add(nameAndAddOrEdit);
            optionalList.setSpacing(10);
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
            addButton.setOnAction(e -> handleButtonAction(addButton, textField, textField.getText(), mandatory.getKey(), mandatory.getValue().dataDefinition().getType(), nameAndAddOrEdit));
            nameAndAddOrEdit.setPrefWidth(mandatoryList.getPrefWidth());
            textField.setPromptText(mandatory.getValue().getUserString());
            mandatoryList.getChildren().add(label);
            mandatoryList.getChildren().add(nameAndAddOrEdit);
            mandatoryList.setSpacing(10);
        }
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

            body.handlerContinuation(targetFlow, currentMandatoryFreeInput, currentOptionalFreeInput,freeInputsMandatory,freeInputsOptional);
        } else
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
    private void handleButtonAction(Button addButton, TextField textField, String data, String nameOfDD, Class<?> type, HBox Hbox) {
        if (addButton.getText().equals("Edit")) {
            if (nameOfDD.equals("FOLDER_NAME")) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");
                File selectedDirectory = directoryChooser.showDialog(null);
                if (selectedDirectory != null) {
                    int index = getIndexOfLabel(Hbox);
                    Label label = (Label) Hbox.getChildren().get(index);
                    label.setText(selectedDirectory.getAbsolutePath());
                    // textField.setText(selectedDirectory.getAbsolutePath());
                    data = selectedDirectory.getAbsolutePath();
                }
            } else {
                addButton.setText("Save");
                for (Pair<String, String> pair : freeInputsTemp) {
                    if (pair.getKey().equals(nameOfDD)) {
                        freeInputsTemp.remove(pair);
                        if (freeInputsTemp.isEmpty()) {
                            //  handleButtonActionForEdit(addButton,textField,data,nameOfDD);
                            break;
                        }
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
            if (freeInputsTemp.size() == getSizeOfMandatoryList()) {
                startExecute.setDisable(false);
                startExecute.setOnMouseEntered(event ->
                        startExecute.setStyle("-fx-background-color: #36e6f3;"));
                startExecute.setOnMouseExited(event ->
                        startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
            }
        }
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

    private void handleButtonActionForEdit(Button addButton, TextField textField, String data, String nameOfDD) {
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
    public void showDetails(ActionEvent actionEvent){
        showExecutionDetails();
    }
}
