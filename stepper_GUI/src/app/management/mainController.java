package app.management;

import app.MVC_controller.MVC_controller;
import app.body.statsScreen.StatsScreen;
import app.body.bodyController;
import app.header.headerController;
import app.management.resizeHelper.ResizeHelper;
import app.management.style.StyleManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static modules.DataManeger.DataManager.stepperData;

public class mainController {
   private List<FlowDefinitionImpl> flows;
   @FXML private Parent headerComponent;
   @FXML private Parent bodyComponent;
   @FXML private AnchorPane generalPane;
   @FXML private headerController headerComponentController;

   @FXML private HBox appBox;
   String appBoxStyle;

   @FXML private ScrollPane scrollPane;
   @FXML private bodyController bodyComponentController;
   private StatsScreen statsScreen;
   private MVC_controller mvcController;
   private FlowExecution lastFlowExecution;
   private double xOffset;
   private double yOffset;
   @FXML
   public void initialize() {


      StyleManager.setTheme(StyleManager.getCurrentTheme());

      if (headerComponentController != null && bodyComponentController != null) {
         headerComponentController.setMainController(this);
         bodyComponentController.setMainController(this);


         //initialize a controller that communicate with the engine
         mvcController = new MVC_controller(this,headerComponentController,bodyComponentController);
         bodyComponentController.setMVCController(mvcController);
         headerComponentController.setMVCController(mvcController);



         //set thread to update the UI

         ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

         // Schedule the task to run every 200 ms
         executorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> updateUI()); // Update UI
         }, 0, 200, TimeUnit.MILLISECONDS);

      }

   }

   private void updateUI() {
      lastFlowExecution = stepperData.getFlowExecutions().get(stepperData.getFlowExecutions().size() - 1);
      if (lastFlowExecution != null) {
         boolean isDone = lastFlowExecution.isDone.get(); // Assuming you have an instance of ExecutionTask named executionTask
         if (isDone) {
            // Flow execution is done
            if (lastFlowExecution.getFlowExecutionResult().equals(FlowExecutionResult.FAILURE)) {
               //refresh executionHistory table
               bodyComponentController.showHistoryExe();

            }
            if (lastFlowExecution.getFlowExecutionResult().equals(FlowExecutionResult.SUCCESS)) {
               bodyComponentController.showHistoryExe();
            } else {//warning
               // ...
            }
         }
      }
   }


   public void setFlows(List<FlowDefinitionImpl> f){
       this.flows = f;
   }
   public void showFlowDefinition() {
      bodyComponentController.showFlowDefinition();
   }
   public void showStats() {
      bodyComponentController.showStatsScreen();
   }
   public void FlowsExecutionInMenu(){
      bodyComponentController.showAllFlowAndExe();
   }
   public List<FlowDefinitionImpl> getFlows(){
      return flows;
   }
   public void showHistoryExe(){
      bodyComponentController.showHistoryExe();
   }

   public void changeTheme(String toSet) {
      if (toSet.equals("dark")) {
       bodyComponent.getStylesheets().add("app/body/theme/bodyDark.css");
       headerComponent.getStylesheets().add("app/header/theme/headerDark.css");
      } else {
            bodyComponent.getStylesheets().add("app/body/theme/bodyLight.css");
            headerComponent.getStylesheets().add("app/header/theme/headerLight.css");
      }
   }

    public void showExecution() {
      bodyComponentController.showAllFlowAndExe();
    }
}
