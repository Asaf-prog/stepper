package modules.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomMapping implements HasSource<String>, HasTarget<String> //Steps not Data
{
    @XmlAttribute
    protected String sourceStep;
    @XmlAttribute
    protected String sourceData;
    @XmlAttribute
    protected String targetStep;
    @XmlAttribute
    protected String targetData1;

    public CustomMapping()
    {
        sourceStep = null;
        sourceData = null;
        targetStep = null;
        targetData1 = null;
    }

    public CustomMapping(String sourceStep, String sourceData, String targetStep, String targetData)
    {
        this.sourceStep = sourceStep;
        this.sourceData = sourceData;
        this.targetStep = targetStep;
        this.targetData1 = targetData;
    }

    public void setSource(String sourceStep)
    {
        this.sourceStep = sourceStep;
    }
    public String getSource()
    {
        return sourceStep;
    }

    public void setTarget(String targetStep)
    {
        this.targetStep = targetStep;
    }
    public String getTarget()
    {
        return targetStep;
    }

    public void setSourceData(String sourceData)
    {
        this.sourceData = sourceData;
    }
    public String getSourceData()
    {
        return sourceData;
    }

    public void setTargetData1(String targetData1)
    {
        this.targetData1 = targetData1;
    }
    public String getTargetData1()
    {
        return targetData1;
    }

}
