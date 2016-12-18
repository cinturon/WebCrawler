package edu.greenriver.it.consumers;

import edu.greenriver.it.producers.PageQueue;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jeremy Belt
 * @version 1.0
 * 11/21/2016
 * Parser.java
 * The Parser class takes in an html string from the page queue
 * It looks for any links or any keywords contained in the html
 */
public class Parser extends Thread {

    private ArrayList<String> keywords;
    private static int parserThreadCounter;
    private static int totalKeywordCount;

    /**
     * The Fetcher constructor creates a thread as well as counts the number of threads
     * that have been created and print out the number of threads
     */
    public Parser(ArrayList<String> keywords) {
        this.keywords = keywords;
        parserThreadCounter++;
        System.out.println("Current Number of Parser Threads: " + parserThreadCounter);
    }


    /**
     * If the thread is awake it look for a page from the page queue
     * then it will check for links and keywords.
     * it will add links to the links queue or increment the keyword counter
     */
    @Override
    public void run() {

        while (true) {
            String pageText = PageQueue.getNextPage();

            for (String keyword: keywords) {

                Pattern keywordPattern = Pattern.compile(keyword);
                Matcher keywordMatcher = keywordPattern.matcher(pageText);
                int count = 0;
                while (keywordMatcher.find()){
                    count +=1;
                }

                totalKeywordCount += count;
            }

            Pattern pattern = Pattern.compile("href=\"(http:.*?)\"");
            Matcher matcher = pattern.matcher(pageText);

            while (matcher.find()) {
                String link = matcher.group(1);

                LinksQueue.addLink(link);
            }
        }
    }

    /**
     * @return int
     * returns the total number of times a keyword was found in the html of a page
     */
    public static int getTotalCount() {
        return totalKeywordCount;
    }

    /**
     *@return  int
     * return the number of Parser threads that have been created
     */
    public static int getParserThreadCounter() {
        return parserThreadCounter;
    }
}
