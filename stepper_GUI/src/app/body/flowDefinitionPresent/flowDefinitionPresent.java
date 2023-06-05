package app.body.flowDefinitionPresent;

import app.body.bodyController;
import app.body.bodyControllerDefinition;
import app.body.flowDefinitionPresent.graph.FlowGraphBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.DataDefinitionDeclaration;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class flowDefinitionPresent implements bodyControllerDefinition {
    @FXML
    private Pane scatchPane;

    private List<FlowDefinitionImpl> flows;
    private bodyController body;
    private FlowDefinitionImpl currentFlow;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private VBox innerVbox;
    @FXML
    private AnchorPane ExecuteButton;
    @FXML
    private VBox firstVbox;
    @FXML
    private VBox seocendVbox;
    @FXML
    private Label nameOfFlowSelected;
    @FXML
    private Label descriptionP;
    @FXML
    private Label FormalOutputs;
    @FXML
    private Label isReadOnly;
    @FXML
    private Label firstLabelOnScreen;
    @FXML
    private RadioButton stepsP;
    @FXML
    private RadioButton FreeInput;
    @FXML
    private RadioButton Outputs;
    @FXML
    private VBox thiredVbox;
    @FXML
    private Button executeButton;
    @FXML
    private Label numberOfSteps;
    @FXML
    private TreeView<String> treeList;

    @FXML
    private ImageView graphPNG;
    @FXML
    private RadioButton graph;
    @FXML
    void initialize() {
        assert ExecuteButton != null : "fx:id=\"ExecuteButton\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert firstVbox != null : "fx:id=\"firstVbox\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert seocendVbox != null : "fx:id=\"seocendVbox\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert nameOfFlowSelected != null : "fx:id=\"nameOfFlowSelected\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert descriptionP != null : "fx:id=\"descriptionP\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert FormalOutputs != null : "fx:id=\"FormalOutputs\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert isReadOnly != null : "fx:id=\"isReadOnly\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert innerVbox != null : "fx:id=\"innerVbox\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert stepsP != null : "fx:id=\"stepsP\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert FreeInput != null : "fx:id=\"FreeInput\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert Outputs != null : "fx:id=\"Outputs\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert thiredVbox != null : "fx:id=\"thiredVbox\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert firstLabelOnScreen != null : "fx:id=\"firstLabelOnScreen\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert treeList != null : "fx:id=\"treeLIst\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert executeButton != null : "fx:id=\"executeButton\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert numberOfSteps != null : "fx:id=\"numberOfSteps\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert graphPNG != null : "fx:id=\"graphPNG\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'flowDefinitionPresent.fxml'.";
        scatchPane.setVisible(false);

        seocendVbox.setVisible(false);
        thiredVbox.setVisible(false);
    }
    @Override
    public void show(){
        ToggleGroup group = new ToggleGroup();
        for (FlowDefinitionImpl flow :flows){
            RadioButton button = new RadioButton(flow.getName());
            button.setStyle("-fx-text-fill: white");
            button.setOnAction(event -> handleButtonAction(flow));
            button.setToggleGroup(group);
            firstVbox.getChildren().add(button);
        }
        firstVbox.setSpacing(10);
    }
    private void handleButtonAction(FlowDefinitionImpl flow) {
        ToggleGroup group = new ToggleGroup();
        executeButton.setDisable(false);
        body.setCurrentFlow(flow);
        seocendVbox.setVisible(true);
        nameOfFlowSelected.setText("Name: " + flow.getName());
        FormalOutputs.setText("Formal Outputs: " + flow.getFlowFormalOutputs().toString());
        descriptionP.setText("Description: " + flow.getDescription());
        if (flow.isReadOnly()) {
            isReadOnly.setText("Read only: True");
        }
        isReadOnly.setText("Read only: False");

        stepsP.setToggleGroup(group);
        stepsP.setOnAction(event -> handleButtonActionForSteps(flow));

        Outputs.setToggleGroup(group);
        Outputs.setOnAction(event -> handleButtonActionForOutputs(flow));

        FreeInput.setToggleGroup(group);
        FreeInput.setOnAction(event -> handleButtonActionForFreeInputs(flow));

        graph.setToggleGroup(group);
        graph.setOnAction(event -> handleButtonActionForGraph(flow));

        //todo => add the number of continuation
        scatchFlow(flow);
    }

    private void handleButtonActionForGraph(FlowDefinitionImpl flow) {
        scatchPane.setVisible(true);
        graphPNG.setVisible(true);
        graphPNG.setFitWidth(400);
        graphPNG.setPreserveRatio(false);
        thiredVbox.setVisible(false);

    }
    private void scatchFlow(FlowDefinitionImpl flow) {
        FlowGraphBuilder.buildFlowGraph(flow);
        //file:/C:/Users/Saar%20Cohen/Documents/GitHub/stepper/stepper_GUI/src
        Image image = new Image("/app/body/flowDefinitionPresent/flow.png");
        graphPNG.setImage(image);
        //graphPNG.setImage(null);
        graphPNG.setOnMouseClicked( event -> {
                    //open the image in big in new window
                    Stage stage = new Stage();
                    stage.setTitle("Flow Graph");
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setFitWidth(1080);
                    imageView.setFitHeight(720);

                    imageView.setPreserveRatio(false);
                    imageView.setSmooth(true);
                    imageView.setCache(true);
                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setContent(imageView);
                    scrollPane.setFitToHeight(true);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setPannable(true);
                    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    scrollPane.setHmax(image.getWidth());
                    scrollPane.setVmax(image.getHeight());
                    scrollPane.setPrefSize(1080, 720);

                    Scene scene = new Scene(scrollPane, 1080, 720);
                    stage.setScene(scene);
                    stage.show();




                }



                );


    }

    private void handleButtonActionForFreeInputs(FlowDefinitionImpl flow){
        scatchPane.setVisible(false);
        thiredVbox.setVisible(true);
        treeList.setVisible(false);
        firstLabelOnScreen.setText("The Number of Free Inputs is: "+flow.getFlowFreeInputs().size());
        TreeItem<String> rootItem = new TreeItem<>("choose Free Input to display its information");
        for(Pair<String, DataDefinitionDeclaration> data :flow.getFlowFreeInputs()){
            TreeItem <String> branch = new TreeItem<>(data.getKey());
            TreeItem <String> branch1 = new TreeItem<>("Is Mandatory? "+data.getValue().isMandatory());
            TreeItem <String> branch2 = new TreeItem<>("The type is: "+ data.getValue().dataDefinition().getTypeName());
            TreeItem <String> branch3 = new TreeItem<>("User String: "+ data.getValue().getUserString());
            branch.getChildren().addAll(branch1,branch2,branch3);
            rootItem.getChildren().addAll(branch);
        }
        rootItem.setExpanded(true);
        treeList.setRoot(rootItem);
        treeList.setShowRoot(false);
        treeList.setVisible(true);
    }
    private void handleButtonActionForOutputs(FlowDefinitionImpl flow){
        scatchPane.setVisible(false);
        thiredVbox.setVisible(true);
        treeList.setVisible(false);
        firstLabelOnScreen.setText("The Number of the Outputs is: "+flow.getFlowFormalOutputs().size());
        TreeItem<String> rootItem = new TreeItem<>("Outputs");
        for(String out: flow.getFlowOutputs()){
            TreeItem <String> branch = new TreeItem<>(out);
            rootItem.getChildren().addAll(branch);
        }
        rootItem.setExpanded(true);
        treeList.setRoot(rootItem);
        treeList.setShowRoot(false);
        treeList.setVisible(true);
    }
    private void handleButtonActionForSteps(FlowDefinitionImpl flow){
        scatchPane.setVisible(false);
        thiredVbox.setVisible(true);
        treeList.setVisible(false);
        firstLabelOnScreen.setText("Choose Step to display its information: ");
        TreeItem<String> rootItem = new TreeItem<>("Steps");

        for(StepUsageDeclaration step: flow.getFlowSteps()){
            TreeItem <String> branch = new TreeItem<>(step.getFinalStepName());
            TreeItem <String> branch1 = new TreeItem<>("Step original name: "+step.getStepDefinition().getName());
            TreeItem <String> branch2 = new TreeItem<>("This step is readOnly? " +step.getStepDefinition().isReadonly());
            branch.getChildren().addAll(branch1,branch2);
            rootItem.getChildren().addAll(branch);
        }
        rootItem.setExpanded(true);
        treeList.setRoot(rootItem);
        treeList.setShowRoot(false);
        treeList.setVisible(true);
    }
    private TreeItem<String> createTreeItem(List<StepUsageDeclaration> steps) {
        for (StepUsageDeclaration step : steps){
            TreeItem<String> item = new TreeItem<>(step.getFinalStepName());
        }
        return null;
    }
    @FXML
    void executeButtonForFlow(ActionEvent event) {
        body.executeExistFlowScreen(body.getCurrentFlow());
    }
   @FXML
    void executeFlowFunc(ActionEvent event) {
        body.executeExistFlowScreen(body.getCurrentFlow());
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
    public void SetCurrentFlow(FlowDefinitionImpl flow){
        currentFlow = flow;
    }
}
