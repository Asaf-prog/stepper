package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import services.user.RoleDTO;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "get all roles DTOs Servlet",urlPatterns = "/getAllRolesDTOs")

public class GetAllRolesDTOsServlet extends HttpServlet {

    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        List<Role> roles=dataManager.getRoleManager().getRoles();
        List<RoleDTO> dtos=new ArrayList<>();
        for (Role role:roles){
            dtos.add(new RoleDTO(role.getName(),role.getFlows()));
        }
        resp.getWriter().println(gson.toJson(dtos));


    }
}
