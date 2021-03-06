import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

public class XMLParser {

   public static Document load(String filepath) {

       try {
           final File inputFile = new File(filepath);
           final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           final Document doc = dBuilder.parse(inputFile);
           doc.getDocumentElement().normalize();
           System.out.println("Loaded file "+filepath);
           return doc;
       } catch (final Exception e) {
         e.printStackTrace();
      }
      return null;
   }


   static Table nodeToTable(Node nNode){
        Table table = new Table();
        ArrayList<String> rows = new ArrayList<>();
        NodeList nChildren = nNode.getChildNodes();
        ArrayList<String> prevcolumns = null;
        for (int temp2 = 0; temp2 < nChildren.getLength(); temp2++) { //for each row of the table
            HashMap<String, String> colValues = new HashMap<>();
            final Node nNode2 = nChildren.item(temp2);                
            if (nNode2.getNodeType() != Node.ELEMENT_NODE) 
                continue;
            ArrayList<String> newRow = new ArrayList<>();
            ArrayList<String> columns = new ArrayList<>();
            rows.add(nNode2.getNodeName());
            NodeList nChildren2 = nNode2.getChildNodes();
            for (int temp3 = 0; temp3 < nChildren2.getLength(); temp3++) {   //for each cell of the row
                final Node nNode3 = nChildren2.item(temp3);
                if (nNode3.getNodeType() != Node.ELEMENT_NODE) 
                    continue;
                columns.add(nNode3.getNodeName());
                colValues.put(nNode3.getNodeName(), nNode3.getTextContent());
            }
            if (table.column_names.size()==0 && table.row_names.size()==0){
                table.setColumnNames(new ArrayList<>(columns));
            }
            for (int temp3 = 0; temp3 < colValues.size(); temp3++) {
                if (table.column_names.contains(columns.get(temp3))) {
                    if (colValues.containsKey(table.column_names.get(temp3)))
                        newRow.add(colValues.get(table.column_names.get(temp3)));
                    else
                        newRow.add("");
                }
                else {
                    table.column_names.add(columns.get(temp3));
                }
            }
            Collections.sort(columns);
            if (!columns.equals(prevcolumns) && prevcolumns != null){
                System.out.println("Warning: different column names in each row");
            }
            prevcolumns = columns;
            table.addRow(newRow);
        }
        for (ArrayList<String> row : table.cells) { //it's necessary to check if all the rows have same number of cells
            while (row.size() < table.column_names.size()){
                row.add("");
            }
        }
        table.setRowsNames(rows);
        return table;
   }

   static ArrayList<Table> getTables(String tableName, ArrayList<Document> docs){
       ArrayList<Table> tables = new ArrayList<Table>();
        for  (Document doc : docs){ //for each document
            NodeList nList = doc.getElementsByTagName(tableName);
            for (int temp = 0; temp < nList.getLength(); temp++) { //for each table
                final Node nNode = nList.item(temp);
                tables.add(nodeToTable(nNode));
            }
        }
        return tables;
    }

    
    static ArrayList<Table> getTables(Structure structure, ArrayList<Document> docs){
        ArrayList<Table> tables = new ArrayList<Table>();
        for  (Document doc : docs){ //for each document
            if (structure.getRowsNames().size()>0){
                NodeList nList = doc.getElementsByTagName(structure.getRowsNames().get(0));
                ArrayList<Node> found_nodes = new ArrayList<>();
                for (int temp = 0; temp < nList.getLength(); temp++) { //for each table
                    final Node nNode = nList.item(temp);
                    final Node tableNode = nNode.getParentNode();          
                    if (found_nodes.contains(tableNode))
                        continue;
                    found_nodes.add(tableNode);
                    Table table = nodeToTable(tableNode);
                    table.fitInto(structure);
                    if (table != null){
                        tables.add(table.fitInto(structure));
                    }
                }
            }
            else {
                NodeList nList = doc.getElementsByTagName(structure.getColumnNames().get(0));
                ArrayList<Node> found_nodes = new ArrayList<>();
                for (int temp = 0; temp < nList.getLength(); temp++) { //for each table
                    final Node nNode = nList.item(temp);
                    final Node tableNode = nNode.getParentNode().getParentNode();
                    if (found_nodes.contains(tableNode))
                        continue;
                    found_nodes.add(tableNode);
                    Table table = nodeToTable(tableNode);
                    table.fitInto(structure);
                    if (table != null){
                        tables.add(table.fitInto(structure));
                    }
                }
            }
        }
        return tables;
    }


}