package modules.dataDefinition.impl.enumerator;

import modules.dataDefinition.api.AbstractDataDefinition;

public class protocolEnumeratorDataDefinition  extends AbstractDataDefinition {

    public protocolEnumeratorDataDefinition() {
        super("Enumerator", true, protocol.class);
    }
}
