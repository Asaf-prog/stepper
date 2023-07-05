package modules.dataDefinition.impl.json;

import modules.dataDefinition.api.AbstractDataDefinition;
import modules.dataDefinition.impl.enumerator.Enumerator;

public class JsonDataDefinition  extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("Jason", true, String.class);
    }
}
