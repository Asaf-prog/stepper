package modules.step.impl;
import java.io.File;
import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.file.FileData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.nio.file.Files.exists;

public class CollectFilesInFolderStep extends AbstractStepDefinition {

    public CollectFilesInFolderStep() {
        super("Collect files in folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING));//full path
        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "Files list", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Optional<String> folderNameOptional = Optional.ofNullable(context.getDataValue("FOLDER_NAME", String.class));
        Optional<String> filterOptional = Optional.ofNullable(context.getDataValue("FILTER", String.class));
        if (folderNameOptional.isPresent()) {
            String folderName = folderNameOptional.get();
            Path path = Paths.get(folderName);//folder path
            context.setLogsForStep("Collect files in folder", "Reading folder " + path.getFileName() + " content with filter " + filterOptional.orElse("none"));

            if (!exists(path)) {
                context.setLogsForStep("Collect files in folder", " Folder " + path.getFileName() + " does not exist");
                context.addSummaryLine("CSV Exporter","End with Failure due folder not exist.");
                return StepResult.FAILURE;
            }
            if (!path.toFile().isDirectory()) {
                context.setLogsForStep("Collect files in folder", " Folder " + path.getFileName() + " is not a directory");
                context.addSummaryLine("CSV Exporter","End with Failure due folder not a directory.");
                return StepResult.FAILURE;
            }
            if (!path.toFile().canRead()) {
                context.setLogsForStep("Collect files in folder", " Folder " + path.getFileName() + " is not readable");
                context.addSummaryLine("CSV Exporter","End with Failure due folder not readable.");
                return StepResult.FAILURE;
            }

            List<File> fileList;
            if (!filterOptional.isPresent() || filterOptional.get().isEmpty()) {//filter is empty
                try {
                    fileList = Files.list(path)
                            .filter(Files::isRegularFile)
                            .map(Path::toFile)
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    context.setLogsForStep("Collect files in folder", "Failed to collect files in folder: " + e.getMessage());
                    context.addSummaryLine("CSV Exporter","End with Failure due folder not readable.");
                    return StepResult.FAILURE;
                }
            } else {
                String filter = filterOptional.get();
                try {
                    fileList = Files.list(path)
                            .filter(Files::isRegularFile)
                            .filter(p -> p.getFileName().toString().endsWith(filter))
                            .map(Path::toFile)
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    context.setLogsForStep("Collect files in folder", "Failed to collect files in folder with filter " + filter + ": " + e.getMessage());
                    context.addSummaryLine("CSV Exporter","End with Failure due unknown error");
                    return StepResult.FAILURE;
                }
            }

            //converting to file data
            List<FileData> filesList = new ArrayList<>();
            for (File file : fileList) {
                if (file.isFile()) {
                    String check = file.getName();
                    if (!(check.equals(".DS_Store"))) {
                        FileData fileData = new FileData(file);
                        filesList.add(fileData);
                    }
                }
            }
            context.storeDataValue("FILES_LIST", filesList);
            context.storeDataValue("TOTAL_FOUND", filesList.size());
            context.setLogsForStep("Collect files in folder", "Found "+filesList.size()+" files in folder matching the filter");
            context.addSummaryLine("Collect files in folder","The step end with success");
            return StepResult.SUCCESS;
        } else {
            context.setLogsForStep("Collect files in folder", "Folder name not provided");
            return StepResult.FAILURE;
        }
    }
}
