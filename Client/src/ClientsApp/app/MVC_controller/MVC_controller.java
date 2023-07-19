package ClientsApp.app.MVC_controller;

import ClientsApp.app.body.bodyController;
import ClientsApp.app.body.executeFlow.executionDetails.ExecutionsDetails;
import ClientsApp.app.header.headerController;
import ClientsApp.app.management.mainController;
import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.ExecutionManager;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import modules.flow.execution.runner.FLowExecutor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.util.List;

import static modules.DataManeger.DataManager.stepperData;

public class MVC_controller {
    private mainController main;
    private headerController header;
    private bodyController body;
    List<Pair<String, String>> freeInputs;

    private Gson gson = new Gson();

    public MVC_controller(mainController main, headerController header, bodyController body) {
        this.main = main;
        this.header = header;
        this.body = body;
    }

    public void executeFlow(FlowDefinitionDTO flow) {
        //send to server flow name and free inputs

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
                    //get flowExecutionDTO from server
                    String flowExecutionDTO = response.body().string();
                    FlowExecutionDTO flowExecutionDTO1 = gson.fromJson(flowExecutionDTO, FlowExecutionDTO.class);

                    popupDetails();

                    //setProgressBar(task);
                    header.setDisableOnExecutionsHistory();
                }else {
                    System.out.println("fail");
                }


            }
        });
    }

    private void popupDetails() {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("executionDetails/ExecutionsDetails.fxml"));
            ExecutionsDetails executionsDetails = new ExecutionsDetails();
            try {

                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Flow Details");
                //set icon as previous stage
                stage.getIcons().add(new Image(("app/management/content/stepperIcon.png")));
                stage.setScene(new Scene(root, 1060, 365));
                stage.show();
                //disable app until the user close the window
            }catch (IllegalStateException | IOException ex) {
                VerySecretCode();
            }
        }

    private void VerySecretCode() {
        String secretcode="skvmbeoivnreonvoirvrev";
        Compile(secretcode);
    }

    private void Compile(String secretcode) {
        //come this far ... eh?
    }

    private void setProgressBar(ExecutionTask task) {
        int nextIndex = header.getNextFreeProgress();
        ProgressBar progressBar = header.getNextProgressBar(nextIndex);
        progressBar.setStyle("-fx-accent: #0049ff;-fx-border-radius: 25;");
        progressBar.progressProperty().bind(task.getProgress());
        task.isFailedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                progressBar.setStyle("-fx-accent: #ff2929;-fx-border-radius: 25;");
            }
        });
        task.isSuccessProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                progressBar.setStyle("-fx-accent: #00ff00;-fx-border-radius: 25;");
            }
        });
        Label label = header.getNextLabel(nextIndex);
        label.setText(task.get4DigId());
       // header.addProgress(progressBar,label,nextIndex);
    }


    public void setFreeInputs(List<Pair<String,String>> freeInputs){
        this.freeInputs = freeInputs;
    }
    public List<Pair<String,String>> getFreeInputs(){
        return  freeInputs;
    }

}
