package modules.dataDefinition.impl;


import modules.dataDefinition.api.DataDefinition;
import modules.dataDefinition.impl.number.DoubleDataDefinition;
import modules.dataDefinition.impl.relation.RelationDataDefinition;
import modules.dataDefinition.impl.string.StringDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition());

    DataDefinitionRegistry(DataDefinition dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    private final DataDefinition dataDefinition;

    @Override
    public String getName() {
        return dataDefinition.getName();
    }

    @Override
    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    @Override
    public Class<?> getType() {
        return dataDefinition.getType();
    }
}
