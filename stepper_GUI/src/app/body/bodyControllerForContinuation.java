package app.body;

import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.List;

public interface bodyControllerForContinuation {
    void showForContinuation();
    void setCurrentFlowForContinuation(FlowDefinitionImpl flow);
    void SetCurrentMandatoryAndOptional(List<Pair<String, DataDefinitionDeclaration>> mandatory,
                                        List<Pair<String, DataDefinitionDeclaration>> optional);
}
