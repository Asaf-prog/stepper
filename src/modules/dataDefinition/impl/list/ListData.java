package modules.dataDefinition.impl.list;
import java.io.Serializable;
import java.util.List;

public class ListData<T> implements Serializable {
    List<T> data;

    public ListData() {
        super();
    }

}
