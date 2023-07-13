package services.stepper.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContinuationDTO implements Serializable {
    private String targetFlow;
    private List<ContinuationMappingDTO> mappingList;

    public ContinuationDTO(String targetFlow, List<ContinuationMappingDTO> mappingList) {
        this.targetFlow = targetFlow;
        this.mappingList = mappingList;
    }

    public ContinuationDTO() {
        this.targetFlow = "";
        this.mappingList = new ArrayList<>();
    }

    public String getTargetFlow() {
        return targetFlow;
    }

    public void setTargetFlow(String targetFlow) {
        this.targetFlow = targetFlow;
    }

    public List<ContinuationMappingDTO> getMappingList() {
        return mappingList;
    }
}