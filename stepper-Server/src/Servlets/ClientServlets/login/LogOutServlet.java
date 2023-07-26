package Servlets.ClientServlets.login;

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

@WebServlet(name = "LogOutServlet",urlPatterns = "/Client/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = SessionUtils.getUsername(req);
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        userManager.setUpToDate(false);
        userManager.logoutUser(username);
        //SessionUtils.clearSession(req);
    }
}
