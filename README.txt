HTTP Log Parser
============================

Welcome to the HTTP Log Parser Tool, a tool to parse log file and produce plain text reports


Usage Notes
-----------

This tool analizes a log file and produces different reports based on user input.
After downloading the log file, a regex expression is used to match the http logs,
all valid reports are stored and then processed to produced the requested report.

Running the shell script NASA_access_log_Aug95 log will be downloaded and extracted.
Program expects first parameter to be the input file, if no file is entered then a
a sample will be selected.

A Menu is used to interact with the user and display the selected extraction.


Building Instructions
---------------------

Auto
---- 
Go to your source folder using the terminal
Run the LogParser.sh (./LogParser.sh)
Don't forget to give the appropriate rights (chmod 755 LogParser.sh)

Manual
------
* run the program
javac source/main/LogParser.java
java source.main.LogParser

* tool expects first parameter to be the input file (ex.NASA_access_log_Aug95)
java source.main.LogParser file

* if no parameter is given then the sample file in the folde will be selected for demo
java source.main.LogParser

* run tests
javac source/test/LogParserTest.java
java source.test.LogParserTest



Dependencies
------------
No depencies are required, only java core libraries are used.



