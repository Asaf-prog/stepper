package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.dataDefinition.impl.enumerator.MethodEnum;
import modules.dataDefinition.impl.enumerator.Protocol;
import modules.dataDefinition.impl.json.JsonData;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.IOException;
import java.util.Optional;

import okhttp3.*;

public class HTTPCall extends AbstractStepDefinition {
    public HTTPCall() {

        super("HTTP Call",false);

        addInput(new DataDefinitionDeclarationImpl("RESOURCE", DataNecessity.MANDATORY, "Resource Name (include query parameters)", DataDefinitionRegistry.STRING));//full path    maybe need to change to listofFiles
        addInput(new DataDefinitionDeclarationImpl("ADDRESS", DataNecessity.MANDATORY, "Domain:Port", DataDefinitionRegistry.STRING));//full path
        addInput(new DataDefinitionDeclarationImpl("PROTOCOL", DataNecessity.MANDATORY, "Protocol", DataDefinitionRegistry.PROTOCOL_ENUMERATOR));//full path
        addInput(new DataDefinitionDeclarationImpl("METHOD", DataNecessity.OPTIONAL, "Method", DataDefinitionRegistry.METHOD_ENUM_ENUMERATION));//full path
        addInput(new DataDefinitionDeclarationImpl("BODY", DataNecessity.OPTIONAL, "Request Body", DataDefinitionRegistry.JASON));//full path

        addOutput(new DataDefinitionDeclarationImpl("CODE", DataNecessity.NA, "Response code", DataDefinitionRegistry.NUMBER));
        addOutput(new DataDefinitionDeclarationImpl("RESPONSE_BODY", DataNecessity.NA, "Response body", DataDefinitionRegistry.STRING));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        StepResult res;
        try{
            res = StepResult.SUCCESS;
        String Resource = context.getDataValue("RESOURCE",String.class);
        String Address = context.getDataValue("ADDRESS",String.class);

        Protocol Protocol = context.getDataValue("PROTOCOL",Protocol.class);
        Optional<MethodEnum> Method = Optional.ofNullable(context.getDataValue("METHOD",MethodEnum.class));
        Optional<JsonData> Body = Optional.ofNullable(context.getDataValue("BODY", JsonData.class));

       // Protocol protocol = Protocol.orElse(Protocol.HTTP);
        String url = buildUrl(Protocol,Address,Resource);
        Request.Builder requestBuilder = new Request
                .Builder().
                url(url);

        MethodEnum method = Method.orElse(MethodEnum.GET);
        JsonData body = Body.orElse(new JsonData("{}"));//Empty body

        switch (method) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), body.toString()));
                break;
            case PUT:
                requestBuilder.put(RequestBody.create(MediaType.parse("application/json"), body.toString()));
                break;
            case DELETE:
                requestBuilder.delete(RequestBody.create(MediaType.parse("application/json"), body.toString()));
                break;
            default:
                context.addSummaryLine("HTTP Call","Step failed cause invalid method");
                res = StepResult.FAILURE;
        }
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(requestBuilder.build());

            context.setLogsForStep("HTTP Call","About to invoke http request: " + Protocol + " | " + method + " | " + Address + " | " + Resource);
            Response response = call.execute();
            context.setLogsForStep("HTTP Call","Received Response. Status code: " + response.code());
            context.addSummaryLine("HTTP Call","Received Response. Status code: " + response.code());
            context.storeDataValue("CODE",response.code());
            String responseBody = response.body() != null ? response.body().string() : "";
            context.storeDataValue("RESPONSE_BODY",responseBody);

        }catch (IOException e){
            context.addSummaryLine("HTTP Call","Step failed cause error occurred during the http call");
            res = StepResult.FAILURE;
        }
        return res;
    }
    private String buildUrl(Protocol Protocol, String Address, String Resource){
        StringBuilder buildUrl = new StringBuilder();
        buildUrl.append(Protocol.toString())
                .append("://")
                .append(Address)
                .append(Resource);

        return buildUrl.toString();
    }
}
