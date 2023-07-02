package Servlets.testers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import java.io.IOException;
@WebServlet(name = "Continue Servlet",urlPatterns = "/xxxxx")
public class ContinueServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        resp.getWriter().println( "Saar!");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

    }








}
