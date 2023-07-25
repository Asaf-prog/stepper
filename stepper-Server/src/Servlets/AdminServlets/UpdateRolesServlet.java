package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.RoleManager;
import modules.DataManeger.users.StepperUser;
import services.user.RoleDTO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "update roles Servlet",urlPatterns = "/updateUserRoles")

public class UpdateRolesServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get list <String> of roles from body using gson
        String roleString = req.getReader().readLine();
        String username = req.getHeader("username");
        String isManager = req.getHeader("isManager");
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        List<String> roles  = gson.fromJson(roleString, typeToken.getType());
        setNewRoles(roles,dataManager,username, isManager);

        if (roles==null){
            resp.setStatus(400);
            return;
        }else{
            //add flows to response body
            resp.addHeader("username",username);
        }
    }

    private void setNewRoles(List<String> roles, DataManager dataManager, String username, String isManager) {
        RoleManager roleManager=dataManager.getRoleManager();

        UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user = userManager.getUser(username);
        userManager.setUpToDate(false);
        user.setManager(Boolean.parseBoolean(isManager));
        if (user.getIsManager() && !roles.contains("all-flows")){
            user.getRoles().add("all-flows");
        }else
            user.setRoles(roles);

    }
}
