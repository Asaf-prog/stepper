package modules.DataManeger;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;

import java.util.List;

public class RoleManager {
    private static List<Role> roles;
    private List<FlowDefinitionImpl> flows=new ArrayList<>();


    public RoleManager(){
        roles=new ArrayList<>();
        roles.add(new Role("all-flows",flows));
        roles.add(new Role("read-only",flows.subList(0,flows.size()-1)));



    }

    public RoleManager(List<FlowDefinitionImpl> flows) {
        roles=new ArrayList<>();
        roles.add(new Role("all-flows",flows));
        roles.add(new Role("read-only",flows.subList(0,flows.size()-1)));


    }

    public static List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public void addRole(Role role){
        roles.add(role);
    }

    public Role getRoleByName(String role) {
        for (Role r : roles) {
            if (r.getName().equals(role)) {
                return r;
            }
        }
        return null;
    }

    public List<String> getAllRolesNames(){
        List<String> rolesNames=new ArrayList<>();
        if(roles==null){
            return null;
        }
        for (Role role:getRoles()){
            rolesNames.add(role.getName());
        }
        return rolesNames;
    }

    public void isRoleExist(String name) {
        for (Role role : roles) {
            if (role.getName().equals(name)) {
                throw new IllegalArgumentException("Role already exist");
            }
        }
    }

    public Role updateFlowsForRole(List<FlowDefinitionImpl> newFlows, String roleName) {
        Role role=getRoleByName(roleName);
        role.setNewFlows(newFlows);
        return role;


    }
}

