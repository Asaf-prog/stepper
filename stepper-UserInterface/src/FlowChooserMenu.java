import modules.DataManeger.DataManager;

public class FlowChooserMenu implements Menu {
    @Override
    public void displayMenu2() {
        System.out.println("FlowChooserMenu:");
        // DataManager dataManager = DataManager.getData();
        for (MenuItems item : MenuItems.values()) {
            if (item == MenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }
    }
}
