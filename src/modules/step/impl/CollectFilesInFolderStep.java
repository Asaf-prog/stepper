package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import java.util.ArrayList;
import java.util.List;

public class CollectFilesInFolderStep extends AbstractStepDefinition {

        public CollectFilesInFolderStep() {
            super("Collect files in folder", true);
            addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "folder name", DataDefinitionRegistry.STRING));//full path
            addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "filter", DataDefinitionRegistry.STRING));

            addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "file list", DataDefinitionRegistry.LIST));
            addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", DataNecessity.NA, "file count", DataDefinitionRegistry.NUMBER));
        }
        @Override
        public StepResult invoke(StepExecutionContext context){
            String folderName = context.getDataValue("FOLDER_NAME", String.class);
            String filter = context.getDataValue("FILTER", String.class);
            //todo - implement the rest
            //open folder from folderName=path
            //get all files in folder
            if (filter != null){
                //filter files by filter
            }
            //return list of files
            //return total count of files
            //list<String> filesList = new ArrayList<>();

            context.storeDataValue("FILES_LIST", null);
            context.storeDataValue("TOTAL_FOUND", null);
            return StepResult.SUCCESS;
        }
}
