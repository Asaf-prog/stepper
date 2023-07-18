package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import services.stepper.FlowDefinitionDTO;

import static mapper.Mapper.convertToFlowDefinitionDTO;

@WebServlet(name = "InitAdmin Servlet",urlPatterns = "/initAdmin")
public class InitAdminServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("init admin servlet...");
        Stepper stepper = DataManager.getData();
        List<FlowDefinitionDTO> flows=getFlowsDTO(stepper);
        String flowsJson = gson.toJson(flows);
        List<String> users = stepper.getUsers();
        String usersJson = gson.toJson(users);
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

    private List<FlowDefinitionDTO> getFlowsDTO(Stepper stepper) {
        List<FlowDefinitionDTO> flowsDTO=new ArrayList<>();
        List<FlowDefinitionImpl> flows = stepper.getFlows();
        for (FlowDefinitionImpl flow : flows) {
            FlowDefinitionDTO toAdd =convertToFlowDefinitionDTO(flow);
            flowsDTO.add(toAdd);
        }
        return flowsDTO;
    }


}
