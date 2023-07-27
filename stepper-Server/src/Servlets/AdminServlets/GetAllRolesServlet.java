package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;

import java.io.IOException;

@WebServlet(name = "get all roles Servlet",urlPatterns = "/getAllRoles")
public class GetAllRolesServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get dataManager from context
        //if dataManager is not null
        //get all roles from dataManager
        //add roles to response body
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        if (dataManager == null) {
            resp.setStatus(400);
            return;
        }
        resp.getWriter().println(gson.toJson(dataManager.getRoleManager().getAllRolesNames()));

    }
}
