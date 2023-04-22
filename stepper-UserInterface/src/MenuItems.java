public enum MenuItems {
    MAIN_MENU("Main Menu"),
    LOAD_DATA("Load Data"),
    FLOW_DEFINITION_MENU("Flow Definition"),
    FLOW_EXECUTION("Flow Execution"),

    FLOW_STATISTICS("Flow Statistics"),
    STEPPER_STATISTICS("Stepper Statistics"),
    EXIT("Exit");

    private String name;

    MenuItems(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue(){
           return this.ordinal();

    }

    public static MenuItems getMenuItem(String name) {//get menu item by name
        for (MenuItems item : MenuItems.values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static MenuItems getMenuItem(int index) {//get menu item by index
            for (MenuItems item : MenuItems.values()) {
                if (item.ordinal() == index) {
                    return item;
                }
            }
            return null;
        }
    public static int getValuesByName(String name) {//get index by name
        for (MenuItems item : MenuItems.values()) {
            if (item.getName().equals(name)) {
                return item.ordinal();
            }
        }
        return -1;
    }

}
