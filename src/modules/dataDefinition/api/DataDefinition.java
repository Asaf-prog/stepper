package modules.dataDefinition.api;

import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclaration;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlRegistry
public interface DataDefinition {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
 //todo add all of the DataDefinitions mentioned in the word document
//etc: Number,List,File ,Mapping-map