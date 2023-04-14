package modules.dataDefinition.api;

public interface DataDefinition {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
