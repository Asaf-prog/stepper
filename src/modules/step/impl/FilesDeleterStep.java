package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.file.FileData;
import modules.dataDefinition.impl.mapping.Mapping;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesDeleterStep extends AbstractStepDefinition {
    public FilesDeleterStep() {
        super("Files Deleter", false);

        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.LIST));//full path

        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.MAPPING));
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", DataNecessity.NA, "Deletion summary results", DataDefinitionRegistry.LIST));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        List<String> survivingFiles = new ArrayList<>();
        List<String> deletedFiles = new ArrayList<>();
        List <FileData> name = context.getDataValue("FILES_LIST", List.class);
        if (name.size() ==0 )
            return StepResult.SUCCESS;
            //todo summary line
        else {
            for (FileData filedata : name) {
                Object runnerFile = filedata.getFile();
                if (runnerFile instanceof File) {
                   context.setLog("Files Deleter","About to start delete"+name.size() +"files");
                    if (((File) runnerFile).exists() && ((File) runnerFile).isFile()) {
                        deletedFiles.add(((File) runnerFile).getName());
                        ((File) runnerFile).delete();
                    }
                    else {
                        survivingFiles.add(((File) runnerFile).getName());
                        context.setLog("Files Deleter","Failed to delete file"+((File) runnerFile).getName());
                    }
                }
                else
                    return StepResult.FAILURE;

            }
        }
        if (deletedFiles.size() == 0){
            deletedFiles.add("0");
            deletedFiles.add("0");
            //summary line
        }
        Mapping<Integer,Integer> deletionStats = new Mapping<>(deletedFiles.size(),survivingFiles.size());
        context.storeDataValue("DELETION_STATS",survivingFiles);
        context.storeDataValue("TOTAL_FOUND",deletionStats);
        //car :number of successfully deleted files
        //cdr :number of unsuccessfully deleted files
        //summary line
        return StepResult.SUCCESS;
    }
}

