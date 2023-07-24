package Servlets.ClientServlets.login;

import Servlets.userManager.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import util.http.HttpClientUtil;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "IsManagerServlet", urlPatterns = "/Client/isManager")
public class IsManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = SessionUtils.getUsername(req);
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        boolean isManager = userManager.isUserManager(username);
        resp.getWriter().write(String.valueOf(isManager));
    }
}
