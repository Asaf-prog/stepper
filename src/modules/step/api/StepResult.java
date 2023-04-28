package modules.step.api;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum StepResult {
    SUCCESS, FAILURE, WARNING
}
