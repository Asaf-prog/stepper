package app.header;

import app.management.mainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import java.io.File;

public class headerController {

    @FXML
    private Button path;
    @FXML
    private Button FlowsDefinition;

    @FXML
    private Button Statistics;

    @FXML
    private Button FlowsExecution;

    @FXML
    private Button ExecutionsHistory;
    private mainController main;


    @FXML
    void FlowsDefinitionPresent(ActionEvent event) {
        main.showFlowDefinition();

    }
    public void setMainController(mainController main){
        this.main =main;
    }
    @FXML
    void FlowsExecutionFunc(ActionEvent event) {

    }
    @FXML
    void StatisticsFunc(ActionEvent event) {

    }
    @FXML
    void loadDataXML(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        main.initialize();
        if (selectedFile != null) {
            try {
                GetDataFromXML.fromXmlFileToObject(selectedFile.getAbsolutePath());
                FlowsExecution.setDisable(false);
                FlowsDefinition.setDisable(false);
                inisilaized();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private  void inisilaized(){
        Stepper stepperData = DataManager.getData();
        main.setFlows(stepperData.getFlows());
    }
}


