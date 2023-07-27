package modules.dataDefinition.impl.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;

public class JsonData implements Serializable {
    private JsonObject jsonObject;
    public JsonData(String json) throws JsonSyntaxException {
        //jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Gson gson = new Gson();
       Object o = gson.fromJson(json,Object.class);
         jsonObject = gson.fromJson(json, JsonObject.class);
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
