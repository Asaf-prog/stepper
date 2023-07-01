package Servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ServletLogin",urlPatterns = "/loginShortResponse")
public class loginServlet extends HttpServlet {
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException{
    response.setContentType("text/plain;charset=UTF-8");
    response.getWriter().println("saar ihomo");
}

}
