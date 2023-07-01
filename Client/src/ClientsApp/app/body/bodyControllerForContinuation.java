package ClientsApp.app.body;

import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.List;
import java.util.Map;

public interface bodyControllerForContinuation {
    void showForContinuation();
    void setCurrentFlowForContinuation(FlowDefinitionImpl flow);
    void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                        List<Pair<String, DataDefinitionDeclaration>> optional,List<Pair<String, String>>mandatoryIn,
                                        List<Pair<String, String>>optionalIn, Map<String,Object> outputs,FlowDefinitionImpl currentFlow);
    void setBodyControllerContinuation(bodyController body);
}
