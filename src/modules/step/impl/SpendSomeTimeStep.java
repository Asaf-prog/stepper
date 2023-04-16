package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

public class SpendSomeTimeStep extends AbstractStepDefinition {
    public SpendSomeTimeStep() {
        super("SpendSomeTime", true);
        addInput(new DataDefinitionDeclarationImpl("Time_To_Spend", DataNecessity.MANDATORY, "Time", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        String time = context.getDataValue("Time_To_Spend", String.class);
        try{
            int seconds = Integer.parseInt(time);
            if (seconds <= 0) {
                System.out.println("Invalid input");
                return StepResult.FAILURE;
            }
            try {
                System.out.println("About to sleep for " + seconds + " seconds...");
                Thread.sleep(seconds*1000);
                System.out.println("Done sleeping...");
            } catch (InterruptedException e) {
                System.out.println("Error");
                return StepResult.FAILURE;
            }
        }
        catch (NumberFormatException e){
            System.out.println("Invalid input");
            return StepResult.FAILURE;
        }
        return StepResult.SUCCESS;

    }
}

