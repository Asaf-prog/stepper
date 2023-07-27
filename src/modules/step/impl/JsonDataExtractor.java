package modules.step.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.json.JsonData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;
import net.minidev.json.JSONArray;

import java.io.IOException;

public class JsonDataExtractor extends AbstractStepDefinition {
    public JsonDataExtractor() {
        super("Json Data Extractor", true);

        addInput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JASON));
        addInput(new DataDefinitionDeclarationImpl("JSON_PATH", DataNecessity.MANDATORY, "Data", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("VALUE", DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        JsonData JSON = context.getDataValue("JSON", JsonData.class);
        String JSON_PATH = context.getDataValue("JSON_PATH",String.class);
        StepResult res = StepResult.SUCCESS;
        try {
            String jsonString = JSON.toString();
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
            JSONArray extractedData = JsonPath.read(document, JSON_PATH);
            StringBuilder dataThatExtract = new StringBuilder();
            for (int i = 0; i < extractedData.size(); i++) {
                dataThatExtract.append(extractedData.get(i));
                if (i < extractedData.size() - 1) {
                    dataThatExtract.append(", ");
                }
            }
            if(extractedData.size() == 0){// still end with successes
                context.setLogsForStep("Json Data Extractor","No value found for json path "+ JSON_PATH);
            }
            else{
                context.setLogsForStep("Json Data Extractor","Extracting data "+ JSON_PATH+". Value: "+ dataThatExtract);
            }
            context.storeDataValue("VALUE",dataThatExtract.toString());
            context.addSummaryLine("Json Data Extractor","Data successfully extracted");

        }catch (PathNotFoundException e){
            res = StepResult.FAILURE;
            context.addSummaryLine("Json Data Extractor","Failure cause Invalid JsonPath ");
        }
          return res;
    }
}

