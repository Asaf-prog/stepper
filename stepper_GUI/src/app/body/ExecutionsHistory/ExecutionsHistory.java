package app.body.ExecutionsHistory;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;
import modules.stepper.Stepper;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;

public class ExecutionsHistory implements bodyControllerDefinition, Initializable {

    private bodyController body;
    private List<FlowDefinitionImpl> flows;
    @FXML
    private TableColumn<conversionObject, String> Flow;
    @FXML
    private TableColumn<conversionObject, String> TImeOfFlow;
    @FXML
    private TableColumn<conversionObject, String> ResultOfFlow;
    @FXML
    private TableView<conversionObject> tableData;
    private ObservableList<conversionObject> list;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Flow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Flow"));
        Flow.setVisible(true);
        TImeOfFlow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Duration"));
        ResultOfFlow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Result"));
        list = FXCollections.observableArrayList(
                new conversionObject("asaf","kaka","shit"),
                new conversionObject("varon","kaka","shit"),
                new conversionObject("varon","kaka","shit")
        );
        tableData.setItems(list);
    }
    @Override
    public void show() {

        Stepper stepperData = DataManager.getData();
        List<FlowExecution> flowsExe = stepperData.getFlowExecutions();
        tableData.setItems(list);
    }
    private void handleButtonAction(FlowExecution flowExecution){

    }
    public void setFlows( List<FlowDefinitionImpl> flows){
        this.flows = flows;
    }
    @Override
    public void setBodyController(bodyController body) {
        this.body = body;
    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {
        this.flows = list;
    }

    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow) {
       // this.flows = flows;
    }


}
