package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
@WebServlet(name = "get all executions",urlPatterns = "/getAllExecutions")

public class GetAllExecutionsServlet extends HttpServlet {
    Gson gson=new Gson();

}
