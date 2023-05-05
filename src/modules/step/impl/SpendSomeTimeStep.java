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
        addInput(new DataDefinitionDeclarationImpl("Time_To_Spend", DataNecessity.MANDATORY, "Time", DataDefinitionRegistry.NUMBER));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        int seconds = context.getDataValue("Time_To_Spend", Integer.class);
        try{
            if (seconds <= 0) {
                System.out.println("Invalid input");
                context.setLogsForStep("Time_To_Spend","Get un-positive number");
                context.addSummaryLine("Time_To_Spend","The step fail because the input is un-positive number");
                return StepResult.FAILURE;
            }
            try {
                context.setLogsForStep("Spend Some Time","About to sleep for " + seconds + " seconds...");
                Thread.sleep(seconds*1000);
                context.setLogsForStep("Spend Some Time","Done sleeping...");

            } catch (InterruptedException e) {
                System.out.println("Error");
                context.addSummaryLine("Time_To_Spend","The step was fall and this is why their is no summary line");
                return StepResult.FAILURE;
            }
        }
        catch (NumberFormatException e){
            System.out.println("Invalid input");
            context.addSummaryLine("Time_To_Spend","The step was fall and this is why their is no summary line");
            return StepResult.FAILURE;
        }
        context.addSummaryLine("Time_To_Spend","The step ended with success");
        return StepResult.SUCCESS;
    }
}

