package ClientsApp.app.body;

import ClientsApp.app.Client.Client;
import ClientsApp.app.MVC_controller.MVC_controller;
import ClientsApp.app.body.bodyInterfaces.bodyControllerDefinition;
import ClientsApp.app.body.bodyInterfaces.bodyControllerExecuteFromHistory;
import ClientsApp.app.body.bodyInterfaces.bodyControllerForContinuation;
import ClientsApp.app.body.bodyInterfaces.bodyControllerForLogin;
import ClientsApp.app.body.mainControllerClient.mainControllerClient;
import ClientsApp.app.management.mainController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.flow.DataDefinitionDeclarationDTO;
import services.user.ContinuationConversionDTO;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class bodyController {
    @FXML
    private TextField textField;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane bodyPane;
    private mainController main;
    private MVC_controller controller;
    private FlowDefinitionDTO currentFlow;
    private mainControllerClient controllerClient;
    private bodyControllerDefinition lastBodyController=null;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Client client;
    public void setMainController(mainController main) {
        this.main = main;
    }
    public void setMainControllerClient(mainControllerClient main) {
        this.controllerClient = main;
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
    private void loadScreenForLogin(FXMLLoader fxmlLoader,URL url) {
        try {
            if (lastBodyController!=null) {
                lastBodyController.onLeave();
            }
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerForLogin bController = fxmlLoader.getController();
            bController.setBodyController(this);
            bController.show();
            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            System.out.println("problem with login-page client");
        }
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
            bController.setClient(getClient());
          //  bController.SetCurrentFlow(currentFlow);

            bController.show();

            bodyPane.getChildren().setAll(screen);
            lastBodyController=bController;
        }
        catch (IOException e) {
            System.out.println("exception in  body controller");
            e.printStackTrace();
        }
    }
    public void executeExistFlowScreen(FlowDefinitionDTO flow) {
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
    private void loadScreenWithCurrentFlow(FXMLLoader fxmlLoader,URL url,FlowDefinitionDTO flow) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerDefinition bController = fxmlLoader.getController();
            bController.setFlowsDetails(main.getFlows());
            bController.setBodyController(this);
            //bController.SetCurrentFlow(flow);
            bController.show();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            System.out.println("BASA2");
        }
    }
    public void showLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("login/loginPage.fxml");
        fxmlLoader.setLocation(url);
        loadScreenForLogin(fxmlLoader, url);
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
    public void setCurrentFlow(FlowDefinitionDTO flow){
        this.currentFlow = flow;
    }
    public FlowDefinitionDTO getCurrentFlow(){
        return currentFlow;
    }
    public MVC_controller getMVC_controller(){
        return controller;
    }

    public void handlerContinuationFromServlet( DataTransfer transfer){
        executeExistFlowScreenOfContinuationServlet(transfer);
    }
    public void executeExistFlowScreenOfContinuationServlet( DataTransfer transfer){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);
            transfer.setUrl(url);
            transfer.setFxmlLoader(fxmlLoader);
            loadScreenWithCurrentFlowForContinuationServlet(transfer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void  loadScreenWithCurrentFlowForContinuationServlet(DataTransfer transfer){
        try {
           Parent screen = transfer.getFxmlLoader().load(transfer.getUrl().openStream());
            bodyControllerForContinuation bodyController = transfer.getFxmlLoader().getController();
            bodyController.setBodyControllerContinuation(this);

            bodyController.setCurrentFlowForContinuation(transfer.getDataListFromServlet().getTargetFlow());
            bodyController.setLastFlowDTO(transfer.getDataListFromServlet().getSourceFlow());
            // bodyController.SetCurrentMandatoryAndOptional(mandatory,optional,mandatoryIn,optionalIn,outputs,this.currentFlow);

            setCurrentFlow(transfer.getDataListFromServlet().getTargetFlow());
            bodyController.setDataTransfer(transfer);
            bodyController.showForContinuationServlet();

            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            System.out.println("BASA3");
        }
    }
    public void handlerForExecuteFromStatisticScreen(List<Pair<String, String>> freeInputMandatory,List<Pair<String
            , String>> freeInputOptional,FlowDefinitionDTO flowDefinition,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsOptionalWithDD ){

        executeExistFlowScreenFromHistoryScreen(freeInputMandatory,freeInputOptional,flowDefinition,freeInputsMandatoryWithDD,freeInputsOptionalWithDD);
    }
    private void executeExistFlowScreenFromHistoryScreen(List<Pair<String, String>> freeInputMandatory,List<Pair<String
            , String>> freeInputOptional,FlowDefinitionDTO flowDefinition,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsOptionalWithDD ){
        setCurrentFlow(flowDefinition);

        try {//first create a new body with the relevant free inputs and then update in context

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("executeFlow/executeFlowController.fxml");
            fxmlLoader.setLocation(url);

            loadScreenFromHistory(fxmlLoader, url,flowDefinition,freeInputMandatory,freeInputOptional,freeInputsMandatoryWithDD,freeInputsOptionalWithDD);

        } catch (Exception e) {
            System.out.println("BASA4");
        }
    }
    private void loadScreenFromHistory(FXMLLoader fxmlLoader,URL url,FlowDefinitionDTO flow,List<Pair<String, String>> freeInputMandatory,
                                       List<Pair<String, String>> freeInputOptional,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsMandatoryWithDD
    ,List<Pair<String, DataDefinitionDeclarationDTO>> freeInputsOptionalWithDD ){

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
            System.out.println("BASA5");
        }
    }
    public void setButtonExecutionFromHeader(FlowDefinitionDTO flowDefinition){
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
    @FXML
    void loginToStepper(ActionEvent event) {

            if (textField.getText().isEmpty()){
                errorMessageProperty.set("User name is empty. You can't login with empty user name");
                return;
            }
            //noinspection ConstantConditions
            String userName = textField.getText();
            String finalUrl = HttpUrl
                    .parse(ClientConstants.LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter("username", userName)
                    .build()
                    .toString();
            updateHttpStatusLine("New request is launched for: " + finalUrl);
            ClientHttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + e.getMessage())
                    );
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println(response.body().string());
                    response.close();
                }
            });
        }
        private void updateHttpStatusLine(String data) {
           // this.mainControllerClient.updateHttpLine(data);
        }
        public  mainController getMain(){
        return   main;
        }
        public  void switchBodyScreen() throws IOException {
        setBodyScreen();
    }
    public void setNameOnScreen(String userName){
        main.getHeaderComponentController().setNameOnScreen(userName);
    }
    public void setClient(Client client){
        this.client = client;
    }
    public Client getClient(){
        return client;
    }
}



