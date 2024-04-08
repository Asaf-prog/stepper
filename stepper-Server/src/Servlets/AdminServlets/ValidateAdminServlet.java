package Servlets.AdminServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "admin checker Servlet",urlPatterns ="/Admin/valid")
public class ValidateAdminServlet extends HttpServlet {
    private static boolean isAdminAppInUse = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("valid admin servlet...");
        if (isAdminAppInUse==true){
            //return header with false
            resp.addHeader("active","true");
            resp.setStatus(200);
        }else{
            resp.addHeader("active","false");
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        isAdminAppInUse=true;
        resp.setStatus(200);
    }
}
