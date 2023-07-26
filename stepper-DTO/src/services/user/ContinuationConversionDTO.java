package services.user;

import javafx.util.Pair;
import services.stepper.FlowDefinitionDTO;

import java.util.List;

public class ContinuationConversionDTO {
    private List<Pair<String,String>> supplyData;
    private List<String> needToSupplyData;
    private FlowDefinitionDTO targetFlow;
    public ContinuationConversionDTO(List<Pair<String,String>> supplyData, List<String> needToSupplyData,FlowDefinitionDTO targetFlow){
        this.supplyData = supplyData;
        this.needToSupplyData = needToSupplyData;
        this.targetFlow = targetFlow;
    }
    List<String> getNeedToSupplyData(){
        return needToSupplyData;
    }
    List<Pair<String,String>> getSupplyData(){
        return supplyData;
    }
    public FlowDefinitionDTO getTargetFlow(){
        return targetFlow;
    }
}
