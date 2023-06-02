package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandLine extends AbstractStepDefinition {
    public CommandLine() {
        super("Command Line", false);
         addInput(new DataDefinitionDeclarationImpl("COMMAND", DataNecessity.MANDATORY, "Command", DataDefinitionRegistry.STRING ));
        addInput(new DataDefinitionDeclarationImpl("ARGUMENTS", DataNecessity.OPTIONAL, "Command arguments", DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Command output", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        String command = context.getDataValue("COMMAND", String.class);
        String arguments = context.getDataValue("ARGUMENTS", String.class);
        context.setLogsForStep("Command Line", "About to invoke: " + command + " " + arguments);
        if (arguments != null) {
            command = command + " " + arguments;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command.split(" "));

            if (arguments != null && !arguments.isEmpty()) {
                processBuilder.command().addAll(Arrays.asList(arguments.split(" ")));
            }

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                context.setLogsForStep("Command Line", "Command executed successfully: " + command);
                context.addSummaryLine("Command Line", "Command executed successfully");
                context.storeDataValue("RESULT", output.toString().trim());
                return StepResult.SUCCESS;
            } else {
                context.setLogsForStep("Command Line", "Command execution failed: " + command);
                context.addSummaryLine("Command Line", "Command execution failed");
                return StepResult.FAILURE;
            }
        } catch (IOException | InterruptedException e) {
            context.setLogsForStep("Command Line", "An error occurred while executing the command: " + command);
            context.addSummaryLine("Command Line", "An error occurred while executing the command");
            return StepResult.FAILURE;
        }
    }
}
