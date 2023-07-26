package Servlets.AdminServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name = "GetIsOnlyOneAdminServlet",urlPatterns = "/getIsOnlyOneAdmin")
public class GetIsOnlyOneAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try {
            boolean isAdminOn = (boolean) getServletContext().getAttribute("AdminOn");
            if (isAdminOn) {
                resp.getOutputStream().print("false");
                return;
            }else{
                getServletContext().setAttribute("AdminOn","true");//admin now on
            }
        }catch (Exception e) {
           //
        }//for first time
        getServletContext().setAttribute("AdminOn",true);//admin on first time
        resp.getOutputStream().print("true");//
    }
}
