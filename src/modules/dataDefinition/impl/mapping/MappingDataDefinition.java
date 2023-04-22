package modules.dataDefinition.impl.mapping;

import java.util.Map;

import modules.dataDefinition.api.AbstractDataDefinition;

public class MappingDataDefinition extends AbstractDataDefinition {

    public MappingDataDefinition() {
        super("Mapping", false, Mapping.class);
    }
}