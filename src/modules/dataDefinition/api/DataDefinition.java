package modules.dataDefinition.api;

public interface DataDefinition {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
     String getTypeName();
}
 //todo add all of the DataDefinitions mentioned in the word document
//etc: Number,List,File ,Mapping-map