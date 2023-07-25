package modules.dataDefinition.impl;


import modules.dataDefinition.api.DataDefinition;
import modules.dataDefinition.impl.enumerator.EnumeratorDataDefinition;
import modules.dataDefinition.impl.enumerator.MethodEnumDefinition;
import modules.dataDefinition.impl.enumerator.protocolEnumeratorDataDefinition;
import modules.dataDefinition.impl.number.DoubleDataDefinition;
import modules.dataDefinition.impl.relation.RelationDataDefinition;
import modules.dataDefinition.impl.string.StringDataDefinition;
import modules.dataDefinition.impl.file.FileDataDefinition;
import modules.dataDefinition.impl.mapping.MappingDataDefinition;
import modules.dataDefinition.impl.json.JsonDataDefinition;
import modules.dataDefinition.impl.list.ListDataDefinition;
import modules.dataDefinition.impl.number.NumberDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    MAPPING(new MappingDataDefinition()),
    FILE(new FileDataDefinition()),
    LIST(new ListDataDefinition()),
    ENUMERATION(new EnumeratorDataDefinition()),
    JASON(new JsonDataDefinition()),
    PROTOCOL_ENUMERATOR(new protocolEnumeratorDataDefinition()),
    METHOD_ENUM_ENUMERATION(new MethodEnumDefinition())

    ;



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

    @Override
    public String getTypeName() {
       return  dataDefinition.getTypeName();
    }
}
