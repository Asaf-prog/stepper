package modules.flow.execution.getNameFromAliasStep;

import javafx.util.Pair;
import modules.flow.definition.api.StepUsageDeclarationImpl;

import java.util.HashMap;
import java.util.Map;

public class getNameFromAliasImpl {
    //mapping of steps
    //represent the real name of the step and the aliasing
    private Map<String,String> aliasToName;
    public getNameFromAliasImpl(){
        aliasToName = new HashMap<>();

    }

    public Map<String, String> getAliasToName() {return aliasToName;}

    public void setAliasToName(Map<String, String> aliasToName) {this.aliasToName = aliasToName;}
    public void addNewNameToMap(String name,String alias){aliasToName.put(name,alias);}
    public String getValByKey(String key){return aliasToName.get(key);}
}
