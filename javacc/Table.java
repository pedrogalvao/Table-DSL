import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.io.FileWriter;
import javax.swing.JTable;
import java.io.IOException;
import org.json.*;

public class Table {

    ArrayList<String> column_names;
    ArrayList<String> row_names;
    ArrayList<ArrayList<String>> cells;
    static final long serialVersionUID=0;
    public Table() {
        column_names = new ArrayList<>();
        row_names = new ArrayList<>();
        cells = new ArrayList<ArrayList<String>>();
    }
    public Table(Structure struct) {
        column_names = new ArrayList<>(struct.column_names);
        row_names = new ArrayList<>(struct.row_names);
        cells = new ArrayList<ArrayList<String>>();
        for (int i=0; i< row_names.size(); i++) {
            cells.add(new ArrayList<String>());
            for (int j=0; j< column_names.size(); j++) 
                cells.get(cells.size()-1).add("");
        }
    }
 
    public void setRowsNames(ArrayList<String> names) {
        this.row_names = new ArrayList<>(names);
    }

    public void setColumnNames(ArrayList<String> names) {
        this.column_names = new ArrayList<>(names);
    }

    public void addRow(ArrayList<String> row) {
        row = new ArrayList<>(row);
        cells.add(row);
    }
    
    public void setColumn(String col_name, ArrayList<String> col) {
        col = new ArrayList<>(col);
        //System.out.println("setcolumn index of column: "+column_names.indexOf(col_name));
        int index = column_names.indexOf(col_name);
        if (index >= 0)
            if (cells.size()==col.size()){
                for ( int i=0; i < cells.size(); i++) {
                    cells.get(i).set(index, col.get(i));
                }
            }
            else if (col.size() > cells.size()){
                for (int i=0; i <= col.size() - cells.size(); i++){
                    ArrayList<String> new_row = new ArrayList<String>(Collections.nCopies(this.column_names.size(), ""));
                    Collections.fill(new_row, "");
                    cells.add(new_row);
                    row_names.add("");
                }
                for ( int i=0; i < cells.size(); i++) {
                    cells.get(i).set(index, col.get(i));
                }
            }
    }

    
    public void setRow(String row_name, ArrayList<String> row) {
        row = new ArrayList<>(row);
        int index = row_names.indexOf(row_name);    
        if (index >= 0)
            if (cells.get(index).size()==row.size()){
                cells.set(index, row);
            }
            else if (row.size() > column_names.size()){
                for (int i=0; i <= row.size() - column_names.size(); i++){
                    column_names.add("");
                }
                cells.set(index, row);
            }
    }

    public void setCell(String row_name, String col_name, String value) {
        //System.out.println("setcolumn index of column: "+column_names.indexOf(col_name));
        int col_index = column_names.indexOf(col_name);
        if (col_index >= 0){
            int row_index = row_names.indexOf(row_name);
            if (row_index >= 0){
                ArrayList<String> row = this.cells.get(row_index);
                row.set(col_index, value);
            }
        }
    }

    public ArrayList<String> getRow(final String rowName) {
        return cells.get(row_names.indexOf(rowName));
    }

    public ArrayList<String> getColumn(final String colName) {
        final int index = column_names.indexOf(colName);
        //System.out.println("getcolumn index of "+colName+":"+index);
        final ArrayList<String> column = new ArrayList<>();
        for (final ArrayList<String> row : cells)
            column.add(row.get(index));
        return column;
    }

    String getCell(final String row, final String column) {
        final int i = row_names.indexOf(row);
        final int j = column_names.indexOf(column);
        return cells.get(i).get(j);
    }

    public void print() {
        
        String header = " | " + new String(new char[20]).replace('\0', ' ') + " | ";
        for (final String col_name : column_names){
            header += col_name.substring(0, Math.min(col_name.length(), 10)) + (new String(new char[Math.max(10-col_name.length(),0)]).replace('\0', ' '));
            header += " | ";
        }
        String top = " "+(new String(new char[header.length()-2]).replace('\0', '_'));
        System.out.println(top);
        System.out.println(header);
        String div = " |" +(new String(new char[header.length()-4]).replace('\0', '-'))+"|";
        System.out.println(div);
        for (int i=0; i < row_names.size(); i++){
            String line = " | " + new String(new char[Math.max(20-row_names.get(i).length(),0)]).replace('\0', ' ') + row_names.get(i).substring(0, Math.min(row_names.get(i).length(), 20)) + " | ";
            for (final String cell : cells.get(i)){
                String ncell = (new String(new char[Math.max(10-cell.length(),0)]).replace('\0', ' ')) + cell.substring(0, Math.min(cell.length(), 10));
                line += ncell + " | ";
            }
            System.out.println(line);
        }
        System.out.println(div);
    }

    public void exportJSON(String filename){
        try {
            FileWriter jsonWriter = new FileWriter(filename);
            
            JSONArray jsArray = new JSONArray();
            jsonWriter.append("{\n");
            jsonWriter.append("\"row_names\": ");
            jsArray = new JSONArray(row_names);
            jsonWriter.append(jsArray.toString());
            jsonWriter.append(",\n");

            jsonWriter.append("\"column_names\": ");
            jsArray = new JSONArray(column_names);
            jsonWriter.append(jsArray.toString());
            jsonWriter.append(",\n");

            jsonWriter.append("\"cells\": ");
            jsonWriter.append("[");
            for (int i=0; i < cells.size(); i++){
                ArrayList<String> row  = cells.get(i);
                jsArray = new JSONArray(row);
                jsonWriter.append(jsArray.toString());
                if (i < cells.size()-1){
                    jsonWriter.append(",");
                }
            }
            jsonWriter.append("]\n");
            jsonWriter.append("}");
            jsonWriter.flush();
            jsonWriter.close();
            System.out.println("Successfuly exported to file "+filename);
            
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    public void exportCSV(String filename){
        try {
            FileWriter csvWriter = new FileWriter(filename);
            
            for (String col_name : column_names){
                csvWriter.append(",");
                csvWriter.append(col_name);
            }
            csvWriter.append("\n");
            for (int j=0; j < cells.size(); j++){
                ArrayList<String> row  = cells.get(j);
                csvWriter.append(row_names.get(j));
                csvWriter.append(",");
                for (int i=0; i< row.size(); i++){
                    String cell = row.get(i);
                    csvWriter.append(cell);
                    if (i< row.size()-1){
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Successfuly exported to file "+filename);
            
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Table transpose(){
        Table result = new Table();
        result.setRowsNames(this.column_names);
        result.setColumnNames(this.row_names);
        for (String col:this.column_names){
            result.addRow(this.getColumn(col));
        }
        return result;

    }

    public Table sum(Table T){
        Table result = new Table();

        if (row_names==T.row_names && column_names==T.column_names){
            result.setRowsNames(row_names); 
            result.setColumnNames(column_names); 
            for (int i=0; i<cells.size(); i++){
                ArrayList<String> row1 = cells.get(i);
                ArrayList<String> row2 = T.cells.get(i);
                ArrayList<String> row3 = new ArrayList<String>();

                for (int j=0; j<row1.size(); i++){
                    String cell1 = row1.get(j);
                    String cell2 = row2.get(j);
                    row3.add(Integer.toString(Integer.parseInt(cell1)+Integer.parseInt(cell2)));

                }
                result.addRow(row3);
            }

        }
        else System.out.println("Error : Can't sum tables with different fields");


        return result;
    }

    public Table fitInto(Structure structure) {
       // System.out.println("fitInto");
        Table fitted_table = new Table(structure);
        Boolean a=false, b=false;
        if (fitted_table.row_names.size()==0)
            a=true;
        if (fitted_table.column_names.size()==0)
            b=true;
        
        if (fitted_table.cells.size()==0){
            for (String col_name : fitted_table.column_names) {
                ArrayList<String> col = this.getColumn(col_name);
                try {
                    col = this.getColumn(col_name);
                }
                catch (java.lang.ArrayIndexOutOfBoundsException e){
                    System.out.println("ERROR: the table can't be fitted to this structure. Columns are missing");
                    return null;
                }
                //System.out.println("fitInto col_name: "+col_name);
                fitted_table.setColumn(col_name, col);
            }
        }
        else if (fitted_table.cells.get(0).size()==0){
            for (int i=0; i<fitted_table.row_names.size(); i++) {
                String row_name = fitted_table.row_names.get(i);
                ArrayList<String> row;
                try {
                    row = this.getRow(row_name);
                }
                catch (java.lang.ArrayIndexOutOfBoundsException e){
                    System.out.println("ERROR: the table can't be fitted to this structure. Rows are missing");
                    return null;
                }
                fitted_table.setRow(row_name, row);
            }
        }
        else {
            for (String col_name : fitted_table.column_names) {
                for (String row_name : fitted_table.row_names) {
                    try {
                        fitted_table.setCell(row_name, col_name, this.getCell(row_name, col_name));
                    }
                    catch (java.lang.ArrayIndexOutOfBoundsException e){
                        System.out.println("Warning: cells are missing");
                        fitted_table.setCell(row_name, col_name, "");
                    }
                }
            }
        }
        if (a)
            fitted_table.setRowsNames(row_names);
        if (b)
            fitted_table.setColumnNames(column_names);
        return fitted_table;
    }

    public Table join(Table table) {
        ArrayList<String> common_columns = new ArrayList<>();
        ArrayList<String> table1_columns = new ArrayList<>();
        ArrayList<String> table2_columns = new ArrayList<>();
        for (String col_name : column_names){
            if (table.column_names.contains(col_name)) {
                common_columns.add(col_name);
            }
            else {
                table1_columns.add(col_name);
            }
        }
        for (String col_name : table.column_names){
            if (!common_columns.contains(col_name)) {
                table2_columns.add(col_name);
            }
        }
        Table result = new Table();
        ArrayList<String> result_columns = new ArrayList<>();
        result_columns.addAll(common_columns);
        result_columns.addAll(table1_columns);
        result_columns.addAll(table2_columns);
        result.setColumnNames(result_columns);

        for (ArrayList<String> row1 : cells){
            for (ArrayList<String> row2 : table.cells) {
                Boolean match = true;
                for (String col_name : common_columns){
                    int i = column_names.indexOf(col_name);
                    int j = table.column_names.indexOf(col_name);
                    if (!row1.get(i).equals(row2.get(j))) {
                        match=false;
                        break;
                    }
                }
                if (!match)
                    continue;
                ArrayList<String> new_row = new ArrayList<>();
                for (String col_name2 : common_columns){
                    int i = column_names.indexOf(col_name2);
                    new_row.add(row1.get(i));
                }
                for (String col_name2 : table1_columns){
                    int i = column_names.indexOf(col_name2);
                    new_row.add(row1.get(i));
                }
                for (String col_name2 : table2_columns){
                    int i = table.column_names.indexOf(col_name2);
                    new_row.add(row2.get(i));
                }
                result.row_names.add("");
                result.addRow(new_row);
            }
        }
        return result;
    }
}