package app.management;

import app.body.bodyController;
import app.header.headerController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class mainController {
   private List<FlowDefinitionImpl> flows;
   @FXML private Parent headerComponent;
   @FXML private headerController headerComponentController;
   @FXML private Parent bodyComponent;
   @FXML private bodyController bodyComponentController;
   public void PresentTpBodyFlowDefinition(){

   }
   @FXML
   public void initialize() {
      if (headerComponentController != null && bodyComponentController != null) {
         headerComponentController.setMainController(this);
         bodyComponentController.setMainController(this);
      }
   }
   public void setFlows(List<FlowDefinitionImpl> f){
       this.flows = f;
   }
   public void showFlowDefinition() {
      bodyComponentController.showFlowDefinition();
   }
   public List<FlowDefinitionImpl> getFlows(){
      return flows;
   }
}
