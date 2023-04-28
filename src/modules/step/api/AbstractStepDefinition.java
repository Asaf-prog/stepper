package modules.step.api;

import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name = "Step-Definition")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractStepDefinition implements StepDefinition {
    @XmlAttribute
    private final String stepName;
    @XmlElement
    private final boolean readonly;
    @XmlElement
    private final List<DataDefinitionDeclarationImpl> inputs;
    @XmlElement(name = "DataDefinitionDeclaration-output")
    private final List<DataDefinitionDeclarationImpl> outputs;

    public AbstractStepDefinition() {
        this.stepName = null;
        this.readonly = false;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }
    public AbstractStepDefinition(String stepName, boolean readonly) {
        this.stepName = stepName;
        this.readonly = readonly;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    protected void addInput(DataDefinitionDeclarationImpl dataDefinitionDeclaration) {
        inputs.add(dataDefinitionDeclaration);
    }
    protected void addOutput(DataDefinitionDeclarationImpl dataDefinitionDeclaration) {
        outputs.add(dataDefinitionDeclaration);
    }
    @Override
    public String name() {
        return stepName;
    }
    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public List<DataDefinitionDeclarationImpl> inputs() {
        return inputs;
    }

    @Override
    public List<DataDefinitionDeclarationImpl> outputs() {
        return outputs;
    }

    @Override
    public String getName() {
        return stepName;
    }
    @Override
    public DataDefinition getDataDefinitionByName(String DDName) {
        for (DataDefinitionDeclaration dataDefinitionDeclaration : outputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.dataDefinition();
            }
            else if(dataDefinitionDeclaration.getFinalName()!=null){
               if (dataDefinitionDeclaration.getFinalName().equals(DDName))
                   return dataDefinitionDeclaration.dataDefinition();
            }
        }
        return null;
    }
    @Override
    public DataDefinition getDataDefinitionByNameTarget(String DDName) {
        for (DataDefinitionDeclaration dataDefinitionDeclaration : inputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.dataDefinition();
            }
        }
        return null;
    }

    static class Adapter extends XmlAdapter<AbstractStepDefinition, StepDefinition> {
        public Adapter() { super(); }
        public StepDefinition unmarshal(AbstractStepDefinition v) { return v; }
        public AbstractStepDefinition marshal(StepDefinition v) { return (AbstractStepDefinition)v; }
    }
}
