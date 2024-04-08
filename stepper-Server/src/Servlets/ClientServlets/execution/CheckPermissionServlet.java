package Servlets.ClientServlets.execution;

import Servlets.userManager.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "check permission",urlPatterns = "/Client/checkPermission")

public class CheckPermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        String flowName = req.getHeader("flowName");
        String username = SessionUtils.getUsername(req);
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user = userManager.getUser(username);
        for (String role : user.getRoles()) {
           Role role1 = dataManager.getRoleManager().getRoleByName(role);
              for (FlowDefinitionImpl flow : role1.getFlows()) {
                  if (flow.getName().equals(flowName)) {
                      resp.setStatus(200);
                      return;
                  }
              }
        }
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
