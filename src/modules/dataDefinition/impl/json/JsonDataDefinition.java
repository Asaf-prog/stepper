package modules.dataDefinition.impl.json;

import modules.dataDefinition.api.AbstractDataDefinition;

public class JsonDataDefinition  extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("Jason", true, JsonData.class);
    }
}
