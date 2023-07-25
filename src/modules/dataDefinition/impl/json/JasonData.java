package modules.dataDefinition.impl.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParser;

import java.io.Serializable;

public class JasonData implements Serializable {
    private JsonObject jsonObject;
    public JasonData(String json) throws JsonSyntaxException {
        //jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Gson gson = new Gson();
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
