LOAD "input.xml" AS input1;

table1 = TABLE InterfaceSummary; //look only for nodes that can be interpreted as tables

table2 = TABLE AreaEstimates;

//The following instruction tries to fit a table into a structure with different columns and rows names
//Warnings will be shown on the terminal and the returned table will be empty
PRINT FIT table1 INTO (AreaEstimates)x(FF, LUT);