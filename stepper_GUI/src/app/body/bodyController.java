package app.body;

import app.MVC_controller.MVC_controller;
import app.management.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class bodyController {
    private mainController main;
    private MVC_controller controller;
    private FlowDefinitionImpl currentFlow;

    @FXML
    private AnchorPane bodyPane;
    public void setMainController(mainController main) {
        this.main = main;
    }
    public void setMVCController(MVC_controller controller){
        this.controller = controller;
    }
    public void showFlowDefinition() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        //URL url = getClass().getResource("flowDefinitionPresent/flowDefinitionPresent.fxml");
        URL url = getClass().getResource("flowDefinitionPresent/test.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    public void showStatsScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("StatsScreen/StatsScreen.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    private void loadScreen(FXMLLoader fxmlLoader,URL url) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerDefinition bController = fxmlLoader.getController();
            bController.setFlowsDetails(main.getFlows());
            bController.setBodyController(this);

            bController.show();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void executeExistFlowScreen(FlowDefinitionImpl flow) {
        setCurrentFlow(flow);
        //need to supply the free inputs
        try {//first create a new body with the relevant free inputs and then update in context

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenWithCurrentFlow(fxmlLoader, url,flow);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadScreenWithCurrentFlow(FXMLLoader fxmlLoader,URL url,FlowDefinitionImpl flow) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerDefinition bController = fxmlLoader.getController();
            bController.setFlowsDetails(main.getFlows());
            bController.setBodyController(this);
            bController.SetCurrentFlow(flow);
            bController.show();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showHistoryExe(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("ExecutionsHistory/ExecutionsHistory.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    public void showAllFlowAndExe(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("FlowExecutions/FlowExecutions.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    public void setCurrentFlow(FlowDefinitionImpl flow){
        this.currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow(){
        return currentFlow;
    }
    public MVC_controller getMVC_controller(){
        return controller;
    }
    public void handlerContinuation(FlowDefinitionImpl flow, List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                    List<Pair<String, DataDefinitionDeclaration>> optional){
        executeExistFlowScreenOfContinuation(flow,mandatory,optional);
    }
    public void executeExistFlowScreenOfContinuation(FlowDefinitionImpl flow,List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                                     List<Pair<String, DataDefinitionDeclaration>> optional) {
        setCurrentFlow(flow);

        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenWithCurrentFlowForContinuation(fxmlLoader, url,flow,mandatory,optional);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadScreenWithCurrentFlowForContinuation(FXMLLoader fxmlLoader,URL url,FlowDefinitionImpl flow,List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                                          List<Pair<String, DataDefinitionDeclaration>> optional) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerForContinuation bodyController = fxmlLoader.getController();
            bodyController.setCurrentFlowForContinuation(flow);
            bodyController.SetCurrentMandatoryAndOptional(mandatory,optional);
            bodyController.showForContinuation();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
