package modules.flow.execution;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum FlowExecutionResult {
    SUCCESS, FAILURE, WARNING
}
