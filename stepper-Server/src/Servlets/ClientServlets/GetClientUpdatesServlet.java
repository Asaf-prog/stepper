package Servlets.ClientServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.users.StepperUser;
import utils.SessionUtils;

import java.io.IOException;
@WebServlet(name = "GetClientUpdatesServlet",urlPatterns = "/Client/getClientUpdates")
public class GetClientUpdatesServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = SessionUtils.getUsername(req);
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        StepperUser updatedUser = userManager.getUser(clientName);
        String isManager=req.getHeader("isManager");
        boolean isManagerBoolean = Boolean.parseBoolean(isManager);
        if(isManagerBoolean==updatedUser.getIsManager()){
           String json = gson.toJson(updatedUser);
              resp.getWriter().print(json);
              resp.setStatus(200);
        }
        else{
            resp.setStatus(400);
        }
    }
}
