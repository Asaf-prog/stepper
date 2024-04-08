package Servlets.AdminServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name = "setAdmin off servlet",urlPatterns = "/Admin/logout")
public class SetAdminOffServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                boolean isAdminOn = false;
                getServletContext().setAttribute("AdminOn",isAdminOn);
        }
    }
