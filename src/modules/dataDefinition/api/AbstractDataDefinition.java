package modules.dataDefinition.api;

public abstract class AbstractDataDefinition implements DataDefinition {
    private final String name;
    private final boolean userFriendly;
    private final Class<?> type;

    public AbstractDataDefinition(String name, boolean userFriendly, Class<?> type) {
        this.name = name;
        this.userFriendly = userFriendly;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isUserFriendly() {
        return userFriendly;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
    @Override
    public String getTypeName() {
        return type.getSimpleName();
    }
}
