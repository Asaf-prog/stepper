package modules.step;

import modules.step.api.StepDefinition;
import modules.step.impl.*;

public enum StepDefinitionRegistry {
    SPEND_SOME_TIME(new SpendSomeTimeStep()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolderStep()),
    FILES_DELETER(new FilesDeleterStep()),
    FILES_RENAMER(new FilesRenamerStep()),
    FILE_DUMPER(new FileDumper()),
    PROPERTIES_EXPORTER(new PropertiesExporter()),
    CSV_EXPORTER(new CSVExporter()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractor()),
    ZIPPER(new Zipper()),
    COMMAND_LINE(new CommandLine())

    ;//this is the real steps
    private final StepDefinition stepDefinition;
    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
    public static StepDefinition getStepDefinitionByName(String name){
        String fixedName = name.replaceAll("[ _]", "").toUpperCase();

        for (StepDefinitionRegistry stepDefinitionRegistry : StepDefinitionRegistry.values()) {
            String mid=stepDefinitionRegistry.getStepDefinition().getName().replaceAll("[ _]", "").toUpperCase();
            if (mid.equals(fixedName)){

                return stepDefinitionRegistry.getStepDefinition();
            }
        }
        return null;
    }
}
