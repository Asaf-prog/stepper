package modules.dataDefinition.api;

import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.DataDefinitionDeclarationImpl;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class AbstractDataDefinition implements DataDefinition {
    private final String name;
    private final boolean userFriendly;
    private final Class<?> type;

    public AbstractDataDefinition(String name, boolean userFriendly, Class<?> type) {
        this.name = name;
        this.userFriendly = userFriendly;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isUserFriendly() {
        return userFriendly;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    static class Adapter extends XmlAdapter<AbstractDataDefinition, DataDefinition> {
        public DataDefinition unmarshal(AbstractDataDefinition v) { return v; }
        public AbstractDataDefinition marshal(DataDefinition v) { return (AbstractDataDefinition)v; }
    }

}
