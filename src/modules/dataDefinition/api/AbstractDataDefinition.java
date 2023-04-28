package modules.dataDefinition.api;

import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.DataDefinitionDeclarationImpl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
@XmlRootElement
public abstract class AbstractDataDefinition  implements DataDefinition{
    @XmlElement
    private final String name;
    @XmlElement
    private final boolean userFriendly;
    @XmlElement
    private final Class<?> type;


    public AbstractDataDefinition() {
        this.name = null;
        this.userFriendly = false;
        this.type = null;
    }

    public AbstractDataDefinition(String name, boolean userFriendly, Class<?> type) {
        this.name = name;
        this.userFriendly = userFriendly;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isUserFriendly() {
        return userFriendly;
    }

    @Override
    public Class<?> getType() {
        return type;
    }



}
