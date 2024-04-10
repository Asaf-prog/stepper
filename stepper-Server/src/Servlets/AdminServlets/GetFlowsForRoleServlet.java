package Servlets.AdminServlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

@WebServlet(name = "get flows for role Servlet",urlPatterns = "/getFlowsForRole")

public class GetFlowsForRoleServlet extends HttpServlet {
    private Gson gson=new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get list <String> of roles from body using gson
        String roleString = req.getReader().readLine();
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        List<String> roles  = gson.fromJson(roleString, typeToken.getType());
        List<FlowDefinitionDTO> flows =getFlowsForRole(roles,resp);
        if (flows==null){
            return;
        }else{
            //add flows to response body
            resp.getWriter().println(gson.toJson(flows));
        }
    }

    private List<FlowDefinitionDTO> getFlowsForRole(List<String> roles, HttpServletResponse resp) {
        //get dataManager from context
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        //if dataManager isn't null
        if (dataManager == null) {
            resp.setStatus(400);
            return null;
        }
        List<FlowDefinitionImpl> flows = dataManager.getFlowsForRole(roles);
        int i=0;
        List<FlowDefinitionDTO> flowsDTO = new ArrayList<>();
        for (FlowDefinitionImpl flow : flows ) {
            flowsDTO.add( Mapper.convertToFlowDefinitionDTO(flows.get(i)));
            if (flowsDTO==null) {
                resp.setStatus(400);
                return null;
            }
            i++;
        }
        return flowsDTO;
    }
}
