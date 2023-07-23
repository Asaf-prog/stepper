package Servlets.ClientServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "get roles Servlet",urlPatterns = "/getRolesForClient")

public class GetRolesServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = SessionUtils.getUsername(req);
        if (username==null){
            resp.setStatus(400);
            return;
}else{
            UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
            List<String> roles = userManager.getUser(username).getRoles();
            String rolesJson = gson.toJson(roles);
            try(PrintWriter out = resp.getWriter()){
                out.println(rolesJson);
                out.flush();
            }
        }
    }
}
