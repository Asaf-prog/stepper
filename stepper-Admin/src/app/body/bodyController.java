package app.body;

import app.MVC_controller.MVC_controller;
import app.body.bodyInterfaces.bodyControllerDefinition;
import app.body.mainControllerClient.mainControllerClient;
import app.management.mainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class bodyController {
    private mainController main;
    private MVC_controller controller;
    private FlowDefinitionImpl currentFlow;
    private mainControllerClient controllerClient;

    private bodyControllerDefinition lastBodyController=null;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    @FXML
    private TextField textField;

    @FXML
    private AnchorPane bodyPane;
    public void setMainController(mainController main) {
        this.main = main;
    }
    public void setMainControllerClient(mainControllerClient main) {
        this.controllerClient = main;
    }
    public void setMVCController(MVC_controller controller){
        this.controller = controller;
    }
    public void showStatsScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("statsScreen/StatsScreen.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    private void loadScreen(FXMLLoader fxmlLoader,URL url) {
        try {
            if (lastBodyController!=null) {
                lastBodyController.onLeave();
            }
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerDefinition bController = fxmlLoader.getController();
            bController.setFlowsDetails(main.getFlows());
            bController.setBodyController(this);
            bController.SetCurrentFlow(currentFlow);

            bController.show();

            bodyPane.getChildren().setAll(screen);
            lastBodyController=bController;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("BASA1");
        }
    }
    public void showHistoryExe(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("executionsHistory/ExecutionsHistory.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    public void showRoleManagement(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("roleManagement/roleManagement.fxml");
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

    public void setBodyScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("body.fxml");
        fxmlLoader.setLocation(url);
        Parent screen = fxmlLoader.load(url.openStream());
        bodyPane.getChildren().setAll(screen);
    }

        public  mainController getMain(){
        return   main;
        }

    public void showUserManagement() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("userManagement/userManagement.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }


    public void initAdminApp() {
        //set body to user management
        showUserManagement();


    }
}



