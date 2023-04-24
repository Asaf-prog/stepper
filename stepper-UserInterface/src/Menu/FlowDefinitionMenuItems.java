package Menu;

public enum FlowDefinitionMenuItems {
    MAIN_MENU("Main Menu"),
    SHOW_FLOWS("Show Exiting Flows"),
    EXIT("Exit");

    private String name;

    FlowDefinitionMenuItems(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue(){
        return this.ordinal();

    }

    public static MainMenuItems getMenuItem(String name) {//get menu item by name
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static MainMenuItems getMenuItem(int index) {//get menu item by index
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item.ordinal() == index) {
                return item;
            }
        }
        return null;
    }
    public static int getValuesByName(String name) {//get index by name
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item.getName().equals(name)) {
                return item.ordinal();
            }
        }
        return -1;
    }

}

