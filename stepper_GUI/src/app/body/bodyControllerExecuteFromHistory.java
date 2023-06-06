package app.body;

import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public interface bodyControllerExecuteFromHistory {
    void showFromHistory();
    void setBodyControllerFromHistory(bodyController body);
    void SetCurrentFlowFromHistory(FlowDefinitionImpl flow);
    void setFreeInputsMandatoryAndOptional(List<Pair<String, String>> freeInputMandatory,
                                           List<Pair<String, String>> freeInputOptional);
}
