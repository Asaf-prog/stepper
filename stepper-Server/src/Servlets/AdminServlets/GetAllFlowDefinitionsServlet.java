package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import services.stepper.FlowDefinitionDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "get all flows definition Servlet",urlPatterns = "/getFlowsDefinitions")

public class GetAllFlowDefinitionsServlet  extends HttpServlet  {
        private Gson gson=new Gson();

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
            List<FlowDefinitionImpl> flows=dataManager.getStepperData().getFlows();
            List<FlowDefinitionDTO> flowsDef=new ArrayList<>();
            for (FlowDefinitionImpl flow:flows){
                flowsDef.add(Mapper.convertToFlowDefinitionDTO(flow));
            }

            String json = gson.toJson(flowsDef);
            resp.getWriter().println(json);


        }
    }
