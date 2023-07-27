package modules.step.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidPathException;
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
import java.util.ArrayList;
import java.util.List;

public class JsonDataExtractor extends AbstractStepDefinition {
    public JsonDataExtractor() {
        super("Json Data Extractor", true);

        addInput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JASON));
        addInput(new DataDefinitionDeclarationImpl("JSON_PATH", DataNecessity.MANDATORY, "Data", DataDefinitionRegistry.STRING));

        addOutput(new DataDefinitionDeclarationImpl("VALUE", DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        JsonData json = context.getDataValue("JSON", JsonData.class);
        String json_path = context.getDataValue("JSON_PATH", String.class);

        String jsonString = json.toString();

        StepResult res = StepResult.SUCCESS;

        try {
            // Parse the JSON string
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);

            // Extract the data using JsonPath
            Object resultVal = JsonPath.read(document, json_path);

            List<Object> extractedData = new ArrayList<>();

            if (resultVal instanceof List<?>) {
                // The result is a list of values
                extractedData = (List<Object>) resultVal;
            } else {
                // The result is a single value
                extractedData.add(resultVal);
            }

            // Append the data with commas
            StringBuilder result = new StringBuilder();
           context.setLogsForStep("Json Data Extractor","About extract data from the Json");

            for (int i = 0; i < extractedData.size(); i++) {
                result.append(extractedData.get(i).toString());
                if (i < extractedData.size() - 1) {
                    result.append(", ");
                }
            }
            if(extractedData.size() == 0)
                context.setLogsForStep("Json Data Extractor","No value found for json path " + json_path);
            else
                context.setLogsForStep("Json Data Extractor","Extracting data " + json_path + ". Value: " + result.toString());

            context.storeDataValue("VALUE", result.toString());
            context.addSummaryLine("Json Data Extractor","Data successfully extracted");
        } catch (InvalidPathException e) {
            context.addSummaryLine("Json Data Extractor","Failure cause Invalid JsonPath expression: " + json_path);
            res = StepResult.FAILURE;
        }
        return res;
    }
}

