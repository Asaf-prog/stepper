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

//    public ObservableList<conversionObject> list = FXCollections.observableArrayList(
//            new conversionObject("asaf","kaka","shit"),
//            new conversionObject("varon","kaka","shit")
//    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         ObservableList<conversionObject> list = FXCollections.observableArrayList(
                new conversionObject("asaf","kaka","shit"),
                new conversionObject("varon","kaka","shit")
        );
        Flow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Flow"));
        TImeOfFlow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Duration"));
        ResultOfFlow.setCellValueFactory(new PropertyValueFactory<conversionObject, String>("Result"));

    }
    @Override
    public void show() {

//        Stepper stepperData= DataManager.getData();
//        List<FlowExecution> flowsExe = stepperData.getFlowExecutions();
//        for (FlowExecution flow : flowsExe){
//            Button button = new Button(flow.getFlowDefinition().getName());
//            button.setOnAction(e -> handleButtonAction(flow));
//            listOfFlowToShow.getChildren().add(button);
//        }



        Stepper stepperData= DataManager.getData();
        List<FlowExecution> flowsExe = stepperData.getFlowExecutions();


//        for (int i = 0; i < flowsExe.size(); i++) {
//            int finalI = i;
//            list.add(new conversionObject(flowsExe.get(finalI).getFlowDefinition().getName(),
//                    flowsExe.get(finalI).getTotalTime().toString(),flowsExe.get(finalI).getFlowExecutionResult().toString()));
//        }


        //System.out.println(list);
       // tableData.setItems(list);
        //tableData.;




//        TableColumn<Person, String> nameColumn = new TableColumn<>("Flow");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
//
//        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Time");
//        ageColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
//
//        TableColumn<Person, String> occupationColumn = new TableColumn<>("End");
//        occupationColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
//
//        // Create table
//        tableView = new TableView<>();
//
//        tableView.getColumns().addAll(nameColumn, ageColumn, occupationColumn);
//        listOfFlowToShow.setSpacing(10);
//        listOfFlowToShow.setPadding(new Insets(10));
//        // Add sample data to the table
////        tableView.setItems(FXCollections.observableArrayList(
////                new Person("John Doe", 30, "Engineer"),
////                new Person("Jane Smith", 25, "Teacher"),
////                new Person("Mike Johnson", 35, "Doctor")
////        ));
//        personData = FXCollections.observableArrayList(
//                new Person("John Doe", 30, "Engineer"),
//                new Person("Jane Smith", 25, "Teacher"),
//                new Person("Mike Johnson", 35, "Doctor")
//        );
//        tableView.setItems(personData);
//        listOfFlowToShow.getChildren().add(tableView);
        // Create layout
    }


    private void handleButtonAction(FlowExecution flowExecution){
        //create a new body that present the data of all this flow
        //String StartTime = flowExecution.getTotalTime().toMillis();
//        System.out.print("Step name: " + stepUsageDeclaration.getFinalStepName());
//        System.out.println(" ,Took about: " +stepUsageDeclaration.getTotalTime().toMillis() + " MS");
//        System.out.println("And finish with " + stepUsageDeclaration.getStepResult());



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
