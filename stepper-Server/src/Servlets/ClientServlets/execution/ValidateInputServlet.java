package Servlets.ClientServlets.execution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "validate input Servlet",urlPatterns = "/Client/validate")
@MultipartConfig
public class ValidateInputServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data=req.getParameter("userInput");
        String type=req.getParameter("dataType");
        if (type.equals("Integer")) {
            try {
                Integer.parseInt(data);
            } catch (NumberFormatException e) {
                String msg = "Please enter a number";
                resp.addHeader("message",msg);
                resp.setStatus(422);
            }
        } else if (type.equals("Double")) {
            try {
                Double.parseDouble(data);
            } catch (NumberFormatException e) {
                String msg = "Please enter a double";
                resp.addHeader("message",msg);
                resp.setStatus(422);
            }
        }else if (type.equals("String")) {
            if (data.isEmpty()) {
                String msg = "Please enter a String";
                resp.addHeader("message", msg);
                resp.setStatus(422);
            }
        } else
            resp.setStatus(200);
    }
}
