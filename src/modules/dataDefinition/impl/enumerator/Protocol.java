package modules.dataDefinition.impl.enumerator;

import java.io.Serializable;

public enum Protocol implements Serializable {
    HTTP,HTTPS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public Protocol orElse(Protocol protocol) {
        if (this == null) {
            return protocol;
        }
        return this;
    }
}
