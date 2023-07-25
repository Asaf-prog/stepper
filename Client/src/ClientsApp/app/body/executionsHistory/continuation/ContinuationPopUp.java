package ClientsApp.app.body.executionsHistory.continuation;

import ClientsApp.app.body.bodyController;
import ClientsApp.app.management.style.StyleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import services.stepper.flow.DataDefinitionDeclarationDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static modules.DataManeger.DataManager.stepperData;

public class ContinuationPopUp {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button runButton;
    @FXML
    private VBox flowsVbox;
    private List<Pair<String, DataDefinitionDeclarationDTO>> currentMandatoryFreeInput;
    private List<Pair<String, DataDefinitionDeclarationDTO>> currentOptionalFreeInput;
    private List<Pair<String, String>> freeInputsMandatory ;
    private List<Pair<String, String>> freeInputsOptional;
    private bodyController body;
    private List<String>  targetFlows;
    private  Stage  stage;
    private FlowDefinitionDTO targetFlow = null;
    private FlowExecutionDTO pickedExecution;
    private String buttonStyle;
    @FXML
    void initialize() {
        setTheme();
        assert runButton != null : "fx:id=\"runButton\" was not injected: check your FXML file 'ContinuationPopUp.fxml'.";
        assert flowsVbox != null : "fx:id=\"flowsVbox\" was not injected: check your FXML file 'ContinuationPopUp.fxml'.";
        String style=flowsVbox.getStyle();
        flowsVbox.setStyle(style+"-fx-spacing: 10;-fx-alignment: center; -fx-padding: 10;");
        runButton.setDisable(true);
        ToggleGroup group = new ToggleGroup();
        for (String flowName : targetFlows) {
            RadioButton flowButton = new RadioButton(flowName);
            flowButton.setToggleGroup(group);
            flowButton.setStyle("-fx-font-size: 18px; -fx-text-fill: white;-fx-pref-width: 600;-fx-alignment: center;-fx-border-color: white;");
            flowButton.setOnAction(e -> {
                //FlowDefinitionDTO flow =  stepperData.getFlowFromName(flowName);
//                if (flow == null) {
//                    System.out.println("flow is null");
//                    return;
//                }
//                targetFlow = flow;
                runButton.setDisable(false);
            });
            flowsVbox.getChildren().add(flowButton);

        }
    }
    public ContinuationPopUp(FlowExecutionDTO pickedExecution, List<String> targetFlows, Stage stage, bodyController body) {
        this.pickedExecution = pickedExecution;
        this.targetFlows = targetFlows;
        this.stage = stage;
        this.body = body;
    }
    @FXML
    void runContinuation(ActionEvent event) {

        Map<String,Object> outputs = pickedExecution.getAllExecutionOutputs();

        FlowDefinitionDTO currentFlow = getFlowDefinitionImplByFlowExecution();
        MandatoryAndOptionalFreeInputWithDD(currentFlow);
//todo possible move to server side
        //body.handlerContinuation(targetFlow, currentMandatoryFreeInput, currentOptionalFreeInput,freeInputsMandatory,freeInputsOptional,outputs,currentFlow);

        stage.close();
    }
    private void MandatoryAndOptionalFreeInputWithDD(FlowDefinitionDTO flow){

        List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs = flow.getFlowFreeInputs();
         currentMandatoryFreeInput = new ArrayList<>();
         currentOptionalFreeInput = new ArrayList<>();
        freeInputsMandatory = new ArrayList<>();
        freeInputsOptional = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclarationDTO> pair : freeInputs) {
            if (pair.getValue().isMandatory()) {
                currentMandatoryFreeInput.add(pair);
                String data = getDataOfFreeInputsFromCurrentFlow(flow,pair.getKey());
                freeInputsMandatory.add(new Pair<>(pair.getKey(),data));

            } else {
                currentOptionalFreeInput.add(pair);
                String data = getDataOfFreeInputsFromCurrentFlow(flow,pair.getKey());
                freeInputsMandatory.add(new Pair<>(pair.getKey(),data));

            }
        }
    }
    private String getDataOfFreeInputsFromCurrentFlow(FlowDefinitionDTO currentFlow,String nameOfDD){
        for (Pair<String, String> pair: pickedExecution.getUserInputs()){
           if (pair.getKey().equals(nameOfDD))
               return pair.getValue();
        }
        return null;
    }
    private FlowDefinitionDTO getFlowDefinitionImplByFlowExecution(){
//        Stepper stepperData = DataManager.getData();
//        for (FlowDefinitionImpl flow: stepperData.getFlows()){
//            if (flow.getName().equals(pickedExecution.getFlowDefinition().getName()))
//                return flow;
//        }
        return null;
    }
    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }

}