package Servlets.ClientServlets.execution;

import Servlets.userManager.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.users.StepperUser;
import utils.SessionUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@WebServlet(name = "IsAuthorizedServlet", urlPatterns = {"/Client/isAuthorized"})
public class IsAuthorizedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String flowName = req.getHeader("flowName");
        String username = SessionUtils.getUsername(req);
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user = userManager.getUser(username);
        AtomicBoolean isAuthorized = new AtomicBoolean(false);
        for (String role : user.getRoles()) {
            dataManager.getRoleManager().getRoleByName(role).getFlows().forEach(flow -> {
                if (flow.getName().equals(flowName)) {
                    isAuthorized.set(true);
                }
            }
            );

        }
        if (isAuthorized.get()) {
            resp.setStatus(200);
        } else {
            resp.setStatus(401);
        }


    }
}
