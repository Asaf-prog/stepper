package modules.dataDefinition.impl.number;


import modules.dataDefinition.api.AbstractDataDefinition;

public class DoubleDataDefinition extends AbstractDataDefinition {
    public DoubleDataDefinition() {
        super("Double", true, Double.class);
    }
}
