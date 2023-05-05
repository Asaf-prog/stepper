package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

public class PropertiesExporter extends AbstractStepDefinition {

        public PropertiesExporter() {
            super("Properties Exporter", true);

            addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, " Source data", DataDefinitionRegistry.RELATION));

            addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Result", DataDefinitionRegistry.STRING));

        }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        RelationData relationTable = context.getDataValue("SOURCE", RelationData.class);
        context.setLogsForStep("Properties Exporter", "About to process "+relationTable.getRows().size() +" lines of data");
        boolean warning=false;
        if (relationTable.getColumns().size() <= 2) {
            context.setLogsForStep("Properties Exporter", "Warning: Source table must have 2 columns");
            //maybe store empty string or with a warning
            warning=true;
            //continue
        }
        if (relationTable.isEmpty()) {
            context.setLogsForStep("Properties Exporter", "Warning: Source table is empty");
            warning=true;
            //continue
        }
        StringBuilder propertiesBuilder = new StringBuilder();
        propertiesBuilder.append(relationTable.getColumns().get(0));
        for (int i = 1; i < relationTable.getColumns().size()-1; i++) {

            propertiesBuilder.append(".").append(relationTable.getColumns().get(i));
        }
        propertiesBuilder.append("=");
        propertiesBuilder.append(relationTable.getColumns().get(relationTable.getColumns().size()-1));
        propertiesBuilder.append("\n");
       //v2: propertiesBuilder.append(relationTable.getColumns().get(0)).append("=").append(relationTable.getColumns().get(1)).append("\n");

        for (RelationData.SingleRow row : relationTable.getRows()) {
        propertiesBuilder.append(row.getData().get(0));
            for (int i = 1; i < row.getData().size()-1; i++) {
                propertiesBuilder.append(".").append(row.getData().get(i));
            }
            propertiesBuilder.append("=").append(row.getData().get(row.getData().size()-1));
            propertiesBuilder.append("\n");//the above isn't a valid properties file format but
            // it was unclear what to present in order to include all the data
//            String key = row.getData().get(0); // this is a valid structure
//            String value = row.getData().get(1);
//            key = key.replace("\\", "\\\\").replace(":", "\\:");
//            value = value.replace("\\", "\\\\").replace("=", "\\=");
//            propertiesBuilder.append(key).append("=").append(value).append("\n");
        }
        if (warning){
            context.setLogsForStep("Properties Exporter", "Extracted total of "+relationTable.getRows().size());
            context.storeDataValue("RESULT", String.valueOf(propertiesBuilder));
            context.addSummaryLine("Properties Exporter","The relation is empty");
            return StepResult.WARNING;
        }

        context.setLogsForStep("Properties Exporter", "Extracted total of "+relationTable.getRows().size());
        context.storeDataValue("RESULT", String.valueOf(propertiesBuilder));
        context.addSummaryLine("Properties Exporter","Finish with success.");
        return StepResult.SUCCESS;

    }
}
