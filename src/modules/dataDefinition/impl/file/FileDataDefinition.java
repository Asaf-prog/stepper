package modules.dataDefinition.impl.file;

import modules.dataDefinition.api.AbstractDataDefinition;
public class FileDataDefinition extends AbstractDataDefinition {
    public FileDataDefinition() {
        super("List", false, File.class);
    }
}

