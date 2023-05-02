package modules.dataDefinition.impl.relation;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelationData {
    public static void main(String[] args) {
        //RelationData data = RelationData.Demo();
       // data.printTable();
    }
    private List<String> columns;
    private List<SingleRow> rows;
    public RelationData(List<String> columns) {
        this.columns = columns;
        rows = new ArrayList<>();
    }
    public RelationData() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }
    public boolean isEmpty() {
        return columns.isEmpty() && rows.isEmpty();
    }
    public static RelationData Demo() {
        RelationData data = new RelationData();
        List<String> columns = new ArrayList<>();
        columns.add("Column 1");
        columns.add("Column 2");
        data.setColumns(columns);
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            List<String> rowData = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                rowData.add(Integer.toString(random.nextInt(10)));
            }
            data.addRow(rowData);
        }
        return data;
    }
    public void printTable(){
        System.out.println(this.getColumns().toString());
        for (RelationData.SingleRow row : this.getRows()) {
            System.out.println(row.getData().toString());
        }

    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getColumns().toString()).append('\n');
        for (RelationData.SingleRow row : this.getRows()) {
            sb.append(row.getData().toString()).append('\n');
        }
        return sb.toString();
    }

    public String getValInList(int index){return columns.get(index);}
    public List<String> getRowDataByColumnsOrder(int rowId) {return new ArrayList<>();}
    public int getNumColumns() {return columns.size();}
    public int getNumRows(){return rows.size();}
    public String getValueAt(int index1, int index2) {
        SingleRow t = rows.get(index1);
        return t.getData().get(index2);
    }
    public List<String> getColumns() {return columns;}

    public void setColumns(List<String> columns) {this.columns = columns;}

    public List<SingleRow> getRows() {return rows;}

    public void setRows(List<SingleRow> rows) {this.rows = rows;}

    public void addRow( List<String> row) {

        SingleRow temp = new SingleRow();
        for (int i = 0; i < row.size(); i++) {
            temp.addData(i, row.get(i));
        }
        rows.add(temp);
    }
   //////////////////////////////
    public static class SingleRow {
        private List<String> data;
        public List<String> getData() {return data;}

        public void setData(List<String> data) {this.data = data;}

        public SingleRow() {
            data = new ArrayList<>();
        }
        public void addData(int columnNum, String value) {
            data.add(columnNum, value);
        }
        // todo=> add a new line, add a new row
    }
}
