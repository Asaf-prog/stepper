package modules.dataDefinition.impl.relation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationData {

    private List<String> columns;
    private List<SingleRow> rows;

    public RelationData(List<String> columns) {
        this.columns = columns;
        rows = new ArrayList<>();
    }

    public List<String> getRowDataByColumnsOrder(int rowId) {
        return new ArrayList<>();
    }

    private static class SingleRow {
        private Map<String, String> data;

        public SingleRow() {
            data = new HashMap<>();
        }

        public void addData(String columnName, String value) {
            data.put(columnName, value);
        }
    }
}
