package ClientsApp.app.body.bodyInterfaces;

import ClientsApp.app.body.bodyController;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;
import services.stepper.FlowDefinitionDTO;
import services.stepper.flow.DataDefinitionDeclarationDTO;

import java.util.List;
import java.util.Map;

public interface bodyControllerForContinuation {
    void showForContinuation();
    void setCurrentFlowForContinuation(FlowDefinitionDTO flow);
    void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclarationDTO>> mandatory,
                                        List<Pair<String, DataDefinitionDeclarationDTO>> optional,List<Pair<String, String>>mandatoryIn,
                                        List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl currentFlow);
    void setBodyControllerContinuation(bodyController body);
}
