package modules.flow.execution.runner;
import Menu.MenuException;
import Menu.MenuExceptionItems;
import javafx.beans.property.DoubleProperty;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;
import modules.flow.execution.context.StepExecutionContext;
import modules.flow.execution.context.StepExecutionContextImpl;
import modules.step.api.StepResult;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
public class FLowExecutor {
    public void executeFlow(FlowExecution flowExecution) throws Exception {//This class implements the flow



        StepExecutionContext context = new StepExecutionContextImpl(); // actual object goes here...
        context.setSteps(flowExecution.getFlowDefinition().getFlowSteps());
        context.setUserInputs(flowExecution);//sets user inputs into the context
        ArrayList<StepResult> flowExeStatus = new ArrayList<>();//flow execution status
        context.initializedCustomMapping(flowExecution);//sets custom mapping into the context
        flowExecution.setUserInputs();//sets user inputs into the flow execution and delete the original user inputs
        boolean checkIfFlowFailed = false;
        try {
            Instant flowStartTime=flowExecution.startStepTimer();
            for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
                StepUsageDeclaration step = flowExecution.getFlowDefinition().getFlowSteps().get(i);
                step.addUsage();
                context.setStep(step);
                context.setInputOfCurrentStep(step.getInputFromNameToAlias());
                context.setOutputOfCurrentStep(step.getOutputFromNameToAlias());
                Instant stepStartTime=step.startStepTimer();//start duration timer
                StepResult stepResult = step.getStepDefinition().invoke(context);
                step.setStepResult(stepResult);
                Instant stepEndTime=step.startStepTimer();
                flowExeStatus.add(stepResult);
                step.setStepDuration(Duration.between(stepStartTime,stepEndTime));
                // check if you should continue etc..
                if (stepResult == StepResult.FAILURE && !step.skipIfFail()) {//means all flow failed
                    setRestOfStepsAsFailed(flowExeStatus, i,flowExecution);
                    break;
                }
                if (stepResult == StepResult.FAILURE && step.skipIfFail()) {
                    continue;
                }
            }

            Instant flowEndTime=flowExecution.stopStepTimer();
            updateFlowExecution(flowExecution, context, flowExeStatus, flowStartTime, flowEndTime);
            checkIfFlowFailed=true;

        }
        catch (IOException e) {
            return;
        }
        catch (Exception e) {
            //System.out.println(e.getMessage());
            throw new MenuException(MenuExceptionItems.EMPTY, "Error in executing flow "+flowExecution.getFlowDefinition().getName());
        }
        finally {
            if (checkIfFlowFailed!=true) {
                Instant flowStartTime=flowExecution.stopStepTimer();
                Instant flowEndTime=flowExecution.stopStepTimer();
                updateFlowExecution(flowExecution, context, flowExeStatus, flowStartTime, flowEndTime);

            }
            flowExecution.setFlowExecutionResult(getFlowExecutionResult(flowExeStatus, flowExecution));//if one step failed or warning the flow failed or warning

            return;
        }
    }

    private void setRestOfStepsAsFailed(ArrayList<StepResult> flowExeStatus, int i, FlowExecution flowExecution) {
        for (int j = i + 1; j < flowExecution.getFlowDefinition().getFlowSteps().size(); j++) {
            flowExeStatus.add(StepResult.FAILURE);
        }
        for(int j=i+1;j<flowExecution.getFlowDefinition().getFlowSteps().size();j++){
            flowExecution.getFlowDefinition().getFlowSteps().get(j).setStepResult(StepResult.FAILURE);
        }
    }

    private void updateFlowExecution(FlowExecution flowExecution, StepExecutionContext context, ArrayList<StepResult> flowExeStatus, Instant flowStartTime, Instant flowEndTime) throws Exception {
            FlowExecutionResult flowExecutionResult = getFlowExecutionResult(flowExeStatus, flowExecution);
            flowExecution.setFlowDuration(Duration.between(flowStartTime, flowEndTime));//update flow execution avg time
            //maybe save the end time for calc the time occurred
            UpdateFlowAvgTiming(Duration.between(flowStartTime, flowEndTime), flowExecution.getFlowDefinition());//update avg time of flow def
            flowExecution.setFlowExecutionResult(flowExecutionResult);
            flowExecution.setFlowExecutionOutputs(context);
            flowExecution.setLogs(context.getLogs());
            flowExecution.setSummaryLines(context.getSummaryLines());
            flowExecution.setAllExecutionOutputs(context);
            flowExecution.setTotalTime(Duration.between(flowStartTime, flowEndTime));

    }

    private void UpdateFlowAvgTiming(Duration between, FlowDefinition flowDefinition) {
        flowDefinition.updateAvgTime(between);
    }

    private FlowExecutionResult getFlowExecutionResult(ArrayList<StepResult> flowExeStatus, FlowExecution flowExecution) {
        int i=0;
        boolean warning=false;
        for (StepResult stepResult : flowExeStatus) {
            if (stepResult == StepResult.FAILURE) {
                if(flowExecution.getFlowDefinition().getFlowSteps().get(i).skipIfFail())
                   warning=true;
                else
                    return FlowExecutionResult.FAILURE;
            }
            if (stepResult == StepResult.WARNING)
                warning=true;
            i++;
        }
        if(warning)
            return FlowExecutionResult.WARNING;
        return FlowExecutionResult.SUCCESS;
    }

    public void executeFlow(FlowExecution flowExecution, DoubleProperty progress) throws Exception {
        StepExecutionContext context = new StepExecutionContextImpl(); // actual object goes here...
        context.setSteps(flowExecution.getFlowDefinition().getFlowSteps());
        context.setUserInputs(flowExecution);//sets user inputs into the context
        ArrayList<StepResult> flowExeStatus = new ArrayList<>();//flow execution status
        context.initializedCustomMapping(flowExecution);//sets custom mapping into the context
        flowExecution.setUserInputs();//sets user inputs into the flow execution and delete the original user inputs
        boolean checkIfFlowFailed = false;
        try {
            Instant flowStartTime=flowExecution.startStepTimer();
            for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
                StepUsageDeclaration step = flowExecution.getFlowDefinition().getFlowSteps().get(i);
                step.addUsage();
                context.setStep(step);
                context.setInputOfCurrentStep(step.getInputFromNameToAlias());
                context.setOutputOfCurrentStep(step.getOutputFromNameToAlias());
                Instant stepStartTime=step.startStepTimer();//start duration timer
                StepResult stepResult = step.getStepDefinition().invoke(context);
                step.setStepResult(stepResult);
                Instant stepEndTime=step.startStepTimer();
                flowExeStatus.add(stepResult);
                step.setStepDuration(Duration.between(stepStartTime,stepEndTime));


                //update progress
                progress.setValue((double)(i+1)/flowExecution.getFlowDefinition().getFlowSteps().size());
                // check if you should continue etc..
                if (stepResult == StepResult.FAILURE && !step.skipIfFail()) {//means all flow failed
                    setRestOfStepsAsFailed(flowExeStatus, i,flowExecution);
                    break;
                }
                if (stepResult == StepResult.FAILURE && step.skipIfFail()) {
                    continue;
                }
            }

            Instant flowEndTime=flowExecution.stopStepTimer();
            updateFlowExecution(flowExecution, context, flowExeStatus, flowStartTime, flowEndTime);
            checkIfFlowFailed=true;

        }
        catch (IOException e) {
            return;
        }
        catch (Exception e) {
            //System.out.println(e.getMessage());
            throw new MenuException(MenuExceptionItems.EMPTY, "Error in executing flow "+flowExecution.getFlowDefinition().getName());
        }
        finally {
            if (checkIfFlowFailed!=true) {
                Instant flowStartTime=flowExecution.stopStepTimer();
                Instant flowEndTime=flowExecution.stopStepTimer();
                updateFlowExecution(flowExecution, context, flowExeStatus, flowStartTime, flowEndTime);

            }
            flowExecution.setFlowExecutionResult(getFlowExecutionResult(flowExeStatus, flowExecution));//if one step failed or warning the flow failed or warning

            return;
        }
    }
}
