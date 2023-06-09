package modules.step.api;

import modules.dataDefinition.api.DataDefinition;
import modules.flow.execution.context.StepExecutionContext;

import java.io.IOException;
import java.util.List;

public interface StepDefinition {
    String name();
    boolean isReadonly();
    List<DataDefinitionDeclaration> inputs();
    List<DataDefinitionDeclaration> outputs();
    StepResult invoke(StepExecutionContext context) throws IOException;

    String getName();

    DataDefinition getDataDefinitionByName(String DDName);
    DataDefinitionDeclaration getDataDefinitionDeclarationByName(String DDName);
    DataDefinition getDataDefinitionByNameTarget(String DDName);
    DataDefinitionDeclaration getDataDefinitionDeclarationByNameInputList(String DDName);
}
