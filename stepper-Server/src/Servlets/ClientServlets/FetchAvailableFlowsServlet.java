package Servlets.ClientServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.*;
import modules.DataManeger.DataManager;
import modules.stepper.Stepper;
import services.stepper.FlowDefinitionDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//todo tomorrow : 1. add to the client the ability to get the flows from the server
// 2. add the new client/getflows new servlet and check i we can present the flows in the client side

@WebServlet(name = "getFlows Servlet",urlPatterns = "/Client/getFlows")
@MultipartConfig
public class FetchAvailableFlowsServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getFlows servlet...");
        Stepper stepper = DataManager.getData();
        List<FlowDefinitionDTO> flows= Mapper.getFlowsDTO(stepper.getFlows());
        String flowsJson = gson.toJson(flows);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {//returning JSON object telling the client what version of chat is returned
            out.print(flowsJson);
            out.flush();
        } catch (Exception e) {
            System.out.println("failed to init admin... ");
            throw new RuntimeException(e);
        }

    }




}
