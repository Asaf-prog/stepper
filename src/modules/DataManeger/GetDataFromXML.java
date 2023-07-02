package modules.DataManeger;

import modules.stepper.StepperDefinitionExceptionItems;
import modules.stepper.Stepper;
import modules.stepper.StepperDefinitionException;
import schemeTest2.generatepackage.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

//all generated classes should be generated into FromXML Folder..
public class GetDataFromXML {
    public static void fromXmlFileToObject(String path)throws Exception{
        File file = new File(path);
        if (!(file.exists() && file.length() > 0)) {//file not exist or empty
            throw new StepperDefinitionException(StepperDefinitionExceptionItems.XML_FILE_NOT_EXIST_OR_EMPTY,"the file "+path+" not exist or empty");
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(STStepper.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        STStepper stStepper  = (STStepper) jaxbUnmarshaller.unmarshal(file);//import all data from xml to stepperDemo
        DeepCopy deepCopy = new DeepCopy(stStepper);
        Stepper stepperData=deepCopy.copyAllDataInFields();//deep copy from stepperDemo to stepper
        stepperData.validateStepper();
        sentToStepper(stepperData);
}
    public static void fromStream2Stepper(InputStream stream)throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(STStepper.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        STStepper stStepper  = (STStepper) jaxbUnmarshaller.unmarshal(stream);//import all data from xml to stepperDemo
        DeepCopy deepCopy = new DeepCopy(stStepper);
        Stepper stepperData=deepCopy.copyAllDataInFields();//deep copy from stepperDemo to stepper
        stepperData.validateStepper();
        sentToStepper(stepperData);
    }
    private static void sentToStepper(Stepper stepperData) {DataManager dataManager = new DataManager(stepperData);}//the one and only data manager

    public static final String FILE_NAME = "/Users/cohen/Documents/GitHub/stepper/ex1.xml";

}
