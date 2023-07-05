package app.body.flowDefinitionPresent;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import app.body.flowDefinitionPresent.graph.FlowGraphBuilder;
import app.management.style.StyleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.DataDefinitionDeclaration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class flowDefinitionPresent implements bodyControllerDefinition {
    @FXML
    private Pane scatchPane;
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
    private  VBox thiredVbox;
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
    private List<FlowDefinitionImpl> flows;
    private bodyController body;
    private FlowDefinitionImpl currentFlow;
    String style="";

    private ToggleGroup flowsToggleGroup;

    private List<Stage> stages=new ArrayList<>();

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
        style=executeButton.getStyle();
        scatchPane.setVisible(false);
        seocendVbox.setVisible(false);
        thiredVbox.setVisible(false);
        executeButton.setOnMouseEntered(event -> {
            executeButton.setStyle(style+"-fx-background-color: rgb(255,0,96); -fx-background-radius: 20;-fx-border-color: #566dff;");
        });
        executeButton.setOnMouseExited(event -> {
            executeButton.setStyle(style);
        });

    }

    @Override
    public void onLeave() {
        for (Stage stage:stages){
            stage.close();
        }
    }

    @Override
    public void show(){
        setTheme();
        flowsToggleGroup = new ToggleGroup();
        for (FlowDefinitionImpl flow :flows){
            RadioButton button = new RadioButton(flow.getName());
            button.getStylesheets().add("app/management/style/darkTheme.css");
            button.getStyleClass().add("flowRadioButton");
            button.setStyle("-fx-text-fill: #fff608; -fx-font-size: 16; -fx-font-family: 'Arial Rounded MT Bold'");
            //button.setStyle("-fx-text-fill: #ffd54a");
            button.getStylesheets().add("app/management/style/darkTheme.css");
            button.getStyleClass().add("radioButton");

           // button.getStylesheets().add("app/management/style/darkTheme.css");
            button.setOnAction(event -> handleButtonAction(flow));
            button.setToggleGroup(flowsToggleGroup);
            firstVbox.getChildren().add(button);
        }
        firstVbox.setSpacing(10);

    }
    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    private void handleButtonAction(FlowDefinitionImpl flow) {

        this.body.setButtonExecutionFromHeader(flow);
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
        DrawFlow(flow);


        flowsToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                executeButton.setDisable(true);
            }else{
                executeButton.setDisable(false);
                //get selected item that was selected from the group and do the onAction that  was selected with the new value(flow)
                RadioButton selectedFlowButton = (RadioButton) newValue;
                String selectedFlow = selectedFlowButton.getText();
                for (FlowDefinitionImpl flow1 : flows) {
                    if (flow1.getName().equals(selectedFlow)) {
                        Toggle selectedToggle = group.getSelectedToggle();
                        if (selectedToggle != null) {
                            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
                            String selectedText = selectedRadioButton.getText();
                            if (selectedText.equals("Steps")) {
                                handleButtonActionForSteps(flow1);
                            } else if (selectedText.equals("Outputs")) {
                                handleButtonActionForOutputs(flow1);
                            } else if (selectedText.equals("Free Inputs")) {
                                handleButtonActionForFreeInputs(flow1);
                            } else if (selectedText.equals("Graph")) {
                                handleButtonActionForGraph(flow1);
                            }
                        }
                    }
                 }
            }
        });
    }
    private void handleButtonActionForGraph(FlowDefinitionImpl flow) {
        scatchPane.setVisible(true);
        graphPNG.setVisible(true);
        graphPNG.setFitWidth(400);
        graphPNG.setPreserveRatio(false);
        thiredVbox.setVisible(false);

    }
    private void DrawFlow(FlowDefinitionImpl flow) {
        FlowGraphBuilder.buildFlowGraph(flow);
        Image image = new Image("file:flow.png");
        graphPNG.setImage(image);
        Image image1 = new Image("app/body/flowDefinitionPresent/graph/clickMe.png");
        graphPNG.setOnMouseEntered(event -> {
            graphPNG.setImage(image1);
        });
        graphPNG.setOnMouseExited(event -> {
            graphPNG.setImage(image);
        });
        graphPNG.setPreserveRatio(true);
        graphPNG.setOnMouseClicked( event -> {
                    //open the image in big in new window
                    Stage stage = new Stage();
                    stages.add(stage);
                    stage.setTitle("Flow Graph");
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setPreserveRatio(true);
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
                    scrollPane.setStyle("-fx-background-color:#24292e;");
                    Scene scene = new Scene(scrollPane, 1080, 720);
                    scene.setFill(Color.valueOf("#36393e"));
                    stage.setScene(scene);
                    try {
                        stage.show();
                    } catch (IllegalStateException e) {
                        // Handle the exception gracefully
                    }
                }
        );
    }
    private void handleButtonActionForFreeInputs(FlowDefinitionImpl flow){
        scatchPane.setVisible(false);
        thiredVbox.setVisible(true);
        treeList.setVisible(false);

        treeList.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new TreeCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setTextFill(Color.BLACK); // Set the default text color
                        } else {
                            setText(item);
                            setTextFill(Color.BLUEVIOLET); // Set the text color to red
                        }
                    }
                };
            }
        });


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
