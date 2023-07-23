package Servlets.ClientServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.*;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import okhttp3.HttpUrl;
import services.stepper.FlowDefinitionDTO;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//todo tomorrow : 1. add to the client the ability to get the flows from the server
// 2. add the new client/getflows new servlet and check i we can present the flows in the client side

@WebServlet(name = "getFlows Servlet",urlPatterns = "/Client/getFlows")
@MultipartConfig
public class FetchAvailableFlowsServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getFlows servlet...");
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        if (dataManager == null) {
            resp.setStatus(400);
            return;
        }
        String username=SessionUtils.getUsername(req);
        List<Role> roles=getRoles4User(username,dataManager);

        List<FlowDefinitionDTO> flows= CollectAllFlows(roles,dataManager);
        String flowsJson = gson.toJson(flows);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {//returning JSON object telling the client what version of chat is returned
            out.print(flowsJson);
            out.flush();
        } catch (Exception e) {
            System.out.println("failed to init admin... ");
            throw new RuntimeException(e);
        }

    }

    private List<FlowDefinitionDTO> CollectAllFlows(List<Role> roles, DataManager dataManager) {
        List<FlowDefinitionDTO> flows=new ArrayList<>();

        for (Role role:roles){
            List<FlowDefinitionImpl> flow1=role.getFlows();
            for (FlowDefinitionImpl flow:flow1){
                flows.add(Mapper.convertToFlowDefinitionDTO(flow));
            }
        }
        return flows;
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


}
