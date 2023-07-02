package Servlets.loadXmlFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.DeepCopy;
import modules.stepper.FlowDefinitionException;
import modules.stepper.Stepper;
import modules.stepper.StepperDefinitionException;
import schemeTest2.generatepackage.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static modules.DataManeger.DataManager.stepperData;
import static modules.DataManeger.GetDataFromXML.sentToStepper;

@WebServlet(name = "loadXml",urlPatterns = "/loadAndDecodeFile")
@MultipartConfig
public class loadXml extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(STStepper.class);
            Unmarshaller jaxbUnmarshaller = null;

            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            STStepper stStepper  = null;//import all data from xml to stepperDemo
            stStepper = (STStepper) jaxbUnmarshaller.unmarshal(new StreamSource(request.getPart("file1").getInputStream()));

            DeepCopy deepCopy = new DeepCopy(stStepper);
            Stepper stepperData = null;//deep copy from stepperDemo to stepper
            stepperData = deepCopy.copyAllDataInFields();
            stepperData.validateStepper();
        } catch (FlowDefinitionException | StepperDefinitionException | JAXBException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        sentToStepper(stepperData);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////








    }
}

