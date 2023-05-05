package modules.step.impl;


import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.StepResult;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.dataDefinition.impl.DataDefinitionRegistry;

public class CSVExporter extends AbstractStepDefinition {

        public CSVExporter() {
            super("CSV Exporter", true);

            addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, " Source data", DataDefinitionRegistry.RELATION));

            addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Result", DataDefinitionRegistry.STRING));

        }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        RelationData dataToConvert = context.getDataValue("SOURCE", RelationData.class);
        if (dataToConvert.getNumColumns() != 0) {
            StringBuilder csvString = new StringBuilder();
            context.setLogsForStep("CSV Exporter", "About to process " + dataToConvert.getRows().size() + " lines of data");
            for (int i = 0; i < dataToConvert.getNumColumns(); i++) {
                csvString.append(dataToConvert.getValInList(i));
                if (i != dataToConvert.getNumColumns() - 1) {
                    csvString.append(",");
                }
            }
            csvString.append("\n");

            for (int i = 0; i < dataToConvert.getNumRows(); i++) {

                for (int j = 0; j < dataToConvert.getNumColumns(); j++) {

                    csvString.append(dataToConvert.getValueAt(i, j));

                    if (j != dataToConvert.getNumColumns() - 1) {
                        csvString.append(",");
                    }
                }
                csvString.append("\n");
            }
            String res= String.valueOf(csvString);
            context.storeDataValue("RESULT",res);
            context.addSummaryLine("CSV Exporter","End with Success.");
            return StepResult.SUCCESS;
        }
        context.addSummaryLine("CSV Exporter"," The table was Empty");
        context.setLogsForStep("CSV Exporter"," The table is Empty");
        return StepResult.WARNING;
    }
}

