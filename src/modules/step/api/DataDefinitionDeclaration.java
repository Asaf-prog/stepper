package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlRegistry
public interface DataDefinitionDeclaration {
    String getName();
    String getFinalName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    void setFinalName(String name);

    boolean isMandatory();
}
