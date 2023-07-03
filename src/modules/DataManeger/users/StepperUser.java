package modules.DataManeger.users;

import modules.flow.execution.FlowExecution;

import java.util.List;

public class StepperUser {
    private String username;
    private String Role;//will change...
    private List<FlowExecution> flowExecutions;

    public StepperUser(String username, String role, List<FlowExecution> flowExecutions) {
        this.username = username;
        Role = role;
        this.flowExecutions = flowExecutions;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
            return Role;
    }

    public List<FlowExecution> getFlowExecutions() {
        return flowExecutions;
    }

    public void setFlowExecutions(List<FlowExecution> flowExecutions) {
        this.flowExecutions = flowExecutions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        Role = role;
    }



}
