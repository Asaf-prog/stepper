package modules.DataManeger;


import modules.flow.execution.FlowExecution;
import schemeTest.generatepackage.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;


//all generated classes should be generated into FromXML Folder..
public class GetDataFromXML {
    public static void main(String[] args) {
        String path = "C:\\Users\\User\\Desktop\\stepper-UserInterface\\src\\modules\\DataManeger\\FromXML\\STStepper.xml";
        }

    public static void fromXmlFileToObject(String path) {
      //  System.out.println("\nFrom File to Object");

        try {
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(STStepper.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            STStepper stStepper  = (STStepper) jaxbUnmarshaller.unmarshal(file);
            // System.out.println(stStepper);


            //do this for all flows
            //by the xml
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void fromObjectToXmlFile(Object obj) {
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