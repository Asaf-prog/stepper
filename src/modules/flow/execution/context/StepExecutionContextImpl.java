package modules.flow.execution.context;
import modules.Map.CustomMapping;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.*;

public class StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private Map<String,List<String>> log;
    private Map<String,String> summaryLine;
    private StepUsageDeclaration currentWorkingStep;
    private List<CustomMapping> customMappings;
    private List<StepUsageDeclaration> steps;
    private StepUsageDeclarationImpl currentStep;

    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
        summaryLine = new HashMap<>();
        log = new HashMap<>();
        customMappings = new ArrayList<>();
    }
    @Override
    public void setCustomMappings(List<CustomMapping> customMappings,Map<String,String> mapOfName){
        this.customMappings = customMappings;
        //set name of steps
        for (StepUsageDeclaration tempStep: steps){
            if (mapOfName.get(tempStep.getName()) != null)
                tempStep.setFinalName(mapOfName.get(tempStep.getName()));
        }

        for (CustomMapping run : customMappings) {

            System.out.println("-----------------------------");
            System.out.println(run.getSource());
            System.out.println(run.getSourceData());
            System.out.println(run.getTarget());
            System.out.println(run.getTargetData());
            System.out.println("---------------------------------------");
        }

        for (StepUsageDeclaration stepRunner: steps){
            for (CustomMapping run : customMappings){
                if (stepRunner.getFinalStepName().equals(run.getTarget())){
                    stepRunner.addAnewValOfDDThatConnectedAddToListOFPair(run.getTargetData(), run.getSourceData());
                }
            }
        }

    }
    @Override
    public void setSteps(List<StepUsageDeclaration> steps){this.steps = steps;}

    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        for(CustomMapping custome: customMappings){
            //if target data == source data (type)
            if(currentWorkingStep.getFinalStepName().equals(custome.getTarget())){
               String targetName = custome.getTargetData();//dd
               String sourceName = custome.getSourceData();//dd
                StepUsageDeclaration sourceStep = null;
               for (StepUsageDeclaration sur :steps){
                 if (sur.getFinalStepName().equals(custome.getSource())){
                     sourceStep = sur;
                 }
               }
                DataDefinition source = sourceStep.getStepDefinition().getDataDefinitionByName(custome.getSourceData());
                DataDefinition target = currentWorkingStep.getStepDefinition().getDataDefinitionByName(targetName);
                if (target.getType() == source.getType()){ //if these steps are same types
                    Object aValue = dataValues.get(dataName);
                    return expectedDataType.cast(aValue);
                }
            }
        }
        //Find of there is an input match
        DataDefinitionDeclaration theExpectedDataDefinition = null;
        Optional<DataDefinitionDeclaration> maybeTheExpectedDataDefinition =
                currentWorkingStep.getStepDefinition()
                        .inputs()
                        .stream()
                        .filter((input) -> input.getName() == dataName)
                        .findFirst();

        if(maybeTheExpectedDataDefinition.isPresent()){
            theExpectedDataDefinition = maybeTheExpectedDataDefinition.get();
            if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.dataDefinition().getType())) {
                Object aValue = dataValues.get(dataName);
                //System.out.println(expectedDataType.cast(aValue).getClass().getName());
                return expectedDataType.cast(aValue);
            }
        }
        else{
            //Handle there is no input;
        }
        return null;
    }

    @Override
    public boolean storeDataValue(String dataName, Object value) {
            dataValues.put(dataName, value);
            return true;
    }
    @Override
    public List getLog(String step){
        return log.get(step);
    }
    @Override
    public void setLog(String step,String logs) {
        if (log.get(step) == null) {
            List<String> values = new ArrayList<>();
            values.add(logs);
            log.put(step, values);

        } else {
            List<String> values = log.get(step);
            values.add(logs);
            log.put(step, values);
        }
    }
    @Override
    public void addSummaryLine(String step,String summary) {
        summaryLine.put(step,summary);
    }

    @Override
    public String getSummary(String step) {return summaryLine.get(step);}
   @Override
    public void setStep(StepUsageDeclaration step) { currentWorkingStep = step;}

}
