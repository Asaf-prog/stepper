package app.body;

import app.MVC_controller.MVC_controller;
import app.body.bodyInterfaces.bodyControllerDefinition;
import app.body.bodyInterfaces.bodyControllerExecuteFromHistory;
import app.body.bodyInterfaces.bodyControllerForContinuation;
import app.management.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class bodyController {
    private mainController main;
    private MVC_controller controller;
    private FlowDefinitionImpl currentFlow;


    private bodyControllerDefinition lastBodyController=null;

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
            System.out.println("BASA1");
        }
    }
    public void executeExistFlowScreen(FlowDefinitionImpl flow) {
        setCurrentFlow(flow);
        //need to supply the free inputs
        try {//first create a new body with the relevant free inputs and then update in context

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            if (url == null)
                System.out.print("FXML file not found");
            else {
                fxmlLoader.setLocation(url);

                loadScreenWithCurrentFlow(fxmlLoader, url, flow);
            }} catch (Exception e) {
            System.out.println("");
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
            System.out.println("BASA2");
        }
    }
    public void showHistoryExe(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("executionsHistory/ExecutionsHistory.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    public void showAllFlowAndExe(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
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
                                    List<Pair<String, DataDefinitionDeclaration>> optional,List<Pair<String, String>>mandatoryIn,List<Pair<String, String>>optionalIn,
                                    Map<String,Object> outputs,FlowDefinitionImpl currentFlow){
        executeExistFlowScreenOfContinuation(flow,mandatory,optional,mandatoryIn, optionalIn,outputs,currentFlow);
    }
    public void executeExistFlowScreenOfContinuation(FlowDefinitionImpl flow,List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                                     List<Pair<String, DataDefinitionDeclaration>> optional,List<Pair<String, String>>mandatoryIn,
                                                     List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl currentFlow) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenWithCurrentFlowForContinuation(fxmlLoader, url,flow,mandatory,optional,mandatoryIn,optionalIn,outputs,currentFlow);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadScreenWithCurrentFlowForContinuation(FXMLLoader fxmlLoader,URL url,FlowDefinitionImpl flow,List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                                          List<Pair<String, DataDefinitionDeclaration>> optional,List<Pair<String, String>>mandatoryIn,
                                                          List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl currentFlow) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerForContinuation bodyController = fxmlLoader.getController();
            bodyController.setCurrentFlowForContinuation(flow);
            bodyController.setBodyControllerContinuation(this);
            bodyController.SetCurrentMandatoryAndOptional(mandatory,optional,mandatoryIn,optionalIn,outputs,this.currentFlow);
            setCurrentFlow(flow);
            bodyController.showForContinuation();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            System.out.println("");
        }
    }
    public void handlerForExecuteFromStatisticScreen(List<Pair<String, String>> freeInputMandatory,List<Pair<String
            , String>> freeInputOptional,FlowDefinitionImpl flowDefinition,List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD ){

        executeExistFlowScreenFromHistoryScreen(freeInputMandatory,freeInputOptional,flowDefinition,freeInputsMandatoryWithDD,freeInputsOptionalWithDD);
    }
    private void executeExistFlowScreenFromHistoryScreen(List<Pair<String, String>> freeInputMandatory,List<Pair<String
            , String>> freeInputOptional,FlowDefinitionImpl flowDefinition,List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD ){
        setCurrentFlow(flowDefinition);

        try {//first create a new body with the relevant free inputs and then update in context

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenFromHistory(fxmlLoader, url,flowDefinition,freeInputMandatory,freeInputOptional,freeInputsMandatoryWithDD,freeInputsOptionalWithDD);

        } catch (Exception e) {
            System.out.println("");
        }
    }
    private void loadScreenFromHistory(FXMLLoader fxmlLoader,URL url,FlowDefinitionImpl flow,List<Pair<String, String>> freeInputMandatory,
                                       List<Pair<String, String>> freeInputOptional,List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD ){

        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerExecuteFromHistory bodyController = fxmlLoader.getController();
            bodyController.setBodyControllerFromHistory(this);
            bodyController.SetCurrentFlowFromHistory(flow);
            bodyController.setFreeInputsMandatoryAndOptional(freeInputMandatory,freeInputOptional,freeInputsMandatoryWithDD,freeInputsOptionalWithDD);
            bodyController.showFromHistory();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            System.out.println("");
        }
    }
    public void setButtonExecutionFromHeader(FlowDefinitionImpl flowDefinition){
        main.getHeaderComponentController().SetExecutionButtonVisible(flowDefinition);
        this.currentFlow = flowDefinition;
    }
    public void setBodyScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("body.fxml");
        fxmlLoader.setLocation(url);
        Parent screen = fxmlLoader.load(url.openStream());
        bodyPane.getChildren().setAll(screen);
    }
}
