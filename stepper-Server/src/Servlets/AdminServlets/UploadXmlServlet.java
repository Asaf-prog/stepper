package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;
import util.Constants;


@WebServlet(name = "upload Servlet",urlPatterns = "/uploadXmlFile")
@MultipartConfig
public class UploadXmlServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            InputStream res=request.getPart("file").getInputStream();
            GetDataFromXML.fromStream2Stepper(res);

            // send stepper  to client via body of response using json
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //forward to servlet that send all the needed information to the admin when uploading xml
            //redirect to post method
            response.sendRedirect(Constants.INIT_ADMIN);
          //  response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("failed to upload xml... :)");
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //print to page hello
        //maybe we need to do there to redirect to post? or cancel this option
        resp.getWriter().println("Hello person!");
    }
    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                .append("Size (of the file): ").append(part.getSize()).append("\n")
                .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }
        out.println(sb.toString());
    }
    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}