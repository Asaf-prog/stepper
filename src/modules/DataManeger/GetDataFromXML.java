package modules.DataManeger;


import modules.flow.execution.FlowExecution;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;


//all generated classes should be generated into FromXML Folder..
public class GetDataFromXML {
    public static void main(String[] args) {
            fromXmlFileToObject();
        }

    private static void fromXmlFileToObject() {
        System.out.println("\nFrom File to Object");

        try {

            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(FlowExecution.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            FlowExecution flowExecution = (FlowExecution) jaxbUnmarshaller.unmarshal(file);

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

    public static final String FILE_NAME = "file.xml";
}
