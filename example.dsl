LOAD "input.xml" AS input1;
LOAD "input2.xml" AS input2;
LOAD "input3.xml" AS input3;


table1 = TABLE AreaEstimates FROM input1;
PRINT table1;
PRINT TABLE AreaEstimates FROM input1;
table2 = TABLE AreaEstimates FROM input2;
//PRINT JOIN table1, table2;


table1 = TABLE AreaEstimates FROM input1; //look only for nodes that can be interpreted as tables


tables = TABLE AreaEstimates; //tables named AreaEstimates in all files

transposedTables = TRANSPOSE tables;

transposedTable3 = TRANSPOSE TABLE AreaEstimates FROM input3;

//The following expression defines a structure (rows names)x(columns names)
struct1 = STRUCTURE (AvailableResources, Resources)
                        x(LUT, FF);                         //notice that it may be written in different lines, but the "x(" cannot be separated

table3 = FIT table1 INTO struct1; //Ignore rows and columns that have different names

PRINT "Result of command 'FIT table1 INTO struct1' :";
PRINT table3;

struct1 = STRUCTURE ()x(LUT, FF); //(rows names)x(columns names)

table3 = FIT table1 INTO struct1; //When the structure has no rows, it uses the table's rows as they are. Same is valid for columns

PRINT "Result of command 'FIT table1 INTO struct1', considering only columns :";
PRINT table3;

table3 = FIT table1 INTO (AvailableResources, Resources)x();

PRINT "Result of command 'FIT table1 INTO struct1', considering only rows :";
PRINT table3;

PRINT "Transposed Tables:";
PRINT transposedTables;

// Find a table with a given structure:
table3 = FIND (AvailableResources, Resources)x(LUT, FF) FROM input1, input2; //Find tables that fit in the structure defined

PRINT "Result of command 'FIND (AvailableResources, Resources)x(LUT, FF) FROM input1, input2;' :";
PRINT table3;

//Alternative ways to create structures:
struct2 = STRUCTURE COL(d, e); //Structure considering only columns names

struct2 = STRUCTURE ROW(d, e); //Structure considering only rows names

table_to_join1 = TABLE AreaEstimates FROM input1;
table_to_join2 = TABLE AreaEstimates FROM input2;

table2 = JOIN table1, table2; //Similar to SQL

PRINT table2;

joinedTable = JOIN table_to_join1, table_to_join2; //Similar to SQL

PRINT "";
PRINT "Joined Table:";

PRINT joinedTable;


//table5 = table1 WHERE URAM=0; //Similar to SQL (only rows that match the value)

//table6 = table1 APPEND table2; //Rows in table2 are added below the rows of table1

//table7 = table1 APPEND_RIGHT table2; //columns in table2 are added to the right of the columnss of table1

//table1withsum = table1 WITH SUM; //add row with sums at the bottom

//table1withsum = table1 WITH MEAN; //add row with means at the bottom

//table8 = 4*table1 + table2*table2  //Matrix sum and multiplication

//row1 = ROW Resources FROM table1; //extract row named Resources from table1. Result is an array

//column1 = COLUMN FF FROM table1; //extract column named FF from table1. Result is an array

PRINT "PRINT TABLE AreaEstimates FROM input1, input2 
        << TRANSPOSE FIT TABLE AreaEstimates FROM input1, 
        input2 INTO STRUCTURE COL(FF, LUT);";

PRINT TABLE AreaEstimates FROM input1, input2 
        << TRANSPOSE FIT TABLE AreaEstimates 
        FROM input1, input2 INTO STRUCTURE COL(FF, LUT);

EXPORT table1 AS "table1.csv";
EXPORT table3 AS "fitted.csv";
EXPORT table1 AS "transposed3.json";
EXPORT transposedTables AS "transposedTables.csv";



