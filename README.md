## DSL for Extracting Data From XML Files

### SUMMARY:
The DSL developed can be used to extract and transform data from xml files, specially in the form of tables
The DSL includes expressions for loading xml files, searching for specific tables inside this files, applying transformations on this tables and exporting the result to csv or json.


### EXECUTE:
To compile use the command gradle build.
Execute the following command to run the parser:
```
java -cp "./bin/main;json-20200518.jar" Parser [<dsl-file>] [<xml-files>...]
```
A REPL is executed when the Parser is called without arguments.
It's possible to specify a file containing a set of instructions in the dsl and, optionally, pass as arguments a set of xml files to be processed by this instructions.


### DEALING WITH SYNTACTIC ERRORS:
Javacc deals with the syntatic errors. The Parser stops after finding the first error.

### SEMANTIC ANALYSIS:
Specific error messages are printed in the following situations:
 - When the program tries to access a variable (table, file or structure) that does not exist.
 - When the program tries to access a parameter (a xml file) but not enough parameters were passed to the program.
 - When the program tries to specify a structure without names for columns or rows.
 - When in an export expression the format of the file to export is not specified or not supported (the file name must end in .csv or .json).
In the first two cases the error will cause the program to stop, because there will be values missing to calculate the result of the expression.
In the other two cases the instruction is simply not executed. 
It is possible to override variables. The parser checks the existence of variables with same names and removes one of them if necessary.
This way the same name may be used to refer to different types of variables in different parts of the program (like in javascript and python).


###vINTERMEDIATE REPRESENTATIONS (IRs): 
A syntax tree is used as Intermediate representation. 
The program is read as a sequence of four basic types of expressions: Load, Assign, Print and Export. 
Each one of these is parsed according to which types of tokens and arguments it must have.
The class Parser has HashMaps which it uses to associate variables names to its values.


### CODE GENERATION: 
There is no code generation, the language is read and interpreted by the Parser without generating code.

### OVERVIEW: 
The development of the DSL was focused on the creation of resources to extract and manipulate data in the form of tables.
For loading xml files:
LOAD "filename.xml" AS <variable-name>;
It is possible to load multiple files separating the names with ",":
LOAD "filename.xml" AS <variable-name>, "filename2.xml" AS <variable-name2>;

There is a number of expressions used for extracting and manipulating tables.
All these expressions may be applied to individual tables or arrays of tables.

To search for a table and assign it to a variable:
<variable-name> = TABLE <table-name>;
This expression indicates the program to search for tables with the given name in all loaded files and assign the results to the variable.
Notice that the result may contain 0, 1 or more tables. In the first case a message will be printed in the terminal indicating that no tables were found, and the variable will not be assigned.
In the other cases, the variable name is associated with the result, which may be a table or an array of tables.

Structures are objects used to specify what rows and/or columns should exist in a table.
The following expressions are used to define structures:
<variable-name> = STRUCTURE (<rows-names>)x(<columns-names>);
<variable-name> = STRUCTURE ROW(<rows-names>);
<variable-name> = STRUCTURE COL(<columns-names>);
The use of the keyword STRUCTURE is optional. The names must be separated with ",".
The following expression fits a table in a structure:
<table-name> = FIT <table-name> INTO <structure-name>;
The following expression finds a table that has the rows and columns required by a structure and fits the table in this structure:
<variable-name> = FIND <structure-name>;

The expressions for finding and extracting tables in xml files may be used without specifying the files, and in this case all the loaded files will be used.
For finding tables in specific files, use the following:
<variable-name> = TABLE <table-name> FROM <file-variable1>, <file-variable2>;
<variable-name> = FIND <structure-name> FROM <file-variable1>, <file-variable2>;

The following expression is used to transpose tables:
<table-name> = TRANSPOSE <table-name>;
The following expression is used to join tables, like in SQL natural join:
<table-name3> = JOIN <table-name>, <table-name2>;
If arrays are passed as arguments to the join instruction, only the first tables of aeach array will be considered.
The following expression is used to print tables or strings in the terminal:
PRINT <table-name>;
PRINT <string>;
Strings must be written with double quote.
Multiple arguments may be passed to the print function. In this case they must be separated with "<<".
Example:
PRINT "Tables 1 and 2:" << table1 << table2;

The following expression is used to export tables to csv or json files:
EXPORT <variable-name> AS <file-name>;
The file name must be a string that ends with ".csv" or ".json".
If an array of tables is passed, multiple files will be created.
The generated files may have for example the names "filename(0).csv", "filename(1).csv", "filename(2).csv", etc.

When referring in the dsl file to a xml file that must be passed as argument to the program, use the symbols $0, $1, $2, etc.

See the files "example.dsl", "example2.dls" and "example3.dsl" for examples on how to use the language.

Packages used:
DOM, to parse the XML files.
JSON, to export to JSON.


### PROS:
Simple syntax, easy to use. 
Can process multiple files and apply operations on multiple tables with a single instruction.
Would be easy to add new features, like exporting or loading files in other formats and adding other kinds of operations.

### CONS:
The program exits after any syntax error or errors involving inexistent variables.



