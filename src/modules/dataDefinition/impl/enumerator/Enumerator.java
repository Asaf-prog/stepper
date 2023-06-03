package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;

public class Enumerator implements Serializable {
   private String first;
   private String second;

    public Enumerator(String first, String second) {
        this.first = first;
        this.second = second;
    }
    public Enumerator(String first){
        this.first = first;
        this.second = "";
    }
    public boolean isBothContainVal(String val){
        if(first.equals(val) || second.equals(val)){
            return true;
        }
        return false;
    }
    public boolean isFirstContainVal(String val){
        if(first.equals(val)){
            return true;
        }
        return false;
    }
}
