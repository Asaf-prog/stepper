package Servlets.ClientServlets.login;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.DataManeger.Role;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.step.api.DataDefinitionDeclaration;
import services.stepper.FlowDefinitionDTO;
import utils.SessionUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static Constants.Constants.FLOWNAME;

@WebServlet(name = "type of file",urlPatterns = "/Client/getFileTypeName")
@MultipartConfig
public class GetFileNameInDD extends HttpServlet {
    private static final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getFlows servlet...");
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        if (dataManager == null) {
            resp.setStatus(400);
            return;
        }
        String username= SessionUtils.getUsername(req);
        //FlowDefinitionImpl = getFlowByName()
        List<Role> roles=getRoles4User(username,dataManager);

        List<FlowDefinitionDTO> flows= CollectAllFlows(roles,dataManager);
        FlowDefinitionImpl flowDefinitions = CollectAllFlowsImpl(roles,dataManager,req.getParameter(FLOWNAME));
        if (flowDefinitions !=null) {
            List<String> nameOfDDOfTypeFile = collectDataDefinitionByFlowFromTypeFile(flowDefinitions);
            if (nameOfDDOfTypeFile!=null) {
                String flowsJson = gson.toJson(nameOfDDOfTypeFile);
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
    }
    List <String>collectDataDefinitionByFlowFromTypeFile(FlowDefinitionImpl flowDefinition){
        List<String> nameOFDD = new ArrayList<>();
        File objForCompare = new File("path/to/file1.txt");
        List<Pair<String, DataDefinitionDeclaration>> freeInputs = flowDefinition.getFlowFreeInputs();
        for (Pair<String, DataDefinitionDeclaration> free: freeInputs){
            System.out.println(free.getValue().dataDefinition().getTypeName());
            if ((free.getValue().dataDefinition().getClass() ==objForCompare.getClass())|| (existInFileName(free.getKey()))){
                nameOFDD.add(free.getKey());
            }
        }
        return nameOFDD;
    }
    private boolean existInFileName(String name2Search){
        for (FileName n : FileName.values()){
            if (n.name().equalsIgnoreCase(name2Search))
                return true;
        }
        return false;
    }
    private  FlowDefinitionImpl CollectAllFlowsImpl(List<Role> roles, DataManager dataManager,String nameOfFlow){
        FlowDefinitionImpl flowDefinitions = null;
        for (Role role:roles){
            List<FlowDefinitionImpl> flow1=role.getFlows();
            for (FlowDefinitionImpl flow:flow1){
                if (flow.getName().equals(nameOfFlow)){
                    flowDefinitions = flow;
                }
            }
        }
        return flowDefinitions;
    }
    private List<FlowDefinitionDTO> CollectAllFlows(List<Role> roles, DataManager dataManager) {
        List<FlowDefinitionDTO> flows=new ArrayList<>();

        for (Role role:roles){
            List<FlowDefinitionImpl> flow1=role.getFlows();
            for (FlowDefinitionImpl flow:flow1){
                flows.add(Mapper.convertToFlowDefinitionDTO(flow));
            }
        }
        return flows;
    }

    private List<Role> getRoles4User(String username, DataManager dataManager) {
        List<Role> roles=new ArrayList<>();
        UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user=userManager.getUser(username);
        List<String> stringRoles=user.getRoles();
        for (String role:stringRoles){
            roles.add(dataManager.getRoleManager().getRoleByName(role));
        }
        return roles;
    }
}
