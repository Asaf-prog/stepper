package modules.flow.execution.runner;
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
    public void executeFlow(FlowExecution flowExecution){//This class implements the flow
        StepExecutionContext context = new StepExecutionContextImpl(); // actual object goes here...
        context.setSteps(flowExecution.getFlowDefinition().getFlowSteps());
        context.setUserInputs(flowExecution);//sets user inputs into the context
        ArrayList<StepResult> flowExeStatus = new ArrayList<>();//flow execution status
        context.initializedCustomMapping(flowExecution);//sets custom mapping into the context
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
                    break;
                }
            }
            Instant flowEndTime=flowExecution.stopStepTimer();
            FlowExecutionResult flowExecutionResult = getFlowExecutionResult(flowExeStatus);
            flowExecution.setFlowDuration(Duration.between(flowStartTime,flowEndTime));//update flow execution avg time
            //maybe save the end time for calc the time occurred
            UpdateFlowAvgTiming(Duration.between(flowStartTime,flowEndTime),flowExecution.getFlowDefinition());//update avg time of flow def
            flowExecution.setFlowExecutionResult(flowExecutionResult);
            flowExecution.setFlowExecutionOutputs(context);
            flowExecution.setLogs(context.getLogs());
            flowExecution.setSummaryLines(context.getSummaryLines());
            flowExecution.setAllExecutionOutputs(context);
            flowExecution.setUserInputs();//sets user inputs into the flow execution and delete the original user inputs
//todo check that each step in flow (step usage dec) is different (if one step is used twice in the same flow)
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            flowExecution.setFlowExecutionResult(getFlowExecutionResult(flowExeStatus));
        }

        //
    }

    private void UpdateFlowAvgTiming(Duration between, FlowDefinition flowDefinition) {
        flowDefinition.updateAvgTime(between);
    }

    private FlowExecutionResult getFlowExecutionResult(ArrayList<StepResult> flowExeStatus) {

        if (flowExeStatus.contains(StepResult.FAILURE)) {
            return(FlowExecutionResult.FAILURE);
        } else if (flowExeStatus.contains(StepResult.WARNING)) {
            return(FlowExecutionResult.WARNING);
        } else {
            return(FlowExecutionResult.SUCCESS);
        }
    }
}
