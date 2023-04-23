package modules.Map;

public class CustomMapping implements HasSource<String>, HasTarget<String> //Steps not Data
{
    protected String sourceStep;
    protected String sourceData;
    protected String targetStep;
    protected String targetData;

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

    public void setTargetData(String targetData)
    {
        this.targetData = targetData;
    }
    public String getTargetData()
    {
        return targetData;
    }

}
