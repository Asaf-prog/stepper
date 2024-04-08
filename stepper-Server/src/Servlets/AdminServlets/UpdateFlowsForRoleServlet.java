package Servlets.AdminServlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.flow.definition.api.FlowDefinitionImpl;
import services.user.RoleDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "update flows for role",urlPatterns = "/updateRoleFlows")
public class UpdateFlowsForRoleServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body=req.getReader().readLine();
        String roleName=req.getHeader("roleName");

        List<String> newFlows=gson.fromJson(body, new TypeToken<List<String>>(){}.getType());
        if (newFlows==null){
            resp.setStatus(400);
            return;
        }
        DataManager dataManager= (DataManager) getServletContext().getAttribute("dataManager");
        List<FlowDefinitionImpl> actualFlows= GetActualFlows(newFlows,dataManager);
        Role role=dataManager.getRoleManager().updateFlowsForRole(actualFlows,roleName);
        RoleDTO roleDTO=new RoleDTO(role.getName(),role.getDescription(),role.getFlows());
        resp.getWriter().write(gson.toJson(roleDTO));
        resp.setStatus(200);

    }

    private List<FlowDefinitionImpl> GetActualFlows(List<String> roleName, DataManager dataManager) {
        List<FlowDefinitionImpl> res=new ArrayList<>();
        for (String role:roleName){
            for (FlowDefinitionImpl flow :dataManager.getStepperData().getFlows()){
                if (flow.getName().equals(role)){
                    res.add(flow);
                }
            }
        }
        return res;
    }
}
