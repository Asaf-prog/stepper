package Servlets.ClientServlets.Continuation;

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
import modules.flow.execution.FlowExecution;
import modules.mappings.Continuation;
import modules.mappings.ContinuationMapping;
import modules.mappings.CustomMapping;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import services.stepper.FlowDefinitionDTO;
import services.user.ContinuationConversionDTO;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "get output from last Flow and return the list that the user need to supply",urlPatterns = "/Client/CreateContinuation")
@MultipartConfig
public class ContinuationGetOutput extends HttpServlet {// this servlet need to return the outputs
    private static final Gson gson = new Gson();
    private List<Pair<String, DataDefinitionDeclaration>> currentMandatoryFreeInput;
    private List<Pair<String, DataDefinitionDeclaration>> currentOptionalFreeInput;
    private List<Pair<String, DataDefinitionDeclaration>> freeInputsOfTargetFlowSupply;
    private List<Pair<String, DataDefinitionDeclaration>> freeInputsOfTargetFlowNeedToSupply;
    private List<Pair<String,String>> suppliedData;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameOfCurrentFlow = req.getParameter("flowName");
        String nameOfTargetFlow = req.getParameter("targetFlowName");
        String username= SessionUtils.getUsername(req);
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        List<Role> roles=getRoles4User(username,dataManager);
        //get the id of the last execution of the user
        String id = getIdOfTheLastExe(username,dataManager);
        List<FlowDefinitionImpl> flows = mergeAllFlowFromRoles(roles);
        FlowDefinitionImpl targetFlowDefinition = getFlowByName(nameOfTargetFlow, flows);
        FlowDefinitionImpl sourceFlowDefinition = getFlowByName(nameOfCurrentFlow, flows);
        FlowExecution flowThatCurrentFinish = getFlowExecutionById(dataManager.getStepperData(),id);
        if (flowThatCurrentFinish == null){
            resp.setStatus(400);
            return;
        }else {
            setCurrentFreeInputsAndSplitToMandatoryAndOptional(sourceFlowDefinition);
           // setTheNewInputsThatTheUserSupply();
            Map<String,Object> outputs = flowThatCurrentFinish.getAllExecutionOutputs();
            checkInputsExistAndWhatTheUserNeedToSupply(targetFlowDefinition.getFreeInputs());
            addDataToList(flowThatCurrentFinish);
            checkIfTheDataSupplyByTheOutputs(outputs);
            customContinuation(sourceFlowDefinition,nameOfTargetFlow,flowThatCurrentFinish);
            List<String> getDataFromUser = getDataThatNotSupplyByContinuation(targetFlowDefinition);
            FlowDefinitionDTO targetFlowDTO = Mapper.convertToFlowDefinitionDTO(targetFlowDefinition);
            ContinuationConversionDTO listToRes = new ContinuationConversionDTO(suppliedData,getDataFromUser,targetFlowDTO);
            String json = new Gson().toJson(listToRes);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }
    }
    //first check if the data supply in the last run, the data found in user inputs
    //second check if the data supply buy the outputs
    //third check if the data custom continuation
    private void checkInputsExistAndWhatTheUserNeedToSupply(List<Pair<String, DataDefinitionDeclaration>> freeInputsTarget){
        freeInputsOfTargetFlowSupply = new ArrayList<>();
        freeInputsOfTargetFlowNeedToSupply = new ArrayList<>();
       //first
        for (Pair<String, DataDefinitionDeclaration> dataTarget: freeInputsTarget){
            if (existInOneOfTheList(dataTarget)) {
                for (Pair<String, DataDefinitionDeclaration> dataSource : currentMandatoryFreeInput) {
                    if (dataTarget.getKey().equals(dataSource.getKey()))
                        freeInputsOfTargetFlowSupply.add(dataSource);
                }
                for (Pair<String, DataDefinitionDeclaration> dataSource : currentOptionalFreeInput) {
                    if (dataTarget.getKey().equals(dataSource.getKey()))
                        freeInputsOfTargetFlowSupply.add(dataSource);
                }
            }
            else
                freeInputsOfTargetFlowNeedToSupply.add(dataTarget);
        }
    }
    private boolean existInOneOfTheList(Pair<String, DataDefinitionDeclaration> dataTarget){
        for (Pair<String, DataDefinitionDeclaration> dataSource : currentMandatoryFreeInput) {
            if (dataTarget.getKey().equals(dataSource.getKey()))
                return true;
        }
        for (Pair<String, DataDefinitionDeclaration> dataSource : currentOptionalFreeInput) {
            if (dataTarget.getKey().equals(dataSource.getKey()))
                return true;
        }
        return false;
    }
    private List<String> getDataThatNotSupplyByContinuation( FlowDefinitionImpl targetFlowDefinition){
        List<Pair<String, DataDefinitionDeclaration>> freeInputs =  targetFlowDefinition.getFlowFreeInputs();
        List<String> flowThatNeedToSupply = new ArrayList<>();
       for (Pair<String, DataDefinitionDeclaration> data : freeInputs){
           if(!isExistInData(data.getKey())){
               flowThatNeedToSupply.add(data.getKey());
           }
       }
       return flowThatNeedToSupply;
    }
    private boolean isExistInData(String nameOfDD){
        for (Pair<String,String> data : suppliedData){
            if (data.getKey().equals(nameOfDD))
                return true;
        }
        return false;
    }
    private void addDataToList(FlowExecution flowThatCurrentFinish){
        suppliedData = new ArrayList<>();
        List<Pair<String,String>> userInputs = flowThatCurrentFinish.getUserInputs();
        for (Pair<String,String> input: userInputs){
            for (Pair<String, DataDefinitionDeclaration> needToSupply : freeInputsOfTargetFlowSupply){
                if (input.getKey().equals(needToSupply.getKey()))
                    suppliedData.add(input);
            }
        }
    }
    private void checkIfTheDataSupplyByTheOutputs(Map<String,Object> outputs){
       //second
        for (Pair<String, DataDefinitionDeclaration> dataSource: freeInputsOfTargetFlowNeedToSupply){
            if (outputs.containsKey(dataSource.getKey()))
                suppliedData.add(new Pair<>(dataSource.getKey(),outputs.get(dataSource.getKey()).toString()));
        }
    }
    private void customContinuation(FlowDefinitionImpl sourceFlowDefinition,String nameOfTargetFlow,FlowExecution sourceFlowExe){
        List<Continuation> continuationList = sourceFlowDefinition.getContinuations();

        for (Continuation continuationMapping : continuationList){
            if (continuationMapping.getTargetFlow().equals(nameOfTargetFlow)){
                //we need to take the source data and connect it to the target data
                List<ContinuationMapping> mappings =   continuationMapping.getMappingList();
                for (ContinuationMapping mapping : mappings){
                    String sourceData = getTheDataByName(mapping.getSourceData(),sourceFlowExe);
                    if (!ifTheDataExist(mapping.getTargetData(),sourceData)){//replace exist data with the new data
                        suppliedData.add(new Pair<>(mapping.getTargetData(),sourceData));
                    }
                }
            }
        }
    }
    private String getTheDataByName(String nameOfDataToSearch,FlowExecution sourceFlowExe){
        for(Pair<String, String> data :sourceFlowExe.getUserInputs()){
            if (data.getKey().equals(nameOfDataToSearch))
                return data.getValue();
        }
        Map<String, Object> outputs = sourceFlowExe.getAllExecutionOutputs();
        if (outputs.containsKey(nameOfDataToSearch)){
            return outputs.get(nameOfDataToSearch).toString();
        }
        return null;
    }
    private boolean ifTheDataExist(String targetDataName,String sourceActualData){//replace
        for (int i = 0; i < suppliedData.size(); i++) {
            Pair<String, String> data = suppliedData.get(i);
            if (data.getKey().equals(targetDataName)) {
                // Data exists, so replace the value of the pair with the new value
                suppliedData.set(i, new Pair<>(targetDataName, sourceActualData));
                return true;
            }
        }
        return false; // Data does not exist in the list
    }
    private FlowExecution getFlowExecutionById(Stepper stepperData ,String id){
        List<FlowExecution> flowExecutions = stepperData.getFlowExecutions();
        for (int i = flowExecutions.size() - 1; i >= 0; i--) {
            FlowExecution flowExecution = flowExecutions.get(i);
            if (flowExecution.getUniqueId().toString().equals(id))
                return flowExecution;
        }
        return null;
    }
    private String getIdOfTheLastExe(String username, DataManager dataManager){
        UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user=userManager.getUser(username);
        int lastIndex = user.getFlowExecutions().size() -1;
        return user.getFlowExecutions().get(lastIndex);
    }
    private List<Role> getRoles4User(String username, DataManager dataManager) {
        List<Role> roles=new ArrayList<>();
        UserManager userManager= (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user=userManager.getUser(username);
        List<String> stringRoles=user.getRoles();
        for (String role:stringRoles)
            roles.add(dataManager.getRoleManager().getRoleByName(role));
        return roles;
    }
    private List<FlowDefinitionImpl> mergeAllFlowFromRoles(List<Role> roles){
        List<FlowDefinitionImpl> flowDefinitions = new ArrayList<>();
        for (Role role : roles){
            List<FlowDefinitionImpl> tempFlow = role.getFlows();
            for (FlowDefinitionImpl flowDefinition: tempFlow)
                flowDefinitions.add(flowDefinition);
        }
        return flowDefinitions;
    }
    private FlowDefinitionImpl getFlowByName(String nameOfFlow, List<FlowDefinitionImpl> flowDefinitions) {
        FlowDefinitionImpl flow = null;
        for (FlowDefinitionImpl flowDefinition : flowDefinitions) {
            if (flowDefinition.getName().equals(nameOfFlow)){
                flow = flowDefinition;
                break;
            }
        }
        return flow;
    }
    private void setCurrentFreeInputsAndSplitToMandatoryAndOptional(FlowDefinitionImpl sourceFlowDefinition){
        List<Pair<String, DataDefinitionDeclaration>> freeInputs = sourceFlowDefinition.getFlowFreeInputs();
        List<Pair<String, DataDefinitionDeclaration>> mandatoryInputs = new ArrayList<>();
        List<Pair<String, DataDefinitionDeclaration>> optionalInputs = new ArrayList<>();

        for (Pair<String, DataDefinitionDeclaration> pair : freeInputs) {
            if (pair.getValue().isMandatory())
                mandatoryInputs.add(pair);
            else
                optionalInputs.add(pair);
        }
        currentMandatoryFreeInput = mandatoryInputs;
        currentOptionalFreeInput = optionalInputs;
    }
}