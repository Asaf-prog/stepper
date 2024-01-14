package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.RoleManager;
import services.user.RoleDTO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AddNewRoleServlet", urlPatterns = "/addRole")
public class AddNewRoleServlet extends HttpServlet {
    private Gson gson = new Gson();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().readLine();
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        RoleDTO newRoleDTO=gson.fromJson(body,RoleDTO.class);
        RoleManager roleManager=dataManager.getRoleManager();
        if (newRoleDTO.getName()==null||newRoleDTO.getName().equals("")){
            resp.setStatus(400);
            return;

        }
        if (isRoleExist(newRoleDTO.getName(),roleManager.getAllRolesNames())){
            resp.setStatus(400);
            return;
        }
        roleManager.addRole(new Role(newRoleDTO.getName(),newRoleDTO.getDescription()));
    }

    private boolean isRoleExist(String name, List<String> allRolesNames) {
        for (String roleName:allRolesNames){
            if (roleName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
