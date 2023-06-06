package app.body.executionsHistory.continuation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;

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

    private List<String>  targetFlows;
    private Stage   stage;
    private FlowDefinitionImpl targetFlow = null;
    private FlowExecution pickedExecution;


    private String buttonStyle;


    public ContinuationPopUp(FlowExecution pickedExecution, List<String> targetFlows, Stage stage) {
        this.pickedExecution = pickedExecution;
        this.targetFlows = targetFlows;
        this.stage = stage;
    }


    @FXML
    void runContinuation(ActionEvent event) {
        //target flow is the flow that we need to continue to
        //pickedExecution is the execution that we need to continue from (that we picked in  the history screen
       // pickedExecution.continueExecution(targetFlow);
        
        //TODO: run the selected flow
        //todo fill with asaf
        //targetFlow.run();
        stage.close();
    }
    @FXML
    void initialize() {
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
                FlowDefinitionImpl flow =  stepperData.getFlowFromName(flowName);
                if (flow == null) {
                    System.out.println("flow is null");
                    return;
                }
                targetFlow = flow;
                runButton.setDisable(false);
            });
            flowsVbox.getChildren().add(flowButton);

        }
    }
}