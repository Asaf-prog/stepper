package modules.step;

import modules.step.api.StepDefinition;
import modules.step.impl.*;

public enum StepDefinitionRegistry {
    HELLO_WORLD(new HelloWorldStep()),
    PERSON_DETAILS(new PersonDetailsStep())
    ,SPEND_SOME_TIME(new SpendSomeTimeStep()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolderStep()),
    FILES_DELETER (new FilesDeleterStep()),
    FILE_RENAMER_STEP(new FilesRenamerStep());//this is the real steps
    private final StepDefinition stepDefinition;
    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
}
