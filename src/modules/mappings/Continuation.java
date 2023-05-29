package modules.mappings;

import schemeTest2.generatepackage.STContinuationMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Continuation implements Serializable {

    protected String targetFlow;
    protected List<ContinuationMapping> mappingList;

    public Continuation(String targetFlow, List<ContinuationMapping> mappingList) {

        this.targetFlow = targetFlow;
        this.mappingList = mappingList;

    }

    public Continuation(){
        this.targetFlow="";
        this.mappingList=new ArrayList<>();
    }

    public String getTargetFlow() {
        return targetFlow;
    }

    public void setTargetFlow(String targetFlow) {
        this.targetFlow = targetFlow;
    }

    public List<ContinuationMapping> getMappingList() {
        return mappingList;
    }

    public void setMappingList(List<ContinuationMapping> mappingList) {
        this.mappingList = mappingList;
    }
    public void initMappingList(List<STContinuationMapping> mappingList) {
        int i=0;
        for(STContinuationMapping stConMap : mappingList){
            ContinuationMapping toAdd = new ContinuationMapping();
            toAdd.setTargetData(stConMap.getTargetData());
            toAdd.setSourceData(stConMap.getSourceData());
            this.mappingList.add(i,toAdd);
            i++;
        }

    }
}

