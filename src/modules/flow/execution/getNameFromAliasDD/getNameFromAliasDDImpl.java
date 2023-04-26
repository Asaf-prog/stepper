package modules.flow.execution.getNameFromAliasDD;

import javafx.util.Pair;
import modules.flow.definition.api.StepUsageDeclarationImpl;

import java.util.HashMap;
import java.util.Map;

public class getNameFromAliasDDImpl {
    private Map<String,String> aliasToName;
    private Map<StepUsageDeclarationImpl, Pair<String,String>> aliasToNameWithObject;
    public getNameFromAliasDDImpl(){
        aliasToName = new HashMap<>();
        aliasToNameWithObject = new HashMap<>();
    }
    public void addNewNameToMapWithObject(StepUsageDeclarationImpl step,String name,String alias){
        aliasToNameWithObject.put(step,new Pair<>(name,alias));
    }
    public boolean isKeyExist(String key){return aliasToName.containsKey(key);}

    public Map<String, String> getAliasToName() {return aliasToName;}

    public void setAliasToName(Map<String, String> aliasToName) {this.aliasToName = aliasToName;}
    public void addNewNameToMap(String name,String alias){aliasToName.put(name,alias);}
    public String getValByKey(String key){return aliasToName.get(key);}

}
