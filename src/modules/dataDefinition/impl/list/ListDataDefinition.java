package modules.dataDefinition.impl.list;

import modules.dataDefinition.api.AbstractDataDefinition;

import java.util.List;

public class ListDataDefinition extends AbstractDataDefinition {
    public ListDataDefinition() {
        super("List", false, List.class);
    }
}
