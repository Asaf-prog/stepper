package Servlets.AdminServlets;

import Servlets.DTO.DTO;
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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import modules.DataManeger.GetDataFromXML;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import util.Constants;

import javax.xml.transform.stream.StreamSource;

@WebServlet(name = "InitAdmin Servlet",urlPatterns = "/initAdmin")
public class InitAdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("init admin servlet...");
        Stepper stepper = DataManager.getData();
        List<FlowDefinitionImpl> flows = stepper.getFlows();
        //copy into string list of flows names
        //need to convert to Gson and then send in to the client all the list of the flow(=> need to present the step in the flows)

//        List<String> flowsNames =new ArrayList<>();
//        for (FlowDefinitionImpl flow : flows) {
//            flowsNames.add(flow.getName());
//        }
        DTO data = new DTO(flows);
        Gson gson = new Gson();

        String flowsJson = gson.toJson(data.grtFlowsName());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {//returning JSON object telling the client what version of chat is returned
            out.print(flowsJson);
            out.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
