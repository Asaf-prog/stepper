package modules.step.api;


import modules.dataDefinition.api.AbstractDataDefinition;
import modules.dataDefinition.api.DataDefinition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement(name = "DataDefinitionDeclaration")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {
    @XmlElement
    private final String name;
    @XmlElement
    private String finalName;
    @XmlElement
    private final DataNecessity necessity;
    @XmlElement
    private final String userString;
    @XmlElement
    private final DataDefinition dataDefinition;
    ////////
    public DataDefinitionDeclarationImpl() {
        this.name = null;
        this.necessity = null;
        this.userString = null;
        this.dataDefinition = null;
        finalName = null;
    }

    public DataDefinitionDeclarationImpl(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
        finalName = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }
    @Override
   public String getFinalName(){return finalName;}
    @Override
    public void setFinalName(String finalName){finalName = finalName;}

    @Override
    public boolean isMandatory() {
        return necessity == DataNecessity.MANDATORY;
    }
    static class Adapter extends XmlAdapter<DataDefinitionDeclarationImpl, DataDefinitionDeclaration> {
        public DataDefinitionDeclaration unmarshal(DataDefinitionDeclarationImpl v) { return v; }
        public DataDefinitionDeclarationImpl marshal(DataDefinitionDeclaration v) { return (DataDefinitionDeclarationImpl)v; }
    }

}
