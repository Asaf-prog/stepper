package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "get all flows string Servlet",urlPatterns = "/getAllFlows")
public class GetAllFlowsStringServlet extends HttpServlet {
    private Gson gson=new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        List<FlowDefinitionImpl> flows=dataManager.getStepperData().getFlows();
        List<String> flowsNames=new ArrayList<>();
        for (FlowDefinitionImpl flow:flows){
            flowsNames.add(flow.getName());
            }

        String json = gson.toJson(flowsNames);
        resp.getWriter().println(json);


    }
}
