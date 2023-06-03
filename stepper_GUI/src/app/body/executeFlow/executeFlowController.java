package app.body.executeFlow;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.executeFlow.executionDetails.ExecutionsDetails;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.mappings.Continuation;
import modules.step.api.DataDefinitionDeclaration;
import static modules.DataManeger.DataManager.stepperData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class executeFlowController implements bodyControllerDefinition {


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

            addButton.setOnAction(e->handleButtonAction(addButton,textField,textField.getText(),optional.getKey(),optional.getValue().dataDefinition().getType(),nameAndAddOrEdit));
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
                addButton.setText("Browse");
                textField.setText("FOLDER_PATH");
                textField.setStyle("-fx-text-fill: #ffffff; -fx-background-color: transparent");
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
                nameAndAddOrEdit.setSpacing(10);
            }else {
                nameAndAddOrEdit.getChildren().add(textField);
                nameAndAddOrEdit.getChildren().add(addButton);
                nameAndAddOrEdit.setSpacing(10);
            }


            addButton.setOnAction(e->handleButtonAction(addButton,textField,textField.getText(),mandatory.getKey(),mandatory.getValue().dataDefinition().getType(),nameAndAddOrEdit));
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
            button.setOnAction(e-> handleButtonActionForContinuation(continuation.getTargetFlow()));
            button.setToggleGroup(group);
            continuationVbox.getChildren().add(button);
        }
        continuationVbox.setSpacing(10);
        continuationVbox.setVisible(true);
        continuationLabel.setText("Continuation for "+ currentFlow.getName());
    }
    private void handleButtonActionForContinuation(String nameOfTargetFlow){
        //todo this function handling the continuation
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
        if (currentFlow.getContinuations().size() != 0) {
            continuation.setDisable(false);
        }

        //pops out flows Details...
        // todo here import stepper and get last execution then add listener to isDone prop and when it's true then show details button
        FlowExecution lastFlowExecution = getLastFlowExecution();
        showDetails.setDisable(false);
        lastFlowExecution.isDoneProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                showDetails.setVisible(true);
            }
        });

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
            stage.setScene(new Scene(root, 1060, 365));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FlowExecution getLastFlowExecution() {
        return stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size()-1);
    }

    private void handleButtonAction(Button addButton,TextField textField,String data,String nameOfDD,Class<?> type, HBox Hbox) {
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
        }
        else {
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
                Hbox.getChildren().set(index,label);
                textField.setText(selectedFile.toString());
            }
            if (freeInputsTemp.size() == getSizeOfMandatoryList()) {
                startExecute.setDisable(false);
                startExecute.setOnMouseEntered(event -> startExecute.setStyle("-fx-background-color: #36e6f3;"));
                startExecute.setOnMouseExited(event -> startExecute.setStyle("-fx-background-color: rgba(255,255,255,0);"));
            }
        }
    }

    private int getIndexOfLabel(HBox hbox) {
        int index = 0;
        for (Node node : hbox.getChildren()){
            if (node instanceof Label){
                index = hbox.getChildren().indexOf(node);
            }
        }
        return index;
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

    public void showDetails(ActionEvent actionEvent) {
        showExecutionDetails();
    }
}
