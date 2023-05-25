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
import modules.flow.definition.api.FlowDefinitionImpl;

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
        URL url = getClass().getResource("flowDefinitionPresent/flowDefinitionPresent.fxml");
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
        System.out.println(flow.getName());
        setCurrentFlow(flow);
        //need to supply the free inputs
        try {//first create a new body with the relevant free inputs and then update in context

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenWithCurrentFlow(fxmlLoader, url,flow);

            controller.executeFlow(flow);
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
    public void setCurrentFlow(FlowDefinitionImpl flow){
        this.currentFlow = flow;
    }
    public FlowDefinitionImpl getCurrentFlow(){
        return currentFlow;
    }
}
