package modules.step.impl;

import com.google.gson.JsonSyntaxException;
import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.json.JasonData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.IOException;

public class ToJson extends AbstractStepDefinition {
    public ToJson() {
        super("To Json", true);

        addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.NA, "Json representation", DataDefinitionRegistry.JASON));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        String content = context.getDataValue("CONTENT", String.class);
        StepResult res = StepResult.SUCCESS;
        try {
            JasonData json = new JasonData(content);
            context.storeDataValue("JSON", json);
            context.setLogsForStep("To Jason","Content is JSON string. Converting it to json…");
            context.addSummaryLine("To Json","Success to convert String to json!");
        }catch (JsonSyntaxException e){
            context.setLogsForStep("To Jason","Content is not a valid JSON representation");
            context.addSummaryLine("To Json","Failed to convert String to Json...");
             res = StepResult.FAILURE;
        }

        return res;
    }
}
