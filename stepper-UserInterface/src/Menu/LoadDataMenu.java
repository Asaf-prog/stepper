package Menu;

import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import javax.xml.bind.JAXBException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class LoadDataMenu implements Menu {
    public static void displayMenu() throws Exception {
        Stepper stepperData = DataManager.getData();
        System.out.println("---Load Data Menu---");
        System.out.println("(1)Load Existing Data \n(2)Save The Data \n(3)Load Stepper Xml Definition ");
        Scanner input = new Scanner(System.in);
        try {
            Optional<Integer> choice = Optional.of(input.nextInt());

            switch (choice.get()) {
                case 0://Main menu
                    return;
                case 1:
                    DataManager.loadData();
                    break;
                case 2:
                    DataManager.saveData();
                    break;
                case 3:
                    getXmlDataFromUser();
                    break;
                default:
                    System.out.println("Not in range of valid options");
                    return;
            }
        } catch (InputMismatchException e) {
            throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, " Load Data Menu");
        }
    }

    private static void getXmlDataFromUser() {
        System.out.println("Please enter the path to the xml file");
        Scanner input = new Scanner(System.in);
        String path = input.nextLine();
        if (path.equals("0")) {
            System.out.println("Back to main menu");
            return;
        }
        try {
            GetDataFromXML.fromXmlFileToObject(path);
            Stepper stepperData = DataManager.getData();
        } catch (JAXBException e) {
            System.out.println("Jaxb Exception: Failed to load stepper, xml file is invalid");
            System.out.println(e.getMessage());
            System.out.println("Failed to load stepper Back to main menu");
            return;
        } catch (InputMismatchException e) {
            System.out.println("Your input is not a valid string, xml file is invalid");
            System.out.println("Please enter a valid path to the xml file");
            getXmlDataFromUser();
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to load stepper Back to main menu");
            return;
        }
    }

    @Override
    public void displayMenu2() {

    }
}
