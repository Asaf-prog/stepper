package ClientsApp.app.body.bodyInterfaces;

import ClientsApp.app.body.bodyController;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;
import services.stepper.FlowDefinitionDTO;

import java.util.List;

public interface bodyControllerExecuteFromHistory {
    void showFromHistory();
    void setBodyControllerFromHistory(bodyController body);
    void SetCurrentFlowFromHistory(FlowDefinitionDTO flow);
    void setFreeInputsMandatoryAndOptional(List<Pair<String, String>> freeInputMandatory,
                                           List<Pair<String, String>> freeInputOptional,
                                           List<Pair<String, DataDefinitionDeclaration>> freeInputsMandatoryWithDD ,
                                           List<Pair<String, DataDefinitionDeclaration>> freeInputsOptionalWithDD );
}
