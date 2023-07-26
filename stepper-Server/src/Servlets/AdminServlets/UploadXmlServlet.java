package Servlets.AdminServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;


@WebServlet(name = "upload Servlet",urlPatterns = "/uploadXmlFile")
@MultipartConfig
public class UploadXmlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //print to page hello world
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DataManager dataManager = (DataManager) request.getServletContext().getAttribute("dataManager");
            InputStream res = request.getPart("file").getInputStream();
            if (dataManager == null) {//first time


                DataManager dataManager1 = GetDataFromXML.fromStream2Stepper(res);
                //send to servletContext
                request.getServletContext().setAttribute("dataManager", dataManager1);
                dataManager1.getStepperData().setXmlPath(getFileName(request.getPart("file")));


                response.addHeader("url", dataManager1.getStepperData().getXmlPath());

            }else{//...
                //compare same flows
                //only add to dataManager new flows
                Stepper stepper = GetDataFromXML.newFlowsToStepper(res);
                //send to servletContext
                try{
                    dataManager.updateStepper(stepper);
                    dataManager.updateRoles();
                    dataManager.getStepperData().setXmlPath(getFileName(request.getPart("file")));
                    response.addHeader("url", dataManager.getStepperData().getXmlPath());


                }catch (Exception e){
                    System.out.println("failed to update stepper... :)");
                    throw new RuntimeException(e);
                }





            }
        }catch (Exception e) {
            System.out.println("failed to upload xml... :)");
            throw new RuntimeException(e);
        }


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
