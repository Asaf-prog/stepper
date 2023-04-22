package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PropertiesExporter extends AbstractStepDefinition {

        public PropertiesExporter() {
            super("Properties Exporter", true);

            addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, " Source data", DataDefinitionRegistry.RELATION));

            addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Result", DataDefinitionRegistry.STRING));

        }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        RelationData relationTable = context.getDataValue("SOURCE", RelationData.class);
       // String fileName = context.getDataValue("FILE_NAME", String.class);
        context.setLog("Properties Exporter", "About to process "+relationTable.getRows().size() +" lines of data");
        RelationData data=relationTable;

        File file = new File("PROP");
        try {
            FileWriter fileWriter = new FileWriter(file);
            // loop through rows and columns to write data in properties format
            for (int i = 0; i < relationTable.getRows().size(); i++) {
                RelationData.SingleRow row = relationTable.getRows().get(i);
                for (int j = 0; j < row.getData().size(); j++) {
                    String key = "Row" + (i+1) + "=Column" + (j+1);
                    String value = row.getData().get(j);
                    fileWriter.write(key + "=" + value + "\n");
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            context.setLog("Properties Exporter", "Error exporting relation data: " + e.getMessage());
            return StepResult.FAILURE;
        }
        if ( relationTable.getRows().size()==0)  {
            //empty table means warning
            context.setLog("Properties Exporter", "Warning: Empty Table=Empty File ! ");
            return StepResult.WARNING;
        }
        // check if file created
        if (file.exists()) {
            System.out.println(relationTable);
            context.setLog("Properties Exporter", "Extracted total of "+relationTable.getRows().size());
            return StepResult.SUCCESS;
        } else {
            context.setLog("Properties Exporter", "Error exporting relation data to file: " + fileName);
            return StepResult.FAILURE;
        }

    }
}
