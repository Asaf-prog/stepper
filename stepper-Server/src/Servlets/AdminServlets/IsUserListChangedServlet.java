package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "IsUserListChangedServlet", urlPatterns = {"/users-list-update"})
public class IsUserListChangedServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        if (userManager == null) {
            System.out.println("user manager is null");
        } else {
            resp.getWriter().println(gson.toJson(userManager.isUpToDate()));
            userManager.setUpToDate(true);
        }
    }
}
