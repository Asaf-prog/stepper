package Servlets.ClientServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import okhttp3.Request;
import okhttp3.RequestBody;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "IsClientRolesChangedServlet", urlPatterns = {"/Client/isRolesChanged"})
public class IsClientRolesChangedServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String body = req.getReader().readLine();
        List<String> roles = gson.fromJson(body, new TypeToken<List<String>>(){}.getType());

        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        boolean isSame = userManager.getRolesForUser(SessionUtils.getUsername(req)).equals(roles);
        if (isSame) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        } else {
            String newRoles = gson.toJson(userManager.getRolesForUser(SessionUtils.getUsername(req)));
            resp.getWriter().write(newRoles);
            resp.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
