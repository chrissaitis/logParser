/**
* HTTP Log Parser Tool
* Purpose: Parse Log file and produce plain text reports
* Script Author: Christos Saitis
* Date: 2022-04-04 
*/
package source.main;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LogParser {

    static Map<ArrayList<String>, ArrayList<Integer>> genericMap = new HashMap<ArrayList<String>, ArrayList<Integer>> ();
    static ArrayList<String> malformedEntries = new ArrayList<String> (); // Create an ArrayList object


    /**
    * use a regular expression to match each record,
    * seperate values and add them to a genericMap HashMap 
    */
    public static void httpLogParser(int lineCounter, String record) {

        // Creating a regular expression for the records
        final String regex = "^(\\S+) (\\S+) (\\S+) " +
            "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" +
            " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(record);
        
        
        //if (matcher.find()) {
        if (matcher.matches()) {
            String host = matcher.group(1);
            String requestUrl = matcher.group(6);
            int responseCode = Integer.parseInt(matcher.group(8));

            ArrayList<String> genericMapKeys = new ArrayList<String> ();
            genericMapKeys.add(host);
            genericMapKeys.add(requestUrl);

            int cntSuccess = (responseCode >= 200) && ((responseCode <= 300)) ? 1 : 0;
            int cntFail = (cntSuccess == 1) ? 0 : 1;

            ArrayList < Integer > genericMapValues = new ArrayList < Integer > ();
            if (genericMap.containsKey(genericMapKeys)) {
                genericMapValues.add(genericMap.get(genericMapKeys).get(0) + cntSuccess);
                genericMapValues.add(genericMap.get(genericMapKeys).get(1) + cntFail);
            } else {
                genericMapValues.add(cntSuccess);
                genericMapValues.add(cntFail);
            }

            if (genericMap.containsKey(genericMapKeys)) {

                genericMap.put(genericMapKeys, genericMapValues);
            } else {
                genericMap.put(genericMapKeys, genericMapValues);
            }

        } else {
            int errorPartIndex = getTheErrorPart(pattern,record);
            malformedEntries.add("Line: " + lineCounter + " - Error Part: " + record.substring(errorPartIndex, record.length()));
        }
    }


    /**
    * for testing of httpLogParser
    */
    public static void checkLogParser(int lineCounter, String record) {
        // Creating a regular expression for the records
        final String regex = "^(\\S+) (\\S+) (\\S+) " +
            "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" +
            " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(record);
        if (matcher.matches()) {
            System.out.println("valid record: " + record);
        }
        else{
            int errorPartIndex = getTheErrorPart(pattern,record);
            System.out.println("invalid record: " + record.substring(errorPartIndex, record.length()));
        }
    }


    /**
    * get the mismatched part of regex that lead to error
    */
    public static int getTheErrorPart(Pattern regex, String str) {
    for (int i = 0; i <= str.length(); i++) {
        Matcher m = regex.matcher(str.substring(0, i));
        if (!m.matches() && !m.hitEnd()) {
            return i - 1;
        }
      }
      if (regex.matcher(str).matches()) {
          return -1;
      } else {
          return str.length();
      }
    }


    /**
    * Convert Map to a List of Entries,
    * sort the List and return the top 10 values
    */
    public static List<Entry<String, Integer>> sortMap(Map<String, Integer> inputMap) {  
        List<Entry<String, Integer>> sortedList = new ArrayList<> ();
        if(!inputMap.isEmpty()){
            List<Entry<String, Integer>> inputList = new ArrayList<> (inputMap.entrySet());
            inputList.sort(Entry.comparingByValue());
            
            // check if size is less than 10
            int countFromTop = (inputList.size() >= 10) ? (inputList.size() - 10) : 0; 
            for (int i = inputList.size() - 1; i >= countFromTop; i--) {
                sortedList.add(inputList.get(i));
            }
        }
        else{
             System.out.println("the list is empty");           
        }   
        return sortedList;   
    }


    /**
    * Calculate percentage, display 2 decimal digits
    */
    public static void percentCalc(double total, double sum, String printTitle) {
        if (total>0){
            double perc = 100 * (sum / total);
            System.out.println("\n\n###  " + printTitle + ": " + String.format("%.2f", perc) + "% ###\n");
        }else{
            System.out.println("\n\n###  invalid total requests(0) ###\n");
        }
    }


    /**
    * Print the Top 10 Values from Sorted Map
    */
    public static void topRequests(Map<String, Integer> inputMap, String printTitle) {
        if(!inputMap.isEmpty()){
            List<Entry<String, Integer>> top10Requests = sortMap(inputMap);
            System.out.println("\n\n###  " + printTitle + "  ###\n");        
            for (int i = 0; i < top10Requests.size(); i++) {
                System.out.println(String.valueOf(i + 1) + ". " + top10Requests.get(i));
            }
        }
        else{  
            System.out.println("the map is empty");
        }

    }


    /**
    * Print all malformed entries,
    * records that failed to match regex
    */
    public static void malformedEntries(ArrayList<String> inputArray, String printTitle) { 
        if (!inputArray.isEmpty()){
            System.out.println("\n\n###  " + printTitle + "  ###\n");
            for (int i = 0; i < inputArray.size(); i++) {
                System.out.println(String.valueOf(i + 1) + ". " + inputArray.get(i));
            }
        }
        else{
            System.out.println("No malformed entries exist in the list");
        }
    }


    /**
    * Loop through top 10 hosts Map
    * compare each host with pageMap
    */
    public static void top5RequestsPerTop10Host(Map<String, Integer> hostMap, Map<String, Integer> pageMap) {
        if(!hostMap.isEmpty() && !pageMap.isEmpty()){
            List<Entry<String, Integer>> top10HostsMap = sortMap(hostMap);
            String host = "";
            for (int i = 0; i < top10HostsMap.size(); i++) {
                Entry<String, Integer> element = top10HostsMap.get(i);
                host = element.getKey();
                getTop5Pages(host, pageMap);
            }
        }
        else{
            System.out.println("the map is empty");
        }
    }


    /**
    * match hosts with their related pages
    * host will be include in hostname (host+page)
    * sort top 5 pages for each host
    */
    public static void getTop5Pages(String host, Map<String, Integer> pageRequestsMap) {
        if(!pageRequestsMap.isEmpty()){
            if(!host.isEmpty()){
            System.out.println("\n\n###  " + host + "  ###\n");
            Map<String, Integer> topPageRequestsMap = new HashMap<String, Integer> ();
            for (Map.Entry<String, Integer> set: pageRequestsMap.entrySet()) {
                if (set.getKey().startsWith(host)) {
                    if (topPageRequestsMap.containsKey(set.getKey())) {} else {
                        topPageRequestsMap.put(set.getKey(), set.getValue());
                    }
                }
            }
            List<Entry<String, Integer>> top5PageRequestsSorted = sortMap(topPageRequestsMap);
            // check if size is less than 5
            int countFromTop = (top5PageRequestsSorted.size() >= 5) ? (top5PageRequestsSorted.size() - 5) : 0; 
            for (int i = 0; i < top5PageRequestsSorted.size() - countFromTop; i++) {
                System.out.println(String.valueOf(i + 1) + ". " + top5PageRequestsSorted.get(i));
            }
            }else{
                  System.out.println("the host is null");
            }
        }
        else{
            System.out.println("the map is empty");
        }
    }


    /**
    * Print Menu Options
    */
    public static void printMenu(String[] options) {
        for (String option: options) {
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }


    /**
    * Main Function
    * Reads input file, runs the log parser
    * displays text report based on user request
    */
    public static void main(String[] args) {
        String line;
        int lineCounter = 1;

        File inFile = null;
        if (0 < args.length) {
            inFile = new File(args[0]);
        } else {
            System.out.println("\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("####### Invalid arguments count:" + args.length);
            System.out.println("####### Please enter your http log file as argument");
            System.out.println("####### Since no file is provided a Sample will be used for demo");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");
            inFile = new File("sample");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            System.out.println("\n\n###################################################");
            System.out.println("#######                                 ###########");
            System.out.println("####### HTTP Log Parser  Tool           ###########");
            System.out.println("####### Script Author: Christos Saitis  ###########");
            System.out.println("####### Date: 2022-04-04                ###########");
            System.out.println("#######                                 ###########");
            System.out.println("###################################################\n\n");
            System.out.println("Calculating please wait...");
            while ((line = br.readLine()) != null) {
                // Run the log parser, compare each record with regex
                httpLogParser(lineCounter, line);
                lineCounter += 1;
            }
        } catch (IOException e) {
            System.out.println("IOException in try block =>" + e.getMessage());
        }

        
        // Create subMaps from genericMap
        Map<String, Integer> topPageRequests = new HashMap<String, Integer> ();
        Map<String, Integer> topUnsuccessfulPageRequests = new HashMap<String, Integer> ();
        Map<String, Integer> topHostsRequests = new HashMap<String, Integer> ();

        int totalSuccess = 0;
        int totalFail = 0;
        int totalRequests = 0;

        for (Map.Entry < ArrayList<String> , ArrayList < Integer >> entry: genericMap.entrySet()) {
            // break generic map to make specific calculations
            String host = entry.getKey().get(0);
            String page = entry.getKey().get(1);
            String hostname = host + page;

            int success = entry.getValue().get(0);
            int fail = entry.getValue().get(1);
            int requests = success + fail;

            totalSuccess += success;
            totalFail += fail;
            totalRequests += requests;

            if (topPageRequests.containsKey(hostname)) {
                topPageRequests.put(hostname, topPageRequests.get(hostname) + requests);
            } else {
                topPageRequests.put(hostname, requests);
            }

            if (topUnsuccessfulPageRequests.containsKey(hostname)) {
                topUnsuccessfulPageRequests.put(hostname, topUnsuccessfulPageRequests.get(hostname) + fail);
            } else {
                topUnsuccessfulPageRequests.put(hostname, fail);
            }

            if (topHostsRequests.containsKey(host)) {
                topHostsRequests.put(host, topHostsRequests.get(host) + requests);
            } else {
                topHostsRequests.put(host, requests);
            }

        }

        String[] options = {
            "\n\n\n\n\n----------------------- Menu --------------------------------\n",
            "1 - All Reports",
            "2 - Top 10 requested pages and the number of requests made for each",
            "3 - Percentage of successful requests (anything in the 200s and 300s range)",
            "4 - Percentage of unsuccessful requests (anything that is not in the 200s or 300s range)",
            "5 - Top 10 unsuccessful page requests",
            "6 - The top 10 hosts making the most requests, displaying the IP address and number of requests made",
            "7 - For each of the top 10 hosts, show the top 5 pages requested and the number of requests for each page",
            "8 - for each malformed line, display an error message and the line number",
            "9 - Exit",
            "\n----------------------------------------------------------------\n"
        };

        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != 9) {
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1:
                        topRequests(topPageRequests, "Top 10 requested pages requests");
                        topRequests(topUnsuccessfulPageRequests, "Top 10 unsuccessful page requests");
                        percentCalc(totalRequests, totalSuccess, "Percentage of successful requests");
                        percentCalc(totalRequests, totalFail, "Percentage of unsuccessful requests");
                        topRequests(topUnsuccessfulPageRequests, "Top 10 unsuccessful page requests");
                        topRequests(topHostsRequests, "Top 10 hosts requests");
                        top5RequestsPerTop10Host(topHostsRequests, topPageRequests);
                        malformedEntries(malformedEntries, "Malformed entries");
                        break;
                    case 2:
                        topRequests(topPageRequests, "Top 10 requested pages requests");
                        break;
                    case 3:
                        percentCalc(totalRequests, totalSuccess, "Percentage of successful requests");
                        break;
                    case 4:
                        percentCalc(totalRequests, totalFail, "Percentage of unsuccessful requests");
                        break;
                    case 5:
                        topRequests(topUnsuccessfulPageRequests, "Top 10 unsuccessful page requests");
                        break;
                    case 6:
                        topRequests(topHostsRequests, "Top 10 hosts requests");
                        break;
                    case 7:
                        top5RequestsPerTop10Host(topHostsRequests, topPageRequests);
                        break;
                    case 8:
                        malformedEntries(malformedEntries, "Malformed entries");
                        break;
                    case 9:
                        System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println("Please enter an integer value between 1 and 9");
                scanner.next();
            }
        }
    }
}
