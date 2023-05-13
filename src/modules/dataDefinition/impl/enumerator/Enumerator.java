package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Enumerator implements Serializable {
   private Set<String> stringSet;
    Enumerator(){
         stringSet = new HashSet<>();
    }
    public void add(String val){
        stringSet.add(val);
    }
    public Set<String> getStringSet(){
        return stringSet;
    }
    public boolean containVal(String val){
        return stringSet.contains(val);
    }
}
