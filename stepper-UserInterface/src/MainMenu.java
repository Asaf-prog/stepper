import java.util.Scanner;

public class MainMenu implements Menu {

    public static void main(String[] args) {
        System.out.println("---Stepper UI---");

        Scanner input = new Scanner(System.in);
        MenuItems choice = null;
        do {
            displayMenu();
            try {
                choice = MenuItems.getMenuItem(input.nextInt());
                if ((choice.getValue() < 0) || (choice.getValue() > MenuItems.values().length) || (choice == MenuItems.MAIN_MENU)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please try again");
                input.nextLine();
                continue;
            }
                switch (choice) {
                    //todo add logics to choices
                    case MAIN_MENU:
                        System.out.println("MAIN_MENU");
                        break;
                    case LOAD_DATA:
                        System.out.println("LOAD_DATA");
                        break;
                    case FLOW_DEFINITION_MENU:
                        System.out.println("FLOW_DEFINITION_MENU");
                        break;
                    case FLOW_EXECUTION:
                        System.out.println("FLOW_EXECUTION");
                        break;
                    case FLOW_STATISTICS:
                        System.out.println("FLOW_STATISTICS");
                        break;
                    case STEPPER_STATISTICS:
                        System.out.println("STEPPER_STATISTICS");
                        break;
                    case EXIT:
                        System.out.println("Exiting... Goodbye!");
                        break;
                }
            } while (choice != MenuItems.EXIT);
        input.close();
    }
    public static void displayMenu() {
        System.out.println("MainMenu:");
        for (MenuItems item : MenuItems.values()) {
            if (item == MenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }

    }

    @Override
    public void displayMenu2() {

    }
}

