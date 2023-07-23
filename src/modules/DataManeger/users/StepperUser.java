package modules.DataManeger.users;

import modules.flow.execution.FlowExecution;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

public class StepperUser {
    private static final String DEFAULT_ROLE = "all-flows";
    private String username;
    private List<String> roles;//will change...
    private List<String> flowExecutionsIDs;

    private boolean isManager=false;

    public StepperUser(String username, List<String> role, List<String> flowExecutions) {
        this.username = username;
        roles = role;
        this.flowExecutionsIDs = flowExecutions;
        isManager=false;
    }

    public StepperUser(String username) {
        this.username = username;
        roles=new ArrayList<>();
        roles.add(DEFAULT_ROLE);
        roles.add("read-only");
        flowExecutionsIDs=new ArrayList<>();
        isManager=false;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
            return roles;
    }

    public List<String> getFlowExecutions() {
        return flowExecutionsIDs;
    }

    public void setFlowExecutions(List<String> flowExecutions) {
        this.flowExecutionsIDs = flowExecutions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(List<String> role) {
        roles = role;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
