package ClientsApp.app.MVC_controller;

import ClientsApp.app.Client.Client;
import ClientsApp.app.body.bodyController;
import ClientsApp.app.body.executeFlow.executionDetails.ExecutionsDetails;
import ClientsApp.app.header.headerController;
import ClientsApp.app.management.mainController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MVC_controller {
    private mainController main;
    private headerController header;
    private bodyController body;
    List<Pair<String, String>> freeInputs;
    private String lastExeId=null;


    private Gson gson = new Gson();

    public MVC_controller(mainController main, headerController header, bodyController body) {
        this.main = main;
        this.header = header;
        this.body = body;
    }
    public void updateRoles(List<String> roles){
        Platform.runLater(() -> {
            header.updateRoles(roles);
        });
    }
    public void updateClient(boolean isManager){
        Platform.runLater(() -> {
            header.updateClient(isManager);
        });
    }

    public List<String> getCurrentRoles(){
        return header.getCurrentRoles();
    }
    public void executeFlow(FlowDefinitionDTO flow) {

        List<Pair<String, String>> userInputs = flow.getUserInputs();
        String flowName = flow.getName();
        //add to the request the free inputs
        //send to server the request
        String userGson = gson.toJson(userInputs);
        //add string to requestbody object
        RequestBody gsonBody = RequestBody.create(MediaType.parse("application/json"), userGson);
        Request request = new Request.Builder()
                .url(ClientConstants.EXECUTE_FLOW)
                .post(gsonBody)
                .addHeader("flowName", flowName)
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("fail");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //means execution over
                if (response.code() == 200) {
                    System.out.println("success");

//                    popupDetails();

                    //setProgressBar(task);
                    header.setDisableOnExecutionsHistory();
                    Timer timer = new Timer();
                    String id=response.header("flowId");
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            //check if the flow ended
                            FlowEnded(id,timer);
                        }
                    }, 0, 200);
                }else {
                    //user not authorized
                        Platform.runLater(() -> {
                            //popout error message
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error");
                            alert.setHeaderText("---User not authorized 401 ---");
                            alert.setContentText("you dont have permission to execute this flow");
                            alert.setOnCloseRequest(event -> {
                                body.showFlowDefinition();

                            });
                            alert.showAndWait();


                        });
                    }
                response.close();
            }
        });
    }
    private boolean FlowEnded(String id, Timer timer) {
        //send to server the request
        Request request = new Request.Builder()
                .url(ClientConstants.FLOW_STATUS_CHECK)
                .get()
                .addHeader("flowId", id)
                .build();
        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               // System.out.println("fail");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {

                    Platform.runLater(() -> {
                        timer.cancel();
                        //open the option to continuation
                        //body.setContinuationButton();
                        lastExeId = id;
                        popupDetails(id);
                    });
                    response.close();

                } if (response.code() ==401) {

                    //probably processing
                    //update process
                    //String progress = response.header("progress");
                    Double progress = Double.parseDouble(response.header("progress"));
                    setProgressBar(id,progress);
                }
            }
        });
        return false;
    }

            private void popupDetails(String id) {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/ClientsApp/app/body/executeFlow/executionDetails/ExecutionsDetails.fxml"));
               // loader.setController(new ExecutionsDetails(id));
                loader.setControllerFactory(controllerClass -> {
                        return new ExecutionsDetails(id);
                });
                try {
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Flow Details");
                    //set icon as previous stage
                    stage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
                    stage.setScene(new Scene(root, 1060, 365));
                    stage.show();
                    //disable app until the user close the window
                } catch (IllegalStateException | IOException ex) {
                    VerySecretCode();
                    //todo remove@!!!
                    ex.printStackTrace();
                }
            }
            private void VerySecretCode() {
                String secretcode = "skvmbeoivnreonvoirvrev";
                Compile(secretcode);
            }

            private void Compile(String secretcode) {
                //come this far ... eh?
            }
            private void setProgressBar(String id, Double progress) {
                int nextIndex = header.getNextFreeProgress();
                ProgressBar progressBar = header.getNextProgressBar(nextIndex);
                progressBar.setStyle("-fx-accent: #0049ff;-fx-border-radius: 25;");
//                progressBar.progressProperty().bind(progress);
//                task.isFailedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                    if (newValue) {
//                        progressBar.setStyle("-fx-accent: #ff2929;-fx-border-radius: 25;");
//                    }
//                });
//                task.isSuccessProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                    if (newValue) {
//                        progressBar.setStyle("-fx-accent: #00ff00;-fx-border-radius: 25;");
//                    }
//                });
                Label label = header.getNextLabel(nextIndex);
                label.setText(id.substring(id.length()-4,id.length()));
                 header.addProgress(progressBar,label,nextIndex);
            }

    public void setFreeInputs(List<Pair<String,String>> freeInputs){
        this.freeInputs = freeInputs;
    }
    public List<Pair<String,String>> getFreeInputs(){
        return  freeInputs;
    }

    public String getLastExecutionId() {
        return lastExeId;
    }

    public void setCurrentRoles(List<String> roles) {
        header.setCurrentRoles(roles);
    }
}
