package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import modules.dataDefinition.impl.file.FileData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesRenamerStep extends AbstractStepDefinition {
    public FilesRenamerStep() {
        super("Files Renamer", false);

        addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME", DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.LIST));//full path    maybe need to change to listofFiles
        addInput(new DataDefinitionDeclarationImpl("PREFIX", DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));//full path
        addInput(new DataDefinitionDeclarationImpl("SUFFIX", DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING));//full path

        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT", DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));

    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        List<FileData> filesToRename = context.getDataValue("FILES_TO_RENAME", List.class);
        List<String>cols= Arrays.asList(new String[]{"Serial Number", "File Name Before Change", "File Name After Change"});
        RelationData outputTable = new RelationData(cols);
        ArrayList<String> fileBeforeChange = new ArrayList<>();
        ArrayList<String> fileAfterChange = new ArrayList<>();
        int numOfFiles=filesToRename.size();
        if (filesToRename == null){
            context.addSummaryLine("Files Renamer","Name of file not entered");
            return StepResult.FAILURE;
        }
        for (FileData f : filesToRename) {
            fileBeforeChange.add(f.getName());//check if really the name and not the path!!!
        }

        boolean warning = false;
        List<String> faileds = new ArrayList<>();//failed files

        //make the next string as optional because they are optional

        String prefix = context.getDataValue("PREFIX", String.class);
        String suffix = context.getDataValue("SUFFIX", String.class);
        if (prefix == null && suffix == null) {
            context.setLogsForStep("Files Renamer","About to start rename "+filesToRename.size()+" files , without adding prefix or suffix");
            context.addSummaryLine("Files Renamer","No changes were made to the files becuase no prefix or suffix were entered");
            createTableForNoChange(filesToRename,outputTable);
            context.storeDataValue("RENAME_RESULT",outputTable);
            return StepResult.SUCCESS;
        }
        if (prefix == null) {
            context.setLogsForStep("Files Renamer","About to start rename "+filesToRename.size()+" files , Adding suffix:"+suffix);
        } else if (suffix == null) {
            context.setLogsForStep("Files Renamer","About to start rename "+filesToRename.size()+" files , Adding prefix:"+prefix);
        } else
            context.setLogsForStep("Files Renamer","About to start rename "+filesToRename.size()+" files , Adding prefix:"+prefix+" Adding suffix:"+suffix);

        for (FileData fileData : filesToRename) {
            String newFileName;
            String folder =fileData.getFile().getParent() + File.separator;
            String oldFileName = fileData.getName();

            if (suffix == null && prefix == null) {
                 newFileName = oldFileName;//.substring(0, oldFileName.lastIndexOf(".")) ;
            }else if(prefix == null) {
                 newFileName = oldFileName + suffix;
            }else if (suffix == null) {
                 newFileName = prefix + oldFileName ;
            } else {
                 newFileName = prefix + oldFileName + suffix;
            }

            fileAfterChange.add(newFileName);//check if changed correctly
            String renamedFilePath = folder + newFileName;

            if (!fileData.getFile().renameTo(new File(renamedFilePath))) {
               //means Warning
                warning = true;
                faileds.add(oldFileName);
                context.setLogsForStep("Files Renamer","Failed to rename file:"+oldFileName);
            }
        }
        if (warning){
            context.setLogsForStep("Files Renamer","Failed to rename some files:"+faileds);
            context.addSummaryLine("Files Renamer","\"Can not able to convert it to a file.");
            return StepResult.WARNING;
        }

        for (int i = 0; i < numOfFiles; i++) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(i+1));
            row.add(fileBeforeChange.get(i));
            row.add(fileAfterChange.get(i));
            outputTable.addRow(row);
        }

        context.storeDataValue("RENAME_RESULT",outputTable);
        context.setLogsForStep("Files Renamer","All files renamed successfully");
        context.addSummaryLine("Files Renamer","Success to change File name");
        return StepResult.SUCCESS;
    }

    private void createTableForNoChange(List<FileData> filesToRename, RelationData outputTable) {
        for (int i = 0; i < filesToRename.size(); i++) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(i+1));
            row.add(filesToRename.get(i).getName());
            row.add(filesToRename.get(i).getName());
            outputTable.addRow(row);
        }
    }
}

