/**
* HTTP Log Parser  Test
* Purpose: Run some unit tests for Log Parser
* Script Author: Christos Saitis
* Date: 2022-04-03 
*/
package source.test;
import source.main.LogParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


class LogParserTest {
    public static void main(String[] args) {

        System.out.println("\n\n### 1.   Check Test Case Divisor is zero  ###\n");
        int divisor = 0;
        int divident = 10;
        LogParser.percentCalc(divisor,divident,"Test");
       
        System.out.println("\n\n### 2.   Test Case Divident is zero ###\n");
        divisor = 10;
        divident = 0;
        LogParser.percentCalc(divisor,divident,"Test");

        System.out.println("\n\n### 3.   Test Case Malformed Entries Empty List ###\n");
        ArrayList<String> checkΑrray = new ArrayList<String> ();
        LogParser.malformedEntries(checkΑrray,"Test");

        System.out.println("\n\n### 4.   Test Case Malformed Entries Add Values ###\n");
        checkΑrray.add("testing first case");
        checkΑrray.add("testing second case");
        LogParser.malformedEntries(checkΑrray,"Test");
 
		System.out.println("\n\n### 5.   Test Case Empty Map ###\n");
        Map<String, Integer> checkMap = new HashMap<String, Integer> ();
        LogParser.topRequests(checkMap, "Test");
        checkMap.put("hostname_1", 10);
        checkMap.put("hostname_2", 20);

        System.out.println("\n\n### 6.   Test Case Add Map Values ###\n");
        LogParser.topRequests(checkMap, "Test");

        System.out.println("\n\n### 7.   Test Case with two empty maps as input ###\n");
        Map<String, Integer> checkMap_1 = new HashMap<String, Integer> ();
        Map<String, Integer> checkMap_2 = new HashMap<String, Integer> ();
        LogParser.top5RequestsPerTop10Host(checkMap_1, checkMap_2);

        System.out.println("\n\n### 8.   Test Case check Sorting function with zero List Entries ###\n");
        Map<String, Integer> checkMapList = new HashMap<String, Integer> ();
        List<Entry<String, Integer>> checkListEntry= LogParser.sortMap(checkMapList);
        for (int i = 0; i < checkListEntry.size(); i++) {
            Entry<String, Integer> element = checkListEntry.get(i);
            System.out.println(element);
            
        }

        System.out.println("\n\n### 8.   Check Sorting function adding only three values to List Entries ###\n");
        checkMapList.put("hostname_1", 10);
        checkMapList.put("hostname_2", 20);
        checkMapList.put("hostname_3", 15);
        checkListEntry= LogParser.sortMap(checkMapList);
        for (int i = 0; i < checkListEntry.size(); i++) {
            Entry<String, Integer> element = checkListEntry.get(i);
            System.out.println(element);
            
        }

       System.out.println("\n\n### 9.   Check for empty map in top5Pages ###\n");
       checkMap = new HashMap<String, Integer> ();
       LogParser.getTop5Pages("host test name", checkMap);
 
       System.out.println("\n\n### 10.   Check for empty host in top5Pages ###\n");
       checkMap.put("hostname_1", 10);
       checkMap.put("hostname_2", 20);
       checkMap.put("hostname_1", 30);   
       LogParser.getTop5Pages("", checkMap);

       System.out.println("\n\n### 11.   Check top5Pages with host and map ###\n");
       LogParser.getTop5Pages("hostname_1", checkMap);

       System.out.println("\n\n### 12.   Check Log Parser with malformed entry ###\n");
       LogParser.checkLogParser(3,"zooropa.res.cmu.edu - - [31/Aug/1995:11:44:19 -0400] \"GET /htbin/cdt_clock.pl HTTP/1.0From:  <berend@blazemonger.pc.cc.cmu.edu>\" 200 -");

       System.out.println("\n\n### 13.   Check Correct Log Parser ###\n");
       LogParser.checkLogParser(4, "slppp6.intermind.net - - [01/Aug/1995:00:00:10 -0400] \"GET /history/skylab/skylab.html HTTP/1.0\" 200 1687");
       
    }
}