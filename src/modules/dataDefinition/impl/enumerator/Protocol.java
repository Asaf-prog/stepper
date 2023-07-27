package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;

public enum Protocol implements Serializable {
    HTTP("http"),
    HTTPS("https");

   private final String val;
    Protocol(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    public String getProtocol(){
        return this.name().toLowerCase();
    }
    public String getVal() {
        return val;
    }
}
