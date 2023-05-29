package modules.mappings;

import java.io.Serializable;

public class ContinuationMapping  implements Serializable {

    protected String targetData;
    protected String sourceData;


    public ContinuationMapping(String targetData , String sourceData) {
        this.targetData = targetData;
        this.sourceData = sourceData;
    }

    public ContinuationMapping() {
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

