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
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "get user list Servlet",urlPatterns = "/users-list")
public class GetUserListServlet extends HttpServlet {
    private Gson gson=new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //System.out.println("get user list servlet...");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        if (userManager == null || userManager.getUsers().size()==0) {

            resp.setStatus(204);

        } else {

            List<StepperUser> usernames = userManager.getUsers();
            String usersJson = gson.toJson(usernames);
            userManager.setUpToDate(true);
            try (PrintWriter out = resp.getWriter()) {//returning JSON object telling the client what version of chat is returned
                out.print(usersJson);
                out.flush();
            } catch (Exception e) {
                System.out.println("failed to init admin... ");
                throw new RuntimeException(e);
            }
        }
    }
}
