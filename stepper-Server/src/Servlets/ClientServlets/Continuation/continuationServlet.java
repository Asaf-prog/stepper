package Servlets.ClientServlets.Continuation;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.mappings.Continuation;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "check Continuation",urlPatterns = "/Client/Continuation")
@MultipartConfig
public class continuationServlet extends HttpServlet {

    private static final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameOfFlow = req.getParameter("flowName");
        String username= SessionUtils.getUsername(req);
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        List<Role> roles=getRoles4User(username,dataManager);
        List<FlowDefinitionImpl> flows = mergeAllFlowFromRoles(roles);
        FlowDefinitionImpl flowDefinition = getFlowByName(nameOfFlow, flows);
        List<String> continuationFlowsName = new ArrayList<>();
        if (flowDefinition == null){
            resp.setStatus(400);
            return;
        }
        else {
            for (Continuation continuation : flowDefinition.getContinuations()){
                continuationFlowsName.add(continuation.getTargetFlow());
            }
        }
        String flowsJson = gson.toJson(continuationFlowsName);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {//returning JSON object telling the client what version of chat is returned
            out.print(flowsJson);
            out.flush();
        } catch (Exception e) {
            System.out.println("failed to receive continuation list... ");
            throw new RuntimeException(e);
        }
    }
    private List<Role> getRoles4User(String username, DataManager dataManager) {
        List<Role> roles=new ArrayList<>();
        UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user=userManager.getUser(username);
        List<String> stringRoles=user.getRoles();
        for (String role:stringRoles){
            roles.add(dataManager.getRoleManager().getRoleByName(role));
        }
        return roles;
    }
    private List<FlowDefinitionImpl> mergeAllFlowFromRoles(List<Role> roles){
        List<FlowDefinitionImpl> flowDefinitions = new ArrayList<>();
        for (Role role : roles){
            List<FlowDefinitionImpl> tempFlow = role.getFlows();
            for (FlowDefinitionImpl flowDefinition: tempFlow){
                flowDefinitions.add(flowDefinition);
            }
        }
        return flowDefinitions;
    }
    private FlowDefinitionImpl getFlowByName(String nameOfFlow, List<FlowDefinitionImpl> flowDefinitions) {
        FlowDefinitionImpl flow = null;
        for (FlowDefinitionImpl flowDefinition : flowDefinitions) {
            if (flowDefinition.getName().equals(nameOfFlow)){
                flow = flowDefinition;
                break;
            }
        }
        return flow;
    }
}
