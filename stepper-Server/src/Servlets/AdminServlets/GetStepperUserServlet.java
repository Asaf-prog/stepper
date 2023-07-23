package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.users.StepperUser;

import java.io.IOException;
@WebServlet(name = "get stepper user Servlet",urlPatterns = "/getUser")
public class GetStepperUserServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getHeader("username");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        if (userManager == null) {
            resp.setStatus(400);
            return;
        }
        StepperUser user = userManager.getUser(username);
        String userJson = gson.toJson(user);
        resp.getWriter().println(userJson);

    }
}
