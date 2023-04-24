package modules.Map;

public class AutomaticMapping implements HasSource<String>, HasTarget<String>
{
    protected String sourceStep;
    protected String sourceData;
    protected String targetStep;
    protected String targetData;//input target name

    @Override
    public void setSource(String sourceStep)
    {
        this.sourceStep = sourceStep;
    }

    public String getSource()
    {
        return sourceStep;
    }
    @Override
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
