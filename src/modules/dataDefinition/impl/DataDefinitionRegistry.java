package modules.dataDefinition.impl;


import modules.dataDefinition.api.DataDefinition;
import modules.dataDefinition.impl.number.DoubleDataDefinition;
import modules.dataDefinition.impl.relation.RelationDataDefinition;
import modules.dataDefinition.impl.string.StringDataDefinition;
import modules.dataDefinition.impl.file.FileDataDefinition;
import modules.dataDefinition.impl.mapping.MappingDataDefinition;
import modules.dataDefinition.impl.json.JsonDataDifinition;
import modules.dataDefinition.impl.list.ListDataDefinition;
import modules.dataDefinition.impl.number.NumberDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {//todo add all of the DD
    STRING(new StringDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    MAPPING(new MappingDataDefinition()),
    FILE(new FileDataDefinition()),
    LIST(new ListDataDefinition());


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
