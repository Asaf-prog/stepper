package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import modules.dataDefinition.impl.file.FileData;

import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesRenamerStep extends AbstractStepDefinition {
    public FilesRenamerStep() {
        super("Files Renamer", false);

        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.LIST));//full path    maybe need to change to listofFiles
        addInput(new DataDefinitionDeclarationImpl("PREFIX", DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));//full path
        addInput(new DataDefinitionDeclarationImpl("SUFFIX", DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING));//full path

        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT", DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));

    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        List<FileData> filesToRename = context.getDataValue("FILES_TO_RENAME", List.class);
        boolean warning = false;
        List<String> faileds = new ArrayList<>();//failed files
        //make the next string as optional because they are optional
        String prefix = context.getDataValue("PREFIX", String.class);
        String suffix = context.getDataValue("SUFFIX", String.class);
        context.setLog("Files Renamer","About to start rename "+filesToRename.size()+" files , Adding prefix:"+prefix+" Adding suffix:"+suffix);

        for (FileData fileData : filesToRename) {
            String folder =fileData.getFile().getParent() +File.separator;
            String oldFileName = fileData.getName();
            String newFileName = prefix + oldFileName.substring(0, oldFileName.lastIndexOf(".")) + suffix + ".txt";
            String renamedFilePath = folder + newFileName;

            if (!fileData.getFile().renameTo(new File(renamedFilePath))) {
               //means Warning
                warning = true;
                faileds.add(oldFileName);
                context.setLog("Files Renamer","Failed to rename file:"+oldFileName);
            }
        }
        if (warning){
            context.setLog("Files Renamer","Failed to rename some files:"+faileds);
            return StepResult.WARNING;
        }

        //todo add output table with the new file names and store
        context.setLog("Files Renamer","All files renamed successfully");
        return StepResult.SUCCESS;
    }
}

