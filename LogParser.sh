#!/bin/sh
# Description: Download log file, unzip it and run LogParser
# Author: Christos Saitis

# compile LogParser
javac source/main/LogParser.java

# compile LogParserTest
javac source/test/LogParserTest.java

echo "Source Files are compiled"

echo "Downloading input file from NASA FTP please wait.."
LOGFILE_URL="ftp://ita.ee.lbl.gov/traces/NASA_access_log_Aug95.gz"

# download requested gzip
curl -sS ${LOGFILE_URL} > inputFile.gz 
echo "Download complete"

# unzip file
gunzip inputFile.gz

echo "Start Log Parser"
# run program
java source.main.LogParser inputFile

