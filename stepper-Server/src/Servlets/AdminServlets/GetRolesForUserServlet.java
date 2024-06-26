package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetRolesForUserServlet", urlPatterns = {"/getRolesForUser"})
public class GetRolesForUserServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get username from request
        String username = req.getHeader("username");

        UserManager userManager = (UserManager) req.getServletContext().getAttribute("userManager" );

        List<String> roles = userManager.getRolesForUser(username);
        String rolesJson = gson.toJson(roles);
        resp.getWriter().write(rolesJson);
    }
}
