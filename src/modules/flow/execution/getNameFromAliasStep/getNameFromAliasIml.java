package modules.flow.execution.getNameFromAliasStep;

import java.util.HashMap;
import java.util.Map;

public class getNameFromAliasIml {
    //mapping of steps
    //represent the real name of the step and the aliasing
    private Map<String,String> aliasToName;
    public getNameFromAliasIml(){aliasToName = new HashMap<>();}

    public Map<String, String> getAliasToName() {return aliasToName;}

    public void setAliasToName(Map<String, String> aliasToName) {this.aliasToName = aliasToName;}
    public void addNewNameToMap(String name,String alias){aliasToName.put(name,alias);}
    public String getValByKey(String key){return aliasToName.get(key);}
}
