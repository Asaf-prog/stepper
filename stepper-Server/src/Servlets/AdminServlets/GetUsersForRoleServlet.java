package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.users.StepperUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "get users for role",urlPatterns = "/getUsersForRole")

public class GetUsersForRoleServlet  extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roleName=req.getHeader("roleName");
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        if (dataManager == null) {
            resp.setStatus(400);
            return;
        }
        List<String> res=new ArrayList<>();
        UserManager userManager=(UserManager) getServletContext().getAttribute("userManager");
        for (StepperUser user:userManager.getUsers()){
            for (String role:user.getRoles()){
                if (role.equals(roleName)){
                   res.add(user.getUsername());
                }
            }
        }
        resp.getWriter().println(gson.toJson(res));

    }
}
