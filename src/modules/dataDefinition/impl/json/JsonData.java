package modules.dataDefinition.impl.json;
import com.google.gson.*;
import java.io.Serializable;
import static com.google.gson.JsonParser.*;
public class JsonData  implements Serializable{
    private JsonElement jsonObject;
    private Gson gson = new Gson();
    public JsonData(String json) throws JsonSyntaxException {
        //jsonObject = JsonParser.parseString(json).getAsJsonObject();
         jsonObject = gson.fromJson(json, JsonObject.class);
        jsonObject = JsonParser.parseString(json);
    }
    @Override
    public String toString(){
        if(jsonObject != null){
            return jsonObject.toString();
        }
        else
            return "Invalid Json";
    }
}

