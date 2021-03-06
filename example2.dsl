table1 = TABLE InterfaceSummary; //look only for nodes that can be interpreted as tables

//The following expression extracts a table from the first parameter
//If no parameters are passed an exception will be thrown;
table2 = TABLE AreaEstimates FROM $0;

PRINT " Table 1:";
PRINT table1;
PRINT TRANSPOSE table1;
