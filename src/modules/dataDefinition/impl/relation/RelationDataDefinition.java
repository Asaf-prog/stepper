package modules.dataDefinition.impl.relation;
import modules.dataDefinition.impl.relation.*;
import modules.dataDefinition.api.AbstractDataDefinition;

public class RelationDataDefinition extends AbstractDataDefinition {
    public RelationDataDefinition() {
        super("Relation", false, RelationData.class);
    }
}
