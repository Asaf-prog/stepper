package services.stepper.other;

import java.io.Serializable;

public class ContinuationMappingDTO implements Serializable {
    private String targetData;
    private String sourceData;

    public ContinuationMappingDTO(String targetData, String sourceData) {
        this.targetData = targetData;
        this.sourceData = sourceData;
    }

    public ContinuationMappingDTO() {
        this.targetData = "";
        this.sourceData = "";
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }
}
