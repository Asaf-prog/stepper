package modules.DataManeger;


import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;
import schemeTest.generatepackage.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;


//all generated classes should be generated into FromXML Folder..
public class GetDataFromXML {
    public static void main(String[] args) {
        //String path = "/Users/cohen/Documents/GitHub/stepper/ex1.xml";
        String path = "C:\\Users\\asafr\\Desktop\\testForXML\\ex1.xml";
        fromXmlFileToObject(path);
        }

    public static void fromXmlFileToObject(String path) {
      //  System.out.println("\nFrom File to Object");

        try {
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(STStepper.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            STStepper stStepper  = (STStepper) jaxbUnmarshaller.unmarshal(file);//import all data from xml to stepperDemo
            DeepCopy deepCopy = new DeepCopy(stStepper);
            Stepper stepperData=deepCopy.copyAllDataInFields();//deep copy from stepperDemo to stepper
            //fix all connections of mapping and stuff ,finish total def of flows
            //validation!!!!!
            stepperData.validateStepperFlows();
            //
            //update all aliases for each step
            stepperData.updateAliasesPerStep();
            sentToStepper(stepperData);//update Data Manager
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void sentToStepper(Stepper stepperData) {DataManager dataManeger = new DataManager(stepperData);}//the one and only data manager

    private static void fromObjectToXmlFile(Object obj) {//todo read to xml (bonus)
        try {

            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(FlowExecution.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(obj, file);
            jaxbMarshaller.marshal(obj, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    public static final String FILE_NAME = "/Users/cohen/Documents/GitHub/stepper/ex1.xml";

}
