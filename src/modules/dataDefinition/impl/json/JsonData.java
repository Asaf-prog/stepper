package modules.dataDefinition.impl.json;

import com.google.gson.*;

import java.io.Serializable;

public class JsonData  implements Serializable{
    private JsonElement jsonObject;
    private final Gson gson = new Gson();
    public JsonData(String json) throws JsonSyntaxException {
        Object o = gson.fromJson(json,Object.class);
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

