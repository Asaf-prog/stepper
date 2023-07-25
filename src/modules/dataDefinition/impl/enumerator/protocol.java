package modules.dataDefinition.impl.enumerator;

public enum protocol {
    HTTP,HTTPS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
