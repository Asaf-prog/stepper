package modules.step.api;

import com.sun.xml.internal.bind.XmlAccessorFactory;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.context.StepExecutionContext;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
@XmlRegistry
@XmlSeeAlso(AbstractStepDefinition.class)
public interface StepDefinition {

    String name();


    boolean isReadonly();


    List<DataDefinitionDeclarationImpl> inputs();


    List<DataDefinitionDeclarationImpl> outputs();
    StepResult invoke(StepExecutionContext context) throws IOException;

    String getName();

    DataDefinition getDataDefinitionByName(String DDName);
    DataDefinition getDataDefinitionByNameTarget(String DDName);
}
