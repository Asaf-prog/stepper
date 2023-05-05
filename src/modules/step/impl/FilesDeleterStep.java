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
import java.nio.file.Path;
import java.nio.file.Paths;
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
        List <FileData> filesList = context.getDataValue("FILES_LIST", List.class);
        if (filesList.size() ==0 ) {
            context.addSummaryLine("Files Deleter", "The list of file is empty");
            return StepResult.SUCCESS;
        }else {
            context.setLogsForStep("Files Deleter","About to start delete "+filesList.size() +" files");
            for (FileData filedata : filesList) {
                Object runnerFile = filedata.getFile();
                if (runnerFile instanceof File) {
                    if (((File) runnerFile).exists() && ((File) runnerFile).isFile()) {
                        deletedFiles.add(((File) runnerFile).getName());
                        ((File) runnerFile).delete();
                    }
                    else {
                        Path path = Paths.get(((File) runnerFile).getAbsolutePath());
                        survivingFiles.add(path.toString());
                        context.setLogsForStep("Files Deleter","Failed to delete file "+((File) runnerFile).getName());
                    }
                }
                else {
                    context.addSummaryLine("Files Deleter", "File deleter failed to delete one of");
                    return StepResult.FAILURE;
                }
            }
        }
        if (deletedFiles.size() == 0){
            deletedFiles.add("0");
            deletedFiles.add("0");
            //summary line
        }
        int car = deletedFiles.size();
        int cdr = survivingFiles.size();
        Mapping<Integer,Integer> deletionStats = new Mapping<>(deletedFiles.size(),survivingFiles.size());
        if (cdr == 0)
            survivingFiles.add(" is Empty...");

        context.storeDataValue("DELETION_STATS",deletionStats);
        context.storeDataValue("DELETED_LIST",survivingFiles);
        //car :number of successfully deleted files
        //cdr :number of unsuccessfully deleted files
        context.addSummaryLine("Files Deleter","Success to delete file");

        return StepResult.SUCCESS;
    }
}

