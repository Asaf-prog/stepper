package app.management;

import app.MVC_controller.MVC_controller;
import app.body.StatsScreen.StatsScreen;
import app.body.bodyController;
import app.header.headerController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class mainController {
   private List<FlowDefinitionImpl> flows;
   @FXML private Parent headerComponent;
   @FXML private Parent bodyComponent;
   @FXML private AnchorPane generalPane;
   @FXML private headerController headerComponentController;
;
   @FXML private bodyController bodyComponentController;
   private StatsScreen statsScreen;
   private MVC_controller mvcController;
   public void PresentTpBodyFlowDefinition(){

   }
   @FXML
   public void initialize() {
      if (headerComponentController != null && bodyComponentController != null) {
         headerComponentController.setMainController(this);
         bodyComponentController.setMainController(this);


         //initialize a controller that communicate with the engine
         mvcController = new MVC_controller(this,headerComponentController,bodyComponentController);
         bodyComponentController.setMVCController(mvcController);
         headerComponentController.setMVCController(mvcController);
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
       bodyComponent.setStyle("-fx-background-color: #e4e5f1\t");
       headerComponent.setStyle("-fx-background-color: #e4e5f1\t");

      } else {
         bodyComponent.setStyle("-fx-background-color:  #36393e\t");
         headerComponent.setStyle("-fx-background-color: #36393e\t");


      }


   }

}
