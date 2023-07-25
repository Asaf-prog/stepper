package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;

public enum protocol implements Serializable {
    HTTP,HTTPS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
