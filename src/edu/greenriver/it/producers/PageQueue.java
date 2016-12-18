package edu.greenriver.it.producers;

import java.util.LinkedList;

/**
 * @author Jeremy Belt
 * @version 1.0
 * 11/21/2016
 * PageQueue.java
 * The Page Queue holds the html of a web page stored as a single string
 * A page from the page will get passed to the parser to search for
 * strings and keywords
 */
public class PageQueue {

    private static final int MAX_SIZE = 50000;
    private static LinkedList<String> pageQueue = new LinkedList<>();
    private static int pageCounter;


    /**
     * If the page queue isn't full add the html string to the queue
     * (wake up a thread if the page queue was empty)
     */
    public static void addPage(String pageText) {
        synchronized (pageQueue) {
            while (pageQueue.size() >= MAX_SIZE){
                try {
                    pageQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                pageQueue.addFirst(pageText);
                pageCounter++;
                pageQueue.notify();
        }
    }


    /**
     * @return String
     * If the page queue isn't empty pull a page off the end of the queue
     * and pass it as a string
     */
    public static String getNextPage()
    {
        synchronized (pageQueue)
        {
            while (pageQueue.size() == 0)
            {

                try {
                    pageQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String removed = pageQueue.removeLast();
            pageCounter++;
            pageQueue.notify();
            return removed;
        }
    }

    /**
     *@return int
     * return the number of pages that have been downloaded
     */
    public static int getPagesDownloaded() {
        return pageCounter;
    }
}
