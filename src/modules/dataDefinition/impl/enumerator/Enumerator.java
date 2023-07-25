package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;

public class Enumerator implements Serializable {
   private String first;
   private String second;
   private String third;
   private String fourth;

    public Enumerator(String first, String second,String third,String fourth) {// add new constructor for http step
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
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
    public String getFirst(){
        return first;
    }
    public String getSecond(){
        return second;
    }
    public String getThird(){
        return third;
    }
    public String getFourth(){
        return fourth;
    }
}
