package modules.flow.execution.getNameFromAliasDD;

import java.util.HashMap;
import java.util.Map;

public class getNameFromAliasDDIml {
    private Map<String,String> aliasToName;
    public getNameFromAliasDDIml(){aliasToName = new HashMap<>();}

    public Map<String, String> getAliasToName() {return aliasToName;}

    public void setAliasToName(Map<String, String> aliasToName) {this.aliasToName = aliasToName;}
    public void addNewNameToMap(String name,String alias){aliasToName.put(name,alias);}
    public String getValByKey(String key){return aliasToName.get(key);}
}
