package modules.mappings;

import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.DataDefinitionDeclaration;

import java.io.Serializable;

public class FlowLevelAlias implements HasSource<String> , Serializable {

    protected String step;
    protected String sourceDataName;

    protected String alias;

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
    public void setAlias(String alias) {this.alias = alias;}
    public String getAlias() {return alias;}

    public String toString() {
        return "Aliasing for step: " + step + " with data called" + sourceDataName + " AKA " + alias;
    }
    public Class<?> getAliasType(StepUsageDeclaration step) {
        for(DataDefinitionDeclaration dd :step.getStepDefinition().inputs()) {
            if(dd.getFinalName().equals(sourceDataName))
                return dd.dataDefinition().getType();
        }
        for(DataDefinitionDeclaration dd :step.getStepDefinition().outputs()) {
            if(dd.getFinalName().equals(sourceDataName))
                return dd.dataDefinition().getType();
        }
        return null;
    }
}
