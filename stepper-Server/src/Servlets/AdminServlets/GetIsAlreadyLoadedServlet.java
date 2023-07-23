package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;

import java.io.IOException;

@WebServlet(name = "GetIsAlreadyLoadedServlet", value = "/getIsAlreadyLoaded")
public class GetIsAlreadyLoadedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        if (dataManager == null) {
            resp.getWriter().write(new Gson().toJson(false));
        } else {
            resp.getWriter().write(new Gson().toJson(true));
        }
    }


}
