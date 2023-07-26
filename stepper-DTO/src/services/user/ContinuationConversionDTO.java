package services.user;

import javafx.util.Pair;
import services.stepper.FlowDefinitionDTO;

import java.util.List;

public class ContinuationConversionDTO {
    private List<Pair<String,String>> supplyData;
    private List<String> needToSupplyData;
    private FlowDefinitionDTO targetFlow;
    private FlowDefinitionDTO sourceFlow;
    public ContinuationConversionDTO(List<Pair<String,String>> supplyData, List<String> needToSupplyData,FlowDefinitionDTO targetFlow,FlowDefinitionDTO sourceFlow){
        this.supplyData = supplyData;
        this.needToSupplyData = needToSupplyData;
        this.targetFlow = targetFlow;
        this.sourceFlow = sourceFlow;
    }
    public List<String> getNeedToSupplyData(){
        return needToSupplyData;
    }
    public List<Pair<String,String>> getSupplyData(){
        return supplyData;
    }
    public FlowDefinitionDTO getTargetFlow(){
        return targetFlow;
    }
    public FlowDefinitionDTO getSourceFlow() {
        return sourceFlow;
    }
}
