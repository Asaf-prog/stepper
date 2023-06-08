package app.management;

import app.MVC_controller.MVC_controller;
import app.body.bodyController;
import app.body.statsScreen.StatsScreen;
import app.header.headerController;
import app.management.style.StyleManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.awt.*;
import java.util.List;

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
   private double xOffset;
   private double yOffset;
   private FlowDefinitionImpl currentFlow;
   @FXML
   public void initialize() {
      //appBoxStyle = appBox.getStyle();
      //appBox.setStyle(appBoxStyle + "-fx-background-radius: 20;");
      StyleManager.setTheme(StyleManager.getCurrentTheme());

      if (headerComponentController != null && bodyComponentController != null) {
         headerComponentController.setMainController(this);
         bodyComponentController.setMainController(this);


         //initialize a controller that communicate with the engine
         mvcController = new MVC_controller(this,headerComponentController,bodyComponentController);
         bodyComponentController.setMVCController(mvcController);
         headerComponentController.setMVCController(mvcController);

      }

   }

   public void setCurrentFlow(FlowDefinitionImpl flow) {
      this.currentFlow = flow;
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
      bodyComponentController.setCurrentFlow(currentFlow);
      bodyComponentController.showAllFlowAndExe();
    }
    public headerController getHeaderComponentController(){
      return headerComponentController;
    }
}
