package modules.dataDefinition.impl.number;

import modules.dataDefinition.api.AbstractDataDefinition;

public class NumberDataDefinition extends AbstractDataDefinition {
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
