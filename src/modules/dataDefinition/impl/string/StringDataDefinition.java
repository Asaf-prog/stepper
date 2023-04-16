package modules.dataDefinition.impl.string;


import modules.dataDefinition.api.AbstractDataDefinition;

public class StringDataDefinition extends AbstractDataDefinition {

    public StringDataDefinition() {
        super("String", true, String.class);//String is a primitive type
    }
}
