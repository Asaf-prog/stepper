package modules.step.impl;
import java.io.File;
import java.io.FilenameFilter;
import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.file.FileData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectFilesInFolderStep extends AbstractStepDefinition {

    public CollectFilesInFolderStep() {
        super("Collect files in folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "folder name", DataDefinitionRegistry.STRING));//full path
        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "filter", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "file list", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", DataNecessity.NA, "file count", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Optional<String> folderNameOptional = Optional.ofNullable(context.getDataValue("FOLDER_NAME", String.class));
        Optional<String> filterOptional = Optional.ofNullable(context.getDataValue("FILTER", String.class));

        if (folderNameOptional.isPresent()) {
            String folderName = folderNameOptional.get();
            File folder = new File(folderName);
            File[] listOfFiles;
            if (filterOptional.isPresent() && !filterOptional.get().isEmpty()) {
                FilenameFilter filenameFilter = (dir, name) -> name.endsWith(filterOptional.get());
                listOfFiles = folder.listFiles(filenameFilter);
            } else {
                listOfFiles = folder.listFiles();
            }
            List<FileData> filesList = new ArrayList<>();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    FileData fileData = new FileData(file);
                    filesList.add(fileData);
                }
            }
            context.storeDataValue("FILES_LIST", filesList);
            context.storeDataValue("TOTAL_FOUND", filesList.size());
            return StepResult.SUCCESS;
        }
        return StepResult.FAILURE;
    }

