package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

public class PersonDetailsStep extends AbstractStepDefinition {

    public PersonDetailsStep() {
        super("STEP 1", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("STRING_1", DataNecessity.MANDATORY, "First Name", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("STRING_2", DataNecessity.OPTIONAL, "Last Name", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("AGE", DataNecessity.MANDATORY, "Age", DataDefinitionRegistry.DOUBLE));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DETAILS", DataNecessity.NA, "Full Person Details", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        // fetch inputs here, somehow
        String firstName = context.getDataValue("STRING_1", String.class);
        String lastName = context.getDataValue("STRING_2", String.class);
        Double age = context.getDataValue("AGE", Double.class);

        // do some complex logic...
        String greeting = "Hello " + firstName.toUpperCase() + " " + lastName.toUpperCase() + "! You were born " + age + " Years ago !";

        // add outputs here, somehow
        context.storeDataValue("DETAILS", greeting);

        // through the context, as part of writing the step's logic I should be able to:
        // 1. add log lines
        // 2. add summary line

        // return result
        return StepResult.SUCCESS;
    }
}
