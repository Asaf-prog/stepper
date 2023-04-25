package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import modules.step.api.DataDefinitionDeclarationImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileDumper extends AbstractStepDefinition {


    public FileDumper() {
        super("File Dumper", true);

        //inputs and outputs
        addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "File Creation Result", DataDefinitionRegistry.STRING));

    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        String content = context.getDataValue("CONTENT", String.class);
        String filePath = context.getDataValue("FILE_NAME", String.class);
        System.out.println(filePath);
        String fileName=filePath.substring(filePath.lastIndexOf("/")+1);

        context.setLog("File Dumper", "About to create file: " + fileName);
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {//check if it counts as len = 0 exception
            context.setLog("File Dumper", "Error Crafting the file: " + e.getMessage());
            return StepResult.FAILURE;
        }
        // check if file created
        if (file.exists()) {
            if (content.length()==0) {
                context.setLog("File Dumper", "Warning file created but as Empty file ");
                return StepResult.WARNING;
            }
            context.setLog("File Dumper", "File created: " + fileName);
            return StepResult.SUCCESS;
        } else {
            context.setLog("File Dumper", "Error Exporting file: " + fileName);
            return StepResult.FAILURE;
        }
    }
}
