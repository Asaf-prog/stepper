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
        context.setLogs("Properties Exporter", "About to process "+relationTable.getRows().size() +" lines of data");
        boolean warning=false;
        if (relationTable.getColumns().size() <= 2) {
            context.setLogs("Properties Exporter", "Warning: Source table must have 2 columns");
            warning=true;
            //continue
        }
        if (relationTable.isEmpty()) {
            context.setLogs("Properties Exporter", "Warning: Source table is empty");
            warning=true;
            //continue
        }
        StringBuilder propertiesBuilder = new StringBuilder();
        propertiesBuilder.append(relationTable.getColumns().get(0)).append("=").append(relationTable.getColumns().get(1)).append("\n");

        for (RelationData.SingleRow row : relationTable.getRows()) {
            String key = row.getData().get(0);
            String value = row.getData().get(1);
            key = key.replace("\\", "\\\\").replace(":", "\\:");
            value = value.replace("\\", "\\\\").replace("=", "\\=");
            propertiesBuilder.append(key).append("=").append(value).append("\n");
        }
        if (warning){
            context.setLogs("Properties Exporter", "Extracted total of "+relationTable.getRows().size());
            context.storeDataValue("RESULT", String.valueOf(propertiesBuilder));
            return StepResult.WARNING;
        }

        //System.out.println(relationTable);
        context.setLogs("Properties Exporter", "Extracted total of "+relationTable.getRows().size());
        context.storeDataValue("RESULT", String.valueOf(propertiesBuilder));
        return StepResult.SUCCESS;

    }
}
