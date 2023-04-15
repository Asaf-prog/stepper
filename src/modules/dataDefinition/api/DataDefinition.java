package modules.dataDefinition.api;

public interface DataDefinition {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
 //todo add all of the DataDefinitions mentioned in the word document
//etc: Number,List,File ,Mapping-map