package modules.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FlowLevelAlias implements HasSource<String> {

    @XmlElement
    protected String step;
    @XmlElement
    protected String sourceDataName;

    protected String alias;
    ////////
    public FlowLevelAlias() {//default constructor
        step = null;
        sourceDataName = null;
        alias = null;
    }

    public FlowLevelAlias(String step, String sourceDataName, String alias) {//default constructor
        this.step = step;
        this.sourceDataName = sourceDataName;
        this.alias = alias;
    }
    public boolean isAliasMatch(String alias, String step, String sourceDataName) {
        if(this.alias.equals(alias) && this.step.equals(step) && this.sourceDataName.equals(sourceDataName))
            return true;
        else
            return false;
    }
    @Override
    public void setSource(String sourceStep){//step ofc
        this.step = sourceStep;
    }

    public String getSource() {return step;}

    public void setSourceData(String sourceDataName) {this.sourceDataName = sourceDataName;}
    public String getSourceData() {return sourceDataName;}
    @XmlAttribute(name = "alias")
    public void setAlias(String alias) {this.alias = alias;}
    public String getAlias() {return alias;}

    public String toString() {
        return "Aliasing for step: " + step + " with data called" + sourceDataName + " AKA " + alias;
    }

}
