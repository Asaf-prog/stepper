package Menu;

import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import javax.xml.bind.JAXBException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class LoadDataMenu {
    public static void displayMenu() throws Exception {
        Stepper stepperData = DataManager.getData();
        System.out.println("---Load Data Menu---");
        System.out.println("(1) Load Existing Data \n(2) Save The Data \n(3) Load Stepper Xml Definition ");
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                Optional<Integer> choice = Optional.of(input.nextInt());
                switch (choice.get()) {
                    case 0://Main menu
                        return;
                    case 1:
                        DataManager.loadData();
                        return;
                    case 2:
                        DataManager.saveData();
                        return;
                    case 3:
                        getXmlDataFromUser();
                        return;
                    default:
                        System.out.println("Not in range of valid options , try again");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Your input is not a valid number, try again");
                input.next();
            }
        }
    }

    private static void getXmlDataFromUser() {
        System.out.println("Please enter the path to the xml file");
        Scanner input = new Scanner(System.in);
        while (true) {
            String path = input.nextLine();
            if (path.equals("0")) {
                System.out.println("Back to main menu");
                return;
            }
            try {
                GetDataFromXML.fromXmlFileToObject(path);
                Stepper stepperData = DataManager.getData();
                return;
            } catch (JAXBException e) {
                System.out.println("Jaxb Exception: Failed to load stepper, xml file is invalid");
                System.out.println(e.getMessage());
                System.out.println("Failed to load stepper Back to main menu");
                return;
            } catch (InputMismatchException e) {
                System.out.println("Your input is not a valid string, xml file is invalid");
                System.out.println("Please enter a valid path to the xml file");
                continue;
            } catch (Exception e) {
                System.out.println(e.getMessage());//for stepper and flow definition exceptions!!
                System.out.println("Failed to load stepper try again or enter 0 to go back to main menu");
            }

        }
    }
}
