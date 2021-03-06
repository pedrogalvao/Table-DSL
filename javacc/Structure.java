import java.util.ArrayList;

public class Structure {
    ArrayList<String> column_names;
    ArrayList<String> row_names;
    
    public Structure() {
        column_names = new ArrayList<>();
        row_names = new ArrayList<>();
    }

    public void addColumn(String col){
        column_names.add(col);
    }

    public void addRow(String row){
        row_names.add(row);
    }
    
    public ArrayList<String> getRowsNames() {
        return this.row_names;
    }

    public ArrayList<String> getColumnNames() {
        return this.column_names;
    }
}